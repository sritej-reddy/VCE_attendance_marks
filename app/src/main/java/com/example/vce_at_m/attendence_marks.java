package com.example.vce_at_m;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class attendence_marks extends AppCompatActivity {
    SharedPreferences pref;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences("login_details",Context.MODE_PRIVATE);
        if(!loggedIn()){
            goToLoginPage();
        }
        setContentView(R.layout.activity_attendence_marks);
        setUserName();
        Button signout = findViewById(R.id.button4);
        signout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                clearPreferences();
                goToLoginPage();
                finish();
            }
        });
        Button attendence = findViewById(R.id.button2), marks = findViewById(R.id.button3);
        attendence.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gotToAt_mar(2);
            }
        });
        marks.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gotToAt_mar(1);
            }
        });
    }

    private void gotToAt_mar(int a) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("at_m",a);
        startActivity(intent);
    }

    private boolean loggedIn() {
        id = pref.getString("id",null);
        return id != null;
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",null);
        editor.putString("pass",null);
        editor.apply();
    }

    private void goToLoginPage() {
        Intent intent=new Intent(this,Login_Screen.class);
        startActivity(intent);
        finish();
    }

    void setUserName(){
        TextView user_name = findViewById(R.id.textView3);
        user_name.setText(id);
    }
}