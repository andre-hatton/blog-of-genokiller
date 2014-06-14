package com.genokiller.blogofgenokiller.controllers;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Date;

/**
 * 
 * @author genokiller
 * @date 2301-10-01 Activité principal permettant de lister les articles du blog
 * 
 */
public class Articles_Controller extends Application_Controller
{

	/**
	 * Methode appelée a la création de l'activitée
	 */
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences("admin", 0);
        SharedPreferences.Editor editor = settings.edit();
        long time = settings.getLong("timestamp", 0);
        Date date = new Date();
        long current_time = date.getTime();
        if(time == 0 || current_time - time > 24 * 60 * 60 * 1000)
            editor.putBoolean("is_admin", false);

        // Commit the edits!
        editor.commit();

		// chargement en arrière plan
		new LoadView(this).execute();
	}
}
