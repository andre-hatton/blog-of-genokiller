package com.genokiller.blogofgenokiller.controllers;

import android.os.Bundle;
import android.widget.Toast;

import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.utils.Admin;
import com.genokiller.blogofgenokiller.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

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

        // on passe en mode admin si on est authentifié
        Admin.admin(this);

        // vérifie si ils y a de nouveaux commentaires
        if(Admin.is_admin(this))
        {
            Url response = null;
            try {
                response = new Application_Model(Application_Model.METHOD_GET, this).execute(Url.BASE_URL + "admin/comments.json").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(response != null && response.getStatus() == 200)
            {
                JSONArray json = null;
                try {
                    json = new JSONArray(response.getResult());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(json != null)
                {
                    if(json.length() > 0)
                    {
                        Toast.makeText(this, json.length() + " nouveaux commentaires en attente de validation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

		// chargement en arrière plan
		new LoadView(this).execute();
	}
}
