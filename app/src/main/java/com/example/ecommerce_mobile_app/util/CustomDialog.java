package com.example.ecommerce_mobile_app.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ecommerce_mobile_app.R;

public class CustomDialog extends DialogFragment {
    private View.OnClickListener btnYesListener = null, btnCancelListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_delete_item_cart,container,false);
        view.findViewById(R.id.btnYes).setOnClickListener(btnYesListener);
        view.findViewById(R.id.btnCancel).setOnClickListener(btnCancelListener);
        return view;
    }

    public void setPositiveButton(View.OnClickListener onClickListener){
        this.btnYesListener = onClickListener;
    }
    public void setNegativeButton(View.OnClickListener onClickListener){
        this.btnCancelListener = onClickListener;
    }

}
