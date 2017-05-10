package com.example.hp.ezlearn;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Comment extends AppCompatActivity {

    EditText eText;
    Button btn;
    SessionManager session;
    public String lesson_id;
    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        lesson_id = intent.getStringExtra("lesson_id");
        eText = (EditText) findViewById(R.id.comment);
        btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comment = eText.getText().toString();
                Toast.makeText(Comment.this, comment, Toast.LENGTH_SHORT).show();
                new addComment().execute();
                onBackPressed();
            }
        });
    }

    private class addComment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // get user data from session
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            String username = user.get(SessionManager.KEY_NAME);
            String user_id = user.get(SessionManager.KEY_ID);
            HttpHandlerAddCom sh = new HttpHandlerAddCom();
            sh.makeServiceCall(comment, username, user_id, lesson_id);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
