package com.alex.galinperrcster.action;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.alex.galinperrcster.misc.FileToProcess;
import com.alex.galinperrcster.misc.Header;
import com.alex.galinperrcster.misc.Substitute;
import com.alex.galinperrcster.misc.SubstituteTemplate;
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
	
	/**
	 * Constructor
	 */
	public Worker()
		{
		try
			{
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
			
			/*
			for(FileToProcess ftp : filesToProcess)
				{
				Variables.getLogger().info("Processing the following file : "+ftp.getFileName());
				//We get the header line
				String[] headers = ftp.getFirstLine().split(UsefulMethod.getTargetOption("separator"));
				for(int i=0; i<headers.length; i++)
					{
					for(Header h : headerList)
						{
						if(Pattern.matches(h.getHeader(), headers[i]))
							{
							//The header match so we apply the template for all the line
							Variables.getLogger().debug("The header match the pattern : "+headers[i]+" = "+h.getHeader());
							Variables.getLogger().debug("So we apply the following template to all the lines : "+h.getTemplateName());
							
							SubstituteTemplate myTemplate = null;
							for(SubstituteTemplate st : substituteTemplateList)
								{
								if(st.getName().equals(h.getTemplateName()))myTemplate = st;
								}
							if(myTemplate == null)
								{
								Variables.getLogger().error("Template name \""+h.getTemplateName()+"\"not found so we ignore this header");
								continue;
								}
							
							for(int j=0; j<ftp.getLines().size(); j++)
								{
								String line = ftp.getLines().get(j);
								String[] params = line.split(UsefulMethod.getTargetOption("separator"));
								for(Substitute sub : myTemplate.getSubstituteList())
									{		
									if((i<params.length) && (Pattern.matches(sub.getPattern(), params[i])))
										{
										params[i] = UsefulMethod.doRegex(params[i], sub.getReplaceBy());
										break;//To avoid matching another substitute
										}
									}
								StringBuffer newLine = new StringBuffer("");
								for(int a=0; a<params.length; a++)
									{
									newLine.append(params[a]);
									if(a != params.length-1)newLine.append(UsefulMethod.getTargetOption("separator"));//To remove the last separator
									}
								ftp.getLines().set(j, newLine.toString());
								Variables.getLogger().debug("New line : "+newLine.toString());
								}
							break;//To avoid matching another template
							}
						}
					System.gc();
					}
				//We write the new file version
				UsefulMethod.writeModifiedFile(ftp, Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("filedirectory"));
				}*/
			/*********/
		
			for(FileToProcess ftp : filesToProcess)
				{
				Variables.getLogger().info("Processing the following file : "+ftp.getFileName());
				
				String[] headers = ftp.getFirstLine().split(UsefulMethod.getTargetOption("separator"));//We get the headers
				
				for(int j=0; j<ftp.getLines().size(); j++)
					{
					int index = j+2;
					String line = ftp.getLines().get(j);
					
					Variables.getLogger().info("Line "+index+" before : "+line);
					
					boolean modified = false;
					boolean toDelete = false;
					String[] params = line.split(UsefulMethod.getTargetOption("separator"));
					
					for(int i=0; i<headers.length; i++)
						{
						for(Header h : headerList)
							{
							if(Pattern.matches(h.getHeader(), headers[i]))
								{
								SubstituteTemplate myTemplate = null;
								for(SubstituteTemplate st : substituteTemplateList)
									{
									if(st.getName().equals(h.getTemplateName()))
										{
										myTemplate = st;
										break;//To avoid matching another template
										}
									}
								if(myTemplate == null)
									{
									continue;
									}
								
								for(Substitute sub : myTemplate.getSubstituteList())
									{		
									if((i<params.length) && (Pattern.matches(sub.getPattern(), params[i])))
										{
										Variables.getLogger().debug("Line "+index+" the value \""+params[i]+"\" matched the pattern \""+sub.getPattern()+"\"");
										
										/**
										 * If the *delete* tag is found we delete the whole line
										 */
										if(sub.getReplaceBy().equals("*delete*"))
											{
											toDelete = true;
											Variables.getLogger().debug("Line "+index+" the *delete* tag matched the following value : "+params[i]);
											}
										else
											{
											params[i] = UsefulMethod.doRegex(params[i], sub.getReplaceBy());
											Variables.getLogger().debug("Line "+index+" new value after replacement : "+params[i]);
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
							if(a != params.length-1)newLine.append(UsefulMethod.getTargetOption("separator"));//To remove the last separator
							}
						ftp.getLines().set(j, newLine.toString());
						Variables.getLogger().info("Line "+index+" after : "+ftp.getLines().get(j));
						}
					else
						{
						Variables.getLogger().info("The line "+index+" was not modified");
						}
					
					System.gc();//To avoid memory leak
					}
				//Finally we write the new file version
				UsefulMethod.writeModifiedFile(ftp, Variables.getMainDirectory()+"/"+UsefulMethod.getTargetOption("filedirectory"));
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong : "+e.getMessage(), e);
			}
		}
	

	/*2018*//*Alexandre RATEL 8)*/
	}