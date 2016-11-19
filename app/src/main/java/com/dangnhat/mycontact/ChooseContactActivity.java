package com.dangnhat.mycontact;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dangnhat.mycontact.Adapter.ContactAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

public class ChooseContactActivity extends AppCompatActivity {

    ContactAdapter contactAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Contact> listContact = new ArrayList<>();
        ContactDBHelper contactDBHelper = new ContactDBHelper(this);
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();

        SparseBooleanArray mSelectedItemIds = new SparseBooleanArray();
        ArrayList<Integer> listId;

        listId = getIntent().getIntegerArrayListExtra("listID");
        if(listId == null){
            listId = new ArrayList<>();
        } else {
            Cursor cursor = database.rawQuery("SELECT * FROM tbl_contact", null);
            try{
                int idIndex = cursor.getColumnIndex("id");
                int firstNameIndex = cursor.getColumnIndex("first_name");
                int lastNameIndex = cursor.getColumnIndex("last_name");
                int avatarIndex = cursor.getColumnIndex("avatar");
                int favoriteIndex = cursor.getColumnIndex("favorite");

                int id;
                String firstName;
                String lastName;
                byte[] avatar;
                int favorite;
                for (int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    id = cursor.getInt(idIndex);
                    if(listId.contains(id)){
                        mSelectedItemIds.put(i, true);
                    }
                    firstName = cursor.getString(firstNameIndex);
                    lastName = cursor.getString(lastNameIndex);
                    avatar = cursor.getBlob(avatarIndex);
                    favorite = cursor.getInt(favoriteIndex);
                    listContact.add(new Contact(id, firstName, lastName, avatar, favorite));
                }
            } finally {
                cursor.close();
                database.close();
            }

        }



        listView = (ListView) findViewById(R.id.list_view);
        contactAdapter = new ContactAdapter(this, listContact, true);
        contactAdapter.setSelectedItemIds(mSelectedItemIds);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contactAdapter.toggleSelection(position);
                contactAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            Intent i = new Intent();
            i.putExtra("listContact", contactAdapter.getContactChecked());
            setResult(RESULT_OK, i);
            Toast.makeText(getApplicationContext(), "jyhjn", Toast.LENGTH_SHORT).show();
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
