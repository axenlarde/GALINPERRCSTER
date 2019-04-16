package com.alex.galinperrcster.main;

import org.apache.log4j.Level;

import com.alex.galinperrcster.action.Worker;
import com.alex.galinperrcster.utils.InitLogging;
import com.alex.galinperrcster.utils.UsefulMethod;
import com.alex.galinperrcster.utils.Variables;


/**
 * Main class of Galinperrcster
 * 
 * This software aims to be a powerful string replacer
 * 
 * @author Alexandre
 *
 */
public class Main
	{
	/**
	 * Variables
	 */
	
	/***************
	 * Constructor
	 ***************/
	public Main()
		{
		//Set the software name
		Variables.setSoftwareName("GALINPERRCSTER");
		//Set the software version
		Variables.setSoftwareVersion("1.0");
		
		/****************
		 * Initialization of the logging
		 */
		Variables.setLogger(InitLogging.init(Variables.getSoftwareName()+"_LogFile.txt"));
		Variables.getLogger().info("\r\n");
		Variables.getLogger().info("#### Entering application");
		Variables.getLogger().info("## Welcome to : "+Variables.getSoftwareName()+" version "+Variables.getSoftwareVersion());
		Variables.getLogger().info("## Author : RATEL Alexandre : 2017");
		/*******/
		
		/******
		 * Initialization of the variables
		 */
		new Variables();
		/************/
		
		/**********************
		 * Reading of the configuration file
		 */
		try
			{
			//Config files reading
			Variables.setTabConfig(UsefulMethod.readMainConfigFile(Variables.getConfigFileName()));
			}
		catch(Exception exc)
			{
			UsefulMethod.failedToInit(exc);
			}
		/********************/
		
		/*****************
		 * Setting of the inside variables from what we read in the configuration file
		 */
		try
			{
			UsefulMethod.initInternalVariables();
			}
		catch(Exception exc)
			{
			Variables.getLogger().error(exc.getMessage());
			Variables.getLogger().setLevel(Level.INFO);
			}
		/*********************/
		
		/*********************
		 * We launch the main method
		 */
		new Worker();
		/*******************/
		}
	
	
	
	public static void main(String[] args)
		{
		new Main();
		}

	/*2018*//*Alexandre RATEL 8)*/
	}
