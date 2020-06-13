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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView1);
        btn = findViewById(R.id.button);
        rollno = findViewById(R.id.textView);
        descrip = findViewById(R.id.textView8);
        overall = findViewById(R.id.textView9);
        pref = getSharedPreferences("login_details", Context.MODE_PRIVATE);
        rollno.setText(pref.getString("id",null));

        /*Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        float height = metrics.heightPixels, width = metrics.widthPixels, dp = metrics.scaledDensity;
        rollno.setTextSize(height*(24f/640f)/dp);
        descrip.setTextSize(height*(15f/640f)/dp);
        overall.setTextSize(height*(15f/640f)/dp);*/

        new Fetch().execute();
        //webView.loadData("<h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1><h1>HELLO</h1>","text/html","utf-8");
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToAttendenceMarks(this);
                finish();
            }
        });
        /*Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        float height = metrics.heightPixels;
        ViewGroup.LayoutParams lp = webView.getLayoutParams();
        System.out.println(height);
        lp.height = (int)(height-((int)height/3.69));
        //lp.height = 833;
        System.out.println(lp.height);
        webView.setLayoutParams(lp);*/
    }



    public class Fetch extends AsyncTask<Void,Void,Void>{
        org.jsoup.nodes.Document doc;
        Elements link;
        String absHref;
        Document attendence;
        StringBuilder url;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = (Document) Jsoup.connect("https://www.vce.ac.in/")
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .data("LoginID","1602-17-737-108","Password","graver")
                        .post();
                //Map<String,String> coky = res.cookies();
                link = doc.select("a[href]");
                absHref = link.get(0).attr("abs:onclick");
                int len = absHref.length();
                url = new StringBuilder();
                for(int i=29;i<len-2;i++){
                    url.append(absHref.charAt(i));
                }

                attendence = Jsoup.connect(url.toString())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .timeout(3000000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Element outerTable = attendence.getElementById("TdDispAttSummary");
            Elements innerTable = outerTable.select("table");
            Elements tableBody = innerTable.get(0).select("tbody");
            Elements tableRow = tableBody.get(0).select("tr");
            Elements rowData = tableRow.get(tableRow.size()-1).select("td");
            Elements datarow = rowData.get(rowData.size()-1).select("font");
            overall.setText(datarow.get(0).text());
            webView.loadData(attendence.toString(),"text/html","utf-8");
            System.out.println("HELLO");
        }
    }

    public void goToAttendenceMarks(View.OnClickListener v){
        Intent intent=new Intent(this,attendence_marks.class);
        startActivity(intent);
        webView.stopLoading();
        finish();
    }
}
