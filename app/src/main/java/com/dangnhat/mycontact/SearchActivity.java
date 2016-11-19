package com.dangnhat.mycontact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.dangnhat.mycontact.Adapter.ContactWithHeaderAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.ContactWithHeader;
import com.dangnhat.mycontact.data.ContactDBHelper;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText editTextKey;
    ContactWithHeaderAdapter adapter;
    RecyclerView recyclerView;
    TextView textViewNoContact;
    ArrayList<Contact> listContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_contact);
        editTextKey = (EditText) findViewById(R.id.edit_text_search);
        textViewNoContact = (TextView) findViewById(R.id.text_view_no_contact);
        listContact = new ArrayList<>();

        resultSearch("");
        editTextKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resultSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ImageView imageViewClose = (ImageView) findViewById(R.id.image_view_clear);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void resultSearch(String key){
        listContact.clear();
        ContactDBHelper contactDBHelper = new ContactDBHelper(this);
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_contact WHERE first_name LIKE '%" + key + "%' OR  last_name LIKE '%" +key+"%'", null);

        try {

            int idIndex = cursor.getColumnIndex("id");
            int firstNameIndex = cursor.getColumnIndex("first_name");
            int lastNameIndex = cursor.getColumnIndex("last_name");
            int avatarIndex = cursor.getColumnIndex("avatar");
            int favoriteIndex = cursor.getColumnIndex("favorite");

            while (cursor.moveToNext())
            {
                int id = cursor.getInt(idIndex);
                String firstName = cursor.getString(firstNameIndex);
                String lastName = cursor.getString(lastNameIndex);
                byte[] avatar = cursor.getBlob(avatarIndex);
                int favorite = cursor.getInt(favoriteIndex);
                Contact contact = new Contact(id, firstName, lastName, avatar, favorite);
                listContact.add(contact);
            }
        }
        finally {
            cursor.close();
            database.close();
        }

        if(listContact.size() == 0){
            textViewNoContact.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoContact.setVisibility(View.GONE);

        }

        adapter = new ContactWithHeaderAdapter(this, listContact);
        recyclerView.setLayoutManager(new LayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

}
