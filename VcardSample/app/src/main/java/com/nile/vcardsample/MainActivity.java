package com.nile.vcardsample;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String DEBUG_TAG = "MainActivity";
    static String DATA_TYPE_PHONE = "data_type_phone";
    static String DATA_TYPE_EMAIL = "data_type_email";
    static String DATA_TYPE_ADDRESS = "data_type_address";
    Button btnPickContact,btnCreateVCard;
    String vcardStorageLocation = "";

    static final int REQUEST_CODE = 1;
    ArrayList<Data> arrData = new ArrayList<>();
    TextView txtDisplayNameValue,txtBlankMessage;
    ListView listDetails;
    DataAdapter adapter;
    String _id,lookup,display_name,first_name,last_name;
    byte [] photoData,photoLarge;
    ImageView imgPick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPick = (ImageView) findViewById(R.id.imgPick);
        btnPickContact = (Button) findViewById(R.id.btnPickContact);
        btnCreateVCard = (Button) findViewById(R.id.btnCreateVCard);
        txtDisplayNameValue = (TextView) findViewById(R.id.txtDisplayNameValue);
        listDetails = (ListView) findViewById(R.id.listDetails);
        txtBlankMessage = (TextView) findViewById(R.id.txtBlankMessage);
        adapter = new DataAdapter(this,arrData);
        listDetails.setAdapter(adapter);
        btnPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactPicker();
            }
        });
        btnCreateVCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (adapter.getCount() == 0 || display_name == null) {
                        Toast.makeText(MainActivity.this, "No data to create Vcard. Please pick a contact", Toast.LENGTH_LONG).show();
                    } else {
                        writeVCFFile();
                        Toast.makeText(MainActivity.this, "Vcard created at Location " + vcardStorageLocation, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showContactPicker(){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void setContactImage(){
        Bitmap bmp = null;
        if (photoLarge != null) {
            bmp = BitmapFactory.decodeByteArray(photoLarge, 0, photoLarge.length);
        }else if (photoData != null){
            bmp = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        }

        if (bmp != null) {

            imgPick.setImageBitmap(bmp);
        }else{
            imgPick.setImageResource(R.drawable.contact);
        }
        //imgPick.setImageDrawable(new BitmapDrawable(bmp));
    }

    private void writeVCFFile() throws IOException {
        vcardStorageLocation = Environment.getExternalStorageDirectory()+ File.separator+"vCard_"+display_name+"_"+System.currentTimeMillis()+".vcf";
        File file = new File(vcardStorageLocation);
        FileWriter fw = new FileWriter(file);
        try {
            fw.write("BEGIN:VCARD\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.write("VERSION:2.1\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fw.write("N:" + "BBB" + ";" + "Aa" + "\r\n");
        //fw.write("FN:" + "Aa BBB"+ "\r\n");

        fw.write("N:" + ((TextUtils.isEmpty(last_name)) ? "":last_name) + ";" + ((TextUtils.isEmpty(last_name)) ? "":first_name) + "\r\n");
        fw.write("FN:" + display_name+ "\r\n");
        /*if (!TextUtils.isEmpty(employee.get(0).group)) {
            fw.write("ORG:" + employee.get(0).group + "\r\n");
        }*/

        /*fw.write("PHOTO;VALUE=URL;TYPE=JPEG:" + "http://wwwin.cisco.com/dir/photo/std/" + employee.get(0).userid + ".jpg" + "\r\n");*/
        //fw.write("TITLE:" + designation.getText().toString() + "\r\n");

        for (Data data : arrData) {
            if(data.getDataType().equalsIgnoreCase(MainActivity.DATA_TYPE_PHONE) && data.isChecked){
                fw.write("TEL;TYPE="+DataAdapter.getPhoneType(Integer.parseInt(data.data2))+":" + data.data1 + "\r\n");
            }

            if(data.getDataType().equalsIgnoreCase(MainActivity.DATA_TYPE_ADDRESS) && data.isChecked){
                fw.write("ADR;TYPE="+DataAdapter.getPhoneType(Integer.parseInt(data.data2))+":;;" + data.data1 + "\r\n");
            }
            if(data.getDataType().equalsIgnoreCase(MainActivity.DATA_TYPE_EMAIL) && data.isChecked){
                fw.write("EMAIL;TYPE="+DataAdapter.getEmailType(Integer.parseInt(data.data2))+":" + data.data1 + "\r\n");
            }
        }
        // get photo in base64 and write it in vCard
        if (photoLarge != null) {
            String encodedImage = Base64.encodeToString(photoLarge, Base64.NO_CLOSE);
            String text = encodedImage.replaceAll("\\r|\\n", "");
            fw.write("PHOTO;ENCODING=BASE64;JPEG:"+encodedImage+"\r\n");
            Log.d(DEBUG_TAG,"Base64 Encoded Image Large "+encodedImage+"\r\n");
        }else if(photoData != null){
            String encodedImage = Base64.encodeToString(photoData, Base64.NO_CLOSE);
            String text = encodedImage.replaceAll("\\r|\\n", "");
            fw.write("PHOTO;ENCODING=BASE64;JPEG:"+encodedImage+"\r\n");
            Log.d(DEBUG_TAG,"Base64 Encoded Image Thumbnail "+encodedImage+"\r\n");
        }
        //fw.write("TEL;TYPE=WORK,VOICE:" + workNo.getText().toString() + "\r\n");
        //fw.write("TEL;TYPE=MOBILE,VOICE:" + mobileNo.getText().toString() + "\r\n");
        //fw.write("ADR;TYPE=WORK:;;" + address.getText().toString() + "\r\n");
        /*if (!TextUtils.isEmpty(employee.get(0).emailAddress)) {
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + employee.get(0).emailAddress + "\r\n");
        }*/
        fw.write("END:VCARD\r\n");
        fw.close();
    }

    private void clearGlobalData(){
        display_name = null;
        first_name = null;
        last_name = null;
        _id = null;
        lookup = null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       //clearGlobalData();

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    arrData.clear();
                    if (data != null && data.getData() != null) {
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


                            txtDisplayNameValue.setText(display_name);
                            if(_id != null){
                                //openPhoto(_id);
                                getFirstAndLlastName(_id);
                                addPhoneData(_id);
                                addEmailData(_id);
                                addAddressData(_id);
                               photoData =  openPhoto(Integer.parseInt(_id));
                                photoLarge = openDisplayPhoto(Integer.parseInt(_id));
                                setContactImage();
                                adapter.notifyDataSetChanged();
                                if(adapter.getCount() > 0){
                                    txtBlankMessage.setVisibility(View.GONE);
                                }else{
                                    txtBlankMessage.setVisibility(View.VISIBLE);
                                }
                            }


                        /*Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
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
                        }*/


                        /*for (String column : columns) {
                            int index = cursor.getColumnIndex(column);
                            Log.v(DEBUG_TAG, "Column: " + column + " == ["
                                    + cursor.getString(index) + "]");
                        }*/


                        }
                    }


                    break;
            }
        }
    }

    public byte [] openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd =
                    getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");

            return IOUtils.toByteArray(fd.createInputStream());
        } catch (IOException e) {
            return null;
        }
    }

    public byte [] openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        //String tempMessage = "Hello World";

        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);


                if (data != null) {
                    File file = new File(Environment.getExternalStorageDirectory()+File.separator+"vCardImage.jpg");
                    /*if(!file.exists()){
                        file.getParentFile().mkdir();
                    }*/
                    /*FileOutputStream fOut = new FileOutputStream(file.getAbsolutePath().toString(), true);
                    fOut.write(data);
                    fOut.close();*/
                    return data;
                }
            }
        } finally {
            cursor.close();

        }
        return null;
    }

    private void getFirstAndLlastName(String contact_id){
        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
                new String[] {String.valueOf(contact_id)}, null);

        while (c.moveToNext()){
            first_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            last_name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));

            Log.d(DEBUG_TAG,"Name : "+ c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                    + " Last Name "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)));
        }
    }

    private void addPhoneData(String contact_id){
        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                new String[] {String.valueOf(contact_id)}, null);

        while (c.moveToNext()){
            Data data = new Data();
            data.setDataType(DATA_TYPE_PHONE);
            data.setData1(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            data.setData2(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
            arrData.add(data);
            Log.d(DEBUG_TAG,"Number : "+ c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    + " Type "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                    + " Email Address "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    + " Email Address Type "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
        }
    }

    private void addEmailData(String contact_id){
        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Contacts.Data._ID,ContactsContract.CommonDataKinds.Email.ADDRESS,ContactsContract.CommonDataKinds.Email.TYPE},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'",
                new String[] {String.valueOf(contact_id)}, null);

        while (c.moveToNext()){
            Data data = new Data();
            data.setDataType(DATA_TYPE_EMAIL);
            data.setData1(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
            data.setData2(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
            arrData.add(data);
            Log.d(DEBUG_TAG," Email Address "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    + " Email Address Type "+c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)));
        }
    }

    private void addAddressData(String contact_id){
        Cursor addressCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Contacts.Data._ID, ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS ,ContactsContract.CommonDataKinds.StructuredPostal.TYPE},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Contacts.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'",
                new String[] {String.valueOf(contact_id)}, null);

        while (addressCursor.moveToNext()){
            Data data = new Data();
            data.setDataType(DATA_TYPE_ADDRESS);
            data.setData1(addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
            data.setData2(addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
            arrData.add(data);
            Log.d(DEBUG_TAG," Postal Address "+addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
                    + " Postal Address Type "+addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
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
        if (id == R.id.action_show_contact_picker) {
            showContactPicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
