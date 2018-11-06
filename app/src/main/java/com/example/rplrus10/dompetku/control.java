package com.example.rplrus10.dompetku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class control extends AppCompatActivity {
    public static final String data = "user";
    String nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(data, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        SharedPreferences sharedPreferences1 = getSharedPreferences("Login", MODE_PRIVATE);
        String username = sharedPreferences1.getString("username","");
        Log.d("username", "onCreate: "+username);
        nama = sharedPreferences.getString("",username);
        Log.d(nama, "onCreate: "+nama);
        if (nama == "") {
            Intent intent = new Intent(control.this,mylogin.class);
            editor.putString("isLogged", username);
            editor.commit();
            startActivity(intent);
            finish();

        } else if (nama == username){
            Intent intent = new Intent(control.this, mainMenu2.class);
            finish();
            startActivity(intent);
        }else {
            Intent intent = new Intent(control.this, mainMenu2.class);
            finish();
            startActivity(intent);
        }
    }
}
