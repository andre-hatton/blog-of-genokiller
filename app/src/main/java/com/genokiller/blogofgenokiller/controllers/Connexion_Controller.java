package com.genokiller.blogofgenokiller.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.utils.Url;

import java.util.concurrent.ExecutionException;


/**
 * Created by yoshizuka on 14/06/14.
 * Page caché permettant de ce connecter à l'admin
 */
public class Connexion_Controller extends Activity {
    /**
     * Bouton de validation
     */
    private Button submit;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion);

        submit = (Button)findViewById(R.id.connexion_submit);
        final Context context = this;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email, password;
                String email_text, password_text, is_checked;
                CheckBox remember;
                email = (EditText)findViewById(R.id.connexion_email);
                password = (EditText)findViewById(R.id.connexion_password);
                remember = (CheckBox)findViewById(R.id.connexion_remember);

                // recuperation des données à envoyer
                email_text = email.getText().toString();
                password_text = password.getText().toString();
                is_checked = remember.isChecked() ? "1" : "0";
                Url result = null;
                String[] params = {Url.BASE_URL + "users/sign_in", "user[email]", email_text, "user[password]", password_text, "user[remember_me]", is_checked, "utf8", "1"};
                try {
                    result = new Application_Model(Application_Model.METHOD_POST).setContext(context).execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                SharedPreferences settings = getSharedPreferences("admin", 0);

                // on vérifie si la connexion a réussi
                if(settings.getBoolean("is_admin", false) && result.getResult().contains("<a href=\"/admin\">Admin</a>"))
                {
                    Intent intent = new Intent(context, Articles_Controller.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
                }
                else
                {
                    Toast.makeText(context, "Connexion échouée", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
