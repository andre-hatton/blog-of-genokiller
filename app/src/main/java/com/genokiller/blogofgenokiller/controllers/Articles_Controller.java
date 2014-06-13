package com.genokiller.blogofgenokiller.controllers;

import android.os.Bundle;

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

		// chargement en arrière plan
		new LoadView(this).execute();
	}
}
