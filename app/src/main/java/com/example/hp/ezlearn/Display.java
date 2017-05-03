package com.example.hp.ezlearn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Display extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    Map<Integer, String> lessons = new HashMap<>();
    public String lesson_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            lesson_id = extras.getString("lesson_id");
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

            HttpHandlerDisp sh = new HttpHandlerDisp();
            String jsonStr = sh.makeServiceCall(lesson_id);
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
                        String key = JO.getString("key");
                        lessons.put(i, key);
                        String value = JO.getString("value");
                        lessons.put(++i, value);
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

            displayLesson();
        }
    }


    public void displayLesson() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 1; i < lessons.size(); i = i + 2) {

            if (lessons.get(i - 1).contains("title")) {
                TextView value = new TextView(this);
                value.setText(lessons.get(i));
                value.setWidth(100);
                value.setHeight(100);
                this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.addView(value, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            } else if (lessons.get(i - 1).contains("text")) {

                TextView value = new TextView(this);
                value.setText(lessons.get(i));
                value.setWidth(100);
                value.setHeight(100);
                this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.addView(value, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            } else if (lessons.get(i - 1).contains("img")) {

                ImageView imageView = new ImageView(this);
                String value = lessons.get(i);
                this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.addView(imageView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                new DownloadImageTask(imageView).execute(value);

            } else if (lessons.get(i - 1).contains("note")) {

                TextView value = new TextView(this);
                value.setText(lessons.get(i));
                value.setWidth(100);
                value.setHeight(100);
                value.setBackgroundColor(Color.parseColor("#eeea87"));
                this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.addView(value, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }

        Button myButton = new Button(this);
        myButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        myButton.setText("Continue");
        myButton.setTextColor(Color.WHITE);
        myButton.setBackgroundColor(getResources().getColor(R.color.button));
        linearLayout.addView(myButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        setOnClick(myButton);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void setOnClick(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
