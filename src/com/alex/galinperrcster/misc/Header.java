package com.alex.galinperrcster.misc;

/**
 * Header class
 * @author Alexandre RATEL
 *
 */
public class Header
	{
	/**
	 * Variables
	 */
	private String header,templateName;

	
	public Header(String header, String templateName)
		{
		super();
		this.header = header;
		this.templateName = templateName;
		}

	public Header(String param)
		{
		String[] tab = param.split(":");
		this.header = tab[0];
		this.templateName = tab[1];
		}

	public String getHeader()
		{
		return header;
		}

	public void setHeader(String header)
		{
		this.header = header;
		}

	public String getTemplateName()
		{
		return templateName;
		}

	public void setTemplateName(String templateName)
		{
		this.templateName = templateName;
		}
	
	/*2018*//*Alexandre RATEL 8)*/
	}
