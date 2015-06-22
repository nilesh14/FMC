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

        String _id,lookup,display_name;
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    Uri contactUri = data.getData();
                    Cursor cursor = getContentResolver().query(contactUri, new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.LOOKUP_KEY}, null, null, null);
                    Log.d(DEBUG_TAG,"Column count = "+cursor.getColumnCount());
                    String [] arrayNames = cursor.getColumnNames();
                    if(arrayNames != null){
                        /*for(int i = 0 ; i < arrayNames.length ; i ++){
                            Log.d(DEBUG_TAG , "Column name at position "+i+" value "+arrayNames[i]);
                        }*/
                        cursor.moveToFirst();
                        String columns[] = cursor.getColumnNames();
                        _id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        lookup = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE},
                                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                                new String[] {String.valueOf(_id)}, null);

                        while (c.moveToNext()){
                            Log.d(DEBUG_TAG,"Number : "+ c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            + " Type "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                                    + " Email Address "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                                    + " Email Address Type "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
                        }

                        Cursor cursor1 = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER ,ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE},
                                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'",
                                new String[] {String.valueOf(_id)}, null);

                        while (cursor1.moveToNext()){
                            Log.d(DEBUG_TAG, "Number : " + cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    + " Type " + cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                                    + " Email Address " + cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                                    + " Email Address Type " + cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
                        }


                        Cursor addressCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER ,ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE},
                                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'",
                                new String[] {String.valueOf(_id)}, null);

                        while (addressCursor.moveToNext()){
                            Log.d(DEBUG_TAG, "Address Formatted : " + addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    + " Type " + addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                                    );
                        }


                        /*for (String column : columns) {
                            int index = cursor.getColumnIndex(column);
                            Log.v(DEBUG_TAG, "Column: " + column + " == ["
                                    + cursor.getString(index) + "]");
                        }*/


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
