package com.atlantis.mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantis.mobileapp.R;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{

    public static final String KEY_USER = "KEY_USER";

    // UI references.
    private EditText editText_login;
    private EditText editText_password;

    private View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(MainActivity.this, EliotAuthActivity.class);
                startActivityForResult(inte,0);
            }
        });

        //InitView
        editText_login = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    if(checkFields()){
                        boolean auth = authenticate(editText_login.getText().toString(), editText_password.getText().toString());
                        if(!auth)
                            Toast.makeText(MainActivity.this, R.string.error_invalid_auth, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, R.string.valid_auth, Toast.LENGTH_LONG).show();
                        return auth;
                    }
                }
                return false;
            }
        });

        Button buttonSignIn = findViewById(R.id.button_signin);
        buttonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    authenticate(editText_login.getText().toString(), editText_password.getText().toString());
                }
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    if(createAccount(editText_login.getText().toString(), editText_password.getText().toString())) {
                        Toast.makeText(MainActivity.this,R.string.user_created, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this,R.string.error_account_creation, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean authenticate(String login, String password){
        boolean auth = true;
        //TODO authenticate to mobile endpoint
        if(auth) {
            Intent i;
            i = new Intent(MainActivity.this, DevicesActivity.class);
            //TODO add user id/login to intent
            //i.putExtra(KEY_USER,login);
            startActivity(i);
        }
        return auth;
    }

    private boolean createAccount(String login, String password){
        boolean creation = false;
        //TODO create account on database and Eliot
        return creation;
    }

    private boolean checkFields() {
        editText_login.setError(null);
        editText_password.setError(null);

        String email = editText_login.getText().toString();
        String password = editText_password.getText().toString();

        boolean error = false;
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            editText_password.setError(getString(R.string.error_invalid_password));
            focusView = editText_password;
            error = true;
        }
        if (TextUtils.isEmpty(email)) {
            editText_login.setError(getString(R.string.error_field_required));
            focusView = editText_login;
            error = true;
        } else if (!isEmailValid(email)) {
            editText_login.setError(getString(R.string.error_invalid_email));
            focusView = editText_login;
            error = true;
        }

        if (error) {
            focusView.requestFocus();
            return false;
        } else
            return true;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("^[a-z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$").matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 4;
    }

}

