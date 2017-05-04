package com.example.hp.ezlearn;

import android.content.Intent;
import android.graphics.Color;
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

public class Chapters extends AppCompatActivity {

    private String TAG = FacebookLogin.class.getSimpleName();
    Map<Integer, String> chapters = new HashMap<>();
    public String lang_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            lang_id = extras.getString("lang_id");
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

            HttpHandlerChap sh = new HttpHandlerChap();
            String jsonStr = sh.makeServiceCall(lang_id);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("chapters");

                    int count = 0;      //JSON Array index (2 dimensional array)
                    int i = 0;          //Hashmap index (Hashmap is 1 dimensional so its size will be JSON array size x 2)
                    while (count < results.length()) {

                        //Getting the chapters data 1 by 1 and storing it into the Hashmap
                        JSONObject JO = results.getJSONObject(count);
                        String chapter_id = JO.getString("chapter_id");
                        chapters.put(i, chapter_id);
                        String chapter_name = JO.getString("chapter_name");
                        chapters.put(++i, chapter_name);
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
        linearLayout.setBackgroundColor(Color.DKGRAY);


        for (int i = 1; i < chapters.size(); i = i + 2) {

            TextView chap_name = new TextView(this);
            chap_name.setText(chapters.get(i));
            chap_name.setWidth(100);
            chap_name.setHeight(50);
            chap_name.setTextColor(getResources().getColor(R.color.texts));
            LinearLayout.LayoutParams x =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            /*this.setContentView(relativeLayout, new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            relativeLayout.addView(chap_name, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));*/
            x.setMargins(100, 100, 100, 10);
            linearLayout.addView(chap_name, x);
            setContentView(linearLayout);


            setOnClick(chap_name, chapters.get(i - 1));
        }
    }

    private void setOnClick(final TextView txt, final String id) {
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Lessons.class);
                intent.putExtra("chap_id", id);
                startActivity(intent);

            }
        });
    }
}
