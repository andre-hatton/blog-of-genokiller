package com.genokiller.blogofgenokiller.models;

public class Comment_Model extends Application_Model
{
	private int		id;
	private String	pseudo;
	private String				description;
	private boolean				accept;

	public final static String	ID			= "id";
	public final static String	PSEUDO		= "pseudo";
	public final static String	DESCRIPTION	= "description";
	public final static String	ACCEPT		= "accept";

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 *        the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo()
	{
		return pseudo;
	}

	/**
	 * @param pseudo
	 *        the pseudo to set
	 */
	public void setPseudo(String pseudo)
	{
		this.pseudo = pseudo;
	}

	/**
	 * @return the comment
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param comment
	 *        the comment to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the accept
	 */
	public boolean isAccept()
	{
		return accept;
	}

	/**
	 * @param accept the accept to set
	 */
	public void setAccept(boolean accept)
	{
		this.accept = accept;
	}
}
