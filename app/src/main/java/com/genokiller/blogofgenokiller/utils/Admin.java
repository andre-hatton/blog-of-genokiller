package com.genokiller.blogofgenokiller.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.genokiller.blogofgenokiller.models.Application_Model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/**
 * Created by yoshizuka on 18/06/14.
 */
public class Admin {
    /**
     * Vérifie si l'utilisateur est administrateur
     * @param context
     */
    public static void admin(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("admin", 0);
        SharedPreferences.Editor editor = settings.edit();

        boolean is_admin = false;
        String result = null;
        try {
            result = new Application_Model(Application_Model.METHOD_GET).setContext(context).execute(new String[]{Url.BASE_URL + "admin/authors.json"}).get().getResult();
            if(result != null)
            {
                try {
                    JSONArray json = new JSONArray(result);
                    is_admin = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        editor.putBoolean("is_admin", is_admin);

        // Commit the edits!
        editor.commit();
    }

    /**
     * Défini si l'utilisateur est un admin
     * @param context
     * @return true si administrateur
     */
    public static boolean is_admin(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences("admin", 0);
        return settings.getBoolean("is_admin", false);
    }
}
