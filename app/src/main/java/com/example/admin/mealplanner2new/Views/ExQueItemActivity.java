package com.example.admin.mealplanner2new.Views;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.admin.mealplanner2new.R;

public class ExQueItemActivity extends AppCompatActivity {

    WebView webView_questions;

    private static final String web_url = "http://code-fuel.in/healthbotics/ex_que/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_que_item);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        webView_questions = findViewById(R.id.exQueItem_webView);

        webView_questions.getSettings().setJavaScriptEnabled(true);
        webView_questions.loadUrl(web_url);

        Toast.makeText(this, "Swipe to change the question", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
