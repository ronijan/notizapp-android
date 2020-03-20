package com.example.memo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Edit_Nottes extends AppCompatActivity {
    
    String notetext;
    File notefile;
    Animation anims, ani;
    EditText editText;
    boolean isDeleted;
    TextView txt_Click, txtani;
    Bitmap bitmap;
    final int SPEECHGINTENT_REQ_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__nottes);

        //// animation from 0 to 1
        txtani = (TextView)findViewById(R.id.textViewMy);
        ani = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        txtani.setAnimation(ani);

        anims = AnimationUtils.loadAnimation(this, R.anim.scale_in_out_anim);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.text_img);
        txt_Click = (TextView) findViewById(R.id.textViewMy);
        txt_Click.getPaint().setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
      
      // Recognizer
       /* txt_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speechRecoqnitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecoqnitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                startActivityForResult(speechRecoqnitionIntent, SPEECHGINTENT_REQ_CODE);
            }
        });*/

        editText = (EditText)findViewById(R.id.editText);

        if (getIntent().hasExtra("EXTRA_NOTE_TEXT") && getIntent().hasExtra("EXTRA_NOTE_FILE") ){
            notetext  = getIntent().getStringExtra("EXTRA_NOTE_TEXT");
            notefile = (File) getIntent().getExtras().get("EXTRA_NOTE_FILE");
            editText.setText(notetext);
        }

        Toolbar mToolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
        menuInflater.inflate(R.menu.edit_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete){
            deleteNote();
        }
        if (item.getItemId() == R.id.menu_edit){
            finish();
            Toast.makeText(Edit_Nottes .this, "Saved.", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menu_clear){
            editText.setText(null);
        }
        if (item.getItemId() == R.id.menu_close){
            finish();
        }
        return true;
    }

    private void deleteNote() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Edit_Nottes.this);
        dialog.setTitle("Delete");
        dialog.setMessage("This memo will be deleted.");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notefile.delete();
                isDeleted  = true;
                Toast.makeText(getApplicationContext(),"1 memo deleted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ///nichts
            }
        });

        dialog.show();
    }

    @Override
    protected void onPause() {

        if (!isDeleted){
            try {
                OutputStream outputStream = new FileOutputStream(notefile);
                outputStream.write(editText.getText().toString().getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onPause();
    }
}
