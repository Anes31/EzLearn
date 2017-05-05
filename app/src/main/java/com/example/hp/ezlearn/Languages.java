package com.example.hp.ezlearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Languages extends AppCompatActivity {

    private String TAG = Login.class.getSimpleName();
    Map<Integer, String> languages = new HashMap<>();
    int i = -1;
    SessionManager session;
    String lang_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        new getJson().execute();
    }

    private class getJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandlerLang sh = new HttpHandlerLang();
            String jsonStr = sh.makeServiceCall();
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
                        String language_id = JO.getString("id");
                        languages.put(i, language_id);
                        String language_name = JO.getString("label");
                        languages.put(++i, language_name);
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

        for (int i = 1; i < languages.size(); i = i + 2) {

            TextView lang_name = new TextView(this);

            lang_name.setText(languages.get(i));
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
            setOnClick(lang_name, languages.get(i - 1));

            Button addFav = new Button(this);

            addFav.setText("Add favourite");
            //addFav.setId();
            addFav.setWidth(100);
            addFav.setHeight(100);
            setOnClickBtn(addFav, languages.get(i - 1), languages.get(i));


            this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            /*linearLayout.addView(lang_name, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));*/
            linearLayout.addView(addFav, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
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

    private void setOnClickBtn(final Button btn, final String lang_id, final String lang_name) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (v == btn) {
                    lang_ID = lang_id;
                    //HttpHandlerAddFav sh = new HttpHandlerAddFav();

                    Toast.makeText(Languages.this, lang_name + " has been added to your favourite", Toast.LENGTH_SHORT).show();
                    new addFavourite().execute();
                    //sh.makeServiceCall(lang_id, user_id);
                }
            }
        });
    }

    private class addFavourite extends AsyncTask<Void, Void, Void> {

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
            HttpHandlerAddFav sh = new HttpHandlerAddFav();
            sh.makeServiceCall(lang_ID, user_id);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}