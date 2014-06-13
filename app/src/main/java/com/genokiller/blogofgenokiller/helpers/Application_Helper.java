package com.genokiller.blogofgenokiller.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.genokiller.Blog_Application;
import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.models.Article_Model;
import com.genokiller.blogofgenokiller.utils.Comment;
import com.novoda.imageloader.core.model.ImageTagFactory;

public class Application_Helper extends ArrayAdapter<HashMap<String, String>>
{
	private final Activity								context;

	private LayoutInflater								layoutInflater;
	private final ArrayList<HashMap<String, String>>	maps;
	ImageTagFactory										imageTagFactory;
	private Application_Controller						main;
	private String[]									infonames	= new String[10];

	static class ViewHolder
	{
		public TextView		description;
		public TextView		title;
		public ImageView	image_url;
		public TextView		info_0, info_1, info_2, info_3, info_4, info_5,
							info_6, info_7, info_8, info_9;
		public TextView		comment_url;
	}

	public Application_Helper(Activity context, ArrayList<HashMap<String, String>> maps, Application_Controller application_Controller)
	{
		super(context, R.layout.activity_article, maps);
		this.context = context;
		this.maps = maps;
		this.main = application_Controller;
		layoutInflater = LayoutInflater.from(application_Controller);
		/* Image Loader */

		imageTagFactory = ImageTagFactory.newInstance(context, R.id.description);
		imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
	}

	@SuppressWarnings(
	{ "unused" })
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			layoutInflater = context.getLayoutInflater();
			convertView = layoutInflater.inflate(R.layout.activity_article, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.description = (TextView) convertView.findViewById(R.id.description);
			viewHolder.image_url = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.info_0 = (TextView) convertView.findViewById(R.id.info_0);
			viewHolder.info_1 = (TextView) convertView.findViewById(R.id.info_1);
			viewHolder.info_2 = (TextView) convertView.findViewById(R.id.info_2);
			viewHolder.info_3 = (TextView) convertView.findViewById(R.id.info_3);
			viewHolder.info_4 = (TextView) convertView.findViewById(R.id.info_4);
			viewHolder.info_5 = (TextView) convertView.findViewById(R.id.info_5);
			viewHolder.info_6 = (TextView) convertView.findViewById(R.id.info_6);
			viewHolder.info_7 = (TextView) convertView.findViewById(R.id.info_7);
			viewHolder.info_8 = (TextView) convertView.findViewById(R.id.info_8);
			viewHolder.info_9 = (TextView) convertView.findViewById(R.id.info_9);
			viewHolder.comment_url = (TextView) convertView.findViewById(R.id.comment);

			convertView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		String description = maps.get(position).get(Article_Model.DESCRIPTION);
		final String title = maps.get(position).get(Article_Model.TITLE);
		String image_url = maps.get(position).get(Article_Model.IMAGE_URL);
		final String comment_url = maps.get(position).get(Article_Model.COMMENT_URL);
		String comment_count = maps.get(position).get(Article_Model.COMMENT_COUNT);
		final String article_id = maps.get(position).get(Article_Model.ID);

		for (int i = 0; i < 10; i++)
		{
			infonames[i] = null;
		}
		for (int i = 0; i < maps.get(position).size(); i++)
		{
			if (maps.get(position).get("info_" + i) != null && !maps.get(position).get("info_" + i).equals(""))
				infonames[i] = maps.get(position).get("info_" + i);
		}

		String[] images = image_url.split("/");

		holder.title.setText(title);
		holder.description.setText(description);
		holder.image_url.setTag(imageTagFactory.build(image_url, main));
		holder.comment_url.setText("Voir les commentaires (" + comment_count + ")");
		holder.comment_url.setTag(comment_url);
		// action du popu des commentaires
		holder.comment_url.setOnClickListener(new Comment(main, context, title, comment_url, article_id));

		if (infonames[0] != null)
		{
			holder.info_0.setVisibility(View.VISIBLE);
			holder.info_0.setText(infonames[0]);
		}
		else
			holder.info_0.setVisibility(View.GONE);
		if (infonames[1] != null)
		{
			holder.info_1.setVisibility(View.VISIBLE);
			holder.info_1.setText(infonames[1]);
		}
		else
			holder.info_1.setVisibility(View.GONE);
		if (infonames[2] != null)
		{
			holder.info_2.setVisibility(View.VISIBLE);
			holder.info_2.setText(infonames[2]);
		}
		else
			holder.info_2.setVisibility(View.GONE);
		if (infonames[3] != null)
		{
			holder.info_3.setVisibility(View.VISIBLE);
			holder.info_3.setText(infonames[3]);
		}
		else
			holder.info_3.setVisibility(View.GONE);
		if (infonames[4] != null)
		{
			holder.info_4.setVisibility(View.VISIBLE);
			holder.info_4.setText(infonames[4]);
		}
		else
			holder.info_4.setVisibility(View.GONE);
		if (infonames[5] != null)
		{
			holder.info_5.setVisibility(View.VISIBLE);
			holder.info_5.setText(infonames[5]);
		}
		else
			holder.info_5.setVisibility(View.GONE);
		if (infonames[6] != null)
		{
			holder.info_6.setVisibility(View.VISIBLE);
			holder.info_6.setText(infonames[6]);
		}
		else
			holder.info_6.setVisibility(View.GONE);
		if (infonames[7] != null)
		{
			holder.info_7.setVisibility(View.VISIBLE);
			holder.info_7.setText(infonames[7]);
		}
		else
			holder.info_7.setVisibility(View.GONE);
		if (infonames[8] != null)
		{
			holder.info_8.setVisibility(View.VISIBLE);
			holder.info_8.setText(infonames[8]);
		}
		else
			holder.info_8.setVisibility(View.GONE);
		if (infonames[9] != null)
		{
			holder.info_9.setVisibility(View.VISIBLE);
			holder.info_9.setText(infonames[9]);
		}
		else
			holder.info_9.setVisibility(View.GONE);

		((ImageView) holder.image_url).setTag(imageTagFactory.build(image_url, context));

		try
		{
			Blog_Application.getImageLoader().getLoader().load(holder.image_url);
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		// holder.image_url.setImageBitmap(bmp);

		return convertView;
	}

}