package com.dangnhat.mycontact;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dangn on 11/19/2016.
 */

public class DialogChooseImageFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choose_image, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thay đổi ảnh")
                .setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.setView(view);
        final Dialog dialog = builder.create();

        TextView chupAnh = (TextView) view.findViewById(R.id.chup_anh);
        TextView chonAnh = (TextView) view.findViewById(R.id.chon_anh);
        chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        chupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(takePictureIntent, 1);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
