package com.elainedv.contacts_test;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private RecyclerView rv;
    private Intent intent;
    private ArrayList<Contacts> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }

        rv=(RecyclerView)findViewById(R.id.rv);
        mList=readContacts();
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyAdapter(this,mList);
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                 intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mList.get(position).getNumber()));
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},2);
                    Log.i("tag","permission没通过");
                }else{
                    startActivity(intent);
                    Log.i("tag","permission通过了");
                }
            }
        });
        rv.setAdapter(adapter);
    }

    public ArrayList<Contacts> readContacts() {
        Cursor cursor = null;
        ArrayList<Contacts> list=new ArrayList<Contacts>();
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            Toast.makeText(this,"成功读取通讯录",Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contacts contacts=new Contacts();
                    contacts.setName(displayName);
                    contacts.setNumber(number);
                    list.add(contacts);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied this permission.", Toast.LENGTH_SHORT);
                }
                break;
            case 2:
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(intent);
                }
            default:
        }
    }
}
