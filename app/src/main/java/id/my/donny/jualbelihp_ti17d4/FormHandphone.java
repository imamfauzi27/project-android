package id.my.donny.jualbelihp_ti17d4;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import id.my.donny.jualbelihp_ti17d4.model.Handphone;
import id.my.donny.jualbelihp_ti17d4.server.AsyncInvokeURLTask;

class FormHandphone extends AppCompatActivity {
    private EditText textNama, textHarga;
    private Handphone handphone;
    public static final String urlSubmit = "submit_phone.php";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_handphone);
        initView();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        handphone = new Handphone();
        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            String nama = getIntent().getStringExtra("nama");
            String harga = getIntent().getStringExtra("harga");
            textNama.setText(nama);
            textHarga.setText(harga);
            handphone.setId(Integer.valueOf(id));
        } else {
            handphone.setId(0);
        }
    }
    private void initView() {
        textNama = (EditText) findViewById(R.id.Add_new_nama);
        textHarga = (EditText) findViewById(R.id.add_new_harga);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_handphone, menu);
        return true;
    }
    private void goToMainActivity() {
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.option_menu_save:
                if (textHarga.getText().toString().trim().isEmpty() || textNama.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama dan Harga tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    sendData();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void sendData() {
        try {
            String nama = textNama.getText().toString();
            String harga = URLEncoder.encode(textHarga.getText().toString(), "utf-8");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("nama", nama));
            nameValuePairs.add(new BasicNameValuePair("harga", harga));
            nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(handphone.getId())));
            AsyncInvokeURLTask task = new AsyncInvokeURLTask(nameValuePairs, new AsyncInvokeURLTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    Log.d("TAG", "savedata :" + result);
                    if (result.equals("timeout") || result.trim().equalsIgnoreCase("Tidak dapat tekoneksi ke database")) {
                    } else {
                        goToMainActivity();
                    }
                }
            });
            task.showdialog = true;
            task.message = "Proses submit data, harap tunggu";
            task.applicationContext = FormHandphone.this;
            task.mNoteItWebUrl = urlSubmit;
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

