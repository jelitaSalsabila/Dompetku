package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.widget.DatePicker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class inputUang extends AppCompatActivity {

    EditText txtTanggal, txtinput, txtToken,nameuser;
    Button btntanggal, btnSave;
    ProgressBar spinner3;
    keuangan in;
    String tanggal;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_uang);
        setTitle("Masukkan Uang");
        nameuser = (EditText) findViewById(R.id.nameuser);
        txtTanggal = (EditText) findViewById(R.id.txtTanggal);
        btntanggal = (Button) findViewById(R.id.btnTanggal);
        txtinput = (EditText) findViewById(R.id.txtinput);
        txtToken = (EditText) findViewById(R.id.txtToken);
        btnSave = (Button) findViewById(R.id.btnSave);
        spinner3 = (ProgressBar) findViewById(R.id.spinner3);
        in = new keuangan();

        btntanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(inputUang.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                tanggal = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.setUsername(nameuser.getText().toString());
                in.setNominal(txtinput.getText().toString());
                in.setTanggal(txtTanggal.getText().toString());
                in.setNoToken(txtToken.getText().toString());
                if (in.getTanggal().equals("") || in.getNominal().equals("") || in.getNoToken().equals("") || in.getUsername().equals("")) {
                    Toast.makeText(getApplicationContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(inputUang.this,inputUang.class);
                    startActivity(intent);
                } else {
                    new inputProcess().execute();
                }
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class inputProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading
            spinner3.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/bulanan.php?noToken=" + in.getNoToken() + "&username=" + in.getUsername() + "&nominal=" + in.getNominal() + "&tanggal=" + in.getTanggal();
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
            spinner3.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);
                    if (sukses.equals("true")) {

                        Toast.makeText(getApplicationContext(), "pemasukan sukses", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(inputUang.this,saldoUang.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "pemasukan gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya " + ignored);
                }
            } else {
            }
        }
    }
}