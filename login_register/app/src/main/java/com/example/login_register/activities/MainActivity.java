package com.example.login_register.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.example.login_register.R;
import com.example.login_register.helper.InputValidation;
import com.example.login_register.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity= MainActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initViews();
        initListener();
        initObjects();

    }
    private void initViews(){
        nestedScrollView=(NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail =(TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword =(TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail =(TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword=(TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin =(AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister=(AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    private void initListener(){
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

    }
    private void initObjects(){
        databaseHelper =new DatabaseHelper(activity);
        inputValidation=new InputValidation(activity);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                 Intent intenRegister=new Intent(getApplicationContext(),RegisterActivity.class);
                  startActivity(intenRegister);
                  break;
        }
    }
    private void verifyFromSQLite(){
            if(!inputValidation.isInputEditTextFilled(textInputEditTextEmail,textInputLayoutEmail ,getString(R.string.error_message_email))) {
                return;
            }
            if(!inputValidation.isInputEditTextFilled(textInputEditTextEmail,textInputLayoutEmail,getString(R.string.error_message_email))){
                return;
            }
            if(!inputValidation.isInputEditTextFilled(textInputEditTextPassword,textInputLayoutPassword,getString(R.string.error_message_password))){
                return;
            }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            Intent accountsIntent = new Intent(activity, UserActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
