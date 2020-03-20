package com.example.memo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText editText;
    File ordner;
    TextView textViewId,textViewClick;
    Bitmap bitmap;
    final int SPEECHGINTENT_REQ_CODE = 11;
    Animation anims,ani, click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //// animation from 0 to 1
        textViewId = (TextView)findViewById(R.id.textViewMy);
        ani = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        textViewId.setAnimation(ani);////

        anims = AnimationUtils.loadAnimation(this, R.anim.scale_in_out_anim);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.text_img);

        click = AnimationUtils.loadAnimation(this, R.anim.click);

        textViewClick = (TextView) findViewById(R.id.textViewMy);
        textViewClick.getPaint().setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
      

        // Click on Text will activate speech Recoqnition
       /* textViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speechRecoqnitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecoqnitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                startActivityForResult(speechRecoqnitionIntent, SPEECHGINTENT_REQ_CODE);
            }
        });*/

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ordner = new File(Environment.getExternalStorageDirectory(), ".Encrypted_data");
        if (!ordner.exists()) {
            ordner.mkdirs();
        }

        editText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.btn_save);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.startAnimation(click);

                if (editText.getText().length() > 0) {
                    File notizdatei = new File(ordner, "Null_" + System.currentTimeMillis() + ".txt");
                    try {
                        OutputStream outputStream = new FileOutputStream(notizdatei);
                        outputStream.write(editText.getText().toString().getBytes());
                        outputStream.close();
                        editText.setText(null);
                        Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Write something!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String finaltext;
        if (requestCode == SPEECHGINTENT_REQ_CODE && resultCode == RESULT_OK){
            ArrayList<String> speechResults =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (editText.getText().length() >0) {
                finaltext = editText.getText().toString() + " " + speechResults.get(0);
            }else{
                finaltext = speechResults.get(0);
            }
            editText.setText(finaltext);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_memo){
            if (ordner.listFiles().length > 0) {
                startActivity(new Intent(MainActivity.this, Notiz_Ansehen.class));
            }else {
                Toast.makeText(MainActivity.this, "No Notes right now.", Toast.LENGTH_SHORT).show();
            }
        }

        if (item.getItemId() == R.id.btn_save){

            if (editText.getText().length() > 0) {
                // Change Null_ to whtever 
                // Filename currentTimeMillis
                File notizdatei = new File(ordner, "Null_" + System.currentTimeMillis() + ".txt");
                try {
                    OutputStream outputStream = new FileOutputStream(notizdatei);
                    outputStream.write(editText.getText().toString().getBytes());
                    outputStream.close();
                    editText.setText(null);
                    Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Write something!", Toast.LENGTH_SHORT).show();
            }
        }
        if (item.getItemId() == R.id.btn_clear){
            editText.setText(null);
            Toast.makeText(getApplicationContext(), "Cleared.", Toast.LENGTH_SHORT).show();
        }

        /*if (item.getItemId() == R.id.menu_time){
            String d = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
            editText.setText(" "+ d);
            Toast.makeText(getApplicationContext(), "Added.", Toast.LENGTH_SHORT).show();
        }*/

        if (item.getItemId() == R.id.exit){
            AlertDialog.Builder builder = new AlertDialog.Builder(
            MainActivity.this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to quit ?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    });
            builder.show();
            return true;
        }

        return true;
    }
}
