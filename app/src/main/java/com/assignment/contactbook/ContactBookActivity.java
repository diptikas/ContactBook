package com.assignment.contactbook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactBookActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ArrayList<Contact> listContacts;
    private ListView lvContacts;


    public static void startContactBookActivity(@NonNull final Activity activity) {
        Intent signInIntent = new Intent(activity, ContactBookActivity.class);
        activity.startActivity(signInIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_book);
        initView();

    }

    private void initView() {
        lvContacts = findViewById(R.id.contacts_lv);
        showContacts();

    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            listContacts = new ContactPresenter(this).fetchAll();
            ContactsAdapter adapterContacts = new ContactsAdapter(this, listContacts);
            lvContacts.setAdapter(adapterContacts);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied_txt), Toast.LENGTH_SHORT).show();
            }
        }
    }
}


