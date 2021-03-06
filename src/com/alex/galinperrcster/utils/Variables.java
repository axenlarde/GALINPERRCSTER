package com.alex.galinperrcster.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Class used to store the static variables
 * @author Alexandre
 *
 */
public class Variables
	{
	/**
	 * Variables
	 */
	private static String softwareName;
	private static String softwareVersion;
	private static Logger logger;
	private static ArrayList<String[][]> tabConfig;
	private static String mainDirectory;
	private static String configFileName;
	private static ArrayList<String> allowedFiles;
	
	/**
	 * Constructor
	 */
	public Variables()
		{
		mainDirectory = "./";
		configFileName = "configFile.xml";
		allowedFiles = new ArrayList<String>();
		}

	public static String getSoftwareName()
		{
		return softwareName;
		}

	public static void setSoftwareName(String softwareName)
		{
		Variables.softwareName = softwareName;
		}

	public static String getSoftwareVersion()
		{
		return softwareVersion;
		}

	public static void setSoftwareVersion(String softwareVersion)
		{
		Variables.softwareVersion = softwareVersion;
		}

	public static Logger getLogger()
		{
		return logger;
		}

	public static void setLogger(Logger logger)
		{
		Variables.logger = logger;
		}

	public static ArrayList<String[][]> getTabConfig()
		{
		return tabConfig;
		}

	public static void setTabConfig(ArrayList<String[][]> tabConfig)
		{
		Variables.tabConfig = tabConfig;
		}

	public static String getMainDirectory()
		{
		return mainDirectory;
		}

	public static void setMainDirectory(String mainDirectory)
		{
		Variables.mainDirectory = mainDirectory;
		}

	public static String getConfigFileName()
		{
		return configFileName;
		}

	public static void setConfigFileName(String configFileName)
		{
		Variables.configFileName = configFileName;
		}

	public static ArrayList<String> getAllowedFiles()
		{
		return allowedFiles;
		}

	public static void setAllowedFiles(ArrayList<String> allowedFiles)
		{
		Variables.allowedFiles = allowedFiles;
		}
	
	
	
	
	/*2018*//*Alexandre RATEL 8)*/
	}
