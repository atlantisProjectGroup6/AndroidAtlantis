package com.atlantis.mobileapp.dataaccess;

import android.app.Application;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlantis.mobileapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;

public class ClientWSSingleton{
    private static ClientWSSingleton Instance = null;
    private RequestQueue requestQueue;
    private static Context context;
    public ClientWSCallBack callback;

    private static String url;

    private ClientWSSingleton(String url, Context context){
        setContext(context);
        setUrl(url);
        requestQueue = getRequestQueue();
    }

    public static synchronized ClientWSSingleton getInstance(String url, Context context) {
        if(Instance == null)
            Instance = new ClientWSSingleton(url,context);
        return Instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public void getWeather(String city){
        try {
            String urlRequest = "http://" + getUrl() + "/weather?q=" + city + "&APPID=741389afba5cc7f8ac5d98d18db34296";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        String s = null;
                        try {
                            s = response.getString("weather");
                            jsonArray = new JSONArray(s);
                            JSONObject current = jsonArray.getJSONObject(0);
                            String main = current.getString("main");
                            String description = current.getString("description");
                            String name = response.getString("name");
                            if (callback != null)
                                callback.endGetWeather(name, main, description);
                        } catch (Exception e) {
                            if (callback != null)
                                callback.endGetError("Failed to get data from web service…");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null)
                            callback.endGetError("Failed to get data from web service…");
                    }
                }
            );
            getRequestQueue().add(request);
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }


    public static String getUrl() {
        return url;
    }
    public static void setUrl(String url) {
        ClientWSSingleton.url = url;
    }
    public static Context getContext() {
        return context;
    }
    public static void setContext(Context context) {
        ClientWSSingleton.context = context;
    }
}
