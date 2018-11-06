package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class mylogin extends AppCompatActivity {

    JSONArray Hasiljson;
    EditText txtusername, txtpassword;
    CheckBox cbxsee;
    Button btnlogin;
    public ArrayList<atribute> atribute = new ArrayList<>();
    TextView tvregistrasi;
    atribute user;
    ProgressBar spinner;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylogin);
        setTitle("LOGIN");
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        tvregistrasi = (TextView) findViewById(R.id.tvregistrasi);
        txtusername = (EditText) findViewById(R.id.txtusername);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        user = new atribute();
        CheckBox c = (CheckBox) findViewById(R.id.cbxSee);
        spinner = (ProgressBar) findViewById(R.id.spinner);

        tvregistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mylogin.this,tampilanRegistrasi.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(txtusername.getText().toString());
                user.setPassword(txtpassword.getText().toString());
                if (user.getUsername().equals("")|| user.getPassword().equals("")){
                    Toast.makeText(getApplicationContext(),"Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mylogin.this,mylogin.class);
                    startActivity(intent);
                }
                else{
                    new LoginProcess().execute();
                    new profilUser().execute();
                }
            }
        });
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                //Membuat Kondisi Checkbox Menggunakan If Else
                if (!isChecked) {
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }

        });
}
    @SuppressLint("StaticFieldLeak")
    public class LoginProcess extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute(){
            //kasih loading
            spinner.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/login.php?username="+user.getUsername()+"&password="+user.getPassword();
                System.out.println("url ku " +url);
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
                System.out.println("json error : "+json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                jsonObject = null;
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.d("hasil json ", "onPostExecute: "+jsonObject.toString());
            spinner.setVisibility(View.INVISIBLE);
            if (jsonObject != null) {
                try {
                    JSONObject Result=jsonObject.getJSONObject("Result");
                    String sukses=Result.getString("Sukses");
                    Log.d("hasil sukses", "onPostExecute: "+sukses);

                    if (sukses.equals("true")){
                        Intent intent = new Intent(mylogin.this,mainMenu2.class);
                        Toast.makeText(getApplicationContext(),"login sukses",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", user.getUsername());
                        editor.commit();

                    }else{
                        Toast.makeText(getApplicationContext(),"Anda belum terdaftar",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            }else{

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class profilUser extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/getProfil.php?username="+user.getUsername();
                System.out.println("url ku " +url);
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
            //System.out.println("Hasilnya adalah " +jsonObject.toString());

            try {
                Hasiljson = jsonObject.getJSONArray("hasil");
                atribute  = new ArrayList<atribute>();
                for (int i = 0; i<Hasiljson.length();i++) {
                    user = new atribute();
                    user.setName(Hasiljson.getJSONObject(i).getString("nama"));
                    user.setSekolah(Hasiljson.getJSONObject(i).getString("sekolah"));
                    user.setNomorOrtu(Hasiljson.getJSONObject(i).getString("nomorOrtu"));

                    atribute.add(user);

                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("nama", user.getName());
                    editor.putString("sekolah", user.getSekolah());
                    editor.putString("nomorOrtu", user.getNomorOrtu());
                    editor.commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
