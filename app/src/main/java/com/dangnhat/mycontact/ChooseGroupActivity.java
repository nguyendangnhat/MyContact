package com.dangnhat.mycontact;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.dangnhat.mycontact.Adapter.GroupAdapter;
import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.data.ContactDBHelper;

import java.util.ArrayList;

public class ChooseGroupActivity extends AppCompatActivity {

    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Group> listGroup = new ArrayList<>();
        ContactDBHelper contactDBHelper = new ContactDBHelper(this);
        SQLiteDatabase database = contactDBHelper.getReadableDatabase();

        SparseBooleanArray mSelectedItemIds = new SparseBooleanArray();
        ArrayList<Integer> listId;

        listId = getIntent().getIntegerArrayListExtra("listID");
        if(listId == null){
            listId = new ArrayList<>();
        }

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_group", null);
        try{
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");

            int id;
            String name;
            for (int i = 0; i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                id = cursor.getInt(idIndex);
                if(listId.contains(id)){
                    mSelectedItemIds.put(i, true);
                }
                name = cursor.getString(nameIndex);
                listGroup.add(new Group(id, name));
            }
        } finally {
            cursor.close();
            database.close();
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        groupAdapter = new GroupAdapter(this, listGroup, true);
        groupAdapter.setSelectedItemIds(mSelectedItemIds);
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupAdapter.toggleSelection(position);
            }
        });

        CheckBox checkBoxChuaGan = (CheckBox) findViewById(R.id.check_box_chua_gan);

        checkBoxChuaGan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    groupAdapter.removeSelection();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            Intent i = new Intent();
            i.putExtra("listGroup", groupAdapter.getGroupChecked());
            setResult(RESULT_OK, i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
