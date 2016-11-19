package com.dangnhat.mycontact;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dangnhat.mycontact.Model.PhoneEmailIM;

import java.util.ArrayList;

/**
 * Created by dangn on 11/13/2016.
 */

public class IMAdapter extends BaseAdapter{
    Context context;
    ArrayList<PhoneEmailIM> listIM;

    public IMAdapter(Context context, ArrayList<PhoneEmailIM> listIM) {
        this.context = context;
        this.listIM = listIM;
    }

    @Override
    public int getCount() {
        return listIM.size();
    }

    @Override
    public PhoneEmailIM getItem(int position) {
        return listIM.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View viewItemIM;
        PhoneEmailIM im = listIM.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemIM = inflater.inflate(R.layout.item_phone_email_im, null);
        }
        else {
            viewItemIM = convertView;
        }

        /**
         * set value cho spinner
         */
        final Spinner spinner = (Spinner) viewItemIM.findViewById(R.id.spinner_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.im_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(im.getName());

        /**
         * set value cho textview email
         */
        EditText editTextIM = (EditText) viewItemIM.findViewById(R.id.edit_text_value);
        editTextIM.setText(im.getValue().toString());
        editTextIM.setHint("Tài khoản");
        editTextIM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position == listIM.size() - 1){
                    listIM.set(position, new PhoneEmailIM(0 , 0, (int) spinner.getSelectedItemId(), s.toString()));
                }
            }
        });

        /**
         * sự kiện xóa email
         */
        ImageView imageDelete = (ImageView) viewItemIM.findViewById(R.id.image_view_delete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        return viewItemIM;
    }

    /**
     * xóa 1 item theo vị trí
     * @param position
     */
    public void remove(int position){
        listIM.remove(position);
        notifyDataSetChanged();
    }
}
