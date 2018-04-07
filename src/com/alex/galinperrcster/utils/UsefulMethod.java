package com.alex.galinperrcster.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.log4j.Level;

import com.alex.galinperrcster.misc.FileToProcess;

/**
 * Class used to store all the useful static method
 * @author Alexandre
 *
 */
public class UsefulMethod
	{
	/*****
	 * Method used to read the main config file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readMainConfigFile(String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			file = xMLReader.fileRead(".\\"+fileName);
			
			listParams.add("config");
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the file : "+fileName+" : "+exc.getMessage());
			}
		}
	
	/***************************************
	 * Method used to get a specific value
	 * in the user preference XML File
	 ***************************************/
	public synchronized static String getTargetOption(String node) throws Exception
		{
		//We first seek in the configFile.xml
		for(String[] s : Variables.getTabConfig().get(0))
			{
			if(s[0].equals(node))return s[1];
			}
		
		/***********
		 * If this point is reached, the option looked for was not found
		 */
		throw new Exception("Option \""+node+"\" not found"); 
		}
	/*************************/
	
	/************
	 * Method used to read a simple configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String> readFile(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading the file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			
			return xMLGear.getResultList(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("ERROR : The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+fileName+" file : "+exc.getMessage());
			}
		}
	
	/************
	 * Method used to read a configuration file
	 * @throws Exception 
	 */
	public static ArrayList<String[][]> readFileTab(String param, String fileName) throws Exception
		{
		String file;
		ArrayList<String> listParams = new ArrayList<String>();
		
		try
			{
			Variables.getLogger().info("Reading of the "+param+" file : "+fileName);
			file = getFlatFileContent(fileName);
			
			listParams.add(param);
			return xMLGear.getResultListTab(file, listParams);
			}
		catch(FileNotFoundException fnfexc)
			{
			fnfexc.printStackTrace();
			throw new FileNotFoundException("The "+fileName+" file was not found : "+fnfexc.getMessage());
			}
		catch(Exception exc)
			{
			exc.printStackTrace();
			Variables.getLogger().error(exc.getMessage(),exc);
			throw new Exception("ERROR with the "+param+" file : "+exc.getMessage());
			}
		}
	
	/**
	 * Used to return the file content regarding the data source (xml file or database file)
	 * @throws Exception 
	 */
	public static String getFlatFileContent(String fileName) throws Exception
		{
		return xMLReader.fileRead(Variables.getMainDirectory()+"/"+fileName);
		}
	
	/**
	 * Method used when the application failed to 
	 * initialize
	 */
	public static void failedToInit(Exception exc)
		{
		exc.printStackTrace();
		JOptionPane.showMessageDialog(null,"Application failed to init :\r\n"+exc.getMessage()+"\r\nThe software will now exit","ERROR",JOptionPane.ERROR_MESSAGE);
		Variables.getLogger().error(exc.getMessage());
		Variables.getLogger().error("Application failed to init : System.exit(0)");
		System.exit(0);
		}
	
	/**
	 * Initialization of the internal variables from
	 * what we read in the configuration file
	 * @throws Exception 
	 */
	public static void initInternalVariables() throws Exception
		{
		/***********
		 * Logger
		 */
		String level = UsefulMethod.getTargetOption("log4j");
		if(level.compareTo("DEBUG")==0)
			{
			Variables.getLogger().setLevel(Level.DEBUG);
			}
		else if (level.compareTo("INFO")==0)
			{
			Variables.getLogger().setLevel(Level.INFO);
			}
		else if (level.compareTo("ERROR")==0)
			{
			Variables.getLogger().setLevel(Level.ERROR);
			}
		else
			{
			//Default level is INFO
			Variables.getLogger().setLevel(Level.INFO);
			}
		Variables.getLogger().info("Log level found in the configuration file : "+Variables.getLogger().getLevel().toString());
		/*************/
		
		/************
		 * Allowed file extension
		 */
		String[] tab = UsefulMethod.getTargetOption("allowedfiles").split(",");
		for(String s : tab)
			{
			Variables.getAllowedFiles().add(s);
			}
		
		if(Variables.getAllowedFiles().size() == 0)throw new Exception("ERROR : Something went wrong while fetching the allowed files");
		/**************/
		
		/************
		 * Etc...
		 */
		//If needed, just write it here
		/*************/
		}
	
	/**
	 * Method used to get a file content
	 * It returns an arraylist of the line
	 * @param fileNamePath
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<String> getFileStringContent(File file) throws Exception
		{
		FileReader fileReader= null;
		BufferedReader tampon  = null;
		ArrayList<String> content = new ArrayList<String>();
		
		try
			{
			fileReader = new FileReader(file);
			tampon = new BufferedReader(fileReader);
			String inputLine = new String(); 
			
			while (((inputLine = tampon.readLine()) != null) && (inputLine.compareTo("") !=0))
	        	{
	            content.add(inputLine);
	         	}
		
			return content;
			}
		catch(IOException exception)
			{
			exception.printStackTrace();
			throw new Exception("ERROR : Failed to read the following file : "+file.getName());
			}
		finally
			{
			try
				{
				tampon.close();
				fileReader.close();
				}
			catch(Exception e)
				{
				e.printStackTrace();
				throw new Exception("ERROR : Failed to read the following file : "+file.getName());
				}
			}
		}
	
	/**
	 * Method used to return the list of the file to process
	 * @param path
	 * @return
	 */
	public static ArrayList<File> getFilesToProcess(String sourceDirectory)
		{
		Variables.getLogger().debug("We look for the file to process");
		ArrayList<File> fileList = new ArrayList<File>();
		
		File directory = new File(sourceDirectory);
		for(File f : directory.listFiles())
			{
			for(String ext : Variables.getAllowedFiles())
				{
				if(f.getName().contains("."+ext))
					{
					fileList.add(f);
					Variables.getLogger().debug("The following file has been added to the list : "+f.getName());
					}
				}
			}
		
		return fileList;
		}
	
	/**
	 * Method used to write the modified file version
	 * @param ftp
	 * @throws IOException 
	 */
	public static void writeModifiedFile(FileToProcess ftp, String filePath)
		{
		FileWriter fileWriter = null;
		BufferedWriter myBuffer = null;
		
		try
			{
			File fileToSave = new File(filePath+"/"+ftp.getFileName());
			fileWriter = new FileWriter(fileToSave);
			myBuffer = new BufferedWriter(fileWriter);
			
			StringBuffer mySBuff = new StringBuffer("");
			
			mySBuff.append(ftp.getFirstLine()+"\r\n");
			
			for(String s : ftp.getLines())
				{
				mySBuff.append(s);
				mySBuff.append("\r\n");
				}
			
			myBuffer.write(mySBuff.toString());
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : file writing error \""+ftp.getFileName()+"\" : "+e.getMessage(), e);
			}
		finally
			{
			try
				{
				myBuffer.flush();
				myBuffer.close();
				fileWriter.close();
				Variables.getLogger().info("The file \""+ftp.getFileName()+"\" has been written successfuly");
				}
			catch(Exception e)
				{
				Variables.getLogger().error("ERROR : While closing the file \""+ftp.getFileName()+"\""+e.getMessage(), e);
				}
			}
		}
	
	public static String doRegex(String currentValue, String patternToApply) throws Exception
		{
		String[] tab = patternToApply.split("\\+");
		StringBuffer mySB = new StringBuffer(""); 
		
		for(String s : tab)
			{
			s = s.replace("'", "");
			
			if(s.contains("*"))
				{
				mySB.append(applyRegex(currentValue, s));
				}
			else
				{
				mySB.append(s);
				}
			}
		
		Variables.getLogger().debug("Value before : "+currentValue);
		Variables.getLogger().debug("Value after : "+mySB.toString());
		
		return mySB.toString();
		}
	
	/****
	 * Method used to apply a regex to a value	
	 * @throws Exception 
	 */
	private static String applyRegex(String newValue, String param) throws Exception
		{
		try
			{
			/*********
			 * Number before
			 **/
			if(Pattern.matches("\\*\\d+_\\*.*", param))
				{
				int number = howMany("\\*\\d+_\\*", param);
				if(newValue.length() >= number)
					{
					newValue = newValue.substring(0, number);
					}
				}
			/**
			 * End number before
			 *************/
			
			/*********
			 * Number after
			 **/
			if(Pattern.matches("\\*_\\d+\\*.*", param))
				{
				int number = howMany("\\*_\\d+\\*", param);
				if(newValue.length() >= number)
					{
					newValue = newValue.substring(newValue.length()-number, newValue.length());
					}
				}
			/**
			 * End number after
			 *************/
			
			/*************
			 * Majuscule
			 **/
			if(Pattern.matches(".*\\*M\\*.*", param))
				{
				newValue = newValue.toUpperCase();
				}
			if(Pattern.matches(".*\\*\\d+M\\*.*", param))
				{
				int majuscule = howMany("\\*\\d+M\\*", param);
				if(newValue.length() >= majuscule)
					{
					String first = newValue.substring(0, majuscule);
					String last = newValue.substring(majuscule,newValue.length());
					first = first.toUpperCase();
					last = last.toLowerCase();
					newValue = first+last;
					}
				}
			/**
			 * End majuscule
			 ****************/
			
			/*************
			 * Minuscule
			 **/
			if(Pattern.matches(".*\\*m\\*.*", param))
				{
				newValue = newValue.toLowerCase();
				}
			if(Pattern.matches(".*\\*\\d+m\\*.*", param))
				{
				int minuscule = howMany("\\*\\d+m\\*", param);
				if(newValue.length() >= minuscule)
					{
					String first = newValue.substring(0, minuscule);
					String last = newValue.substring(minuscule,newValue.length());
					first = first.toLowerCase();
					last = last.toUpperCase();
					newValue = first+last;
					}
				}
			/**
			 * End minuscule
			 ****************/
			
			/*************
			 * Split
			 * 
			 * Example : *1S/*
			 * means to split using "/" and to keep the first value
			 **/
			if(Pattern.matches(".*\\*\\d+S.+\\*.*", param))
				{
				int split = howMany("\\*\\d+S.+\\*", param);
				String splitter = getSplitter("\\*\\d+S.+\\*", param);
				newValue = newValue.split(splitter)[split-1];
				}
			/**
			 * End Split
			 ****************/
			
			/*************
			 * Replace
			 * 
			 * Example : *"test"R"testo"*
			 **/
			if(Pattern.matches(".*\\*\".+\"R\".*\"\\*.*", param))
				{
				String pattern = null;
				String replaceBy = null;
				Pattern begin = Pattern.compile("\".+\"R");
				Matcher mBegin = begin.matcher(param);
				Pattern end = Pattern.compile("R\".*\"");
				Matcher mEnd = end.matcher(param);
				
				if(mBegin.find())
					{
					String str = mBegin.group();
					str = str.substring(0,str.length()-1);//We remove the "R"
					str = str.replace("\"", "");
					pattern = str;
					}
				if(mEnd.find())
					{
					String str = mEnd.group();
					str = str.substring(1,str.length());//We remove the "R"
					str = str.replace("\"", "");
					replaceBy = str;
					}
				if((pattern != null) && (replaceBy != null))
					{
					newValue = newValue.replace(pattern, replaceBy);
					}
				}
			/**
			 * End Replace
			 ****************/
			return newValue;
			}
		catch(Exception exc)
			{
			throw new Exception("An issue occured while applying the regex : "+exc.getMessage());
			}
		}
	
	/**
	 * Method used to return a number present in a regex
	 * 
	 * for instance : *1M* return 1
	 */
	private static int howMany(String regex, String param) throws Exception
		{
		Pattern p = Pattern.compile(regex);
		Pattern pChiffre = Pattern.compile("\\d+");
		Matcher m = p.matcher(param);
		
		if(m.find())
			{
			Matcher mChiffre = pChiffre.matcher(m.group());
			if(mChiffre.find())
				{
				return Integer.parseInt(mChiffre.group());
				}
			}
		return 0;
		}
	
	/**
	 * Method used to find and return 
	 * Character used to split
	 */
	private static String getSplitter(String regex, String param) throws Exception
		{
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(param);
		
		if(m.find())
			{
			String temp = m.group().replace("*", "");
			return temp.split("S")[1];
			}
		throw new Exception();
		}
	
	/**
	 * Method used to check if a directory exists
	 * and if not, to create it
	 */
	public static void createDirectory(String directoryPath) throws Exception
		{
		File myD = new File(directoryPath);
		if(myD.exists())
			{
			//We do nothing
			}
		else
			{
			//We create it
			myD.mkdir();
			Variables.getLogger().info("Directory created : "+directoryPath);
			}
		}
	
	/*2018*//*Alexandre RATEL 8)*/
	}
