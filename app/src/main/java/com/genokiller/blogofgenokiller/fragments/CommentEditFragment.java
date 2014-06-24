package com.genokiller.blogofgenokiller.fragments;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.CommentAdmin_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.utils.Item;
import com.genokiller.blogofgenokiller.utils.Url;

import java.util.concurrent.ExecutionException;

/**
 * Created by yoshizuka on 24/06/14.
 */
public class CommentEditFragment implements View.OnClickListener {

        /**
         * L'activité en cours
         */
        private Context context;
        /**
         * La valeur à éditer
         */
        private String value;
        /**
         * Le nom de la valeur à éditer
         */
        private String name;
        private int id;
    private TextView pseudo;
    private TextView description;
    private int article_id;
    private boolean accept;

        /**
         * Ajoute les données pour le onClick
         * @param context
         *
         */
        public CommentEditFragment(Context context, int id, TextView pseudo, TextView description, int article_id, boolean accept){
            this.context = context;
            this.id = id;
            this.pseudo = pseudo;
            this.description = description;
            this.article_id = article_id;
            this.accept = accept;
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            LinearLayout viewGroup = (LinearLayout) ((Activity)context).findViewById(R.id.comment_edit_popup_layout);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.comment_edit_popup_layout, viewGroup);
            LinearLayout l = (LinearLayout)layout.findViewById(R.id.comment_edit_popup_layout);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(context);
            popup.setContentView(layout);
            popup.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            popup.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popup.setFocusable(true);


            // Displaying the popup at the specified location, + offsets.
            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

            final EditText pseudoEdit = new EditText(context);
            pseudoEdit.setText(pseudo.getText());
            l.addView(pseudoEdit);

            final EditText descriptionEdit = new EditText(context);
            descriptionEdit.setText(description.getText());
            l.addView(descriptionEdit);

            // Getting a reference to Close button, and close the popup when clicked.
            Button valid = new Button(context);
            valid.setText("Valider");
            valid.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    value = pseudoEdit.getText().toString();
                    String strAccept = accept ? "1" : "0";
                    try {
                        // envoie des nouvelles valeurs au serveur
                        Url result = new Application_Model(Application_Model.METHOD_PUT).setContext(context).execute(new String[]{Url.BASE_URL + "admin/comments/" + id, "admin_comment[accept]", strAccept, "admin_comment[pseudo]", pseudoEdit.getText().toString(), "admin_comment[description]", descriptionEdit.getText().toString(), "admin_comment[article_id]", String.valueOf(article_id), "commit", "Update Comment"}).get();
                        if (result != null) {
                            description.setText(descriptionEdit.getText());
                            pseudo.setText(pseudoEdit.getText());
                            Toast.makeText(context, "Modification réussie", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Modification échoué", Toast.LENGTH_LONG).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Modification échoué", Toast.LENGTH_LONG).show();
                    }
                    popup.dismiss();
                }
            });
            l.addView(valid);
        }

}
