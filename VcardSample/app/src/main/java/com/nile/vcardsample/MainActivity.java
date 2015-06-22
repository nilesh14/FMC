package com.nile.vcardsample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private static final String DEBUG_TAG = "MainActivity";
    Button btnPickContact;
    static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPickContact = (Button) findViewById(R.id.btnPickContact);

        btnPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    Uri contactUri = data.getData();
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                    Log.d(DEBUG_TAG,"Column count = "+cursor.getColumnCount());
                    String [] arrayNames = cursor.getColumnNames();
                    if(arrayNames != null){
                        /*for(int i = 0 ; i < arrayNames.length ; i ++){
                            Log.d(DEBUG_TAG , "Column name at position "+i+" value "+arrayNames[i]);
                        }*/
                        cursor.moveToFirst();
                        String columns[] = cursor.getColumnNames();
                        for (String column : columns) {
                            int index = cursor.getColumnIndex(column);
                            Log.v(DEBUG_TAG, "Column: " + column + " == ["
                                    + cursor.getString(index) + "]");
                        }
                    }
                    while (cursor.moveToNext()){

                    }
                    break;
            }
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
