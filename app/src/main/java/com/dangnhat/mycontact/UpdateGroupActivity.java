package com.dangnhat.mycontact;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dangnhat.mycontact.Adapter.ContactAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

public class UpdateGroupActivity extends AppCompatActivity {

    ListView listView;
    EditText editTextGroupName;

    ContactAdapter contactAdapter;
    ArrayList<Contact> listContact;
    Group group;
    ArrayList<Integer> listID;

    boolean edit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list_view);
        listContact = new ArrayList<>();

        group = (Group) getIntent().getSerializableExtra("group");
        listID = new ArrayList<>();
        if(group == null){
            group = new Group();
            edit = false;
            getSupportActionBar().setTitle("Thêm nhóm mới");
        } else {
            getSupportActionBar().setTitle("Sửa nhóm");
            ContactDBHelper myMoneyDbHelper = new ContactDBHelper(this);
            SQLiteDatabase database = myMoneyDbHelper.getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM tbl_contact INNER JOIN tbl_contact_group ON tbl_contact.id = tbl_contact_group.contact_id WHERE tbl_contact_group.group_id = " + group.getId(), null);
            try {
                int idIndex = cursor.getColumnIndex("id");
                int firstNameIndex = cursor.getColumnIndex("first_name");
                int lastNameIndex = cursor.getColumnIndex("last_name");
                int avatarIndex = cursor.getColumnIndex("avatar");
                int favoriteIndex = cursor.getColumnIndex("favorite");
                while (cursor.moveToNext()){
                    int id = cursor.getInt(idIndex);
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    byte[] avatar = cursor.getBlob(avatarIndex);
                    int favorite = cursor.getInt(favoriteIndex);
                    listID.add(id);
                    listContact.add(new Contact(id, firstName, lastName, avatar, favorite));
                }
            }finally {
                cursor.close();
                database.close();
            }
        }

        editTextGroupName = (EditText) findViewById(R.id.edit_text_group_name);
        editTextGroupName.setText(group.getName());

        LinearLayout linearLayoutAddContact = (LinearLayout) findViewById(R.id.linear_layout_add_contact);
        linearLayoutAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChooseContactActivity.class);
                i.putExtra("listID", listID);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            TextInputLayout textInputLayoutGroupName = (TextInputLayout) findViewById(R.id.text_input_layout_group_name);
            String ten = editTextGroupName.getText().toString().trim();
            if(TextUtils.isEmpty(ten)) {
                textInputLayoutGroupName.setError("Không được bỏ trống");
            } else {
                ContactDBHelper myMoneyDbHelper = new ContactDBHelper(this);
                SQLiteDatabase database = myMoneyDbHelper.getWritableDatabase();
                Cursor cursor;
                if(edit)
                {
                    cursor = database.rawQuery("SELECT * FROM tbl_group WHERE name = '" + ten +"' and name != '" + group.getName() + "';", null);
                }
                else
                {
                    cursor = database.rawQuery("SELECT * FROM tbl_group WHERE name = '" + ten +"';", null);
                }
                if(cursor.getCount() == 0) {
                    if(edit) {
                        editGroup(ten, database);
                        editContactGroup(database);
                    }
                    else {
                        insertGroup(ten, database);
                        insertContactGroup(database);
                    }
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                }
                else {
                    textInputLayoutGroupName.setError("Tên nhóm đã tồn tại");
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertGroup(String ten, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("name", ten);

        long newRowId = database.insert("tbl_group", null, values);
        group.setId((int)newRowId);


        if(newRowId == -1)
        {
            Toast.makeText(this, "Xảy ra lỗi khi thêm nhóm mới", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "nhóm mới đã được thêm", Toast.LENGTH_SHORT).show();
        }
    }

    private void editGroup(String ten, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("name", ten);

        long newRowId = database.update("tbl_group", values, "id" + " = ?", new String[]{ String.valueOf(group.getId()) });

        if(newRowId == -1)
        {
            Toast.makeText(this, "Xảy ra lỗi khi sửa nhóm", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "nhóm đã được sửa", Toast.LENGTH_SHORT).show();
        }
    }

    private void editContactGroup(SQLiteDatabase database) {
        database.delete("tbl_contact_group", " group_id = ?", new String[]{ String.valueOf(group.getId()) });
        insertContactGroup(database);
    }

    private void insertContactGroup(SQLiteDatabase database) {
        for (int i = 0; i < listContact.size(); i++){
            ContentValues values = new ContentValues();
            values.put("contact_id", listContact.get(i).getId());
            values.put("group_id", group.getId());
            long newRowId = database.insert("tbl_contact_group", null, values);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        displayContact();
    }

    private void displayContact(){
        contactAdapter = new ContactAdapter(this, listContact);
        listView.setAdapter(contactAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                listContact.clear();
                listContact = (ArrayList<Contact>)data.getSerializableExtra("listContact");
                listID.clear();
                for (int i = 0; i<listContact.size(); i++){
                    listID.add(listContact.get(i).getId());
                }
                displayContact();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}