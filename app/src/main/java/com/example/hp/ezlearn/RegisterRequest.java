package com.example.hp.ezlearn;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 5/3/2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://ez-learndb.000webhostapp.com/userregistration/register.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String password, String email, int mobile, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("email", email);
        params.put("mobile", mobile + "");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
