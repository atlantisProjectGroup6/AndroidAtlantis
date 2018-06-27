package com.atlantis.mobileapp.activities;

import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atlantis.mobileapp.R;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EliotAuthActivity extends AppCompatActivity {

    //https://login.microsoftonline.com/eliotclouduamprd.onmicrosoft.com/oauth2/v2.0/authorize?
    //&p=b2c_1_eliot-signuporsignin-wwl
    //&client_id=c3b23ef7-aa7d-4d3a-8253-efbb296457cd
    // &redirect_uri=https%3a%2f%2fdeveloper.legrand.com%2fsignin-aad
    // &response_mode=form_post
    // &response_type=id_token
    // &scope=openid
    // &state=OpenIdConnect.AuthenticationProperties%3diw2lNyeAO-oKNG_Uu1tgNDZg3HMFVzqYcMbxKTMGzonKyme1obUSv2LCjL69F9HKGa9AzzHL7elVTAZiQtybzHXY9VLAYmwgSFScyHCLTEkvrP_5FRdXyYo_QN3u3k8she2k9f7w022c588nY69ZqPwy3Ngj4Ydi3bxYq9i2kkTLrDV8nuBVLS9aaYmMXIxQ0G62-p9hbQ4fun_O1vmkb4kMyTCLH2FlFADMZIZVRoDRo_1pmS_vinX-PYcSz23hKzrGVEoIWyaKcURwh7kOjA&nonce=636656962822245049.NjcyZjZhY2YtNDA1ZC00MzIwLTlhYTgtOTdkMmNmZTQwNDZlNzA3MmNmMDAtNzQ3Yi00NTljLWI5MjUtYmEwNTgxYTg5NmU0

    //https://login.microsoftonline.com/eliotclouduamprd.onmicrosoft.com/oauth2/v2.0/authorize?client_id=358ca400-fdf6-4357-8cca-27caa6699197&p=B2C_1_ThirdApp-AccountLinking&response_type=code&state=d8cdccaa-0c37-4493-ab37-d5d92bc99cd7&scope=openid&p=B2C_1_ThirdApp-AccountLinking&redirect_uri=https://login.microsoftonline.com/tfp/oauth2/nativeclient

    //https://partners-login.eliotbylegrand.com/authorize?client_id=358ca400-fdf6-4357-8cca-27caa6699197&response_type=code&redirect_uri=https://login.microsoftonline.com/tfp/oauth2/nativeclient
    //?client_id=358ca400-fdf6-4357-8cca-27caa6699197
    // &p=B2C_1_ThirdApp-AccountLinking
    // &response_type=code
    // &state=d8cdccaa-0c37-4493-ab37-d5d92bc99cd7
    // &scope=openid
    // &p=B2C_1_ThirdApp-AccountLinking
    // &redirect_uri=https://login.microsoftonline.com/tfp/oauth2/nativeclient

    /* Azure AD v2 Configs */
    final static String CLIENT_ID = "358ca400-fdf6-4357-8cca-27caa6699197";
    final static String RESPONSE_TYPE = "code";
    final static String TENANT_FULL = "https://login.microsoftonline.com/0d8816d5-3e7f-4c86-8229-645137e0f222/v2.0";
    final static String TENANT_DEFAULT = "https://login.microsoftonline.com/9188040d-6c67-4c5b-b112-36a304b66dad/v2.0";
    final static String TENANT = "0d8816d5-3e7f-4c86-8229-645137e0f222";
    final static String REDIRECT_URI = "msal358ca400-fdf6-4357-8cca-27caa6699197://auth";
    final static String AUTHORITY_URL = "https://login.microsoftonline.com/";
    static final String KEY_CODE = "KEY_CODE";
    final static String AUTH_TAG = "auth";
    final static String SCOPES [] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    private static final String POLICY = "B2C_1_ThirdApp-AccountLinking";

    /* UI & Debugging Variables */
    WebView webView;

    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliot_auth_activity);


/*
        PublicClientApplication myApp = new PublicClientApplication(this.getApplicationContext(),CLIENT_ID);
        myApp.setValidateAuthority(false);


        myApp.acquireToken(this, new String[]{"User.Read"}, new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                Toast.makeText(EliotAuthActivity.this,authenticationResult.getIdToken(),Toast.LENGTH_LONG);
                String accesstoken = authenticationResult.getAccessToken();
                Log.d("token",accesstoken);
            }

            @Override
            public void onError(MsalException exception) {

            }

            @Override
            public void onCancel() {

            }
        });
*/

    }

}
