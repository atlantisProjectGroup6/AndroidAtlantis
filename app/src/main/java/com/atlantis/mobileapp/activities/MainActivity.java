package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.atlantis.mobileapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonSignIn = findViewById(R.id.button_signIn);
        Button buttonSignUp = findViewById(R.id.button_signUp);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DevicesActivity.class);
                startActivity(intent);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://login.microsoftonline.com/eliotclouduamprd.onmicrosoft.com/oauth2/v2.0/authorize?p=b2c_1_eliot-signup&client_id=c3b23ef7-aa7d-4d3a-8253-efbb296457cd&redirect_uri=https%3a%2f%2fdeveloper.legrand.com%2fsignin-aad&response_mode=form_post&response_type=id_token&scope=openid&state=OpenIdConnect.AuthenticationProperties%3d-yRRSn4uv1QP3DLT-j2p5njMh7gaEkUAcVCiYaWItqYy77SnLnM8za0LBlS3OCe33PyNmxcPoVddA0vsEkmhkR8RAwnW1dCnUo1ZquwSNmNw8qJpESiRr7msGxBiG_gAMhmudTDuVc0_RV0Adl9eNtg6ipiNBR8MKeMs1WNdFxUwyTt8iEFN-u98-ka6fvCF0Stdme3dW_zpfGbjN0jmVNjTodJ11mi33Re1IsV01luODWHBdfTrXfkHP-7Llt5XkRFkeN58AOWJAcVg1xqrV2fxf9YICK2dwyaHUudLOVE&nonce=636662043439642493.ZDI5NzdlYzMtYWUwZS00YTQzLWI3MTktMmY5MDMyNmVjNGFlMWRlZTQ2NmMtOTZiNi00NDQzLWFlNDYtZmY3ZWQ3MzQ1NDNj"));
                startActivity(browserIntent);
            }
        });
    }
}
