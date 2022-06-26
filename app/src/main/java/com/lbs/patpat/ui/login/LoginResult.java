package com.lbs.patpat.ui.login;

import androidx.annotation.Nullable;

import org.json.JSONObject;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {

    private boolean resultState;
    private String error;
    private JSONObject userData;

    public LoginResult(JSONObject userData){
        resultState = true;
        this.userData=userData;

    }
    public LoginResult(String errMessage){
        resultState = false;
        error = errMessage;
    }

    public Boolean isSuccess(){
        return resultState;
    }

    public JSONObject getUserData(){
        return this.userData;
    }

    public String getError() {
        return error;
    }
}