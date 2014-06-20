package com.genokiller.blogofgenokiller.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.genokiller.blogofgenokiller.controllers.Application_Controller;
import com.genokiller.blogofgenokiller.controllers.R;
import com.genokiller.blogofgenokiller.models.Application_Model;
import com.genokiller.blogofgenokiller.models.Comment_Model;

public class Comment implements OnClickListener
{
	private Application_Controller	main;
	private Context context;
	private String title;
	private String comment_url; 
	private String article_id;

	/**
	 * Recupert les paramètres de l'article pour le popup des commentaires
	 * 
	 * @param main
	 *        Le controller principal de la page
	 * @param context
	 *        Le context de la page
	 * @param title
	 *        Le titre de l'article
	 * @param comment_url
	 *        L'url du commentaire
	 * @param article_id
	 *        L'identifiant de l'article
	 */
	public Comment(Application_Controller main, Context context, String title, String comment_url, String article_id)
	{
		this.main = main;
		this.context = context;
		this.title = title;
		this.comment_url = comment_url;
		this.article_id = article_id;
	}

	@Override
	public void onClick(View v)
	{
		// recuperation du layout du popup
		View popUpView = main.getLayoutInflater().inflate(R.layout.comment_popup, null);
		// creation du popup (taille max de l'application)
		final PopupWindow p = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// affichage du popup avec centrage au milieu
		p.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 0);
		// recuperation du bouton de fermeture du popup
		Button close = (Button) popUpView.findViewById(R.id.close_popup);
		close.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// ferme le popup
				p.dismiss();
			}
		});

		// recuperation du model des commentaires
		Comment_Model comment = new Comment_Model();
		// recuperation de la listview permettant d'afficher les commentaire
		ListView list = (ListView) popUpView.findViewById(R.id.comment_list);
		// Création de la ArrayList qui nous permettra de remplir la listView
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;

		// prepartion a la recuperation des données json
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// recuperation des données json
		String json = comment.getJsonString(comment_url + ".json");
		JSONArray jsArray = null;
		try
		{
			jsArray = new JSONArray(json);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// parcours des commentaires
		if (jsArray != null && jsArray.length() > 0)
		{
			int i;
			for (i = 0; i < jsArray.length(); i++)
			{
				JSONObject jsObject = null;
				try
				{
					jsObject = jsArray.getJSONObject(i);
					map = new HashMap<String, String>();
					// verifie si le commentaire a été validé et l'ajoute a la
					// liste
					if (jsObject.getBoolean(Comment_Model.ACCEPT))
					{
						map.put(Comment_Model.PSEUDO, jsObject.getString(Comment_Model.PSEUDO));
						map.put(Comment_Model.DESCRIPTION, jsObject.getString(Comment_Model.DESCRIPTION));
						listItem.add(map);
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			// aucun commentaire trouvé
			map = new HashMap<String, String>();
			map.put(Comment_Model.DESCRIPTION, "Aucun commentaire pour " + title);
			listItem.add(map);
		}
		// on créer un adpter pour la liste et utilisant le pseudo et le
		// commentaire
		SimpleAdapter mSchedule = new SimpleAdapter(context, listItem, R.layout.list_comment, new String[]
		{ Comment_Model.PSEUDO, Comment_Model.DESCRIPTION }, new int[]
		{ R.id.pseudo, R.id.description });
		list.setAdapter(mSchedule);

		// recuperation du pseudo et du commentaire du popup (champ editable)
		EditText pseudo = (EditText) popUpView.findViewById(R.id.pseudo);
		EditText description = (EditText) popUpView.findViewById(R.id.description_popup);

		// on recupert le bouton valider et y ajoutons un listener sur le click
		Button submit = (Button) popUpView.findViewById(R.id.submit);
		submit.setOnClickListener(new Submit(pseudo, description, comment, popUpView, submit, list));

	}

	/**
	 * 
	 * @date 20/10/2013
	 * @author genokiller Recuperation des données sur le click de valider
	 * 
	 */
	private class Submit implements OnClickListener
	{
		/**
		 * Champ pseudo et commentaire
		 */
		private EditText	pseudo, description;
		/**
		 * Les données d'un commentaire
		 */
		private Comment_Model	comment;
		/**
		 * Vue du popup
		 */
		private View			popupView;
		/**
		 * bouton de validation
		 */
		private Button			submit;
		/**
		 * Liste des commentaires
		 */
		private ListView		list;

		/**
		 * Récupération des données pour la validation d'un commentaire
		 * 
		 * @param pseudo
		 *        EditText du pseudo
		 * @param description
		 *        EditText du commentaire
		 * @param comment
		 *        Models des commentaires
		 * @param popupView
		 *        Vue du popup
		 * @param submit
		 *        Button bouton de validation
		 * @param list
		 *        ListView vue des commentaire
		 */
		public Submit(EditText pseudo, EditText description, Comment_Model comment, View popupView, Button submit, ListView list)
		{
			this.pseudo = pseudo;
			this.description = description;
			this.comment = comment;
			this.popupView = popupView;
			this.submit = submit;
			this.list = list;
		}
		@Override
		public void onClick(View v)
		{
			// recuperation des données du formulaire
			String pseudo_text = pseudo.getText().toString();
			String description_text = description.getText().toString();
			// verification de la longueur soumise
			if (pseudo_text.length() > 2 && description_text.length() > 20 && article_id != null)
			{
                String response = null;
				// envoie des données du commentaire et recuperation du serveur
				// (qui verifie lui aussi les données de façon plus sur)
                try {
                    response = new Application_Model(Application_Model.METHOD_POST).execute("http://blog-of-genokiller.herokuapp.com/articles.json", "admin_comment[pseudo]", URLEncoder.encode(pseudo_text, "UTF-8"), "admin_comment[description]", URLEncoder.encode(description_text, "UTF-8"), "admin_comment[article_id]", article_id, "utf8", "1").get();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }
				JSONObject jsonObject = null;
				try
				{
					// recuperation de la reponse du serveur puis affichage d'un
					// message d'erreur ou de succès
					jsonObject = new JSONObject(response);
					TextView t = (TextView) popupView.findViewById(R.id.message);
					int success = jsonObject.getInt("success");
					t.setVisibility(View.VISIBLE);
					t.setText(jsonObject.getString("message"));
					if (success == 1)
						t.setTextColor(Color.GREEN);
					else
						t.setTextColor(Color.RED);
					if (success != 2)
					{
						// si le commentaire et valide on bloque le formulaire
						description.setText("");
						pseudo.setText("");
						t.setFocusable(true);
						description.setEnabled(false);
						description.setVisibility(View.GONE);
						pseudo.setEnabled(false);
						pseudo.setVisibility(View.GONE);
						submit.setEnabled(false);
						submit.setVisibility(View.GONE);
						list.setSelection(0);
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// commentaire invalide
			else if (pseudo_text.length() > 2 && description_text.length() < 21)
			{
				TextView t = (TextView) popupView.findViewById(R.id.message);
				t.setVisibility(View.VISIBLE);
				t.setTextColor(Color.RED);
				t.setText("Le commentaire doit contenir un minimum de 20 caractères.");
			}
			// pseudo invalide
			else if (pseudo_text.length() < 3 && description_text.length() > 20)
			{
				TextView t = (TextView) popupView.findViewById(R.id.message);
				t.setVisibility(View.VISIBLE);
				t.setTextColor(Color.RED);
				t.setText("Le pseudo doit contenir un minimum de 3 caractères.");
			}
			// pseudo et commentaire invalide
			else
			{
				TextView t = (TextView) popupView.findViewById(R.id.message);
				t.setVisibility(View.VISIBLE);
				t.setTextColor(Color.RED);
				t.setText("Vérifiez que les champs soient bien remplis.");
			}
		}

	}

}
