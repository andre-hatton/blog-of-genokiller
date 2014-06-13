package com.genokiller.blogofgenokiller.controllers;

import android.os.Bundle;

public class Search_Controller extends Application_Controller
{
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		new LoadView(this).execute();

	}
}
