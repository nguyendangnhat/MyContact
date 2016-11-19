package com.dangnhat.mycontact.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.Model.ContactWithHeader;
import com.dangnhat.mycontact.R;
import com.dangnhat.mycontact.ViewContactActivity;
import com.dangnhat.mycontact.data.ContactDBHelper;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dangn on 11/19/2016.
 */

public class ContactWithHeaderAdapter extends RecyclerView.Adapter {
    private ArrayList<ContactWithHeader> listContact;
    private Context context;
    private int mHeaderDisplay;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CONTENT = 1;
    private SparseBooleanArray mSelectedItemIds;
    private boolean checkMode;


    public ContactWithHeaderAdapter(Context context, ArrayList<Contact> list) {
        this.listContact = addHeader(list);
        this.context = context;
        this.mHeaderDisplay = 18;
        this.mSelectedItemIds = new SparseBooleanArray();
        this.checkMode = false;
    }

    private ArrayList<ContactWithHeader> addHeader(ArrayList<Contact> list){
        ArrayList<ContactWithHeader> listContact = new ArrayList<>();
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < list.size(); i++) {
            Contact contact = list.get(i);
            String header = contact.getFirstName().substring(0, 1);
            header = header.toUpperCase();
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                Contact a = new Contact();
                a.setFirstName(lastHeader);
                listContact.add(new ContactWithHeader(sectionFirstPosition, a));
            }
            listContact.add(new ContactWithHeader(sectionFirstPosition, contact));
        }
        return listContact;
    }

    private ArrayList<ContactWithHeader> refreshHeader(ArrayList<ContactWithHeader> list){
        ArrayList<ContactWithHeader> listContact = new ArrayList<>();
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getContact().getId() != 0){
                Contact contact = list.get(i).getContact();
                String header = contact.getFirstName().substring(0, 1);
                header = header.toUpperCase();
                if (!TextUtils.equals(lastHeader, header)) {
                    // Insert new header view and update section data.
                    sectionFirstPosition = i + headerCount;
                    lastHeader = header;
                    headerCount += 1;
                    Contact a = new Contact();
                    a.setFirstName(lastHeader);
                    listContact.add(new ContactWithHeader(sectionFirstPosition, a));
                }
                listContact.add(new ContactWithHeader(sectionFirstPosition, contact));
            }
        }
        return listContact;
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public CircleImageView imgAvatar;
        public CheckBox checkBox;
        ContactHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.contact_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.contact_avatar);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public HeaderHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.header);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listContact.get(position).getContact().getId() == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewContact;
        if(viewType == VIEW_TYPE_HEADER) {
            viewContact = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header_contact, parent, false);
            return new HeaderHolder(viewContact);
        }
        else {
            viewContact = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact, parent, false);
            return new ContactHolder(viewContact);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ContactWithHeader contact = listContact.get(position);
        View itemView = holder.itemView;
        if(holder instanceof ContactHolder){
            ((ContactHolder) holder).txtName.setText(contact.contact.getLastName() + " " + contact.getContact().getFirstName());
            Bitmap bmAvatar = getBitmapFromArrayByte(contact.getContact().getAvatar());
            ((ContactHolder) holder).imgAvatar.setImageBitmap(bmAvatar);

            if(checkMode){
                ((ContactHolder) holder).checkBox.setVisibility(View.VISIBLE);
                if(mSelectedItemIds.get(position)) {
                    ((ContactHolder) holder).checkBox.setChecked(true);
                } else {
                    ((ContactHolder) holder).checkBox.setChecked(false);
                }
            } else {
                ((ContactHolder) holder).checkBox.setVisibility(View.GONE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent i = new Intent(context, ViewContactActivity.class);
                    i.putExtra("contactId", contact.getContact().getId());
                    context.startActivity(i);
                    }
                });
            }
        }
        else {
            ((HeaderHolder) holder).txtHeader.setText(contact.getContact().getFirstName());
        }

        itemView.setClickable(true);

        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (contact.getContact().getId() == 0) {
            lp.headerDisplay = mHeaderDisplay;
        }
        lp.setSlm(LinearSLM.ID);
        lp.setColumnWidth(itemView.getHeight());
        lp.setFirstPosition(contact.getSectionFirstPosition());
        itemView.setLayoutParams(lp);
    }

    public void setCheckMode(boolean checkMode) {
        this.checkMode = checkMode;
    }

    @Override
    public int getItemCount() {
        return listContact.size();
    }

    public ContactWithHeader getItem(int position) {
        return listContact.get(position);
    }

    public void remove(ContactWithHeader contact, SQLiteDatabase database) {
        listContact.remove(contact);
        database.delete("tbl_contact", " id = ?", new String[]{ String.valueOf(contact.getContact().getId()) });
        database.delete("tbl_contact_phone", " contact_id = ?", new String[]{ String.valueOf(contact.getContact().getId())});
        database.delete("tbl_contact_email", " contact_id = ?", new String[]{ String.valueOf(contact.getContact().getId())});
        database.delete("tbl_contact_im", " contact_id = ?", new String[]{ String.valueOf(contact.getContact().getId())});
        database.delete("tbl_contact_group", " contact_id = ?", new String[]{ String.valueOf(contact.getContact().getId())});
    }
    public void removes(){
        for (int i = (mSelectedItemIds.size()-1); i>=0 ; i--) {
            if(mSelectedItemIds.valueAt(i)) {
                ContactDBHelper myMoneyDbHelper = new ContactDBHelper(context);
                SQLiteDatabase database = myMoneyDbHelper.getWritableDatabase();
                ContactWithHeader selectedItem = getItem(mSelectedItemIds.keyAt(i));
                remove(selectedItem, database);
                database.close();
            }
        }
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        if(listContact.get(position).getContact().getId() != 0)
            selectView(position, !mSelectedItemIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemIds.put(position, value);
        else
            mSelectedItemIds.delete(position);

        notifyDataSetChanged();
    }


    public int getSelectedCount() {
        return mSelectedItemIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemIds;
    }

    private Bitmap getBitmapFromArrayByte(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
