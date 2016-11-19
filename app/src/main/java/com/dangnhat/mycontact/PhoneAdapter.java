package com.dangnhat.mycontact;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
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

public class PhoneAdapter extends BaseAdapter{
    Context context;
    ArrayList<PhoneEmailIM> listPhone;

    public PhoneAdapter(Context context, ArrayList<PhoneEmailIM> listPhone) {
        this.context = context;
        this.listPhone = listPhone;
    }

    @Override
    public int getCount() {
        return listPhone.size();
    }

    @Override
    public PhoneEmailIM getItem(int position) {
        return listPhone.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View viewItemPhone;
        PhoneEmailIM phone = listPhone.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemPhone = inflater.inflate(R.layout.item_phone_email_im, null);
        }
        else {
            viewItemPhone = convertView;
        }

        /**
         * set value cho spinner
         */
        final Spinner spinner = (Spinner) viewItemPhone.findViewById(R.id.spinner_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.phone_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(phone.getName());


        /**
         * set value cho textview so dien thoai
         */
        EditText editTextPhone = (EditText) viewItemPhone.findViewById(R.id.edit_text_value);
        editTextPhone.setText(phone.getValue().toString());
        editTextPhone.setHint("Số điện thoại");
        editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position == listPhone.size() - 1){
                    listPhone.set(position, new PhoneEmailIM(0 , 0, (int) spinner.getSelectedItemId(), s.toString()));
                }
            }
        });

        /**
         * sự kiện xóa phone
         */
        ImageView imageDelete = (ImageView) viewItemPhone.findViewById(R.id.image_view_delete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        return viewItemPhone;
    }

    /**
     * xóa 1 item(số điện thoại) theo vị trí
     * @param position
     */
    public void remove(int position){
        listPhone.remove(position);
        notifyDataSetChanged();
    }
}
