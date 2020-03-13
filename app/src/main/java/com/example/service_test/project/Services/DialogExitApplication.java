package com.example.service_test.project.Services;


import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.service_test.R;

public class DialogExitApplication extends Service {

    private Context mContext;							// Variable de context
    private SharedPreferences preferences;				// Définition de l'objet préférence pour les variables globales
    private Activity mActivity;							// Variable de context
    private String TAG = "MaFranchiseCaissier";

    public DialogExitApplication(){}

    public DialogExitApplication(Context context, Activity mActivity){

        mContext = context;
        this.mActivity = mActivity;

    }

    public void ExiteApplicationPopUp(){

        // custom dialog
        final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_information);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int fWidthDp = dm.widthPixels;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = fWidthDp;
        dialog.getWindow().setAttributes(lp);

        // set the custom dialog components - text, image and button
        TextView tTitre = (TextView) dialog.findViewById(R.id.tv_titre);
        tTitre.setText(mContext.getResources().getString(R.string.app_desc));
        TextView tDescription = (TextView) dialog.findViewById(R.id.tv_description);
        tDescription.setText(mContext.getResources().getString(R.string.popup_text_exit_application));
        TextView valider = (TextView) dialog.findViewById(R.id.tv_appliquer);

        valider.setText(mContext.getResources().getString(R.string.popup_button_oui).toUpperCase());
        valider.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mActivity.finishAffinity();
                dialog.dismiss();

            }

        });

        ImageView annuler = (ImageView) dialog.findViewById(R.id.iv_annuler);
        annuler.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        try{
            dialog.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setPreferences (String sPreference, String sValue) {
        preferences = mContext.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(sPreference,sValue);
        prefEditor.commit();
    }

    public String getPreferences(String sPreference, String defValue) {
        preferences = mContext.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return preferences.getString(sPreference, defValue);
    }

}
