package com.example.hp.ezlearn;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by HP on 5/3/2017.
 */

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://ez-learndb.000webhostapp.com/userregistration/login.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
