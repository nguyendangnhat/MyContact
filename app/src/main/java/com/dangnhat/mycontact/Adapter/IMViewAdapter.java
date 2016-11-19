package com.dangnhat.mycontact.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * Created by Dang Nhat on 11/18/2016.
 */

public class IMViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhoneEmailIM> listIM;

    public IMViewAdapter(Context context, ArrayList<PhoneEmailIM> listIM) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItemIM;
        final PhoneEmailIM im = listIM.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewItemIM = inflater.inflate(R.layout.item_email, null);
        } else {
            viewItemIM = convertView;
        }

        TextView textViewIM = (TextView) viewItemIM.findViewById(R.id.text_view_email);
        TextView textViewName = (TextView) viewItemIM.findViewById(R.id.text_view_name);
        ImageView imageViewIM = (ImageView) viewItemIM.findViewById(R.id.image_view_email);

        final String[] name = context.getResources().getStringArray(R.array.im_name);

        textViewIM.setText(im.getValue());
        textViewName.setText(name[im.getName()]);

        if(im.getName() == 0){
            imageViewIM.setImageResource(R.drawable.ic_facebook_messenger);
            imageViewIM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "<---YOUR TEXT HERE--->.");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.facebook.orca");
                    try {
                        context.startActivity(sendIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (im.getName() == 1){
            imageViewIM.setImageResource(R.drawable.ic_skype);
            imageViewIM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent skype_intent = new Intent("android.intent.action.VIEW");
                    skype_intent.setClassName("com.skype.raider", "com.skype.raider.Main");
                    skype_intent.setData(Uri.parse("skype:" + im.getValue()));
                    try {
                        context.startActivity(skype_intent);
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context,"Please Install Skype", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            imageViewIM.setVisibility(View.GONE);
        }



        return viewItemIM;
    }
}
