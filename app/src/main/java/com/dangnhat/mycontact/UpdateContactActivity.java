package com.dangnhat.mycontact;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.Model.PhoneEmailIM;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class UpdateContactActivity extends AppCompatActivity {
    ImageView imageViewAvatar;

    ListView listViewPhone;
    ListView listViewEmail;
    ListView listViewIM;
    TextView textViewGroup;
    ArrayList<Integer> listID;

    ContactDBHelper contactDBHelper;
    boolean edit;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contact = (Contact) getIntent().getSerializableExtra("contact");
        contactDBHelper = new ContactDBHelper(this);
        /**
         * thay doi anh
         */
        FrameLayout frameLayoutAvatar = (FrameLayout) findViewById(R.id.frame_layout_avatar);
        imageViewAvatar = (ImageView) findViewById(R.id.image_view_avatar);
        frameLayoutAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChooseImageFragment dialogChooseImageFragment = new DialogChooseImageFragment();
                dialogChooseImageFragment.show(getFragmentManager(), "aa");
            }
        });

        EditText firstName = (EditText) findViewById(R.id.edit_text_first_name);
        EditText lastName = (EditText) findViewById(R.id.edit_text_last_name);

        listViewPhone = (ListView) findViewById(R.id.list_view_phone);
        listViewEmail = (ListView) findViewById(R.id.list_view_email);
        listViewIM = (ListView) findViewById(R.id.list_view_im);
        textViewGroup = (TextView) findViewById(R.id.text_add_group) ;

        ArrayList<PhoneEmailIM> listPhone = new ArrayList<>();
        ArrayList<PhoneEmailIM> listEmail = new ArrayList<>();
        ArrayList<PhoneEmailIM> listIM = new ArrayList<>();
        ArrayList<Group> listGroup = new ArrayList<>();
        listID = new ArrayList<>();

        if(contact == null){
            contact = new Contact();
            edit = false;
            listPhone.add(new PhoneEmailIM());
            listEmail.add(new PhoneEmailIM());
            listIM.add(new PhoneEmailIM());
            listGroup.add(new Group(0, "Chưa gán"));
            contact.setAvatar(getByteArrayFromImageView(imageViewAvatar));
            getSupportActionBar().setTitle("Thêm liên hệ mới");
        } else {
            edit = true;
            getSupportActionBar().setTitle("Sửa liên hệ");
            imageViewAvatar.setImageBitmap(getBitmapFromArrayByte(contact.getAvatar()));
            firstName.setText(contact.getFirstName());
            lastName.setText(contact.getLastName());

            listPhone = (ArrayList<PhoneEmailIM>) getIntent().getSerializableExtra("listPhone");
            listEmail = (ArrayList<PhoneEmailIM>) getIntent().getSerializableExtra("listEmail");
            listIM = (ArrayList<PhoneEmailIM>) getIntent().getSerializableExtra("listIM");
            listGroup = (ArrayList<Group>) getIntent().getSerializableExtra("listGroup");
            listID = getIntent().getIntegerArrayListExtra("listID");
        }
        PhoneAdapter phoneAdapter = new PhoneAdapter(this, listPhone);
        EmailAdapter emailAdapter = new EmailAdapter(this, listEmail);
        IMAdapter imAdapter = new IMAdapter(this, listIM);
        listViewPhone.setAdapter(phoneAdapter);
        listViewEmail.setAdapter(emailAdapter);
        listViewIM.setAdapter(imAdapter);
        textViewGroup.setText(displayGroup(listGroup));


        /**
         * click nut them so dien thoai, email, mang xa hoi
         */
        LinearLayout linearLayoutAddPhone = (LinearLayout) findViewById(R.id.linear_layout_add_phone);
        LinearLayout linearLayoutAddEmail = (LinearLayout) findViewById(R.id.linear_layout_add_email);
        LinearLayout linearLayoutAddIM = (LinearLayout) findViewById(R.id.linear_layout_add_im);
        LinearLayout linearLayoutAddGroup = (LinearLayout) findViewById(R.id.linear_layout_add_group);
        linearLayoutAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhone();
            }
        });
        linearLayoutAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmail();
            }
        });
        linearLayoutAddIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIM();
            }
        });
        linearLayoutAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChooseGroupActivity.class);
                i.putExtra("listID", listID);
                startActivityForResult(i, 3);
            }
        });
    }


    private String displayGroup(ArrayList<Group> list){
        String s = "";
        for (int i = 0; i< list.size(); i++){

            s += list.get(i).getName() + ", ";
            if(s.length()>35){
                s = s.substring(0, 35) + "....";
                break;
            }
        }
        if(s!="")
            s = s.substring(0, s.length() - 2);
        else s = "Chưa gán";
        return s;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewAvatar.setImageBitmap(imageBitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri imageUri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageViewAvatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            ArrayList<Group> listGroup  = (ArrayList<Group>)data.getSerializableExtra("listGroup");
            listID.clear();
            for (int i = 0; i<listGroup.size(); i++){
                listID.add(listGroup.get(i).getId());
            }

            textViewGroup.setText(displayGroup(listGroup));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            EditText editTextFirstName = (EditText) findViewById(R.id.edit_text_first_name);
            EditText editTextLastName = (EditText) findViewById(R.id.edit_text_last_name);
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            if(TextUtils.isEmpty(firstName)) {
                editTextFirstName.setError("Không được bỏ trống");
            } else {
                if(edit) {
                    editContact(firstName, lastName);
                    editPhone();
                    editEmail();
                    editIM();
                    editContactGroup();
                }
                else {
                    insertContact(firstName, lastName);
                    insertPhone();
                    insertEmail();
                    insertIM();
                    editContactGroup();
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Ham them 1 danh ba moi vao co so du lieu
     * va ham update danh ba
     */
    private void insertContact(String firstName, String lastName) {
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("avatar", getByteArrayFromImageView(imageViewAvatar));
        values.put("favorite", 0);

        long newRowId = database.insert("tbl_contact", null, values);
        contact.setId((int)newRowId);

        if(newRowId == -1)
        {
            Toast.makeText(this, "Xảy ra lỗi khi thêm liên hệ mới", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Liên hệ đã được thêm", Toast.LENGTH_SHORT).show();
        }
        database.close();
    }
    private void editContact(String firstName, String lastName) {
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("avatar", getByteArrayFromImageView(imageViewAvatar));
        values.put("favorite", contact.getFavorite());

        long newRowId = database.update("tbl_contact", values, "id" + " = ?", new String[]{ String.valueOf(contact.getId()) });

        if(newRowId == -1)
        {
            Toast.makeText(this, "Xảy ra lỗi khi sửa liên hệ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Liên hệ đã được sửa", Toast.LENGTH_SHORT).show();
        }
        database.close();
    }

    /**
     * Ham insert va update so dien thoai vao co so du lieu
     */
    private void insertPhone(){
        int count = listViewPhone.getCount();
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewPhone.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);
            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString();
            if(!TextUtils.isEmpty(value)) {
                ContentValues values = new ContentValues();
                values.put("contact_id", contact.getId());
                values.put("name", name);
                values.put("phone_number", value);

                database.insert("tbl_contact_phone", null, values);
            }
        }
        database.close();
    }
    private void editPhone(){
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();

        database.delete("tbl_contact_phone", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
        insertPhone();

        database.close();
    }

    /**
     * Ham insert va update email
     */
    private void insertEmail(){
        int count = listViewEmail.getCount();
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewEmail.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);

            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString().trim();
            if(!TextUtils.isEmpty(value)){
                ContentValues values = new ContentValues();
                values.put("contact_id", contact.getId());
                values.put("name", name);
                values.put("email", value);

                database.insert("tbl_contact_email", null, values);
            }
        }
        database.close();
    }
    private void editEmail(){
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();

        database.delete("tbl_contact_email", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
        insertEmail();

        database.close();
    }

    /**
     * ham insert va update mang xa hoi
     */
    private void insertIM(){
        int count = listViewIM.getCount();
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewIM.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);
            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString().trim();
            if(!TextUtils.isEmpty(value)){
                ContentValues values = new ContentValues();
                values.put("contact_id", contact.getId());
                values.put("name", name);
                values.put("account", value);
                database.insert("tbl_contact_im", null, values);
            }
        }
        database.close();
    }
    private void editIM(){
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        database.delete("tbl_contact_im", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
        insertIM();
        database.close();
    }

    private void insertContactGroup(){
        int count = listID.size();
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        for (int i = 0; i < count; i++){
            ContentValues values = new ContentValues();
            values.put("contact_id", contact.getId());
            values.put("group_id", listID.get(i));
            long newRowId = database.insert("tbl_contact_group", null, values);
        }
        database.close();

    }
    private void editContactGroup(){
        SQLiteDatabase database = contactDBHelper.getWritableDatabase();
        database.delete("tbl_contact_group", " contact_id = ?", new String[]{ String.valueOf(contact.getId()) });
        insertContactGroup();
        database.close();
    }


    /**
     * them 1 item moi vao list(isert 1 item vao giao dien)
     */
    private void addPhone(){
        int count = listViewPhone.getCount();
        ArrayList<PhoneEmailIM> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewPhone.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);
            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString().trim();
            list.add(new PhoneEmailIM(0,0,name, value));
        }
        list.add(new PhoneEmailIM());
        PhoneAdapter phoneAdapter = new PhoneAdapter(this, list);
        listViewPhone.setAdapter(phoneAdapter);
    }
    private void addEmail(){
        int count = listViewEmail.getCount();
        ArrayList<PhoneEmailIM> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewEmail.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);
            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString().trim();
            list.add(new PhoneEmailIM(0,0,name, value));
        }
        list.add(new PhoneEmailIM());
        EmailAdapter emailAdapter = new EmailAdapter(this, list);
        listViewEmail.setAdapter(emailAdapter);
    }
    private void addIM(){
        int count = listViewIM.getCount();
        ArrayList<PhoneEmailIM> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LinearLayout itemLayout = (LinearLayout)listViewIM.getChildAt(i); // Find by under LinearLayout
            Spinner spinner_name = (Spinner) itemLayout.findViewById(R.id.spinner_name);
            EditText editTextValue = (EditText)itemLayout.findViewById(R.id.edit_text_value);
            int name = (int)spinner_name.getSelectedItemId();
            String value = editTextValue.getText().toString().trim();
            list.add(new PhoneEmailIM(0,0,name, value));
        }
        list.add(new PhoneEmailIM());
        IMAdapter imAdapter = new IMAdapter(this, list);
        listViewIM.setAdapter(imAdapter);
    }


    /**
     * xu ly anh truoc khi luu vao csdl
     */
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

