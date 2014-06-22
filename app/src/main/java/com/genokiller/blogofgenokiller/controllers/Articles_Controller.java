package com.genokiller.blogofgenokiller.controllers;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.genokiller.blogofgenokiller.utils.Admin;

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

        Admin.admin(this);

		// chargement en arrière plan
		new LoadView(this).execute();
	}
}
