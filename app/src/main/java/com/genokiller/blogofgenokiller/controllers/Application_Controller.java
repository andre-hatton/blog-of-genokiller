package com.genokiller.blogofgenokiller.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.genokiller.blogofgenokiller.helpers.Application_Helper;
import com.genokiller.blogofgenokiller.models.Article_Model;
import com.genokiller.blogofgenokiller.utils.Admin;
import com.genokiller.blogofgenokiller.utils.Item;
import com.genokiller.blogofgenokiller.utils.Url;
import com.genokiller.blogofgenokiller.views.Applications;
import com.genokiller.blogofgenokiller.views.Applications.OnLoadMoreListener;
import com.genokiller.blogofgenokiller.views.Articles;

public class Application_Controller extends ListActivity
{
	/**
	 * Liste des articles récupérés
	 */
	protected ArrayList<HashMap<String, Item>>	articleList		= new ArrayList<HashMap<String, Item>>();
	/**
	 * Lien au model des articles
	 */
	protected Article_Model							article;
	/**
	 * Lien a la vue des articles
	 */
	protected Applications							articles;
	/**
	 * Retour json dans un tableau
	 */
	protected JSONArray								jsonArray;
	/**
	 * position en X lors de l'appui sur l'ecran tactile
	 */
	protected float									firstX			= 0f;
	/**
	 * Taille de l'ecran en largeur
	 */
	protected int									width_screen;
	/**
	 * Page du site chargé
	 */
	protected int									page			= 1;
	/**
	 * Permet de savoir si on est a la fin des articles
	 */
	protected boolean								end				= false;
	/**
	 * Permet de savoir si une recherche a été demandé
	 */
	protected boolean								has_search		= false;
	/**
	 * Données d'une recherche
	 */
	protected String								search			= "";
	/**
	 * Nombre de page sur le site
	 */
	protected int									max_page;
	/**
	 * Previens que la fin a été atteinte
	 */
	protected boolean								isEnd			= false;
	/**
	 * Listes des données json récupérées
	 */
	protected ArrayList<HashMap<String, Item>>	maps;
    private boolean is_admin = false;

	/**
	 * Données affichées dans la vue
	 */
	private void loadFinish()
	{
		setContentView(R.layout.activity_article);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width_screen = metrics.widthPixels;

		articles = (Articles) findViewById(android.R.id.list);
		articles.setActivity(this);
		articles.setWidthScreen(width_screen);
		articles.setPage(page);
		articles.setAdapter(new Application_Helper(this, maps, this));
		articles.setSearch(search);
		articles.setMax_page(max_page);

		final Application_Controller main = this;
		// chargement de la suite des articles
		((Articles) getListView()).setOnLoadMoreListener(new OnLoadMoreListener()
		{

			@Override
			public void onLoadMore()
			{
				if (!isEnd)
					new LoadDataTask(main).execute();
			}

			@Override
			public void onLoadMoreComplete()
			{

			}
		});
	}

	/**
	 * Récupération des données json dans notre map
	 * 
	 * @return Le liste des données json
	 */
	public ArrayList<HashMap<String, Item>> getMap()
	{
        is_admin = Admin.is_admin(this);

		article = new Article_Model();
		// charge dans un thread secondaire
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// creation de la page appelé
		String url = Url.BASE_URL + "articles/" + page + "/10.json";
		// données json trouvées
		Url json = null;
		if (has_search)
			try
			{
				url = Url.BASE_URL + "articles/" + page + "/10.json?search[title_or_description_cont]=" + URLEncoder.encode(search, "UTF-8");
			}
			catch (UnsupportedEncodingException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        json = article.setContext(this).getJsonString(url);
        Log.d("RESULT", json.getResult() + " " + json.getStatus());
        try
		{
			// Getting Array of Contacts

			jsonArray = new JSONArray(json.getResult());
			if (jsonArray.length() < 1)
			{
				isEnd = true;
				try
				{
					// supprime le footer si aucun autre article
					articles.removeFooterView(articles.getFooter());
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}
			}
			else
				isEnd = false;

			// parcours les données json
			for (int i = 0; i < jsonArray.length(); i++)
			{

				JSONObject c = jsonArray.getJSONObject(i);

				// Storing each json item in variable
				max_page = c.getInt("max");
				String title = c.getString(Article_Model.TITLE);
				String description = c.getString(Article_Model.DESCRIPTION).replaceAll("<br />", "\n");
				String image_url = c.getString(Article_Model.IMAGE_URL);
				String comment_url = c.getString(Article_Model.COMMENT_URL);
				int comment_count = c.getInt(Article_Model.COMMENT_COUNT);
				int id = c.getInt(Article_Model.ID);
				HashMap<String, Item> map = new HashMap<String, Item>();
				// adding HashList to ArrayList
				map.put(Article_Model.ID, new Item(String.valueOf(id)));
				map.put(Article_Model.TITLE, new Item(title));
				map.put(Article_Model.DESCRIPTION, new Item(description));
				map.put(Article_Model.IMAGE_URL, new Item(image_url));
				map.put(Article_Model.COMMENT_URL, new Item(comment_url));
				map.put(Article_Model.COMMENT_COUNT, new Item(String.valueOf(comment_count)));

				JSONArray jsArray = c.getJSONArray("infonames");
                Button more = new Button(this);
                Button less = new Button(this);
                for (int j = 0; j < jsArray.length(); j++)
				{
					JSONObject jsObject = jsArray.getJSONObject(j);
                    if (!is_admin && !jsObject.getBoolean("admin"))
                    {
                        String info = jsObject.getString("info");
                        if (jsObject.getString("name") != null && !jsObject.getString("name").equals("") && !jsObject.getString("name").equals("null"))
                            if(is_admin)
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("name"), jsObject.getInt("id"), Item.BUTTON_EDIT, "name"));
                            else
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("name"), jsObject.getInt("id"), "name"));
                        else if (jsObject.getString("entier") != null && !jsObject.getString("entier").equals("") && !jsObject.getString("entier").equals("null"))
                        {
                            if(is_admin)
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("entier"), jsObject.getInt("id"), Item.BUTTON_MORE_LESS_EDIT, "int"));
                            else
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("entier"), jsObject.getInt("id"), "int"));
                        }
                        else if (jsObject.getString("long_name") != null && !jsObject.getString("long_name").equals("") && !jsObject.getString("long_name").equals("null"))
                            if(is_admin)
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("long_name"), jsObject.getInt("id"), Item.BUTTON_EDIT, "long_name"));
                            else
                                map.put("info_" + j, new Item(info + " : " + jsObject.getString("long_name"), jsObject.getInt("id"), "long_name"));
                    }
                    else if (is_admin)
                    {
                        String info = jsObject.getString("info");
                        if (jsObject.getString("name") != null && !jsObject.getString("name").equals("") && !jsObject.getString("name").equals("null"))
                            map.put("info_" + j, new Item(info + " : " + jsObject.getString("name"), jsObject.getInt("id"), Item.BUTTON_EDIT, "name"));
                        else if (jsObject.getString("entier") != null && !jsObject.getString("entier").equals("") && !jsObject.getString("entier").equals("null"))
                            map.put("info_" + j, new Item(info + " : " + jsObject.getString("entier"), jsObject.getInt("id"), Item.BUTTON_MORE_LESS_EDIT, "int"));
                        else if (jsObject.getString("long_name") != null && !jsObject.getString("long_name").equals("") && !jsObject.getString("long_name").equals("null"))
                            map.put("info_" + j, new Item(info + " : " + jsObject.getString("long_name"), jsObject.getInt("id"), Item.BUTTON_EDIT, "long_name"));
                    }
				}
				articleList.add(map);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return articleList;
	}

	/**
	 * Action sur l'écran tactile
	 */
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			firstX = event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			float lastX = event.getX();
			if (Math.abs(firstX - lastX) > width_screen / 2)
			{
				if (firstX > lastX)
				{
					if (max_page > page)
					{
						Intent intent = null;
						if (has_search)
							intent = new Intent(this, Search_Controller.class);
						else
							intent = new Intent(this, Articles_Controller.class);
						Bundle bundle = new Bundle();
						bundle.putInt("page", page + 1);
						bundle.putString("search", search);
						bundle.putInt("max_page", max_page);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
						overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
					}
				}
				else
				{
					if (page > 1)
					{
						Intent intent = new Intent(Application_Controller.this, Articles_Controller.class);
						Bundle bundle = new Bundle();
						bundle.putInt("page", page - 1);
						bundle.putString("search", search);
						bundle.putInt("max_page", max_page);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
						overridePendingTransition(R.xml.translate_left_center, R.xml.translate_center_right);
					}
				}
			}
			firstX = 0f;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.articles__controller, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, Articles_Controller.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.xml.translate_left_center, R.xml.translate_center_right);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Charge les données json puis les ajoute dans la liste
	 * 
	 * @author genokiller
	 * 
	 */
	private class LoadDataTask extends AsyncTask<Void, Void, Void>
	{
		private Application_Controller	main;
		private int					position	= 0;
		private int					y			= 0;

		public LoadDataTask(Application_Controller main2)
		{
			this.main = main2;
		}

		public void onPreExecute()
		{
			position = articles.getLastVisiblePosition();
			View v = articles.getChildAt(0);
			y = v.getTop();
		}

		@Override
		protected Void doInBackground(Void... params)
		{

			if (isCancelled())
			{
				return null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if (!isEnd)
			{
				page++;
				ArrayList<HashMap<String, Item>> maps = getMap();
				articles.setAdapter(new Application_Helper(main, maps, main));
				articles.setSelectionFromTop(position - 1, y);
			}
			else
			{
				articles.removeFooterView(articles);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled()
		{}
	}

	/**
	 * Chargement de l'application
	 * 
	 * @author genokiller
	 * 
	 */
	class LoadView extends AsyncTask<Void, Void, Void>
	{

		private ProgressDialog	load_dialog;
		private Context			context;

		public LoadView(Context context)
		{
			this.context = context;
		}

		protected void onPreExecute()
		{
			super.onPreExecute();

			load_dialog = new ProgressDialog(context, ProgressDialog.STYLE_HORIZONTAL);
			load_dialog.setMessage("chargement...");
			try
			{
				load_dialog.show();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			ActionBar actionBar = getActionBar();
			actionBar.setTitle("");
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				synchronized (this)
				{
					Bundle bundle = getIntent().getExtras();
					if (bundle != null && bundle.getInt("page") > 0)
					{
						page = bundle.getInt("page");
						search = bundle.getString("search");
                        max_page = bundle.getInt("max_page", 1000);
						if (search == null || search.equals(""))
							has_search = false;
						else
							has_search = true;
					}
					maps = getMap();
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result)
		{
			load_dialog.dismiss();
			loadFinish();
			super.onPostExecute(result);
		}
	}
}
