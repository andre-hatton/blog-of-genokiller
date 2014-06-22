package com.genokiller.blogofgenokiller.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.utils.EditTextInfo;
import com.genokiller.blogofgenokiller.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by yoshizuka on 22/06/14.
 * Page d'édition d'un article
 */
public class EditArticle_Controller extends Activity {
    /**
     * Methode appelée a la création de l'activitée
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recuperation de la vue
        LayoutInflater l = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = l.inflate(R.layout.edit_article, null);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.edit_article_layout);

        // recuperation de l'id de l'article passé à la page
        Bundle extras = getIntent().getExtras();
        final int article_id = Integer.parseInt(extras.getString("id"));
        Url show = null;
        EditTextInfo[] listInput = new EditTextInfo[100];
        int j = 0;
        try {
            // on récupere les données de l'article que l'on veut modifier
            show = new Application_Model(Application_Model.METHOD_GET, this).execute(Url.BASE_URL + "admin/articles/" + article_id + ".json").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (show != null) {
            String result = show.getResult();
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(json != null)
            {
                // on lit les données de l'article et on en créer un formulaire
                try {
                    TextView titleLabel = new TextView(this);
                    titleLabel.setText("Titre");
                    layout.addView(titleLabel);

                    EditTextInfo titleEdit = new EditTextInfo(this);
                    titleEdit.setText(json.getString("title"));
                    titleEdit.setMandatory(true);
                    titleEdit.setName("admin_article[title]");
                    listInput[j++] = titleEdit;
                    layout.addView(titleEdit);

                    TextView descriptionLabel = new TextView(this);
                    descriptionLabel.setText("Description");
                    layout.addView(descriptionLabel);

                    EditTextInfo descriptionEdit = new EditTextInfo(this);
                    // en java le br n'est pas interpreté on le change en \n (qui redeviendre br sur le serveur)
                    descriptionEdit.setText(json.getString("description").replace("<br />", "\n"));
                    descriptionEdit.setMandatory(true);
                    descriptionEdit.setName("admin_article[description]");
                    listInput[j++] = descriptionEdit;
                    layout.addView(descriptionEdit);

                    TextView imageLabel = new TextView(this);
                    imageLabel.setText("Url de l'image");
                    layout.addView(imageLabel);

                    EditTextInfo imageEdit = new EditTextInfo(this);
                    imageEdit.setText(json.getString("image_url"));
                    imageEdit.setMandatory(true);
                    imageEdit.setName("admin_article[remote_image_url]");
                    listInput[j++] = imageEdit;
                    layout.addView(imageEdit);

                    // parcours des infos
                    JSONArray infos = json.getJSONArray("infonames");
                    for(int i = 0; i < infos.length(); i++)
                    {
                        JSONObject info = infos.getJSONObject(i);
                        final int infoname_id = info.getInt("id");
                        final boolean mandatory = info.getBoolean("mandatory");
                        final String info_name = info.getString("info");
                        final String long_name = info.getString("long_name");
                        final String name = info.getString("name");
                        final String str_entier = info.getString("entier");
                        int int_entier = -1;
                        if(!str_entier.equals("null")) {
                            int_entier = Integer.parseInt(str_entier);
                        }
                        final int entier = int_entier;
                        final int info_id = info.getInt("info_id");

                        TextView label = new TextView(this);
                        label.setText(info_name);
                        layout.addView(label);

                        EditTextInfo edit = new EditTextInfo(this);
                        edit.setMandatory(mandatory);
                        edit.setInfo_id(info_id);
                        edit.setName_info_id("admin_article[infonames_attributes][" + info_id + "][info_id]");
                        edit.setInfoname_id(infoname_id);
                        edit.setName_infoname_id("admin_article[infonames_attributes][" + info_id + "][id]");
                        if(!long_name.isEmpty())
                        {
                            edit.setText(long_name);
                            edit.setName("admin_article[infonames_attributes][" + info_id + "][long_name]");
                            edit.setInfo_type("text");
                        }
                        else if(!name.isEmpty())
                        {
                            edit.setText(name);
                            edit.setName("admin_article[infonames_attributes][" +info_id + "][name]");
                            edit.setInfo_type("string");
                        }
                        else if(entier > -1)
                        {
                            edit.setText(String.valueOf(entier));
                            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                            edit.setName("admin_article[infonames_attributes][" +info_id + "][entier]");
                            edit.setInfo_type("integer");
                        }
                        listInput[j++] = edit;
                        layout.addView(edit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final EditTextInfo[] listInfo = listInput;
            final Context context = this;
            Button submit = new Button(this);
            submit.setText("Mettre à jour");
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] params = new String[listInfo.length * 10 + 8];
                    params[0] = Url.BASE_URL + "admin/articles/" + article_id + ".json";
                    boolean submit = true;
                    int j = 1;
                    // creation des parametres a envoyer
                    for(int i = 0; i < listInfo.length; i++)
                    {
                        if(listInfo[i] == null)
                            break;
                        if(listInfo[i].isMandatory() && listInfo[i].getText().toString().trim().length() <= 0)
                            submit = false;
                        params[j++] = listInfo[i].getName_info_id();
                        params[j++] = String.valueOf(listInfo[i].getInfo_id());
                        params[j++] = listInfo[i].getName();
                        params[j++] = listInfo[i].getText().toString().trim();
                        params[j++] = listInfo[i].getName_infoname_id();
                        params[j++] = String.valueOf(listInfo[i].getInfoname_id());
                        if(listInfo[i].has_type_info())
                        {
                            if(listInfo[i].getInfo_type().equals("integer"))
                            {
                                params[j++] = listInfo[i].getName().replace("[entier]", "[name]");
                                params[j++] = "";
                                params[j++] = listInfo[i].getName().replace("[entier]", "[long_name]");
                                params[j++] = "";
                            }
                            else if(listInfo[i].getInfo_type().equals("string"))
                            {
                                params[j++] = listInfo[i].getName().replace("[name]", "[entier]");
                                params[j++] = "";
                                params[j++] = listInfo[i].getName().replace("[name]", "[long_name]");
                                params[j++] = "";
                            }
                            else if(listInfo[i].getInfo_type().equals("text"))
                            {
                                params[j++] = listInfo[i].getName().replace("[long_name]", "[entier]");
                                params[j++] = "";
                                params[j++] = listInfo[i].getName().replace("[long_name]", "[name]");
                                params[j++] = "";
                            }
                        }
                    }
                    params[j++] = "admin_article[avis]";
                    params[j++] = "";
                    params[j++] = "admin_article[type_id]";
                    params[j++] = "1";
                    params[j++] = "admin_article[author_id]";
                    params[j++] = "1";
                    params[j++] = "commit";
                    params[j++] = "Update Article";

                    if(submit == true)
                    {
                        Url result = null;
                        try {
                            result = new Application_Model(Application_Model.METHOD_PUT, context).execute(params).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // en cas de success de modification le status HTTP est 204
                        if(result.getStatus() == 204)
                        {
                            Toast.makeText(context, "Enregistrement effectué", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, Articles_Controller.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.xml.translate_left_center, R.xml.translate_center_right);
                        }
                        else if(result.getStatus() == 422)
                            Toast.makeText(context, "Vérifier les parametres, l'URL est elle correct ?", Toast.LENGTH_LONG).show();
                        else if(result.getStatus() > 399)
                            Toast.makeText(context, "Uns erreur est survenue", Toast.LENGTH_LONG).show();
                        else if (result.getStatus() > 500)
                            Toast.makeText(context, "Le serveur ne répond pas", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context, "Parametres obligatoire", Toast.LENGTH_LONG).show();
                    }
                }
            });
            layout.addView(submit);
        }


        setContentView(view);
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
}
