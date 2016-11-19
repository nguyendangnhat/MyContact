package com.dangnhat.mycontact.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.R;
import com.dangnhat.mycontact.ViewContactActivity;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dangn on 11/19/2016.
 */

public class ContactAdapter extends BaseAdapter {

    Context context;
    ArrayList<Contact> listContact;
    SparseBooleanArray mSelectedItemIds;

    private boolean checkMode;


    public ContactAdapter(Context context, ArrayList<Contact> listContact){
        this.context = context;
        this.listContact = listContact;
        mSelectedItemIds = new SparseBooleanArray();
        this.checkMode = false;
    }

    public ContactAdapter(Context context, ArrayList<Contact> listContact, boolean checkMode){
        this.context = context;
        this.listContact = listContact;
        mSelectedItemIds = new SparseBooleanArray();
        this.checkMode = checkMode;
        mSelectedItemIds.put(0, true);
    }

    @Override
    public int getCount() {
        return listContact.size();
    }

    @Override
    public Contact getItem(int position) {
        return listContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemContact;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemContact = inflater.inflate(R.layout.item_contact, null);
        }
        else {
            itemContact = convertView;
        }

        final Contact contact = listContact.get(position);

        TextView txtName = (TextView) itemContact.findViewById(R.id.contact_name);
        txtName.setText(contact.getLastName() + " " + contact.getFirstName());
        CircleImageView imgAvatar = (CircleImageView) itemContact.findViewById(R.id.contact_avatar);

        Bitmap bmAvatar = getBitmapFromArrayByte(contact.getAvatar());
        imgAvatar.setImageBitmap(bmAvatar);
        CheckBox checkBox = (CheckBox) itemContact.findViewById(R.id.check_box);
        if(checkMode){
            checkBox.setVisibility(View.VISIBLE);
            if(mSelectedItemIds.get(position)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        } else {
            checkBox.setVisibility(View.GONE);
            itemContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ViewContactActivity.class);
                    i.putExtra("contactId", contact.getId());
                    context.startActivity(i);
                }
            });
        }

        return itemContact;
    }

    public void setCheckMode(boolean checkMode){
        this.checkMode = checkMode;
    }



    public ArrayList<Contact> getContactChecked()
    {
        ArrayList<Contact> list = new ArrayList<>();
        for (int i = 0; i < listContact.size(); i++){
            if (mSelectedItemIds.get(i)){
                list.add(listContact.get(i));
            }
        }
        return list;
    }

    public void selectView(int position, boolean value)
    {
        if(value)
        {
            mSelectedItemIds.put(position, true);
        }
        else
        {
            mSelectedItemIds.delete(position);
        }
        notifyDataSetChanged();
    }



    public void toggleSelection(int position)
    {
        selectView(position, !mSelectedItemIds.get(position));
    }

    public void removeSelection(){
        mSelectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectAll(){
        for (int i = 0 ; i < listContact.size(); i++){
            toggleSelection(i);
        }
        notifyDataSetChanged();
    }

    public void setSelectedItemIds(SparseBooleanArray mSelectedItemIds){
        this.mSelectedItemIds = mSelectedItemIds;
    }
    private Bitmap getBitmapFromArrayByte(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
