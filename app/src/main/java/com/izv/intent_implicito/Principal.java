package com.izv.intent_implicito;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.izv.implicitintent.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

//lector de texto
public class Principal extends Activity {

    EditText editor;
    Button guardar;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        editor = (EditText)findViewById(R.id.editText);
        guardar = (Button)findViewById(R.id.button);
        Intent intent = getIntent();
        Uri data = intent.getData();
        path = data.getPath();
        abrirArchivo(intent);
    }

    public void abrirArchivo(Intent intent){
        if(isLegible()) {
            if (intent.getType().equals("text/plain")) {
                File archivo = new File(path);
                try {
                    BufferedReader in = new BufferedReader(new FileReader(archivo));
                    String linea;
                    StringBuilder texto = new StringBuilder("");
                    while ((linea = in.readLine()) != null) {
                        texto.append(linea + '\n');
                    }
                    in.close();
                    editor.setText(texto.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void guardar(View view) throws IOException {
        if (isModificable() && espacioSuficiente(new File(path))) {
            FileOutputStream archivo = new FileOutputStream(path);
            OutputStreamWriter fout = new OutputStreamWriter(archivo);
            fout.write(editor.getText().toString());
            fout.close();
            Toast.makeText(this, R.string.guardado, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean isModificable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isLegible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean espacioSuficiente(File f) {
        double eTotal, eDisponible, porcentaje;
        eTotal = (double) f.getTotalSpace();
        eDisponible = (double) f.getFreeSpace();
        porcentaje = (eDisponible / eTotal) * 100;
        return porcentaje > 10;
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
