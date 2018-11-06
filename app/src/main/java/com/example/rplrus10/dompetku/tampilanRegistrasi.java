package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.Attributes;

public class tampilanRegistrasi extends AppCompatActivity {

    EditText txtuser,txtpass,txtname,txtsekolah,Nomorhportu;
    Button btndaftar;
    daftar user;
    ProgressBar spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilan_registrasi);

        setTitle("Registrasi");
        txtuser=(EditText)findViewById(R.id.txtuser);
        txtpass=(EditText)findViewById(R.id.txtpass);
        txtname=(EditText)findViewById(R.id.txtname);
        spinner1 = (ProgressBar) findViewById(R.id.spinner1);
        user = new daftar();
        txtsekolah=(EditText)findViewById(R.id.txtsekolah);
        Nomorhportu=(EditText)findViewById(R.id.Nomorhportu);
        btndaftar=(Button) findViewById(R.id.btndaftar);

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(txtuser.getText().toString());
                user.setName(txtname.getText().toString());
                user.setPassword(txtpass.getText().toString());
                user.setSekolah(txtsekolah.getText().toString());
                user.setNomorOrtu(Nomorhportu.getText().toString());
                new register().execute();
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    public class register extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            //kasih loading
            spinner1.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;
            try {
                SharedPreferences mlogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String username = mlogin.getString("username", "");
                //tmp name -- java string change space into %20
                String tmpName= user.getName().replaceAll(" ", "%20");
                String tmpsekolah= user.getSekolah().replaceAll(" ", "%20");
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/registrasi.php?username="+user.getUsername()+"&password="+user.getPassword()+ "&nama=" + tmpName +"&sekolah="+ tmpsekolah+"&nomorOrtu="+user.getNomorOrtu();
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
                System.out.println("json nya error " +e.toString());
                jsonObject = null;
            }
            return jsonObject;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            System.out.println("hasil " +jsonObject.toString());
            spinner1.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result = jsonObject.getJSONObject("Result");
                    String sukses = Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: " + sukses);

                    if (sukses.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Registrasi Sukses, Silahkan Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(tampilanRegistrasi.this,mylogin.class);
                        startActivity(intent);
                        //to main menu
                    } else {
                        Toast.makeText(getApplicationContext(), "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(tampilanRegistrasi.this,tampilanRegistrasi.class);
                        startActivity(intent);
                    }
                } catch (Exception ignored) {
                }
            } else {
            }

        }
    }
}
