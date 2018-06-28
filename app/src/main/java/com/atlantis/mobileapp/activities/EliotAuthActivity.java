package com.atlantis.mobileapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.atlantis.mobileapp.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class EliotAuthActivity extends AppCompatActivity {

    final static String CLIENT_ID = "358ca400-fdf6-4357-8cca-27caa6699197";
    final static String CLIENT_SECRET = "*d,|`89Jnx/Ea5O8y$T724W4";
    final static String REDIRECT_URI = "https://login.microsoftonline.com/tfp/oauth2/nativeclient";
    static final String KEY_CODE = "KEY_CODE";
    final static String URL_AUTH = "https://partners-login.eliotbylegrand.com/authorize?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;

    String token;
    WebView webView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliot_auth_activity);

        webView = (WebView) findViewById(R.id.webView_auth);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "?code=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    webView.stopLoading();
                    final String code = url.substring(start + fragment.length(), url.length());
                    Log.d("CODE",code);

                    dialog = ProgressDialog.show(EliotAuthActivity.this, "Loading","Connecting to Eliot account, please wait...", true);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                token(code);
                            } catch (MalformedURLException e) {
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

    private void token(String code) throws MalformedURLException {
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
            try {
                is = conn.getInputStream();
            }
            catch (IOException e) {
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            reader.close();
            token = sb.toString();
            Log.d("D","ATA : " + token);
        }  catch (IOException e) {
            e.printStackTrace();
        }
        dialog.cancel();
        Intent intent = new Intent(EliotAuthActivity.this,DevicesActivity.class);
        intent.putExtra(KEY_CODE,token);
        startActivity(intent);
    }

}
