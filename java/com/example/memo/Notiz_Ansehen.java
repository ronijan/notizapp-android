package com.example.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Notiz_Ansehen extends AppCompatActivity {

    ListView listView;
    ArrayList<File> dateinListe;
    ArrayList<String> texteliste;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notiz__ansehen);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {

        super.onResume();

        listView = (ListView) findViewById(R.id.listView);

        arryListSetup();
        ArrayAdapter<String> arryAdapter = new ArrayAdapter<String>(Notiz_Ansehen.this,R.layout.row_item,R.id.tv,texteliste);
        listView.setAdapter(arryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editIntent = new Intent(Notiz_Ansehen.this,Edit_Nottes.class);
                editIntent.putExtra("EXTRA_NOTE_TEXT", texteliste.get(i));
                editIntent.putExtra("EXTRA_NOTE_FILE", dateinListe.get(i));
                startActivity(editIntent);
            }
        });
    }

    private void arryListSetup() {
        dateinListe = new ArrayList<>();
        texteliste = new ArrayList<>();
        // Change Folder Name to whatever you like.
        // With dot (.) means to hide Folder in Storage
        File ordner = new File(Environment.getExternalStorageDirectory(), ".Encrypted_data");

        dateinListe.addAll(Arrays.asList(ordner.listFiles()));
        Collections.sort(dateinListe);
        Collections.reverse(dateinListe);

        int dateicounter = 0;
        while (dateicounter < dateinListe.size()) {
            texteliste.add(getTextFromFile(dateinListe.get(dateicounter)));// 0 if not
            dateicounter++;
        }
    }

    public String getTextFromFile  (File datei){

        StringBuilder stringBilder =   new StringBuilder();
        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(datei));
            String aktuelleZeile;
            while ((aktuelleZeile = bufferReader.readLine()) != null){
                stringBilder.append(aktuelleZeile);
                stringBilder.append("\n");
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBilder.toString().trim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notiz_ansehen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_home){
            finish(); // Exit from current Page
        }
        
        if (item.getItemId() == R.id.menu_help){
            try {
                InputStream inputStream = getAssets().open("help.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder msg = new StringBuilder();

                while ((line = bufferReader.readLine()) != null) {
                    msg.append(line + "\n");
                }

                AlertDialog.Builder build = new AlertDialog.Builder(Notiz_Ansehen.this);
                build.setTitle(R.string.help);
                build.setIcon(R.drawable.ic_help_text_black_24dp);
                build.setMessage(Html.fromHtml(msg + ""));
                build.setNegativeButton(R.string.dilog_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Negative
                    }
                }).show();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return true;
    }
}
