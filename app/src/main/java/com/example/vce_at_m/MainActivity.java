package com.example.vce_at_m;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    Button btn;
    TextView rollno, descrip, overall;
    SharedPreferences pref;
    String id,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView1);
        btn = findViewById(R.id.button);
        rollno = findViewById(R.id.textView);
        descrip = findViewById(R.id.textView8);
        overall = findViewById(R.id.textView9);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setInitialScale(100);
        pref = getSharedPreferences("login_details", Context.MODE_PRIVATE);
        id = pref.getString("id",null);
        pass = pref.getString("pass",null);
        rollno.setText(id);

        Intent intent = getIntent();
        int flag = intent.getIntExtra("at_m",1);
        if(flag == 2)
            new attendaceFetch().execute();
        else
            new marksFetch().execute();
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class attendaceFetch extends AsyncTask<Void,Void,Void>{
        org.jsoup.nodes.Document doc;
        Elements link;
        Element checker;
        int flag=2;
        String absHref;
        Document attendence;
        StringBuilder url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            descrip.setText("Attendance:");
            overall.setText(" Fetching..");
            webView.loadData("<p  style=\"text-align: center\">Fetching...</p>","text/html","utf-8");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = (Document) Jsoup.connect("https://www.vce.ac.in/")
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .timeout(3000000)
                        .data("LoginID",id,"Password",pass)
                        .post();
                checker = doc.getElementById("studForm");
                if(checker!=null) {
                    link = doc.select("a[href]");
                    absHref = link.get(0).attr("abs:onclick");
                    int len = absHref.length();
                    url = new StringBuilder();
                    for (int i = 29; i < len - 2; i++) {
                        url.append(absHref.charAt(i));
                    }
                    attendence = Jsoup.connect(url.toString())
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                            .timeout(3000000)
                            .get();
                    flag = 3;
                }
                else{
                    checker = doc.getElementById("loginForm");
                    if(checker == null){
                        flag = 1;//server down
                    }
                    else{
                        flag = 0;//invalid
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag == 0) {
                overall.setText("sorry");
                webView.loadData("<h2>Invalid credentials</h2><h4>Please follow following steps<ol><li>Go back to previous screen</li><li>Click on signout</li><li>Enter valid details and click on save</li></ol>Then try again</h4>","text/html","utf-8");
            }
            else if(flag == 1){
                overall.setText("sorry");
                webView.loadData("<h2>College server down<h2>","text/html","utf-8");
            }
            else if(flag == 2){
                overall.setText("sorry");
                webView.loadData("<h2>No Internet<h2><h3>check your connection and try again</h3>","text/html","utf-8");
            }
            else{
                Element outerTable = attendence.getElementById("TdDispAttSummary");
                Elements innerTable = outerTable.select("table");
                Elements tableBody = innerTable.get(0).select("tbody");
                Elements tableRow = tableBody.get(0).select("tr");
                Elements rowData = tableRow.get(tableRow.size()-1).select("td");
                Elements datarow = rowData.get(rowData.size()-1).select("font");
                overall.setText(new String(' '+datarow.get(0).text()+"%"));
                webView.loadData(attendence.toString(),"text/html","utf-8");
            }
        }
    }

    public class marksFetch extends AsyncTask<Void,Void,Void>{
        org.jsoup.nodes.Document doc;
        Elements link;
        Element checker;
        String absHref;
        Document attendence;
        StringBuilder url;
        int flag=2;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            descrip.setText("CGPA:");
            overall.setText(" Fetching..");
            webView.loadData("<p  style=\"text-align: center\">Fetching...</p>","text/html","utf-8");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = (Document) Jsoup.connect("https://www.vce.ac.in/")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .timeout(3000000)
                        .data("LoginID",id,"Password",pass)
                        .post();
                checker = doc.getElementById("studForm");
                if(checker!=null) {
                    link = doc.select("a[href]");
                    absHref = link.get(1).attr("abs:onclick");
                    int len = absHref.length();
                    url = new StringBuilder();
                    for (int i = 29; i < len - 2; i++) {
                        url.append(absHref.charAt(i));
                    }
                    attendence = Jsoup.connect(url.toString())
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                            .timeout(3000000)
                            .get();
                    flag = 3;
                }
                else{
                    checker = doc.getElementById("loginForm");
                    if(checker == null){
                        flag = 1;//server down
                    }
                    else{
                        flag = 0;//invalid
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag == 0) {
                overall.setText("sorry");
                webView.loadData("<h2>Invalid credentials</h2><h4>Please follow following steps<ol><li>Go back to previous screen</li><li>Click on signout</li><li>Enter valid details and click on save</li></ol>Then try again</h4>","text/html","utf-8");
            }
            else if(flag == 1){
                overall.setText("sorry");
                webView.loadData("<h2>College server down<h2>","text/html","utf-8");
            }
            else if(flag == 2){
                overall.setText("sorry");
                webView.loadData("<h2>No Internet<h2><h3>check your connection and try again</h3>","text/html","utf-8");
            }
            else {
                Element outerTable = attendence.getElementById("TdDispCGPAInfo");
                Elements innerTable = outerTable.select("table");
                Elements tableBody = innerTable.get(0).select("tbody");
                Elements tableRow = tableBody.get(0).select("tr");
                Elements rowData = tableRow.get(0).select("td");
                Elements datarow = rowData.get(rowData.size() - 1).select("font");
                overall.setText(new String(' '+datarow.get(0).text()));
                webView.loadData(attendence.toString(), "text/html", "utf-8");
            }
        }
    }
}
