package com.dangnhat.mycontact;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.dangnhat.mycontact.Adapter.ContactWithHeaderAdapter;
import com.dangnhat.mycontact.Model.ContactWithHeader;

/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private ContactWithHeaderAdapter contactWithHeaderAdapter;

    public Toolbar_ActionMode_Callback(Context context, ContactWithHeaderAdapter contactWithHeaderAdapter) {
        this.context = context;
        this.contactWithHeaderAdapter = contactWithHeaderAdapter;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        contactWithHeaderAdapter.setCheckMode(true);
        mode.getMenuInflater().inflate(R.menu.menu_select, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }


    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Fragment listFragment = new MainActivity().getFragment(1);
                ((ContactFragment) listFragment).deleteRows();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Fragment listFragment = new MainActivity().getFragment(1);
        ((ContactFragment) listFragment).setNullToActionMode();
        contactWithHeaderAdapter.removeSelection();
        contactWithHeaderAdapter.setCheckMode(false);
    }
}
