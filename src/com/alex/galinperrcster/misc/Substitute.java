package com.alex.galinperrcster.misc;

/**
 * Class used to store a substitute
 * @author Alexandre
 *
 */
public class Substitute
	{
	/**
	 * Variables
	 */
	String pattern,replaceBy;

	public Substitute(String pattern, String replaceBy)
		{
		super();
		this.pattern = pattern;
		this.replaceBy = replaceBy;
		}
	
	public Substitute(String param)
		{
		String[] tab = param.split(":");
		this.pattern = tab[0];
		this.replaceBy = tab[1];
		}

	public String getPattern()
		{
		return pattern;
		}

	public void setPattern(String pattern)
		{
		this.pattern = pattern;
		}

	public String getReplaceBy()
		{
		return replaceBy;
		}

	public void setReplaceBy(String replaceBy)
		{
		this.replaceBy = replaceBy;
		}
	
	
	/*2018*//*Alexandre RATEL 8)*/
	}
