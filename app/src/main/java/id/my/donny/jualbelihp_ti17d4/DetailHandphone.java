package id.my.donny.jualbelihp_ti17d4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import id.my.donny.jualbelihp_ti17d4.model.Handphone;
import id.my.donny.jualbelihp_ti17d4.server.AsyncInvokeURLTask;

public class DetailHandphone extends AppCompatActivity {
    public static final String urlDelete = "delete_phone.php";
    private EditText textNama, textHarga;
    private Handphone handphone;
    protected void onCrete(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_handphone);
        handphone = new Handphone();
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initView() {
        textNama = (EditText) findViewById(R.id.Add_new_nama);
        textHarga = (EditText) findViewById(R.id.add_new_harga);
        String id = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("nama");
        String harga = getIntent().getStringExtra("harga");
        textNama.setText(nama);
        textHarga.setText(harga);
        handphone.setId(Integer.valueOf(id));
        handphone.setNama(nama);
        handphone.setHarga(harga);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_action, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.action_menu_edit:
                Intent in = new Intent(getApplicationContext(), FormHandphone.class);
                in.putExtra("id", handphone.getId().toString());
                in.putExtra("nama", handphone.getNama());
                in.putExtra("harga", handphone.getHarga());
                startActivity(in);
                break;
            case R.id.Action_menu_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void goToMainActivity() {
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete" + handphone.getNama() + " ?");
        builder.setTitle("Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData();
                Toast.makeText(getApplicationContext(), "Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.setIcon(android.R.drawable.ic_menu_delete);
        alert.show();
    }
    public void deleteData() {
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(handphone.getId())));
            AsyncInvokeURLTask task = new AsyncInvokeURLTask(nameValuePairs, new AsyncInvokeURLTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    Log.d("TAG", "Delete :" + result);
                    if (result.equals("Timeout") || result.trim().equalsIgnoreCase("Tidak dapat terkoneksi ke database")) {
                        Toast.makeText(getBaseContext(), "Tidak dapat terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                    } else {
                        goToMainActivity();
                    }
                }
            });
            task.showdialog = true;
            task.message = "Proses delete data, harap tunggu..";
            task.applicationContext =DetailHandphone.this;
            task.mNoteItWebUrl = urlDelete;
            task.execute();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

