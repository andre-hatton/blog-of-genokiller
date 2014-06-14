package com.genokiller.blogofgenokiller.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class Application_Model extends AsyncTask<String, Integer, String>
{
    public static int METHOD_GET = 1;
    public static int METHOD_POST = 2;
    public static int METHOD_PUT = 3;
    private int type = METHOD_GET;
    public Application_Model(){}
    Context context;
    public Application_Model(int method)
    {
        this.type = method;
    }
	public String getJsonString(String url)
	{
		HttpGet httpGet = null;
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		try
		{
			httpGet = new HttpGet(url);
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
		return builder.toString();
	}

    public String postJsonUrl(String url, List<? extends NameValuePair> params)
    {
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        HttpPost httpPost = null;
        StringBuilder builder = new StringBuilder();
        DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        try
        {
            httpPost = new HttpPost(url);
        }
        catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);



            List<Cookie> cookies;
            cookies = client.getCookieStore().getCookies();
            if(cookies.size() > 0)
            {
                String name = cookies.get(1).getName();
                String value = cookies.get(1).getValue();

                SharedPreferences settings = context.getSharedPreferences("admin", 0);
                if(settings.getString("cookie_name", "").isEmpty())
                {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("is_admin", true);
                    editor.putString("cookie_name", name);
                    editor.putString("cookie_value", value);
                    Date date = new Date();
                    long timestamp = date.getTime();
                    editor.putLong("timestamp", timestamp);

                    // Commit the edits!
                    editor.commit();
                }
            }



            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
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
        return builder.toString();
    }

    public String putJsonUrl(String url, List<? extends NameValuePair> params)
    {
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        HttpPut httpPut = null;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient(httpParameters);
        try
        {
            httpPut = new HttpPut(url);
        }
        catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            httpPut.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPut);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
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
        return builder.toString();
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
    protected String doInBackground(String... params) {
        String url = params[0];
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        for(int i = 1; i < params.length; i+=2)
        {
            nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
        }
        return putJsonUrl(url, nameValuePairs);
    }
}
