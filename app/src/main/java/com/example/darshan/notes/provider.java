package com.example.darshan.notes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.darshan.notes.databasehelp.TABLE_NOTES;


public class provider extends ContentProvider{
    private static final String AUTHORITY = "com.example.notes.provider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static final UriMatcher uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE="Note";


    static {

        uriMatcher.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH +"/#",NOTES_ID);
    }
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        databasehelp helper = new databasehelp(getContext());
        database = helper.getWritableDatabase();
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)== NOTES_ID){
            selection=databasehelp.NOTE_ID+"="+uri.getLastPathSegment();

        }
        return database.query(databasehelp.TABLE_NOTES,databasehelp.ALL_COLUMNS,selection,null,null,null,databasehelp.NOTE_CREATED+" DESC");

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id=database.insert(databasehelp.TABLE_NOTES,null,values);

        return Uri.parse(BASE_PATH + "/" +id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(databasehelp.TABLE_NOTES,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(databasehelp.TABLE_NOTES,values,selection,selectionArgs);
    }
}
