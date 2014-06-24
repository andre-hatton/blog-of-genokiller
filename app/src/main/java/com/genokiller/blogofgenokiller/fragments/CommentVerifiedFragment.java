package com.genokiller.blogofgenokiller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by yoshizuka on 24/06/14.
 */
public class CommentVerifiedFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comment_verified_layout, container, false);
        LinearLayout layout = (LinearLayout)v.findViewById(R.id.comment_verified_layout);

        Url results = null;
        try {
            results = new Application_Model(Application_Model.METHOD_GET, getActivity()).execute(Url.BASE_URL + "admin/comments.json").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JSONArray json = null;
        try {
            json = new JSONArray(results.getResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(json == null || json.length() == 0)
        {
            TextView empty = new TextView(getActivity());
            empty.setGravity(Gravity.CENTER);
            empty.setText("Aucun nouveau commentaire");
            layout.addView(empty);
        }
        else
        {
            for(int i = 0; i < json.length(); i++)
            {
                JSONObject obj = null;
                try {
                    obj = json.getJSONObject(i);
                    final int id = obj.getInt("id");
                    final String pseudo = obj.getString("pseudo");
                    final String description = obj.getString("description");
                    final int article_id = obj.getInt("article_id");
                    final boolean accept = obj.getBoolean("accept");

                    final TextView article = new TextView(getActivity());
                    Url art = null;
                    try {
                        art = new Application_Model(Application_Model.METHOD_GET, getActivity()).execute(Url.BASE_URL + "admin/articles/" + article_id + ".json").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(art != null)
                    {
                        JSONObject objArt = null;
                        objArt = new JSONObject(art.getResult());
                        String str = objArt.getString("title");
                        article.setText("Titre de l'article : " + str);
                    }
                    layout.addView(article);
                    final TextView pseudoLabel = new TextView(getActivity());
                    pseudoLabel.setText(pseudo);
                    layout.addView(pseudoLabel);

                    final TextView descriptionLabel = new TextView(getActivity());
                    descriptionLabel.setText(description);
                    layout.addView(descriptionLabel);

                    final CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setText("Accepter");
                    checkBox.setChecked(accept);
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Url res = null;
                            try {
                                res = new Application_Model(Application_Model.METHOD_PUT, getActivity()).execute(Url.BASE_URL + "admin/comments/"+id+"/updateAcceptComment", "accept", "true").get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if(res != null && (res.getStatus() >= 200 && res.getStatus() < 210))
                            {
                                pseudoLabel.setVisibility(View.GONE);
                                descriptionLabel.setVisibility(View.GONE);
                                checkBox.setVisibility(View.GONE);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Le commentaire n'a pas été accepé", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    layout.addView(checkBox);

                    final Button delete = new Button(getActivity());
                    delete.setText("Supprimer le commentaire");
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(getActivity()).setTitle("Suppresion de l'article").setMessage("Voullez vous supprimer cet article ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Url res = null;
                                            try {
                                                res = new Application_Model(Application_Model.METHOD_DELETE, getActivity()).execute(Url.BASE_URL + "admin/comments/"+id).get();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }
                                            if(res != null && (res.getStatus() >= 200 && res.getStatus() < 210))
                                            {
                                                pseudoLabel.setVisibility(View.GONE);
                                                descriptionLabel.setVisibility(View.GONE);
                                                checkBox.setVisibility(View.GONE);
                                                delete.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "Le commentaire n'a pas été supprimé", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("non", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    });
                    layout.addView(delete);

                    final Button edit = new Button(getActivity());
                    edit.setText("Editer le commentaire");
                    edit.setOnClickListener(new CommentEditFragment(getActivity(), id, pseudoLabel, descriptionLabel, article_id, accept));
                    layout.addView(edit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("RES", results.getResult() + " " + results.getStatus());

        return v;
    }
}
