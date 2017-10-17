package com.example.darshan.notes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
//import android.support.app.LoaderManager;
//import android.support.v4.content.CursorLoader;
//import android.support.content.Loader;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.darshan.notes.provider.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDITOR_REQUEST_CODE = 1001 ;
    private android.widget.CursorAdapter cursorAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enote=(Button)findViewById(R.id.elayout);
        final Intent intent=new Intent(this,Main2Activity.class);
        enote.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        startActivityForResult(intent,EDITOR_REQUEST_CODE);

        }
        });


        cursorAdapter = new notescursouradapter(this,null,0);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                Uri uri=Uri.parse(provider.CONTENT_URI+"/"+id);
                intent.putExtra(provider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent,EDITOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void insertNote(String notetxt) {
        ContentValues values = new ContentValues();
        values.put(databasehelp.NOTE_TEXT, notetxt);
        Uri noteuri = getContentResolver().insert(CONTENT_URI, values);
        Log.d("MainActivity", "inserted note" + noteuri.getLastPathSegment());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==EDITOR_REQUEST_CODE && requestCode==RESULT_OK){
            restartLoader();
            
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.darshan.notes/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.darshan.notes/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
