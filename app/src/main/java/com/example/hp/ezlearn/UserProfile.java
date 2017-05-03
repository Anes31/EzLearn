package com.example.hp.ezlearn;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    public static Map<Integer, String> favourites = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        displayLanguages();
    }



    /*public void putMap (int i, String id) {
        favourites.put(i, id);
    }*/

    public void displayLanguages() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.DKGRAY);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //scrollView.addView(linearLayout);
        scrollView.addView(linearLayout);

        for (int i = 0; i < favourites.size(); i++) {

            TextView lang_name = new TextView(this);

            lang_name.setText(favourites.get(i));
            lang_name.setTextSize(20);
            lang_name.setWidth(100);
            lang_name.setHeight(100);
            lang_name.setTextColor(getResources().getColor(R.color.texts));
            LinearLayout.LayoutParams x =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            x.setMargins(100, 100, 100, 10);
            linearLayout.addView(lang_name, x);
            //scrollView.addView(linearLayout);
            setContentView(linearLayout);

            /*this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));*/
        }
    }
}