package com.genokiller.blogofgenokiller.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.genokiller.blogofgenokiller.utils.Admin;
import com.genokiller.blogofgenokiller.utils.Url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class Application_Model extends AsyncTask<String, Integer, Url>
{
    /**
     * Methode d'envoie en GET
     */
    public static int METHOD_GET = 1;
    /**
     * Methode d'envoie en POST
     */
    public static int METHOD_POST = 2;
    /**
     * Methode d'envoie en PUT
     */
    public static int METHOD_PUT = 3;
    /**
     * Methode d'envoie en DELETE
     */
    public static int METHOD_DELETE = 4;
    /**
     * Methode par defaut
     */
    private int type = METHOD_GET;
    /**
     * Timeout des requetes
     */
    private int timeout = 0;
    public Application_Model(){}

    /**
     * Lien vers le context
     */
    Context context;
    public Application_Model(int method)
    {
        this.type = method;
    }
    public Application_Model(int method, Context context)
    {
        this.type = method;
        this.context = context;
    }
    public Application_Model(int method, Context context, int timeout)
    {
        this.type = method;
        this.context = context;
        this.timeout = timeout;
    }

    /**
     * Création d'une requête avec des paramètre passer en GET
     * @param url
     * @return Url Données retourné par le serveur
     */
	public Url getJsonString(String url)
	{
        Url results = new Url();
		HttpGet httpGet = null;
		StringBuilder builder = new StringBuilder();
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
        HttpClient client = new DefaultHttpClient(httpParameters);
		try
		{
			httpGet = new HttpGet(url);
            SharedPreferences settings = context.getSharedPreferences("admin", 0);
            String name = settings.getString("cookie_name", "");
            String value = settings.getString("cookie_value", "");
            if(!name.isEmpty() && !value.isEmpty())
                httpGet.addHeader("Cookie", name + "=" + value);
            httpGet.setHeader("Accept-Charset", "charset=utf-8");
            httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Accept-Type", "application/json");
            httpGet.setHeader("Accept", "*/*");
		}
		catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
		}
		try
		{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
            results.setStatus(statusCode);
			if (statusCode == 200)
			{
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null)
				{
					builder.append(line);
				}
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        results.setResult(builder.toString());
		return results;
	}

    /**
     * Envoie des données POST au serveur
     * @param url Url du site
     * @param params Parametres à envoyer
     * @return Url Resultat retourné par le serveur
     */
    public Url postJsonUrl(String url, List<? extends NameValuePair> params)
    {
        Url results = new Url();
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        HttpPost httpPost = null;
        StringBuilder builder = new StringBuilder();
        DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        try
        {
            httpPost = new HttpPost(url);
            // ajout du cookier de session ainsi que les en-tête HTTP
            SharedPreferences settings = context.getSharedPreferences("admin", 0);
            String name = settings.getString("cookie_name", "");
            String value = settings.getString("cookie_value", "");
            if(!name.isEmpty() && !value.isEmpty())
                httpPost.addHeader("Cookie", name + "=" + value);
            httpPost.setHeader("Accept-Charset", "charset=utf-8");
            httpPost.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate");
            httpPost.setHeader("Accept-Type", "application/json");
            httpPost.setHeader("Accept", "*/*");
        }
        catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            // on recupere le token permettant l'authentification pour l'envoyé dans la requête
            if(Admin.is_admin(context))
            {
                Url token = getJsonString(Url.BASE_URL + "admin/articles/getCsrf.json");
                String csrf = "";
                try {
                    JSONObject json = new JSONObject(token.getResult());
                    csrf = json.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((List<NameValuePair>)params).add(new BasicNameValuePair("authenticity_token", csrf));

            }
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);

            // on recupere le cookie de session il est envoyé
            if(!Admin.is_admin(context))
            {
                List<Cookie> cookies;
                cookies = client.getCookieStore().getCookies();
                cookie:if(cookies.size() > 0)
                {
                    int session = -1;
                    for(int i = 0; i < cookies.size(); i++)
                    {
                        if(cookies.get(i).getName().contains("session"))
                            session = i;
                    }
                    if(session < 0)
                        break cookie;
                    String name = cookies.get(session).getName();
                    String value = cookies.get(session).getValue();
                    SharedPreferences settings = context.getSharedPreferences("admin", 0);
                    if((settings.getString("cookie_name", "").isEmpty() || !settings.getBoolean("is_admin", false)) && !value.isEmpty())
                    {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("is_admin", true);
                        editor.putString("cookie_name", name);
                        editor.putString("cookie_value", value);

                        // Commit the edits!
                        editor.commit();
                    }
                }
            }



            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            results.setStatus(statusCode);
            if (statusCode == 302 || statusCode == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
            }

        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        results.setResult(builder.toString());
        return results;
    }

    /**
     * Envoie des données pour une mise à jour
     * @param url Url de la page mis à jour
     * @param params Parametres à envoyer
     * @return Url Les données reçu par le serveur
     */
    public Url putJsonUrl(String url, List<? extends NameValuePair> params)
    {
        Url results = new Url();
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        HttpPut httpPut = null;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient(httpParameters);
        try
        {
            httpPut = new HttpPut(url);
            SharedPreferences settings = context.getSharedPreferences("admin", 0);
            String name = settings.getString("cookie_name", "");
            String value = settings.getString("cookie_value", "");
            if(!name.isEmpty() && !value.isEmpty())
                httpPut.addHeader("Cookie", name + "=" + value);
            httpPut.setHeader("Accept-Charset", "charset=utf-8");
            httpPut.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpPut.setHeader("Accept-Encoding", "gzip, deflate");
            httpPut.setHeader("Accept-Type", "application/x-www-form-urlencoded");
            httpPut.setHeader("Accept", "*/*");
        }
        catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            if(Admin.is_admin(context))
            {
                Url token = getJsonString(Url.BASE_URL + "admin/articles/getCsrf.json");
                String csrf = "";
                try {
                    JSONObject json = new JSONObject(token.getResult());
                    csrf = json.getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((List<NameValuePair>)params).add(new BasicNameValuePair("authenticity_token", csrf));

            }
            httpPut.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            HttpResponse response = client.execute(httpPut);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            results.setStatus(statusCode);
            if (statusCode == 302 || statusCode == 200)
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    builder.append(line);
                }
            }
            for(int i = 0; i < httpPut.getAllHeaders().length; i++)
                Log.d("HEADER", httpPut.getAllHeaders()[i].getName() + " : " + httpPut.getAllHeaders()[i].getValue());
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.d("PUT", builder.toString());

        results.setResult(builder.toString());
        return results;
    }

    /**
     * Envoi d'une requête de suppression (qui correspon à une requête POST avec la parametre _method à delete)
     * @param url L'url de la page de suppression
     * @param params Parametres à envoyer
     * @return Url donnée retourné par le serveur
     */
    public Url deleteJsonUrl(String url, List<? extends NameValuePair> params)
    {
        ((List<NameValuePair>)params).add(new BasicNameValuePair("_method", "delete"));
        return postJsonUrl(url, params);
    }

    public Application_Model setContext(Context context)
    {
        this.context = context;
        return this;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Url doInBackground(String[] params) {

        String url = params[0];
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        for(int i = 1; i < params.length; i+=2)
        {
            if(params[i] != null)
                nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
        }
        Url result;
        if(type == METHOD_POST)
            result = postJsonUrl(url, nameValuePairs);
        else if(type == METHOD_PUT)
            result = putJsonUrl(url, nameValuePairs);
        else if(type == METHOD_DELETE)
            result = deleteJsonUrl(url, nameValuePairs);
        else
            result = getJsonString(url);
        if(result.getStatus() < 200 || result.getStatus() > 306)
            return null;
        return result;
    }

    public int getTimeout() {
        return timeout;
    }

    public Application_Model setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}
