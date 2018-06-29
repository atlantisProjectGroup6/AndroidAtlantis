package com.atlantis.mobileapp.dataaccess;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlantis.mobileapp.objects.Device;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public void sendUserId(String id){
        try {
            String urlRequest = "http://" + getUrl() + "/AtlantisJavaEE-war/services/mobile/addUser";
            JSONObject json = new JSONObject("{\"userId\":\"" + id + "\"}");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRequest, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null)
                        callback.endGetError("Failed to get data from web service…");
                }
            });
            getRequestQueue().add(request);
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }

    public void getUserDevices(String idUser){
        try {
            String urlRequest = "http://" + getUrl() + "/AtlantisJavaEE-war/services/mobile/getUserDevices";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray jsonArray = null;
                    String s = null;
                    try {
                        s = response.getString("Devices");
                        jsonArray = new JSONArray(s);
                        ArrayList<Device> devices = new ArrayList<>();
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject current = jsonArray.getJSONObject(i);
                                String mac = current.getString("mac");
                                String name = current.getString("name");
                                int type = current.getInt("Description");
                                devices.add(new Device(mac,name,type));
                            }
                            if (callback != null)
                                callback.endGetUserDevices(devices);
                        }
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
            });
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
