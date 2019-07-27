package com.example.project_final_fadjriaf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainAdd extends AppCompatActivity {
    private EditText iNama, iPosisi;
    private Button btnSubmit, btnCancel;
    private String process;
    private ProgressDialog progressDialog;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add);

        iNama = findViewById(R.id.iNama);
        iPosisi = findViewById(R.id.iPosisi);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        if(intent != null) {
            process = intent.getStringExtra("process");
            if(process != null) {
                id = intent.getStringExtra("id");
                iNama.setText(intent.getStringExtra("nama"));
                iPosisi.setText(intent.getStringExtra("posisi"));
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iNama.getText().toString().equals("")) {
                    Toast.makeText(MainAdd.this, "Isi Kolom Nama", Toast.LENGTH_SHORT).show();
                } else {
                    if(iPosisi.getText().toString().equals("")) {
                        Toast.makeText(MainAdd.this, "Isi Kolom Posisi", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(MainAdd.this);
                        progressDialog.setMessage("Harap Tunggu...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        String nama = iNama.getText().toString();
                        String posisi = iPosisi.getText().toString();
                        if(process != null) {
                            editEmployee(id, nama, posisi);
                        } else {
                            addEmployee(nama, posisi);
                        }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAdd.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void addEmployee(String nama, String posisi) {
        new apiAddEmployee(MainAdd.this, nama, posisi).execute();
    }

    private void editEmployee(String id, String nama, String posisi) {
        new apiEditEmployee(id, MainAdd.this, nama, posisi).execute();
    }

    private class apiAddEmployee extends AsyncTask<Void, Void, String> {

        public Context context;
        public String nama, posisi;

        public apiAddEmployee(MainAdd context, String name, String posisi) {
            this.context = context;
            this.nama = name;
            this.posisi = posisi;

        }

        @Override
        protected String doInBackground(Void... params) {
            String Url = "http://192.168.100.121/vsga/tambahpgw.php";
            InputStream is = null;
            List<NameValuePair> paramPost = new ArrayList<NameValuePair>();
            paramPost.add(new BasicNameValuePair("name", nama));
            paramPost.add(new BasicNameValuePair("position", posisi));
            paramPost.add(new BasicNameValuePair("salary", "0"));
            String result = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Url);
                httpPost.setEntity(new UrlEncodedFormEntity(paramPost));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                is = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.contains("Berhasil Menambahkan Pegawai")){
                Toast.makeText(context, "Berhasil Menambahkan Pegawai", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Gagal Menambahkan Pegawai", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    private class apiEditEmployee extends AsyncTask<Void, Void, String> {

        private String id;
        private Context context;
        public String nama, posisi;

        public apiEditEmployee(String id, MainAdd context, String nama, String posisi) {
            this.id = id;
            this.context = context;
            this.nama = nama;
            this.posisi = posisi;
        }

        @Override
        protected String doInBackground(Void... params) {
            String Url = "http://192.168.100.121/vsga/updatepgw.php";
            InputStream is = null;
            List<NameValuePair> paramPost = new ArrayList<NameValuePair>();
            paramPost.add(new BasicNameValuePair("id", id));
            paramPost.add(new BasicNameValuePair("name", nama));
            paramPost.add(new BasicNameValuePair("position", posisi));
            paramPost.add(new BasicNameValuePair("salary", "0"));
            String result = null;

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Url);
                httpPost.setEntity(new UrlEncodedFormEntity(paramPost));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                is = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.contains("Berhasil Update Data Pegawai")){
                Toast.makeText(context, "Berhasil Update Data Pegawai", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Gagal Update Data Pegawai", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
