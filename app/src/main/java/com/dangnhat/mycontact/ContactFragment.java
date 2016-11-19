package com.dangnhat.mycontact;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dangnhat.mycontact.Adapter.ContactWithHeaderAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.cab.RecyclerClick_Listener;
import com.dangnhat.mycontact.cab.RecyclerTouchListener;
import com.dangnhat.mycontact.data.ContactDBHelper;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

/**
 * Created by dangn on 11/19/2016.
 */

public class ContactFragment extends Fragment {

    ContactDBHelper contactDBHelper;

    RecyclerView recyclerViewContact;
    TextView textViewNoContact;
    ContactWithHeaderAdapter adapter;
    private android.support.v7.view.ActionMode mActionMode;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewCOntact = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerViewContact =(RecyclerView) viewCOntact.findViewById(R.id.recycler_view_contact);
        textViewNoContact = (TextView) viewCOntact.findViewById(R.id.text_view_no_contact);
        contactDBHelper = new ContactDBHelper(getContext());
        return viewCOntact;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayContact();
    }

    private void displayContact()
    {
        ArrayList<Contact> listContact = new ArrayList<>();
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();

        String SQL_PHONE = " ";
        String SQL_EMAIL = " " ;
        String SQL_IM = " ";
        String SQL_GROUP =  " ";
        String SQL_ON = " ";
        String SQL_ON_PHONE = " ";
        String SQL_ON_EMAIL = " ";
        String SQL_ON_IM = " ";
        String SQL_ON_GROUP = " ";

//            if(phoneCheck){
//                SQL_PHONE = "INNER JOIN tbl_contact_phone ";
//                SQL_ON_PHONE = "= tbl_contact_phone.contact_id ";
//            }
//            if(emailCheck){
//                SQL_EMAIL = "INNER JOIN tbl_contact_email " ;
//                SQL_ON_EMAIL = "= tbl_contact_email.contact_id ";
//            }
//            if(imCheck){
//                SQL_IM = "INNER JOIN tbl_contact_im ";
//                SQL_ON_IM = "= tbl_contact_im.contact_id ";
//            }
//            if(groupID != 0){
//                SQL_GROUP =  "INNER JOIN tbl_contact_group ";
//                SQL_ON_GROUP = "= tbl_contact_group.contact_id ";
//            }
//            if(phoneCheck || emailCheck || imCheck || groupID != 0){
//                SQL_ON =  "ON tbl_contact.id ";
//            }

        String SQL_QUERY = "SELECT * FROM tbl_contact " +
                SQL_PHONE +
                SQL_EMAIL +
                SQL_IM+
                SQL_GROUP +
                SQL_ON +
                SQL_ON_PHONE +
                SQL_ON_EMAIL +
                SQL_ON_IM +
                SQL_ON_GROUP + "ORDER BY first_name;";

        Cursor cursor = database.rawQuery(SQL_QUERY, null);
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
            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_contact);
            adapter = new ContactWithHeaderAdapter(getContext(), listContact);
            recyclerView.setLayoutManager(new LayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
                @Override
                public void onClick(View view, int position) {
                    //If ActionMode not null select item
                    if (mActionMode != null)
                        onListItemSelect(position);
                }

                @Override
                public void onLongClick(View view, int position) {
                    //Select item on long click
                    onListItemSelect(position);
                }
            }));
        }
    }

    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);
        boolean hasCheckedItems = adapter.getSelectedCount() > 0;
        if (hasCheckedItems && mActionMode == null) {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), adapter));
            ((MainActivity) getActivity()).getSupportActionBar().hide();
            ((MainActivity) getActivity()).hideFloatingActionButton();
        }else if (!hasCheckedItems && mActionMode != null)
            mActionMode.finish();

        if (mActionMode != null)
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " được chọn");
    }

    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
        try {
            ((MainActivity) getActivity()).getSupportActionBar().show();
        } catch (Exception e){
            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
        }
        ((MainActivity) getActivity()).showFloatingActionButton();
    }

    public void deleteRows() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        int count = adapter.getSelectedCount();
        alert.setTitle(count +" liên hệ được chọn.");
        alert.setMessage("Bạn có chắc chắn muốn xóa không?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.removes();
                mActionMode.finish();
                displayContact();

            }

        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActionMode.finish();
            }
        });
        alert.show();
    }
}
