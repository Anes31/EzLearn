package com.example.hp.ezlearn;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Lessons extends AppCompatActivity {

    private String TAG = FacebookLogin.class.getSimpleName();
    Map<Integer, String> lessons = new HashMap<>();
    public String chap_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            chap_id = extras.getString("chap_id");
            new getJson().execute();
        }
    }

    private class getJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandlerLess sh = new HttpHandlerLess();
            String jsonStr = sh.makeServiceCall(chap_id);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("lessons");

                    int count = 0;      //JSON Array index (2 dimensional array)
                    int i = 0;          //Hashmap index (Hashmap is 1 dimensional so its size will be JSON array size x 2)
                    while (count < results.length()) {

                        //Getting the chapters data 1 by 1 and storing it into the Hashmap
                        JSONObject JO = results.getJSONObject(count);
                        String lesson_id = JO.getString("lesson_id");
                        lessons.put(i, lesson_id);
                        String title = JO.getString("title");
                        lessons.put(++i, title);
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

            displayChapters();
        }
    }

    public void displayChapters() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 1; i < lessons.size(); i = i + 2) {

            TextView title = new TextView(this);
            title.setText(lessons.get(i));
            title.setWidth(100);
            title.setHeight(100);
            this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.addView(title, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            setOnClick(title, lessons.get(i - 1));
        }
    }

    private void setOnClick(final TextView txt, final String id) {
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Display.class);
                intent.putExtra("lesson_id", id);
                startActivity(intent);

            }
        });
    }
}
