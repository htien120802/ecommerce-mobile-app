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
//        dismiss();
        this.btnYesListener = onClickListener;
    }
    public void setNegativeButton(View.OnClickListener onClickListener){
//        dismiss();
        this.btnCancelListener = onClickListener;
    }
    //    public interface IClickCustomDialog{
//        public void clickYes();
//        public void clickNo();
//    }
//    private Dialog dialog;
//    private IClickCustomDialog clickCustomDialog;
//    public CustomDialog(Context context, IClickCustomDialog clickCustomDialog){
//        this.clickCustomDialog = clickCustomDialog;
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_dialog_delete_item_cart);
//        dialog.setCancelable(true);
//
//        dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickCustomDialog.clickYes();
//            }
//        });
//
//        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickCustomDialog.clickNo();
//                dialog.dismiss();
//            }
//        });
//    }
//    public void show(int gravity){
//        Window window = dialog.getWindow();
//        if (window == null)
//            return;
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = gravity;
//        window.setAttributes(windowAttributes);
//    }
//    public void dismiss(){
//        dialog.dismiss();
//    }
}
