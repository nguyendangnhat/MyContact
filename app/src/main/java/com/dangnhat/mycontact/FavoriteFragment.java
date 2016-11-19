package com.dangnhat.mycontact;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dangnhat.mycontact.Adapter.ContactAdapter;
import com.dangnhat.mycontact.Model.Contact;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

/**
 * Created by dangn on 11/19/2016.
 */

public class FavoriteFragment extends Fragment {

    ContactDBHelper contactDBHelper;
    private ListView listViewContact;
    private TextView textViewNoFavotite;

    public FavoriteFragment() {
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
        View viewFavorite = inflater.inflate(R.layout.fragment_favorite, container, false);
        listViewContact = (ListView) viewFavorite.findViewById(R.id.list_view_favorite);
        textViewNoFavotite = (TextView) viewFavorite.findViewById(R.id.text_view_no_favorite);
        contactDBHelper = new ContactDBHelper(getContext());
        return viewFavorite;
    }

    @Override
    public void onStart() {
        displayFavorite();
        super.onStart();
    }

    private void displayFavorite(){
        final ArrayList<Contact> listContact = new ArrayList<>();
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();
        String SQL_QUERY = "SELECT * FROM tbl_contact WHERE favorite != 0 ORDER BY first_name;";
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
            textViewNoFavotite.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoFavotite.setVisibility(View.GONE);
            final ContactAdapter adapter = new ContactAdapter(getContext(), listContact);

            listViewContact.setAdapter(adapter);

        }

    }
}
