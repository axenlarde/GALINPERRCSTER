package com.alex.galinperrcster.misc;

import java.util.ArrayList;

/**
 * Class used to host a substitute template
 * @author Alexandre
 *
 */
public class SubstituteTemplate
	{
	/**
	 * Variables
	 */
	private String name;
	private ArrayList<Substitute> substituteList;
	
	public SubstituteTemplate(String name, ArrayList<Substitute> substituteList)
		{
		super();
		this.name = name;
		this.substituteList = substituteList;
		}

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public ArrayList<Substitute> getSubstituteList()
		{
		return substituteList;
		}

	public void setSubstituteList(ArrayList<Substitute> substituteList)
		{
		this.substituteList = substituteList;
		}

	/*2018*//*Alexandre RATEL 8)*/
	}
