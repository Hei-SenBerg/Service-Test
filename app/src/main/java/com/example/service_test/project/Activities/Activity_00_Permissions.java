package com.example.service_test.project.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.service_test.R;
import com.example.service_test.project.Classes.Element_Permissions;

import java.util.ArrayList;
import java.util.List;

public class Activity_00_Permissions extends AppCompatActivity {
    private TextView tvNext ;
    private ArrayList<String> permission;
    private List<Element_Permissions> ListPermissions;

    private Context mContext;
    private static final String TAG = "MaFranchiseCaissier";
    private Activity mActivity;
    private SharedPreferences preferences;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private String[] permissionArray;
    private boolean bPermissionsGood;
    private boolean bNOPermissionsRequired = false;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_00__permissions);

        mContext = Activity_00_Permissions.this;
        mActivity = Activity_00_Permissions.this;

        tvNext = (TextView) findViewById(R.id.bt_next);

        ListPermissions = new ArrayList<>();
        ListPermissions.clear();
        ListPermissions.add(new Element_Permissions("android.permission.INTERNET",false));
        ListPermissions.add(new Element_Permissions("android.permission.ACCESS_NETWORK_STATE",false));
        ListPermissions.add(new Element_Permissions("android.permission.WRITE_EXTERNAL_STORAGE",false));
        ListPermissions.add(new Element_Permissions("android.permission.READ_EXTERNAL_STORAGE",false));

        setNextButtons();

    }

    private void setNextButtons() {

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!getBooleanPreferences("NonPermissionAllowed", false)) ActivityCompat.requestPermissions(mActivity, permissionArray, REQUEST_CODE_PERMISSION);
                else {

                    Intent inMenu = new Intent(mContext, Activity_02_Login.class);
                    mContext.startActivity(inMenu);
                    Activity_00_Permissions.this.finish();

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Verification des permissions
        permission = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) permission.add(android.Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) permission.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permission.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) permission.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

        permissionArray = new String[ permission.size() ];
        permission.toArray( permissionArray );

        if (permission.size() > 0) bPermissionsGood = false;
        else bPermissionsGood = true;

        toMenu();

    }

    private void toMenu(){

        if( bPermissionsGood || bNOPermissionsRequired || getBooleanPreferences("NonPermissionAllowed", false)) {

            Intent inMenu = new Intent(mContext, Activity_RegistreLogin.class);
            mContext.startActivity(inMenu);
            Activity_00_Permissions.this.finish();

        }

    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean bPermissionReq = false ;
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int i = 0 ; i < permissions.length; i++) {
                String permission = permissions[i];
                for(int j = 0 ; j < ListPermissions.size() ; j ++) {
                    if(ListPermissions.get(j).getsPermission().equals(permission)) {
                        bPermissionReq = ListPermissions.get(j).isbObligatoire();
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            boolean showRationale = mActivity.shouldShowRequestPermissionRationale( permission );
                            if (!showRationale ) {
                                setBooleanPreferences("NonPermissionAllowed", true);
                            }
                            if(!bPermissionReq) bNOPermissionsRequired = true;
                        }
                    }
                }
            }
            setNextButtons();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) return false;
        else return true;
    }

    //------------------------------------
    //  Définition des variables partagées
    //------------------------------------

    public void setBooleanPreferences (String sPreference, boolean bValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(sPreference,bValue);
        prefEditor.commit();
    }

    public boolean getBooleanPreferences(String sPreference, boolean defValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return preferences.getBoolean(sPreference, defValue);
    }

}
