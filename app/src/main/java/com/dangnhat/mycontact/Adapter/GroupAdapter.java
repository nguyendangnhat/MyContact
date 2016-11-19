package com.dangnhat.mycontact.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangnhat.mycontact.Model.Group;
import com.dangnhat.mycontact.R;
import com.dangnhat.mycontact.data.ContactDBHelper;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;

/**
 * Created by dangn on 11/19/2016.
 */

public class GroupAdapter extends BaseAdapter {

    Context context;
    ArrayList<Group> listGroup;
    SparseBooleanArray mSelectedItemIds;
    private boolean checkMode;


    public GroupAdapter(Context context, ArrayList<Group> listGroup){
        this.context = context;
        this.listGroup = listGroup;
        mSelectedItemIds = new SparseBooleanArray();
        this.checkMode = false;
    }

    public GroupAdapter(Context context, ArrayList<Group> listGroup, boolean checkMode){
        this.context = context;
        this.listGroup = listGroup;
        mSelectedItemIds = new SparseBooleanArray();
        this.checkMode = checkMode;
    }

    @Override
    public int getCount() {
        return listGroup.size();
    }

    @Override
    public Group getItem(int position) {
        return listGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemGroup;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemGroup = inflater.inflate(R.layout.item_group, null);
        }
        else {
            itemGroup = convertView;
        }

        Group group = listGroup.get(position);

        TextView groupName = (TextView) itemGroup.findViewById(R.id.group_name);
        groupName.setText(group.getName());
        CheckBox checkBox = (CheckBox) itemGroup.findViewById(R.id.check_box);
        if(checkMode){
            checkBox.setVisibility(View.VISIBLE);
            if(mSelectedItemIds.get(position)){
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        } else {
            checkBox.setVisibility(View.GONE);
        }
        return itemGroup;
    }

    public void setCheckMode(boolean checkMode){
        this.checkMode = checkMode;
    }

    public void remove(Group group, SQLiteDatabase database)
    {
        listGroup.remove(group);
        database.delete("tbl_group", " id = ?", new String[]{ String.valueOf(group.getId()) });
    }

    public void removes(){
        ContactDBHelper myMoneyDbHelper = new ContactDBHelper(context);
        SQLiteDatabase database = myMoneyDbHelper.getWritableDatabase();
        for (int i = (mSelectedItemIds.size()-1); i>=0 ; i--)
        {
            if(mSelectedItemIds.valueAt(i))
            {
                Group selectedItem = getItem(mSelectedItemIds.keyAt(i));
                remove(selectedItem, database);
            }
        }
        database.close();
        notifyDataSetChanged();
    }

    public ArrayList<Group> getGroupChecked()
    {
        ArrayList<Group> list = new ArrayList<>();
        for (int i = 0; i < listGroup.size(); i++){
            if (mSelectedItemIds.get(i)){
                list.add(listGroup.get(i));
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




    public void setSelectedItemIds(SparseBooleanArray mSelectedItemIds){
        this.mSelectedItemIds = mSelectedItemIds;
    }
}
