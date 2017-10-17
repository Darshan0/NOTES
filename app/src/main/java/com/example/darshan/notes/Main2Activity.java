package com.example.darshan.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.security.PublicKey;

import static com.example.darshan.notes.provider.CONTENT_URI;

public class Main2Activity extends AppCompatActivity {
    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editor=(EditText)findViewById(R.id.editText);
        Intent intent= getIntent();
        Uri uri=intent.getParcelableExtra(provider.CONTENT_ITEM_TYPE);
        if (uri== null){
            action=Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        }else{
            action=Intent.ACTION_EDIT;
            noteFilter=databasehelp.NOTE_ID+"="+uri.getLastPathSegment();

            Cursor cursor=getContentResolver().query(uri,databasehelp.ALL_COLUMNS,noteFilter,null,null);
            cursor.moveToFirst();
            oldText=cursor.getString(cursor.getColumnIndex(databasehelp.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }

        return  true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_delete:
                deletenote();
                break;
        }
        return true;

    }

    private void deletenote() {
        getContentResolver().delete(provider.CONTENT_URI,noteFilter,null);
        Toast.makeText(this,"note deleted",Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();

    }

    private  void finishEditing(){
        String newText =editor.getText().toString().trim();
        switch (action){
            case Intent.ACTION_INSERT:
                if(newText.length()==0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    insertnote(newText);

                }
                break;
            case Intent.ACTION_EDIT:
                if(newText.length()==0){
                deletenote();

                }else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }else {
                    updateNote(newText);
                    
                }


                finish();
        }
    }

    private void updateNote(String notetxt) {
        ContentValues values = new ContentValues();
        values.put(databasehelp.NOTE_TEXT, notetxt);
        getContentResolver().update(provider.CONTENT_URI,values,noteFilter,null);
        Toast.makeText(this, R.string.note_updated,Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);


    }

    private void insertnote(String notetxt) {
        ContentValues values = new ContentValues();
        values.put(databasehelp.NOTE_TEXT, notetxt);
        getContentResolver().insert(CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed(){
        finishEditing();
    }


}
