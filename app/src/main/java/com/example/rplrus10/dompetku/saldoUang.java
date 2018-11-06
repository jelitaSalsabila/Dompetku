package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.N;

public class saldoUang extends AppCompatActivity {

    JSONArray Hasiljson;
    TextView tvnominal;
    Button btnOpsiKeluar;
    keuangan in;
    atribute user;
    keluar out;
    int totalbaru;
    int total = 0;
    int ttl= 0;
    ArrayList<keuangan> arraylist;
    ArrayList<keluar> keluarArrayList;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_uang);
        setTitle("Saldo");
        tvnominal = (TextView) findViewById(R.id.tvnominal);
        btnOpsiKeluar = (Button) findViewById(R.id.btnOpsiKeluar);
        in = new keuangan();
        user = new atribute();
        out = new keluar();

        btnOpsiKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(saldoUang.this, pengeluaran.class);
                    startActivityForResult(intent, 2);
                }
        });

        new dataSaldo().execute();
        new datakeluar().execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            int ttl = Integer.parseInt(message);
            int totalbaru;
            totalbaru = total-ttl;
            tvnominal.setText(""+totalbaru);


            if (totalbaru<100){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("PERINGATAN!");
                builder.setMessage("Uang anda telah habis, silahkan isi kembali!");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class dataSaldo extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                SharedPreferences mlogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String username = mlogin.getString("username", "");
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/getData2.php?username="+username;
                System.out.println("url ku " + url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                System.out.println("json nya " + json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                System.out.println("json error : " + e.toString());
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                Hasiljson = jsonObject.getJSONArray("hasil");
                arraylist = new ArrayList<keuangan>();
                for (int i = 0; i < Hasiljson.length(); i++) {
                    keuangan s = new keuangan();
                    s.setUsername(Hasiljson.getJSONObject(i).getString("username"));
                    s.setNominal(Hasiljson.getJSONObject(i).getString("nominal"));
                    arraylist.add(s);
                }
                int tmp = 0;
                for (int i = 0; i < arraylist.size(); i++) {
                   tmp=Integer.parseInt(arraylist.get(i).getNominal());
                   total+=tmp;
                   System.out.println("jumlah uang=" +total);

                    sharedpreferences = getSharedPreferences("saldo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", user.getUsername());
                    editor.putString("nominal", in.getNominal());
                    editor.putString("total", in.getNominal());
                    editor.commit();

                    tvnominal.setText(""+total);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class datakeluar extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                SharedPreferences mlogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String username = mlogin.getString("username", "");
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/getkeluar.php?username="+username;
                System.out.println("url ku " + url);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"
                ), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                String json = stringBuilder.toString();
                System.out.println("json nya " + json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                System.out.println("json error : " + e.toString());
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                Hasiljson = jsonObject.getJSONArray("hasil");
                keluarArrayList = new ArrayList<keluar>();
                for (int i = 0; i < Hasiljson.length(); i++) {
                    keluar out = new keluar();
                    out.setUsername(Hasiljson.getJSONObject(i).getString("username"));
                    out.setKeluar(Hasiljson.getJSONObject(i).getString("keluar"));
                    keluarArrayList.add(out);
                }
                int tmp1 = 0;
                for (int i = 0; i < keluarArrayList.size(); i++) {
                    tmp1=Integer.parseInt(keluarArrayList.get(i).getKeluar());
                    ttl+=tmp1;
                    System.out.println("jumlahUangKurang=" +ttl);
                    int totalNew;
                    totalNew = total-ttl;
                    System.out.println("hasil akhir " + totalNew);

                    tvnominal.setText(""+totalNew);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
