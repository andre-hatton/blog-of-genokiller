package com.genokiller.blogofgenokiller.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
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
 */
public class EditArticle_Controller extends Activity {
    /**
     * Methode appelée a la création de l'activitée
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Url result = null;
        try {
            result = new Application_Model(Application_Model.METHOD_GET, getApplicationContext()).execute(new String[]{Url.BASE_URL + "admin/articles/1/getInfos.json"}).get();
            Log.d("DATA", "" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONArray json = null;
        try {
            json = new JSONArray(result.getResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        EditTextInfo[] listInput = new EditTextInfo[json.length() + 3];

        LayoutInflater l = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = l.inflate(R.layout.create_article, null);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.create_article_layout);

        TextView titleLabel = new TextView(this);
        titleLabel.setText("Title");
        layout.addView(titleLabel);

        EditTextInfo titleEdit = new EditTextInfo(this);
        titleEdit.setName("admin_article[title]");
        titleEdit.setMandatory(true);
        listInput[0] = titleEdit;
        layout.addView(titleEdit);

        TextView descriptionLabel = new TextView(this);
        descriptionLabel.setText("Description");
        layout.addView(descriptionLabel);

        EditTextInfo descriptionEdit = new EditTextInfo(this);
        descriptionEdit.setMandatory(true);
        descriptionEdit.setName("admin_article[description]");
        descriptionEdit.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        listInput[1] = descriptionEdit;
        layout.addView(descriptionEdit);

        TextView urlLabel = new TextView(this);
        urlLabel.setText("Url de l'image");
        layout.addView(urlLabel);

        EditTextInfo urlEdit = new EditTextInfo(this);
        urlEdit.setName("admin_article[remote_image_url]");
        urlEdit.setMandatory(true);
        listInput[2] = urlEdit;
        layout.addView(urlEdit);

        int j = 3;
        for(int i = 0; i < json.length(); i++)
        {
            JSONObject object = null;
            try {
                object = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String label = null;
            String type_info = null;
            boolean mandatory = false;
            int id = 0;
            try {
                label = object.getString("name");
                type_info = object.getString("type_info");
                mandatory = object.getBoolean("mandatory");
                id = object.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(label != null)
            {
                TextView labelText = new TextView(this);
                labelText.setText(label);
                layout.addView(labelText);
                EditTextInfo info = new EditTextInfo(this);
                if(type_info.equals("string"))
                {
                    info.setInputType(InputType.TYPE_CLASS_TEXT);
                    info.setName("admin_article[infonames_attributes][" + id + "][name]");
                }
                else if(type_info.equals("integer"))
                {
                    info.setInputType(InputType.TYPE_CLASS_NUMBER);
                    info.setName("admin_article[infonames_attributes][" + id + "][entier]");
                }
                else if(type_info.equals("text"))
                {
                    info.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    info.setName("admin_article[infonames_attributes][" + id + "][long_name]");
                }
                info.setInfo_type(type_info);
                info.setName_info_id("admin_article[infonames_attributes][" + id + "][info_id]");
                info.setInfo_id(id);
                info.setMandatory(mandatory);
                listInput[j] = info;
                j++;
                layout.addView(info);

            }
        }

        final EditTextInfo[] listInfo = listInput;
        final Context context = this;

        Button valid = new Button(this);
        valid.setText("Valider");
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] params = new String[listInfo.length * 8 + 9];
                params[0] = Url.BASE_URL + "admin/articles.json";
                boolean submit = true;
                int j = 1;
                for(int i = 0; i < listInfo.length; i++)
                {
                    if(listInfo[i].isMandatory() && listInfo[i].getText().toString().trim().length() <= 0)
                        submit = false;
                    params[j++] = listInfo[i].getName_info_id();
                    params[j++] = String.valueOf(listInfo[i].getInfo_id());
                    params[j++] = listInfo[i].getName();
                    params[j++] = listInfo[i].getText().toString().trim();
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
                params[j++] = "Create Article";

                if(submit == true)
                {
                    Url result = null;
                    try {
                        result = new Application_Model(Application_Model.METHOD_POST, context).execute(params).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Log.d("result", result.getResult()+"");
                    if(result.getStatus() == 201)
                    {
                        Toast.makeText(context, "Enregistrement effectué", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, Articles_Controller.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.xml.translate_left_center, R.xml.translate_center_right);
                    }
                    else if(result.getStatus() == 422)
                        Toast.makeText(context, "Vérifier les parametres, l'URL est elle correct ?", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "Parametres obligatoire", Toast.LENGTH_LONG).show();
                }
            }
        });
        layout.addView(valid);
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
