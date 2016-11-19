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

public class EmailAdapter extends BaseAdapter{
    Context context;
    ArrayList<PhoneEmailIM> listEmail;
    public EmailAdapter(Context context, ArrayList<PhoneEmailIM> listEmail) {
        this.context = context;
        this.listEmail = listEmail;
    }

    @Override
    public int getCount() {
        return listEmail.size();
    }

    @Override
    public PhoneEmailIM getItem(int position) {
        return listEmail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View viewItemEmail;
        PhoneEmailIM email = listEmail.get(position);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemEmail = inflater.inflate(R.layout.item_phone_email_im, null);
        }
        else {
            viewItemEmail = convertView;
        }

        /**
         * set value cho spinner
         */
        final Spinner spinner = (Spinner) viewItemEmail.findViewById(R.id.spinner_name);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.email_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(email.getName());

        /**
         * set value cho textview email
         */
        EditText editTextEmail = (EditText) viewItemEmail.findViewById(R.id.edit_text_value);
        editTextEmail.setText(email.getValue().toString());
        editTextEmail.setHint("Email");
        editTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(position == listEmail.size() - 1){
                    listEmail.set(position, new PhoneEmailIM(0 , 0, (int) spinner.getSelectedItemId(), s.toString()));
                }
            }
        });

        /**
         * sự kiện xóa email
         */
        ImageView imageDelete = (ImageView) viewItemEmail.findViewById(R.id.image_view_delete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        return viewItemEmail;
    }

    /**
     * xóa 1 item(email) theo vị trí
     * @param position
     */
    public void remove(int position){
        listEmail.remove(position);
        notifyDataSetChanged();
    }
}
