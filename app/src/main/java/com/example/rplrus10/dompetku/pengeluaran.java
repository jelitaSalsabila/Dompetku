package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.widget.Toolbar;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class pengeluaran extends AppCompatActivity {
    EditText txtnominal, txttoken, txtketerangan, txtTanggal, txtuser_out;
    Button btnTanggal, btnSave;
    keluar out;
    EditText editText;
    ProgressBar spinner4;
    SharedPreferences sharedpreferences;

    private int mYear1, mMonth2, mDay3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        setTitle("Pengeluaran");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtnominal = (EditText) findViewById(R.id.txtnominal);
        txtuser_out = (EditText) findViewById(R.id.txtuser_out);
        txttoken = (EditText) findViewById(R.id.txttoken);
        txtketerangan = (EditText) findViewById(R.id.txtketerangan);
        txtTanggal = (EditText) findViewById(R.id.txtTanggal);
        out = new keluar();
        spinner4 = (ProgressBar) findViewById(R.id.spinner4);
        btnTanggal = (Button) findViewById(R.id.btnTanggal);
        btnSave = (Button) findViewById(R.id.btnSave);
        Toolbar topToolBar = findViewById(R.id.spread);
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear1 = c.get(Calendar.YEAR);
                mMonth2 = c.get(Calendar.MONTH);
                mDay3 = c.get(Calendar.DAY_OF_MONTH);

                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(pengeluaran.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear1, mMonth2, mDay3);
                datePickerDialog.show();
            }
        });

        SharedPreferences msaldo = getSharedPreferences("saldo", Context.MODE_PRIVATE);
        final String total = msaldo.getString("total", "");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out.setTanggal(txtTanggal.getText().toString());
                out.setNoToken(txttoken.getText().toString());
                out.setKeterangan(txtketerangan.getText().toString());
                out.setKeluar(txtnominal.getText().toString());
                out.setUsername(txtuser_out.getText().toString());


                new outProcess().execute();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public class outProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading
            spinner4.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String tmpketerangan = out.getKeterangan().replaceAll(" ", "%20");
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/pengeluaran.php?noToken=" + out.getNoToken() + "&keterangan=" + tmpketerangan + "&keluar=" + out.getKeluar() + "&tanggal=" + out.getTanggal() + "&username=" + out.getUsername();
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
                System.out.println("json error : " + json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: " + jsonObject.toString());
            spinner4.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {

                        String message = txtnominal.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", message);
                        setResult(2, intent);
                        sharedpreferences = getSharedPreferences("message", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("MESSAGE", message);
                        editor.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Pengeluaran uang gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya " + ignored);
                }
            } else {

            }
        }
    }

    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "0");
        setResult(2, intent);
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "0");
        setResult(2, intent);
        finish();
    }
}

