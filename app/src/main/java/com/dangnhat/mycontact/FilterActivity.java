package com.dangnhat.mycontact;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.data.ContactDBHelper;

public class FilterActivity extends AppCompatActivity {

    CheckBox phone;
    CheckBox email;
    CheckBox im;
    Spinner spinnerGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        phone = (CheckBox) findViewById(R.id.check_box_phone);
        email = (CheckBox) findViewById(R.id.check_box_email);
        im = (CheckBox) findViewById(R.id.check_box_im);
        spinnerGroup =(Spinner) findViewById(R.id.spinner_group);
        phone.setChecked(ContactFragment.phoneCheck);
        phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContactFragment.phoneCheck = b;
            }
        });
        email.setChecked(ContactFragment.emailCheck);
        email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContactFragment.emailCheck = b;
            }
        });
        im.setChecked(ContactFragment.imCheck);
        im.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ContactFragment.imCheck = b;
            }
        });


        ContactDBHelper contactDBHelper = new ContactDBHelper(this);
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT name FROM tbl_group", null);
        String[] groupArray = new String[cursor.getCount()+1];
        groupArray[0] = "Tất cả các nhóm";
        try{
            for (int i = 0; i < cursor.getCount();i++){
                cursor.moveToPosition(i);
                groupArray[i + 1] = cursor.getString(0);
            }

        }finally {
            cursor.close();
            database.close();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groupArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(spinnerArrayAdapter);
        spinnerGroup.setSelection(ContactFragment.groupID);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ContactFragment.groupID = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
