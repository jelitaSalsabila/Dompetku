package com.example.rplrus10.dompetku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;

public class ListPengeluaran extends AppCompatActivity {

    JSONArray Hasiljson;
    LinearLayout listdata, listload;
    int index;
    ListView list_keluar;
    ArrayList<keluar> arraylist;
    private static CustomAdapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengeluaran);

        list_keluar = (ListView) findViewById(R.id.list_keluar);
        listdata = (LinearLayout) findViewById(R.id.listdata);
        listload = (LinearLayout) findViewById(R.id.listload);
        registerForContextMenu(list_keluar);
        new ambildata().execute();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.list_keluar){
            this.getMenuInflater().inflate(R.menu.menu_context_main, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //find out which menu item was pressed
        index = info.position;
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(getApplicationContext(),""+arraylist.get(index).getTanggal(), Toast.LENGTH_SHORT).show();
                new deleteData().execute();
                return true;
            default:
                return false;
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class ambildata extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                SharedPreferences mlogin = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String username = mlogin.getString("username", "");
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/getData.php?username="+username;
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
            listload.setVisibility(View.GONE);
            listdata.setVisibility(View.VISIBLE);

            try {
                arraylist = new ArrayList<>();
                Hasiljson  = jsonObject.getJSONArray("hasil");
                for (int i = 0; i<Hasiljson.length();i++){
                    keluar s = new keluar();
                    s.setTanggal(Hasiljson.getJSONObject(i).getString("tanggal"));
                    s.setKeluar(Hasiljson.getJSONObject(i).getString("keluar"));
                    s.setKeterangan(Hasiljson.getJSONObject(i).getString("keterangan"));

                    arraylist.add(s);
                }
                //pasang data arraylist ke listview
                Adapter = new CustomAdapter(arraylist,getApplicationContext());
                list_keluar.setAdapter(Adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class deleteData extends AsyncTask<Void, Void, JSONObject> {


        @Override
        protected void onPreExecute() {
            //kasih loading
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonObject;

            try {
                String url = "https://penyimpananuangapp.000webhostapp.com/pocket/deleteData.php?tanggal="+arraylist.get(index).getTanggal();
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
            if (jsonObject != null) {
                try {
                    JSONObject Result=jsonObject.getJSONObject("Result");
                    String sukses=Result.getString("Sukses");
                    Log.d("hasil sukses ", "onPostExecute: "+sukses);
                    if (sukses.equals("true")){
                        Toast.makeText(getApplicationContext(),"Delete sukses",Toast.LENGTH_SHORT).show();
                        new ambildata().execute();
                    }else{
                        Toast.makeText(getApplicationContext(),"Delete gagal",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ignored) {
                    System.out.println("erornya "+ignored);
                }
            }else{

            }
        }
    }
}