package com.example.yogidot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class SiteToString extends AppCompatActivity {

    public String site;
    static String htmlPageUrl = "null";
    private String htmlContentInStringFormat;
    //    private TextView textviewHtmlDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.asdfasdf);

        Intent intent = getIntent();
        site = intent.getStringExtra("SITE"); //Main에서 Site 받고 넣기!

        htmlPageUrl = site;
//        textviewHtmlDocument = (TextView)findViewById(R.id.p_data);
//        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod());


        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }


    void handleSendText() {
        if (htmlContentInStringFormat != null) {
            Intent addcontentintent = new Intent(this, AddContentActivity.class);
            addcontentintent.putExtra("EVALUATION", htmlContentInStringFormat);
            startActivity(addcontentintent);
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements links = doc.select("title");

                for (Element link : links) {
                    htmlContentInStringFormat += (link.attr("abs:href")
                            + "("+link.text().trim() + ")\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            handleSendText();
        }

    }

}
