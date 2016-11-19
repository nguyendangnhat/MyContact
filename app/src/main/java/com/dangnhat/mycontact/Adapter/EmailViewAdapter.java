package com.dangnhat.mycontact.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangnhat.mycontact.Model.PhoneEmailIM;
import com.dangnhat.mycontact.R;

import java.util.ArrayList;

/**
 * Created by Dang Nhat on 11/18/2016.
 */

public class EmailViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhoneEmailIM> listEmail;

    public EmailViewAdapter(Context context, ArrayList<PhoneEmailIM> listEmail) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItemEmail;
        final PhoneEmailIM email = listEmail.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemEmail = inflater.inflate(R.layout.item_email, null);
        } else {
            viewItemEmail = convertView;
        }

        TextView textViewEmail = (TextView) viewItemEmail.findViewById(R.id.text_view_email);
        TextView textViewName = (TextView) viewItemEmail.findViewById(R.id.text_view_name);
        ImageView imageViewEmail = (ImageView) viewItemEmail.findViewById(R.id.image_view_email);

        String[] name = context.getResources().getStringArray(R.array.email_name);

        textViewEmail.setText(email.getValue());
        textViewName.setText(name[email.getName()]);
        imageViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("mailto:" + email.getValue());
                intent.putExtra(Intent.EXTRA_EMAIL, "dangna");
                intent.putExtra(Intent.EXTRA_SUBJECT, "adsf");
                intent.putExtra(Intent.EXTRA_STREAM, "adsf");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        return viewItemEmail;
    }
}
