package com.atlantis.mobileapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.atlantis.mobileapp.R;
import com.atlantis.mobileapp.dataaccess.ClientWSCallBack;
import com.atlantis.mobileapp.dataaccess.ClientWSSingleton;
import com.atlantis.mobileapp.objects.Device;
import com.atlantis.mobileapp.objects.Metrics;
import com.atlantis.mobileapp.utilities.Consts;
import com.atlantis.mobileapp.utilities.CustomAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DevicesActivity extends AppCompatActivity implements ClientWSCallBack{
    final static String CLIENT_ID = "358ca400-fdf6-4357-8cca-27caa6699197";
    final static String CLIENT_SECRET = "*d,|`89Jnx/Ea5O8y$T724W4";
    final static String REDIRECT_URI = "https://login.microsoftonline.com/tfp/oauth2/nativeclient";
    final static String PRIVACY = "p=B2C_1_ThirdApp-AccountLinking";
    final static String URL_AUTH = "https://partners-login.eliotbylegrand.com/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;

    public static final String KEY_DEVICEMAC = "KEY_DEVICEMAC";
    public static final String KEY_DEVICENAME = "KEY_DEVICENAME";
    public static final String KEY_DEVICETYPE = "KEY_DEVICETYPE";
    //UI
    ListView listView_devices;
    String token;
    WebView webView;
    ProgressDialog dialog;

    String sub;

    private ClientWSSingleton clientWS = null;
    private ArrayList<Device> devices;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        //UI INIT
        webView = (WebView) findViewById(R.id.webView_authBis);
        listView_devices = (ListView)findViewById(R.id.listView_devices);
        webView.setVisibility(View.VISIBLE);
        listView_devices.setVisibility(View.GONE);

        devices = new ArrayList<>();

        //customAdapter = new CustomAdapter(this.getApplicationContext(),devices);
        //listView_devices.setAdapter(customAdapter);
        listView_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!devices.get(i).getName().equals(getString(R.string.listview_no_devices))) {
                    Intent intent = new Intent(DevicesActivity.this, DeviceDetailsActivity.class);
                    intent.putExtra(KEY_DEVICEMAC, devices.get(i).getMac());
                    intent.putExtra(KEY_DEVICENAME, devices.get(i).getName());
                    intent.putExtra(KEY_DEVICETYPE, devices.get(i).getType());
                    startActivity(intent);
                }
            }
        });

        //WEB VIEW LOADING
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearMatches();
        webView.clearSslPreferences();
        startWebView();

        clientWS = ClientWSSingleton.getInstance(Consts.serverUrl, DevicesActivity.this);
        clientWS.callback = this;
    }

    private void startWebView() {
        webView.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "?code=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    webView.stopLoading();
                    final String code = url.substring(start + fragment.length(), url.length());
                    dialog = ProgressDialog.show(DevicesActivity.this, "Loading","Connecting to Eliot account, please wait...", true);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getToken(code);
                                Log.d("access_token",":" + token);
                                JSONObject jsonObject = new JSONObject(token);
                                String idToken = jsonObject.getString("access_token");
                                String refreshToken = jsonObject.getString("profile_info");
                                sub = decoded(idToken);
                                if(sub != null) {
                                    int indexSub = sub.indexOf("\"sub\":") + 7;
                                    sub = sub.substring(indexSub, indexSub + 36);
                                    Log.d("sub",":" + sub);
                                    clientWS.sendUserId(sub);
                                    //clientWS.getUserDevices(sub);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();
                }
            }
        });
        webView.loadUrl(URL_AUTH);
    }

    private void getToken(String code) throws MalformedURLException {
        URL url = new URL("https://partners-login.eliotbylegrand.com/token");
        HttpURLConnection conn;
        try {
            String data = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8");
            data += "&grant_type=" + URLEncoder.encode("authorization_code", "UTF-8");
            data += "&code=" + URLEncoder.encode(code, "UTF-8");
            data += "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8");
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            InputStream is = null;
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            token = sb.toString();
            reader.close();
        }  catch (IOException e) {
            Log.d("Exception", e.getMessage());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage(getString(R.string.dialog_retrieving_devices));
                webView.setVisibility(View.GONE);
                listView_devices.setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });
    }

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            Log.d("Exception", e.getMessage());
        }
        return null;
    }
    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    @Override
    public void endGetError(String error) {
        Log.d("error", error);
    }

    @Override
    public void endSendUserId(String s) {
        Log.d("sendUserId", "Done");
        if(s.equals("true")){
            String name;
            AlertDialog.Builder dial = new AlertDialog.Builder(DevicesActivity.this);
            final EditText input = new EditText(DevicesActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            dial.setView(input);
            dial.setTitle("What's your name ?");
            dial.setMessage("Enter your name to associate your account with : ");
            dial.setCancelable(false);
            dial.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("UserName",input.getText().toString());
                    clientWS.sendUserName(input.getText().toString(),sub);
                    dialog.show();
                }
            });
            dialog.hide();
            dial.show();
        }else{
            clientWS.getUserDevices(sub);
        }
        //Thread.sleep(5000);
    }

    @Override
    public void endSendUserName(String response) {
        Log.d("sendUserName", "Done");
        clientWS.getUserDevices(sub);
    }

    @Override
    public void endGetUserDevices(ArrayList<Device> devs) {
        Log.d("getUserDevices", "Done");
        if(devs.size() == 0){
            devs.add(new Device("",getString(R.string.listview_no_devices),0));
        }
        customAdapter = new CustomAdapter(DevicesActivity.this,devs);
        listView_devices.setAdapter(customAdapter);
        devices = new ArrayList<>(devs);
        dialog.cancel();
    }

    @Override
    public void endGetLatestMetrics(ArrayList<Metrics> metrics) {

    }

    //TODO TEST ONRESUME WEBVIEW.CLEARHISTORY ETC....
}
