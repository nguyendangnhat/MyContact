package com.dangnhat.mycontact.Adapter;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangnhat.mycontact.Model.PhoneEmailIM;
import com.dangnhat.mycontact.R;

import java.util.ArrayList;

/**
 * Created by dangn on 11/16/2016.
 */

public class PhoneViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhoneEmailIM> listPhone;

    public PhoneViewAdapter(Context context, ArrayList<PhoneEmailIM> listPhone) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItemPhone;
        final PhoneEmailIM phone = listPhone.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemPhone = inflater.inflate(R.layout.item_phone, null);
        } else {
            viewItemPhone = convertView;
        }

        TextView textViewPhone = (TextView) viewItemPhone.findViewById(R.id.text_view_phone);
        TextView textViewName = (TextView) viewItemPhone.findViewById(R.id.text_view_name);
        ImageView imageViewCall = (ImageView) viewItemPhone.findViewById(R.id.image_view_call);
        ImageView imageViewChat = (ImageView) viewItemPhone.findViewById(R.id.image_view_chat);

        String[] name = context.getResources().getStringArray(R.array.phone_name);

        textViewPhone.setText(phone.getValue());
        textViewName.setText(name[phone.getName()]);
        imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getValue().trim();

                String uri = "tel:" + phoneNumber;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });

        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send SMS", "");
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);

                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String ("01234"));

                try {
                    context.startActivity(smsIntent);
                    Log.i("Finished sending SMS...", "");
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(context, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewItemPhone;
    }
}
