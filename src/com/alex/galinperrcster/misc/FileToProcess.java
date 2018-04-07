package com.alex.galinperrcster.misc;

import java.util.ArrayList;

/**
 * File to process
 * @author Alexandre
 *
 */
public class FileToProcess
	{
	/**
	 * Variables
	 */
	private String fileName;
	private String firstLine;
	private ArrayList<String> lines;
	
	public FileToProcess(String fileName, ArrayList<String> lines)
		{
		super();
		this.fileName = fileName;
		this.lines = new ArrayList<String>();
		
		for(int i=0; i<lines.size(); i++)
			{
			if(i==0)
				{
				firstLine = lines.get(i);
				}
			else
				{
				this.lines.add(lines.get(i));
				}
			}
		}

	public String getFileName()
		{
		return fileName;
		}

	public void setFileName(String fileName)
		{
		this.fileName = fileName;
		}

	public ArrayList<String> getLines()
		{
		return lines;
		}

	public void setLines(ArrayList<String> lines)
		{
		this.lines = lines;
		}

	public String getFirstLine()
		{
		return firstLine;
		}

	public void setFirstLine(String firstLine)
		{
		this.firstLine = firstLine;
		}

	
	/*2018*//*Alexandre RATEL 8)*/
	}
