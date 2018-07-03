package com.atlantis.mobileapp.dataaccess;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlantis.mobileapp.objects.CalcMetrics;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.Metrics;
import com.atlantis.mobileapp.activities.Consts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClientWSSingleton{
    private static ClientWSSingleton Instance = null;
    private RequestQueue requestQueue;
    private static Context context;
    public ClientWSCallBack callback;

    private static String urlJee;
    private static String urlNet;

    public static String getUrlNet() {
        return urlNet;
    }

    public static void setUrlNet(String urlNet) {
        ClientWSSingleton.urlNet = urlNet;
    }

    public static String getPortJee() {
        return portJee;
    }

    public static void setPortJee(String portJee) {
        ClientWSSingleton.portJee = portJee;
    }

    private static String portJee = Consts.port;
    private static String portNet = Consts.portNet;

    public static String getPortNet() {
        return portNet;
    }

    public static void setPortNet(String portNet) {
        ClientWSSingleton.portNet = portNet;
    }

    private ClientWSSingleton(String urlJee, String urlNet, Context context){
        setContext(context);
        setUrlJee(urlJee);
        setUrlNet(urlNet);
        requestQueue = getRequestQueue();
    }

    public static synchronized ClientWSSingleton getInstance(String urlJee, String urlNet, Context context) {
        if(Instance == null)
            Instance = new ClientWSSingleton(urlJee,urlNet,context);
        return Instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public void sendUserId(final String id){
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/addUser";

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
                            callback.endSendUserId(resp);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null)
                        callback.endGetError(error.toString());
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
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/addUserName";

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
                        callback.endGetError(error.toString());
                }
            }
            );
            getRequestQueue().add(request);
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }

    public void getUserDevices(String idUser){
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/user/" + idUser + "/devices";
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

    public void getCalculatedMetrics(String deviceMac) {
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/device/" + deviceMac + "/calculatedMetrics";
            JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        CalcMetrics calcMetrics = new CalcMetrics();
                        if (response.length() > 0) {
                            calcMetrics.setMonthAvg(Float.parseFloat(response.getString("monthAvg")));
                            calcMetrics.setMonthMin(Float.parseFloat(response.getString("monthMin")));
                            calcMetrics.setMonthMax(Float.parseFloat(response.getString("monthMax")));
                            calcMetrics.setWeekAvg(Float.parseFloat(response.getString("weekAvg")));
                            calcMetrics.setWeekMin(Float.parseFloat(response.getString("weekMin")));
                            calcMetrics.setWeekMax( Float.parseFloat(response.getString("weekMax")));
                            calcMetrics.setDayAvg(Float.parseFloat(response.getString("dayAvg")));
                            calcMetrics.setDayMin(Float.parseFloat(response.getString("dayMin")));
                            calcMetrics.setDayMax(Float.parseFloat(response.getString("dayMax")));
                        }
                        if (callback != null)
                            callback.endGetCalculatedMetrics(calcMetrics);
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

    public void getLatestMetrics(String deviceMac, String period) {
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/device/" + deviceMac + "/" + period;
            JsonArrayRequest request = new JsonArrayRequest(Method.GET, urlRequest, null, new Response.Listener<JSONArray>() {
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

    public void getCommand(String deviceMac) {
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/device/" + deviceMac + "/getCommand";
            JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.length() > 0) {
                            String resp = null;
                            try {
                                resp = response.getString("command");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (callback != null)
                                callback.endGetCommand(resp);
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

    public void sendCommand(String deviceMac, String command){
        try {
            String urlRequest = "http://" + getUrlJee() + ":" + getPortJee() + "/AtlantisJavaEE-war/services/mobile/device/sendCommand";
            JSONObject json = new JSONObject("{\"deviceMac\":\"" + deviceMac + "\", \"command\":\"" + command + "\"}");
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
                            callback.endSendCommand(resp);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null)
                        callback.endGetError(error.toString());
                }
            }
            );
            getRequestQueue().add(request);
        } catch (Exception e) {
            if (callback != null)
                callback.endGetError("Failed to get data from web service…");
        }
    }

    public static String getUrlJee() {
        return urlJee;
    }
    public static void setUrlJee(String urlJee) {
        ClientWSSingleton.urlJee = urlJee;
    }
    public static Context getContext() {
        return context;
    }
    public static void setContext(Context context) {
        ClientWSSingleton.context = context;
    }


}