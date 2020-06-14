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
        id = findViewById(R.id.editTextTextPersonName);
        pass = findViewById(R.id.editTextTextPassword);
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

    private boolean verify(){
        String roll = id.getText().toString();
        if((pass.getText().toString()).length()!=6 || roll.length()!=15)
            return false;
        return roll.substring(0, 4).equals("1602") && roll.charAt(4) == '-' && roll.charAt(7) == '-' && roll.charAt(11) == '-';
    }

    public void goToAttendenceMarks(View.OnClickListener v){
        Intent intent=new Intent(this,attendence_marks.class);
        startActivity(intent);
        finish();
    }
}