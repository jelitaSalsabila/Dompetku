package com.example.rplrus10.dompetku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class mainMenu2 extends AppCompatActivity {

    ImageView imginputU,imgpengeluaran,imglist,imgprofil,imgsaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu2);

        imginputU=(ImageView)findViewById(R.id.imginputU);
        imgpengeluaran=(ImageView)findViewById(R.id.imgpengeluaran);
        imglist=(ImageView)findViewById(R.id.imglist);
        imgprofil=(ImageView)findViewById(R.id.imgprofil);
        imgsaldo=(ImageView)findViewById(R.id.imgsaldo);

        imginputU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainMenu2.this,inputUang.class);
                startActivity(intent);
            }
        });

        imgpengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainMenu2.this,pengeluaran.class);
                startActivity(intent);
            }
        });

        imglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainMenu2.this,ListPengeluaran.class);
                startActivity(intent);
            }
        });

        imgprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainMenu2.this,Profil.class);
                startActivity(intent);
            }
        });

        imgsaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainMenu2.this,saldoUang.class);
                startActivity(intent);
            }
        });
    }
}
