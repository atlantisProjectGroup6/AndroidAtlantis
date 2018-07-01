package com.atlantis.mobileapp.dataaccess;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.Metrics;
import com.atlantis.mobileapp.utilities.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientWSSingleton{
    private static ClientWSSingleton Instance = null;
    private RequestQueue requestQueue;
    private static Context context;
    public ClientWSCallBack callback;

    private static String url;

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        ClientWSSingleton.port = port;
    }

    private static String port = Consts.port;

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

    public void sendUserId(final String id){
        try {
            String urlRequest = "http://" + getUrl() + ":" + getPort() + "/AtlantisJavaEE-war/services/mobile/addUser";

            JSONObject json = new JSONObject("{\"userId\":\"" + id + "\"}");
            JsonObjectRequest request = new JsonObjectRequest(Method.POST, urlRequest, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        String resp = null;
                        try {
                            resp = response.getString("isValid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (callback != null)
                            callback.endSendUserId(resp.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null)
                        callback.endSendUserId(error.toString());
                }
                }
            );
            getRequestQueue().add(request);
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }

    public void sendUserName(String name, String id){
        try {
            String urlRequest = "http://" + getUrl() + ":" + getPort() + "/AtlantisJavaEE-war/services/mobile/addUserName";

            JSONObject json = new JSONObject("{\"userId\":\"" + id + "\",\"name\":\"" + name + "\"}");
            JsonObjectRequest request = new JsonObjectRequest(Method.POST, urlRequest, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.length() > 0) {
                        String resp = null;
                        try {
                            resp = response.getString("isValid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (callback != null)
                            callback.endSendUserName(resp);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null)
                        callback.endSendUserName(error.toString());
                }
            }
            );
            getRequestQueue().add(request);

/*

            StringRequest postRequest = new StringRequest(Request.Method.POST, urlRequest,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            callback.endSendUserId(response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (callback != null)
                            callback.endGetError("Failed to get data from web service…");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("userId", id);
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json");
                    return params;
                }
            };
            requestQueue.add(postRequest);
*/
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }

    public void getUserDevices(String idUser){
        try {
            String urlRequest = "http://" + getUrl() + ":" + getPort() + "/AtlantisJavaEE-war/services/mobile/user/" + idUser + "/devices";
            JsonArrayRequest request = new JsonArrayRequest(Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        ArrayList<Device> devices = new ArrayList<>();
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject current = response.getJSONObject(i);
                                String mac = current.getString("MACAddress");
                                String name = current.getString("name");
                                int type = current.getInt("typeId");
                                devices.add(new Device(mac,name,type));
                            }
                        }
                        if (callback != null)
                            callback.endGetUserDevices(devices);
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

    public void getLatestMetrics(String deviceMac, long timestamp) {
        try {
            String urlRequest = "http://" + getUrl() + ":" + getPort() + "/AtlantisJavaEE-war/services/mobile/latestMetrics";
            JSONArray json = new JSONArray("[{\"deviceMac\":\"" + deviceMac + "\",\"timestamp\":" + timestamp + "}]");
            JsonArrayRequest request = new JsonArrayRequest(Method.POST, urlRequest, json, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        ArrayList<Metrics> metrics = new ArrayList<>();
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject current = response.getJSONObject(i);
                                String value = current.getString("value");
                                long date = current.getLong("date");
                                int id = current.getInt("id");
                                metrics.add(new Metrics(id,value,date));
                            }
                        }
                        if (callback != null)
                            callback.endGetLatestMetrics(metrics);
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
