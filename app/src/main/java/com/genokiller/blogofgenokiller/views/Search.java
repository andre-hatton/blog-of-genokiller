package com.genokiller.blogofgenokiller.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.genokiller.blogofgenokiller.controllers.Connexion_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.controllers.Search_Controller;

public class Search extends SearchView
{

	public Search(Context context)
	{
		super(context);
		final Context c = context;

		// soumet la recherche au controleur
		setOnQueryTextListener(new OnQueryTextListener()
		{

			@Override
			public boolean onQueryTextSubmit(String query)
			{
                SharedPreferences settings = c.getSharedPreferences("admin", 0);

                if(!settings.getBoolean("is_admin", false) && (query.toLowerCase().trim().equals("connexion admin") || query.toLowerCase().trim().equals("connexion administration") || query.toLowerCase().trim().equals("connection admin") || query.toLowerCase().trim().equals("connection administration")))
                {
                    Intent intent = new Intent(c, Connexion_Controller.class);
                    Bundle bundle = new Bundle();
                    c.startActivity(intent);
                    ((Activity) c).finish();
                    ((Activity) c).overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
                    setVisibility(View.GONE);

                }
                else
                {
                    Intent intent = new Intent(c, Search_Controller.class);
                    Bundle bundle = new Bundle();
                    // revien sur la premiere page
                    bundle.putInt("page", 1);
                    bundle.putString("search", query);
                    intent.putExtras(bundle);
                    c.startActivity(intent);
                    ((Activity) c).finish();
                    ((Activity) c).overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
                    setVisibility(View.GONE);
                }
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
	}


}
