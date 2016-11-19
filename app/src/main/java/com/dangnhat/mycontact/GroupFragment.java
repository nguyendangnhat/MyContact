package com.dangnhat.mycontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dangnhat.mycontact.Adapter.GroupAdapter;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

/**
 * Created by dangn on 11/19/2016.
 */

public class GroupFragment extends Fragment {
    ListView listViewGroup;
    TextView textViewNoGroup;
    ContactDBHelper contactDBHelper;
    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewGroup = inflater.inflate(R.layout.fragment_group, container, false);
        listViewGroup = (ListView) viewGroup.findViewById(R.id.list_view_group);
        textViewNoGroup = (TextView) viewGroup.findViewById(R.id.text_view_no_group);
        contactDBHelper = new ContactDBHelper(getContext());
        return viewGroup;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayGroup();
    }

    private void displayGroup()
    {
        ArrayList<Group> listGroup = new ArrayList<>();
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();

        String SQL_QUERY = "SELECT * FROM tbl_group ORDER BY name;";

        Cursor cursor = database.rawQuery(SQL_QUERY, null);
        try {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                Group group = new Group(id, name);
                listGroup.add(group);
            }
        }
        finally {
            cursor.close();
            database.close();
        }

        if(listGroup.size() == 0){
            textViewNoGroup.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoGroup.setVisibility(View.GONE);
            final GroupAdapter adapter = new GroupAdapter(getContext(), listGroup);

            listViewGroup.setAdapter(adapter);
            listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getContext(), UpdateGroupActivity.class);
                    i.putExtra("group", adapter.getItem(position));
                    getActivity().startActivityForResult(i, 1);
                }
            });

            listViewGroup.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listViewGroup.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    adapter.toggleSelection(position);
                    mode.setTitle(listViewGroup.getCheckedItemCount() + " nhóm được chọn");
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    adapter.setCheckMode(true);
                    ((MainActivity) getActivity()).hideFloatingActionButton();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                    mode.getMenuInflater().inflate(R.menu.menu_select, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.action_delete:
                            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            int count = listViewGroup.getCheckedItemCount();
                            alert.setTitle(count +" danh mục được chọn.");
                            alert.setMessage("Bạn có chắc chắn muốn xóa không?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.removes();
                                    mode.finish();
                                }
                            });
                            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mode.finish();
                                }
                            });
                            alert.show();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.setCheckMode(false);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                    ((MainActivity) getActivity()).showFloatingActionButton();
                    adapter.removeSelection();
                }
            });
        }

    }
}
