package com.alex.galinperrcster.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.alex.galinperrcster.misc.FileToProcess;
import com.alex.galinperrcster.misc.Header;
import com.alex.galinperrcster.misc.Substitute;
import com.alex.galinperrcster.misc.SubstituteTemplate;
import com.alex.galinperrcster.utils.ClearSpecialChar;
import com.alex.galinperrcster.utils.UsefulMethod;
import com.alex.galinperrcster.utils.Variables;

/**
 * Class used to start the main treatment
 * @author Alexandre
 *
 */
public class Worker
	{
	/**
	 * Variables
	 */
	private long startTime;
	private long elapsedTime;
	private Date myDate;
	private SimpleDateFormat timeFormat;
	
	/**
	 * Constructor
	 */
	public Worker()
		{
		startTime = System.currentTimeMillis();
		timeFormat = new SimpleDateFormat("mm:ss:SSS");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		myDate = new Date();
		
		try
			{
			String separator = UsefulMethod.getTargetOption("separator");
			boolean clearSC = Boolean.parseBoolean(UsefulMethod.getTargetOption("clearspecialcharacters"));
			
			/*****************************
			 * We get the following files :
			 * - Substitute file
			 * - Template file
			 * - The files to process
			 */
			//Substitute list
			ArrayList<String[][]> substituteFile = UsefulMethod.readFileTab("template", UsefulMethod.getTargetOption("substitutefile"));
			ArrayList<SubstituteTemplate> substituteTemplateList = new ArrayList<SubstituteTemplate>();
			for(String[][] tab : substituteFile)
				{
				ArrayList<Substitute> substituteList = new ArrayList<Substitute>();
				for(int i=1; i<tab.length; i++)
					{
					substituteList.add(new Substitute(tab[i][1]));
					Variables.getLogger().debug("Adding "+tab[i][1]+" as a new substitute in the template "+tab[0][1]);
					}
				substituteTemplateList.add(new SubstituteTemplate(tab[0][1], substituteList));
				}
			
			//Template list
			ArrayList<String> templateFile = UsefulMethod.readFile("headers",UsefulMethod.getTargetOption("templatefile"));
			ArrayList<Header> headerList = new ArrayList<Header>();
			for(String s : templateFile)
				{
				headerList.add(new Header(s));
				Variables.getLogger().debug("Adding "+s+" as a new header");
				}
			
			//Files to process
			ArrayList<FileToProcess> filesToProcess = new ArrayList<FileToProcess>();
			ArrayList<File> fileList = UsefulMethod.getFilesToProcess(Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("filedirectory")+"/");
			for(File f : fileList)
				{
				filesToProcess.add(new FileToProcess(f.getName(), UsefulMethod.getFileStringContent(f)));
				}
			/****************************/
		
			/***********
			 * We then process the files
			 */
			for(FileToProcess ftp : filesToProcess)
				{
				Variables.getLogger().info("Processing the following file : "+ftp.getFileName());
				
				//We manage separator escaper
				Variables.getLogger().debug("Checking for escapers");
				for(int a=0; a<ftp.getLines().size(); a++)
					{
					ftp.getLines().set(a, UsefulMethod.manageEscaper(ftp.getLines().get(a), a+2));
					}
				
				//If asked we check the file integrity
				if(UsefulMethod.getTargetOption("checkforintegrity").equals("true"))
					{
					ArrayList<Integer> errorList = UsefulMethod.checkCSVFileIntegrity(ftp);
					if(errorList.size() != 0)
						{
						Variables.getLogger().error("The file integrity is incorrect. The following lines are incorrect :");
						for(Integer error : errorList)
							{
							Variables.getLogger().error(" "+error);
							}
						throw new Exception("The file integrity is incorrect. Check the logs for more detail");
						}
					}
				
				String[] headers = ftp.getFirstLine().split(separator);//We get the headers
				
				for(int j=0; j<ftp.getLines().size(); j++)
					{
					int index = j+2;
					String line = ftp.getLines().get(j);
					
					//Variables.getLogger().info("Line "+index+" before : "+line);
					
					boolean modified = false;
					boolean toDelete = false;
					String[] params = line.split(separator, -1);//-1 do not remove the empty values
					
					for(int i=0; i<headers.length; i++)
						{
						String column = headers[i];
						boolean foundMatchingHeader = false;
						for(Header h : headerList)
							{
							if((!foundMatchingHeader) && (Pattern.matches(h.getHeader(), headers[i])))
								{
								SubstituteTemplate myTemplate = null;
								for(SubstituteTemplate st : substituteTemplateList)
									{
									if(st.getName().equals(h.getTemplateName()))
										{
										myTemplate = st;
										foundMatchingHeader = true; //To avoid matching another header at the next loop
										break;//To avoid matching another template
										}
									}
								if(myTemplate == null)
									{
									//No template have been found so we just apply the pattern has it is. Normally it is what the user asked
									params[i] = UsefulMethod.doRegex(params[i], h.getTemplateName(), ftp, j);
									//Variables.getLogger().debug("Line "+index+" new value after replacement : "+params[i]);
									modified = true;
									
									continue;//To avoid to use the substitute template
									}
								
								for(Substitute sub : myTemplate.getSubstituteList())
									{
									String patterns[] = sub.getPattern().split("AND");//AND is to manage multiple condition
									boolean match = true;
									String value = params[i];
									
									for(String pattern : patterns)
										{
										if(pattern.startsWith("*Get "))//Manage the *Get* special feature
											{
											String columnName = pattern.split("\\*")[1].substring(4);//To get the column name
											//We now get the value according to the column name
											value = UsefulMethod.getValue(ftp, columnName, j);
											
											String st = "*Get "+columnName+"*";
											pattern = pattern.substring(st.length());//Remove the get pattern
											}
										
										if(!Pattern.matches(pattern, value))
											{
											match = false;
											break;
											}
										}
										
									if((i<params.length) && match)
										{
										Variables.getLogger().debug("Line "+index+" column "+column+" the value \""+value+"\" matched the pattern "+sub.getPattern());
										
										/**
										 * If the *delete* tag is found we delete the whole line
										 */
										if(sub.getReplaceBy().equals("*delete*"))
											{
											toDelete = true;
											Variables.getLogger().debug("Line "+index+" column "+column+" the *delete* tag matched the value "+params[i]);
											}
										else
											{
											String oldValue = params[i];
											if(clearSC)
												{
												params[i] = ClearSpecialChar.translate(params[i]);
												}
											params[i] = UsefulMethod.doRegex(params[i], sub.getReplaceBy(), ftp, j);
											Variables.getLogger().debug("Line "+index+" column "+column+", "+oldValue+" was replaced by "+params[i]);
											modified = true;
											}
										break;//To avoid matching another substitute
										}
									}
								}
							if(toDelete)break;//We jump directly to the next line because this one has to be deleted
							}
						if(toDelete)break;//We jump directly to the next line because this one has to be deleted
						}
					
					/**
					 * First if the toDelete boolean is true we delete the line.
					 * 
					 * Then if the line didn't match anything and the deleteifnomatch option is set
					 * to true, we also delete the line
					 */
					if(toDelete)
						{
						Variables.getLogger().info("The line "+index+" matched the *delete* tag so we delete it");
						ftp.getLines().remove(j);
						j--;
						}
					else if((modified == false) && (UsefulMethod.getTargetOption("deleteifnomatch").equals("true")))
						{
						Variables.getLogger().info("The line "+index+" was not modified so we delete it");
						ftp.getLines().remove(j);
						j--;
						}
					else if(modified)
						{
						//We then reassemble the line with the new value
						StringBuffer newLine = new StringBuffer("");
						
						for(int a=0; a<params.length; a++)
							{
							newLine.append(params[a]);
							if(a != params.length-1)newLine.append(separator);//To remove the last separator
							}
						
						String strNewLine = newLine.toString();
						
						//We replace the escaped value
						String sub = UsefulMethod.getTargetOption("separatorsubstitute");
						if(strNewLine.contains(sub))
							{
							strNewLine = strNewLine.replaceAll(sub, separator);
							}
						
						ftp.getLines().set(j, strNewLine);
						//Variables.getLogger().info("Line "+index+" after : "+ftp.getLines().get(j));
						}
					else
						{
						Variables.getLogger().info("The line "+index+" was not modified");
						}
					
					System.gc();//To avoid memory leak
					}
				//Finally we write the new file version
				String directoryPath;
				if(UsefulMethod.getTargetOption("donotoverwrite").equals("true"))
					{
					//We do not overwrite the file. So we put it in a new directory
					Variables.getLogger().info("The configuration file ask to not overwrite the source file. So we create a copy in a new directory");
					
					directoryPath = Variables.getMainDirectory()+"/"+
					UsefulMethod.getTargetOption("filedirectory")+"/"+
					UsefulMethod.getTargetOption("newfiledirectory");
					
					//We check if the directory exists
					UsefulMethod.createDirectory(directoryPath);
					}
				else
					{
					directoryPath = Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("filedirectory");
					}
				
				
				UsefulMethod.writeModifiedFile(ftp, directoryPath);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong : "+e.getMessage(), e);
			}
		
		elapsedTime = System.currentTimeMillis() - startTime;
		myDate.setTime(elapsedTime);
		Variables.getLogger().debug("Total processing time : "+timeFormat.format(myDate));
		}
	

	/*2018*//*Alexandre RATEL 8)*/
	}
