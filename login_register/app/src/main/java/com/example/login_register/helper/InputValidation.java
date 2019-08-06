package com.example.login_register.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class InputValidation {
    private Context context;

    public InputValidation(Context context){
        this.context=context;
    }
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout ,String message){
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()){
            textInputEditText.setError(message);
            hideKeyBoardFrom(textInputEditText);
            return false;

        }else {
            textInputLayout.setErrorEnabled(false);

        }
        return true;

    }
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText,TextInputLayout textInputLayout ,String message){
        String value = textInputEditText.getText().toString().trim();
        if(value.isEmpty()  || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            textInputLayout.setError(message);
            hideKeyBoardFrom(textInputEditText);
            return false;

        }else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    public boolean isInputEditTexMaches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2 ,TextInputLayout textInputLayout,String message){
        String valuel= textInputEditText1.getText().toString().trim();
        String value2 =textInputEditText2.getText().toString().trim();
        if(!valuel.contentEquals(value2)){
            textInputLayout.setError(message);
            hideKeyBoardFrom(textInputEditText2);
            return  false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return  true;
    }
    private  void hideKeyBoardFrom(View view){
        InputMethodManager imm =(InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}



