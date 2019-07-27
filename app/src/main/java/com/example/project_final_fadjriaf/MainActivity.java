package com.example.project_final_fadjriaf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.project_final_fadjriaf.SqliteHelper.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private JSONObject jsonobject;
    private JSONArray jsonarray;
    private ArrayList<Employee> employee;
    private ProgressDialog progressDialog;
    private ListView listNama;
    private Adapter adapter;
    private TextView noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNama = findViewById(R.id.listNama);
        noData = findViewById(R.id.noData);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainAdd.class));
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Harap Tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new searchEmployee().execute();
    }

    public class searchEmployee extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            employee = new ArrayList<>();
            String Url = "http://192.168.100.121/vsga/tampilsemuapgw.php";

            try {
                URL url = new URL(Url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.flush();
                wr.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response = line;
                }

                bufferedReader.close();
                httpURLConnection.disconnect();

                jsonobject = new JSONObject(response);
                jsonarray = jsonobject.getJSONArray("result");

                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Employee populasi = new Employee();

                    populasi.setId(jsonobject.optString("id"));
                    populasi.setNama(jsonobject.optString("nama"));
                    populasi.setPosisi(jsonobject.optString("posisi"));
                    employee.add(populasi);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            try {
                if(employee.size() == 0) {
                    noData.setVisibility(View.VISIBLE);
                } else {
                    noData.setVisibility(View.GONE);
                    adapter = new Adapter(MainActivity.this, employee);
                    listNama.setAdapter(adapter);

                    listNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Pilih Aksi!");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog = new ProgressDialog(MainActivity.this);
                                    progressDialog.setMessage("Harap Tunggu...");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();
                                    new deleteEmployee(adapter.items.get(pos).getId(), MainActivity.this).execute();
                                }
                            });

                            builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(MainActivity.this, MainAdd.class);
                                    intent.putExtra("process", "edit");
                                    intent.putExtra("id", adapter.items.get(pos).getId());
                                    intent.putExtra("nama", adapter.items.get(pos).getNama());
                                    intent.putExtra("posisi", adapter.items.get(pos).getPosisi());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        }
                    });
                }
                progressDialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class deleteEmployee extends AsyncTask<Void, Void, String> {

        private String id;
        private Context context;

        public deleteEmployee(String paramId, MainActivity context) {
            id = paramId;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String Url = "http://192.168.100.121/vsga/hapuspgw.php?id="+id;
            String result = null;

            try {
                URL url = new URL(Url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.flush();
                wr.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = bufferedReader.readLine()) != null)
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
            if(result.contains("Berhasil Menghapus Pegawai")){
                Toast.makeText(context, "Berhasil Menghapus Pegawai", Toast.LENGTH_SHORT).show();
                new searchEmployee().execute();
            } else {
                progressDialog.dismiss();
                Toast.makeText(context, "Gagal Menghapus Pegawai", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Adapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<Employee> items;

        public Adapter(Activity activity, ArrayList<Employee> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int location) {
            return items.get(location);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null)
                convertView = inflater.inflate(R.layout.activity_main_row, null);

            final TextView nama = (TextView) convertView.findViewById(R.id.nama);
            final TextView posisi = (TextView) convertView.findViewById(R.id.posisi);

            Employee data = items.get(position);
            nama.setText(data.getNama());
            posisi.setText(data.getPosisi());

            return convertView;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
