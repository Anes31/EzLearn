package com.example.hp.ezlearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private String TAG = FacebookLogin.class.getSimpleName();
    Map<Integer, String> favourites = new HashMap<>();
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        new getJson().execute();
    }

    private class getJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // get user data from session
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String user_id = user.get(SessionManager.KEY_ID);

            HttpHandlerFav sh = new HttpHandlerFav();
            String jsonStr = sh.makeServiceCall(user_id);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Storing languages data in JSON Array
                    JSONArray results = jsonObj.getJSONArray("languages");

                    int count = 0;      //JSON Array index (2 dimensional array)
                    int i = 0;          //Hashmap index (Hashmap is 1 dimensional so its size will be JSON array size x 2)
                    while (count < results.length()) {

                        //Getting the languages data 1 by 1 and storing it into the Hashmap
                        JSONObject JO = results.getJSONObject(count);
                        String language_id = JO.getString("lang_id");
                        favourites.put(i, language_id);
                        String language_name = JO.getString("lang_name");
                        favourites.put(++i, language_name);
                        count++;
                        i++;
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            displayLanguages();

        }
    }

    public void displayLanguages() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.DKGRAY);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        for (int i = 1; i < favourites.size(); i = i + 2) {

            TextView lang_name = new TextView(this);

            lang_name.setText(favourites.get(i));
            lang_name.setTextSize(20);
            //lang_name.setTextColor(#FFFF);
            lang_name.setId(i - 1);
            lang_name.setWidth(70);
            lang_name.setHeight(70);
            lang_name.setTextColor(getResources().getColor(R.color.texts));
            LinearLayout.LayoutParams x =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            x.setMargins(100, 20, 50, 10);
            linearLayout.addView(lang_name, x);
            setContentView(linearLayout);
            setOnClick(lang_name, favourites.get(i - 1));


            this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            /*linearLayout.addView(lang_name, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));*/
        }
    }

    private void setOnClick(final TextView txt, final String id) {
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txt) {
                    Intent intent = new Intent(getApplicationContext(), Chapters.class);
                    intent.putExtra("lang_id", id);
                    startActivity(intent);
                }
            }
        });
    }
}