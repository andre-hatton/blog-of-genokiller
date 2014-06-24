package com.genokiller.blogofgenokiller.models;


public class Article_Model extends Application_Model
{
	/**
	 * Identifiant d'un article
	 */
	private int		id;
	/**
	 * Titre de l'article
	 */
	private String	title;
	/**
	 * Description de l'article
	 */
	private String	description;
	/**
	 * Url de l'image de l'article
	 */
	private String	image_url;
    private int height;
	private String				comment_url;
	private int					comment_count;

	public final static String	ARTILCE		= "article";
	public final static String	ID			= "id";
	public final static String	TITLE		= "title";
	public final static String	DESCRIPTION	= "description";
	public final static String	IMAGE_URL	= "image_url";
	public final static String	COMMENT_URL	= "comment";
	public final static String	COMMENT_COUNT	= "comment_count";
    public final static String  IMAGE_HEIGHT = "height";

	public Article_Model()
	{
        super();
    }
	public Article_Model(int id, String title, String description, String image_url)
	{
		super();
		this.title = title;
		this.description = description;
		this.image_url = image_url;
		this.id = id;
	}

	public Article_Model(int id)
	{
        super(id);
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the image_url
	 */
	public String getImage_url()
	{
		return image_url;
	}
	/**
	 * @param image_url the image_url to set
	 */
	public void setImage_url(String image_url)
	{
		this.image_url = image_url;
	}
	/**
	 * @return the comment_url
	 */
	public String getComment_url()
	{
		return comment_url;
	}
	/**
	 * @param comment_url the comment_url to set
	 */
	public void setComment_url(String comment_url)
	{
		this.comment_url = comment_url;
	}

	/**
	 * @return the comment_count
	 */
	public int getComment_count()
	{
		return comment_count;
	}

	/**
	 * @param comment_count
	 *        the comment_count to set
	 */
	public void setComment_count(int comment_count)
	{
		this.comment_count = comment_count;
	}

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
