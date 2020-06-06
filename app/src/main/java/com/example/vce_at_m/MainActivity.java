package com.example.vce_at_m;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebView data ;
    Button btn;
    TextView rollno, descrip, overall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = findViewById(R.id.webview);
        btn = findViewById(R.id.button6);
        rollno = findViewById(R.id.textView8);
        descrip = findViewById(R.id.textView9);
        overall = findViewById(R.id.textView10);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        float height = metrics.heightPixels, width = metrics.widthPixels, dp = metrics.scaledDensity;
        rollno.setTextSize(height*(24f/640f)/dp);
        descrip.setTextSize(height*(15f/640f)/dp);
        overall.setTextSize(height*(15f/640f)/dp);
        data


        data.loadData("<h1>HELLO</h1>","text/html","utf-8");
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //data.setText("hi");
               // new Fetch().execute();
            }
        });
    }
    public class Fetch extends AsyncTask<Void,Void,Void>{
        org.jsoup.nodes.Document doc;
        Elements link;
        String absHref,attendence;

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
                String url = "";
                for(int i=29;i<len-2;i++){
                    url += absHref.charAt(i);
                }

               /* OkHttpClient client = new OkHttpClient().Builder
                        .readTimeoutMillis(100000, TimeUnit.MILLISECONDS)
                        .build();
                Request req = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        data.setText("Success");
                    }
                });*/
                attendence = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .ignoreContentType(true)
                        .timeout(30000)
                        .get()
                        .text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // data.setText(attendence);
        }
    }
}
