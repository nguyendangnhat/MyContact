package com.dangnhat.mycontact;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.dangnhat.mycontact.Adapter.EmailViewAdapter;
import com.dangnhat.mycontact.Adapter.GroupAdapter;
import com.dangnhat.mycontact.Adapter.IMViewAdapter;
import com.dangnhat.mycontact.Adapter.PhoneViewAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.Model.PhoneEmailIM;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ViewContactActivity extends AppCompatActivity {

    Contact contact;
    ImageView imageViewAvatar;
    ListView listViewPhone;
    ListView listViewEmail;
    ListView listViewIM;
    ListView listViewGroup;

    ArrayList<PhoneEmailIM> listPhone;
    ArrayList<PhoneEmailIM> listEmail;
    ArrayList<PhoneEmailIM> listIM;
    ArrayList<Group> listGroup;
    ArrayList<Integer> listID;
    TextView textViewNoPhone;
    TextView textViewNoEmail;
    CardView cardViewIM ;
    CardView cardViewGroup;
    int contactId;

    ContactDBHelper contactDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactId = getIntent().getIntExtra("contactId", 0);
        contactDBHelper = new ContactDBHelper(this);

        imageViewAvatar = (ImageView) findViewById(R.id.image_view_avatar);
        listViewPhone = (ListView) findViewById(R.id.list_view_phone);
        listViewEmail = (ListView) findViewById(R.id.list_view_email);
        listViewIM = (ListView) findViewById(R.id.list_view_im);
        listViewGroup = (ListView) findViewById(R.id.list_view_group);
         textViewNoPhone = (TextView) findViewById(R.id.text_view_no_phone);
         textViewNoEmail = (TextView) findViewById(R.id.text_view_no_email);
         cardViewIM = (CardView) findViewById(R.id.card_view_im);
         cardViewGroup = (CardView) findViewById(R.id.card_view_group);


    }

    @Override
    protected void onStart() {
        listPhone = new ArrayList<>();
        listEmail = new ArrayList<>();
        listIM = new ArrayList<>();
        listGroup = new ArrayList<>();
        listID = new ArrayList<>();





        SQLiteDatabase database = contactDBHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_contact WHERE id = " + contactId, null);
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
                contact = new Contact(id, firstName, lastName, avatar, favorite);
            }
        }finally {
            cursor.close();
        }
        Cursor cursorPhone = database.rawQuery("SELECT * FROM tbl_contact_phone WHERE contact_id = " + contactId, null);
        try {
            int idIndex = cursorPhone.getColumnIndex("id");
            int contactIdIndex = cursorPhone.getColumnIndex("contact_id");
            int nameIndex = cursorPhone.getColumnIndex("name");
            int phoneNumberIndex = cursorPhone.getColumnIndex("phone_number");
            while (cursorPhone.moveToNext()){
                int id = cursorPhone.getInt(idIndex);
                int contactId = cursorPhone.getInt(contactIdIndex);
                int name = cursorPhone.getInt(nameIndex);
                String phoneNumber = cursorPhone.getString(phoneNumberIndex);
                listPhone.add(new PhoneEmailIM(id, contactId, name, phoneNumber));
            }
        }finally {
            cursorPhone.close();
        }
        Cursor cursorEmail = database.rawQuery("SELECT * FROM tbl_contact_email WHERE contact_id = " + contactId, null);
        try {
            int idIndex = cursorEmail.getColumnIndex("id");
            int contactIdIndex = cursorEmail.getColumnIndex("contact_id");
            int nameIndex = cursorEmail.getColumnIndex("name");
            int emailIndex = cursorEmail.getColumnIndex("email");
            while (cursorEmail.moveToNext()) {
                int id = cursorEmail.getInt(idIndex);
                int contactId = cursorEmail.getInt(contactIdIndex);
                int name = cursorEmail.getInt(nameIndex);
                String email = cursorEmail.getString(emailIndex);
                listEmail.add(new PhoneEmailIM(id, contactId, name, email));
            }
        }finally {
            cursorEmail.close();
        }
        Cursor cursorIM = database.rawQuery("SELECT * FROM tbl_contact_im WHERE contact_id = " + contactId, null);
        try {
            int idIndex = cursorIM.getColumnIndex("id");
            int contactIdIndex = cursorIM.getColumnIndex("contact_id");
            int nameIndex = cursorIM.getColumnIndex("name");
            int accountIndex = cursorIM.getColumnIndex("account");
            while (cursorIM.moveToNext()) {
                int id = cursorIM.getInt(idIndex);
                int contactId = cursorIM.getInt(contactIdIndex);
                int name = cursorIM.getInt(nameIndex);
                String account = cursorIM.getString(accountIndex);
                listIM.add(new PhoneEmailIM(id, contactId, name, account));
            }
        }finally {
            cursorIM.close();
        }
        Cursor cursorGroup = database.rawQuery("SELECT * FROM tbl_group INNER JOIN tbl_contact_group ON tbl_group.id = tbl_contact_group.group_id WHERE tbl_contact_group.contact_id = " + contactId, null);
        try {
            int nameIndex = cursorGroup.getColumnIndex("name");
            while (cursorGroup.moveToNext()){
                int id = cursorGroup.getInt(0);
                String name = cursorGroup.getString(nameIndex);
                listID.add(id);
                listGroup.add(new Group(id, name));
            }
        }finally {
            cursorGroup.close();
        }
        database.close();


        getSupportActionBar().setTitle(contact.getLastName() + " " + contact.getFirstName());

        imageViewAvatar.setImageBitmap(getBitmapFromArrayByte(contact.getAvatar()));
        if(listPhone.size() != 0) {
            textViewNoPhone.setVisibility(View.GONE);
            PhoneViewAdapter phoneViewAdapter = new PhoneViewAdapter(this, listPhone);
            listViewPhone.setAdapter(phoneViewAdapter);
        }
        if(listEmail.size() != 0) {
            textViewNoEmail.setVisibility(View.GONE);
            EmailViewAdapter emailViewAdapter = new EmailViewAdapter(this, listEmail);
            listViewEmail.setAdapter(emailViewAdapter);
        }
        if(listIM.size() == 0) {
            cardViewIM.setVisibility(View.GONE);
        } else {
            IMViewAdapter imViewAdapter = new IMViewAdapter(this, listIM);
            listViewIM.setAdapter(imViewAdapter);
        }
        if(listGroup.size() == 0) {
            cardViewGroup.setVisibility(View.GONE);
        } else {
            GroupAdapter groupAdapter = new GroupAdapter(this, listGroup);
            listViewGroup.setAdapter(groupAdapter);
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_contact, menu);
        if(contact.getFavorite() != 0){
            MenuItem menuItem = menu.findItem(R.id.action_favorite);
            menuItem.setIcon(R.drawable.ic_star);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if(contact.getFavorite() == 0){
                item.setIcon(R.drawable.ic_star);
                contact.setFavorite(1);
            } else {
                item.setIcon(R.drawable.ic_star_border);
                contact.setFavorite(0);
            }

            /**
             * update vao co so du lieu
             */
            SQLiteDatabase database = contactDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("favorite", contact.getFavorite());
            database.update("tbl_contact", values, "id" + " = ?", new String[]{ String.valueOf(contact.getId()) });
            database.close();
            return true;
        }
        if (id == R.id.action_edit){
            Intent i = new Intent(this, UpdateContactActivity.class);
           i.putExtra("contact", contact);
            i.putExtra("listPhone", listPhone);
            i.putExtra("listEmail", listEmail);
            i.putExtra("listIM", listIM);
            i.putExtra("listGroup", listGroup);
            i.putExtra("listID", listID);
            startActivity(i);

            return true;
        }
        if (id == R.id.action_delete){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Xóa");
            alert.setMessage("Bạn có chắc chắn muốn xóa không?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SQLiteDatabase database = contactDBHelper.getWritableDatabase();
                    database.delete("tbl_contact", " id = ?", new String[]{ String.valueOf(contact.getId()) });
                    database.delete("tbl_contact_phone", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
                    database.delete("tbl_contact_email", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
                    database.delete("tbl_contact_im", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
                    database.delete("tbl_contact_group", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
                    database.close();

                    finish();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private byte[] getByteArrayFromImageView(ImageView image){
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bm = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private Bitmap getBitmapFromArrayByte(byte[] image){
        Bitmap bmAvatar = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bmAvatar;
    }
}
