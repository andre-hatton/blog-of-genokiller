package com.genokiller.blogofgenokiller.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genokiller.Blog_Application;
import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.CreateArticle_Controller;
import com.genokiller.blogofgenokiller.controllers.EditArticle_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.controllers.Search_Controller;
import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.models.Article_Model;
import com.genokiller.blogofgenokiller.utils.Admin;
import com.genokiller.blogofgenokiller.utils.Comment;
import com.genokiller.blogofgenokiller.utils.Item;
import com.genokiller.blogofgenokiller.utils.Url;
import com.novoda.imageloader.core.model.ImageTagFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

public class Application_Helper extends ArrayAdapter<HashMap<String, Item>>
{
	private final Activity								context;

	private LayoutInflater								layoutInflater;
	private final ArrayList<HashMap<String, Item>>	maps;
	ImageTagFactory										imageTagFactory;
	private Application_Controller						main;
	private Item[]									infonames	= new Item[50];


	public Application_Helper(Activity context, ArrayList<HashMap<String, Item>> maps, Application_Controller application_Controller)
	{
		super(context, R.layout.activity_article, maps);
		this.context = context;
		this.maps = maps;
		this.main = application_Controller;
		layoutInflater = LayoutInflater.from(application_Controller);
		/* Image Loader */

		imageTagFactory = ImageTagFactory.newInstance(context, R.id.description);
		imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
	}

	@SuppressWarnings(
	{ "unused" })
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
        View row = convertView;
        View elem;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.list, parent, false);


        String description = maps.get(position).get(Article_Model.DESCRIPTION).getText();
        String title = maps.get(position).get(Article_Model.TITLE).getText();
        String image_url = maps.get(position).get(Article_Model.IMAGE_URL).getText();
        String comment_url = maps.get(position).get(Article_Model.COMMENT_URL).getText();
        String comment_count = maps.get(position).get(Article_Model.COMMENT_COUNT).getText();
        final String article_id = maps.get(position).get(Article_Model.ID).getText();

        RelativeLayout list = (RelativeLayout)row.findViewById(R.id.listview);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView titre = new TextView(context);
        titre.setText(title);
        titre.setId(1);
        elem = titre;
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 1);
        list.addView(titre, layoutParams1);

        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView img = new ImageView(context);
        img.setTag(imageTagFactory.build(image_url, context));
        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
        img.setId(elem.getId() + 1);

        try
        {
            Blog_Application.getImageLoader().getLoader().load(img);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        elem = img;
        list.addView(img, layoutParams1);
        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView desc = new TextView(context);
        desc.setText(description);
        desc.setId(elem.getId() + 1);
        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
        elem = desc;
        list.addView(desc, layoutParams1);


        for (int i = 0; i < infonames.length; i++)
        {
            infonames[i] = null;
        }
        for (int i = 0; i < maps.get(position).size(); i++)
        {
            if (maps.get(position).get("info_" + i) != null && !maps.get(position).get("info_" + i).equals(""))
                infonames[i] = maps.get(position).get("info_" + i);
        }
        for(int i = 0; i < infonames.length; i++)
        {
            if(infonames[i] == null) continue;
            layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final TextView info = new TextView(context);
            info.setId(elem.getId() + 1);
            layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
            info.setText(infonames[i].getText());
            elem = info;
            list.addView(info, layoutParams1);

            if(infonames[i].has_button())
            {
                final int id = infonames[i].getId();
                final int type = infonames[i].getButton();

                switch (type)
                {
                    case Item.BUTTON_MORE_LESS:
                    case Item.BUTTON_MORE_LESS_EDIT:
                        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
                        Button more = new Button(context, null, android.R.attr.buttonStyleSmall);
                        Button less = new Button(context, null, android.R.attr.buttonStyleSmall);
                        more.setText("+");
                        less.setText("-");
                        less.setId(elem.getId() + 1);
                        final int h = i;
                        less.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Url.BASE_URL + "admin/articles/updateInfo/" + id;
                                try {
                                    Url response = new Article_Model(Application_Model.METHOD_PUT).setContext(context).execute(new String[]{url, "type", "less"}).get();
                                    if(response == null)
                                    {
                                        Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        String text = String.valueOf(info.getText());
                                        String[] infos = text.split(":");
                                        int info_value = Integer.parseInt(infos[1].trim());
                                        String info_name = infos[0];
                                        int info_value_new = info_value - 1;
                                        info.setText(info_name + ": "  + info_value_new);
                                    }
                                } catch (InterruptedException e) {
                                    Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                        elem = less;
                        list.addView(less, layoutParams1);

                        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId() - 1);
                        layoutParams1.addRule(RelativeLayout.RIGHT_OF, elem.getId());
                        more.setId(elem.getId() + 1);
                        more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Url.BASE_URL + "admin/articles/updateInfo/" + id;
                                try {
                                    if(new Article_Model(Application_Model.METHOD_PUT).setContext(context).execute(new String[]{url, "type", "more"}).get() == null)
                                    {
                                        Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        String text = String.valueOf(info.getText());
                                        String[] infos = text.split(":");
                                        int info_value = Integer.parseInt(infos[1].trim());
                                        String info_name = infos[0];
                                        int info_value_new = info_value + 1;
                                        info.setText(info_name + ": "  + info_value_new);
                                    }
                                } catch (InterruptedException e) {
                                    Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    Toast.makeText(context, "Echec de la modification de la valeur", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                        elem = more;
                        list.addView(more, layoutParams1);
                        if(type == Item.BUTTON_MORE_LESS_EDIT)
                        {
                            layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams1.addRule(RelativeLayout.BELOW, elem.getId() - 2);
                            layoutParams1.addRule(RelativeLayout.RIGHT_OF, elem.getId());
                            Button edit = new Button(context, null, android.R.attr.buttonStyleSmall);
                            edit.setText("edit");
                            edit.setId(elem.getId() + 1);
                            edit.setOnClickListener(new Edit(context, infonames[i], info));
                            elem = edit;
                            list.addView(edit, layoutParams1);
                        }
                        break;
                    case Item.BUTTON_EDIT:
                        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
                        Button edit = new Button(context, null, android.R.attr.buttonStyleSmall);
                        edit.setText("edit");
                        edit.setId(elem.getId() + 1);
                        edit.setOnClickListener(new Edit(context, infonames[i], info));
                        elem = edit;
                        list.addView(edit, layoutParams1);
                        break;
                }

            }
        }
        layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView comm = new TextView(context);
        layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
        comm.setText("Voir les commentaires (" + comment_count + ")");
        comm.setTag(comment_url);
        comm.setId(elem.getId() + 1);
        comm.setOnClickListener(new Comment(main, context, title, comment_url, article_id));
        elem = comm;
        list.addView(comm, layoutParams1);

        if(Admin.is_admin(context))
        {
            layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
            Button create = new Button(context, null, android.R.attr.buttonStyleSmall);
            create.setText("Nouvel article");
            create.setId(elem.getId() + 1);
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CreateArticle_Controller.class);
                    //Bundle bundle = new Bundle();
                    // revien sur la premiere page
                    //bundle.putInt("page", 1);
                    //bundle.putString("search", query);
                    //intent.putExtras(bundle);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.xml.translate_right_center, R.xml.translate_center_left);
                }
            });
            elem = create;
            list.addView(create, layoutParams1);

            layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams1.addRule(RelativeLayout.BELOW, elem.getId());
            Button edit = new Button(context, null, android.R.attr.buttonStyleSmall);
            edit.setText("Modifier article");
            edit.setId(elem.getId() + 1);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditArticle_Controller.class);

                }
            });
            elem = edit;
            list.addView(edit, layoutParams1);
        }

        String[] images = image_url.split("/");

		return row;
	}

    /**
     * Classe interne permettant de géré la validation de l'édition des informations
     */
    class Edit implements View.OnClickListener {
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
        /**
         * L'item qui correspond à l'information
         */
        private Item item;
        /**
         * La vue contenant le texte de l'info
         */
        private TextView info;

        /**
         * Ajoute les données pour le onClick
         * @param context
         * @param item
         * @param info
         */
        public Edit(Context context, Item item, TextView info){
            this.context = context;
            String values[] = item.getText().split(":");
            this.value = values[1].trim();
            this.name = values[0].trim();
            this.item = item;
            this.info = info;
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            LinearLayout viewGroup = (LinearLayout) ((Activity)context).findViewById(R.id.edit_popup_layout);
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.edit_popup, viewGroup);

            // Creating the PopupWindow
            final PopupWindow popup = new PopupWindow(context);
            popup.setContentView(layout);
            popup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popup.setFocusable(true);


            // Displaying the popup at the specified location, + offsets.
            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

            final EditText editText = (EditText)layout.findViewById(R.id.edit_popup_text);
            editText.setText(value);

            // Getting a reference to Close button, and close the popup when clicked.
            Button close = (Button) layout.findViewById(R.id.edit_popup_submit);
            close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    value = editText.getText().toString();
                    try {
                        // envoie des nouvelles valeurs au serveur
                        Url result = new Application_Model(Application_Model.METHOD_PUT).setContext(context).execute(new String[]{Url.BASE_URL + "admin/articles/updateInfoname/" + item.getId(), "value", value, "pk", "1", "name", "infoname_" + item.getType()+"_"+item.getId()}).get();
                        if(result != null)
                        {
                            Toast.makeText(context, "Modification réussie", Toast.LENGTH_LONG).show();
                            Log.d("result", result.getResult()+" " + value);
                            info.setText(name + " : " + value);
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
        }
    }

}