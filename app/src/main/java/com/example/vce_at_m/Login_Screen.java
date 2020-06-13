package com.example.vce_at_m;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login_Screen extends AppCompatActivity {
    EditText id,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        Button login = findViewById(R.id.button5);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(verify()) {
                    SharedPreferences pref = getSharedPreferences("login_details",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id",id.getText().toString());
                    editor.putString("pass",pass.getText().toString());
                    editor.apply();
                    goToAttendenceMarks(this);
                }
                else{
                    findViewById(R.id.textView7).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean verify() {
        id = findViewById(R.id.editTextTextPersonName);
        pass = findViewById(R.id.editTextTextPassword);
        return true;
    }

    public void goToAttendenceMarks(View.OnClickListener v){
        Intent intent=new Intent(this,attendence_marks.class);
        startActivity(intent);
        finish();
    }
}