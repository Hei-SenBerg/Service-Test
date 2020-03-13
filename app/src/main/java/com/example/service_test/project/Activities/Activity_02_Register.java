package com.example.service_test.project.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service_test.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Activity_02_Register extends AppCompatActivity {
    private TextView annuler ;
    private Context mContext ;
    private EditText edit_username;
    private EditText edit_firstname;
    private EditText edit_lastname;
    private EditText edit_email;
    private EditText edit_password;
    private EditText edit_confirm_password;
    private TextView btn_register;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_02__register);

        mContext = Activity_02_Register.this ;

        //Gestion des objets :
        edit_username = (EditText) findViewById(R.id.user);
        edit_firstname = (EditText) findViewById(R.id.prenom);
        edit_lastname = (EditText) findViewById(R.id.nom);
        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.psw);
        edit_confirm_password = (EditText) findViewById(R.id.confirm_psw);
        btn_register = (TextView) findViewById(R.id.btn_connexion);

        //Gestion de btn Annuler :
        annuler = (TextView) findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Gestion de btn connexion :
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On ferme le clavier
                View view = Activity_02_Register.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (validation()) {

                    if (isNetworkAvailable()) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                registerWS(
                                        edit_username.getText().toString(),
                                        edit_firstname.getText().toString(),
                                        edit_lastname.getText().toString(),
                                        edit_email.getText().toString(),
                                        edit_password.getText().toString(),
                                        edit_confirm_password.getText().toString()); }}, 450);

                    } else {

                        popupConnexionKO();

                    }

                }

            }

        });

    }

    public void registerWS(final String sUsername, final String sFirstName, final String sLastName, final String sEmail, final String sPassword, final String sConfirmPassword){

        Thread t = new Thread() {

            @Override
            public void run() {

                HttpURLConnection myURLConnection = null;
                try {

                    //Configuration de la toAccueilConnectee
                    URL url = new URL(mContext.getResources().getString(R.string.environement_url) + "/users/create/");
                    myURLConnection = (HttpURLConnection) url.openConnection();

                    myURLConnection.setRequestMethod("POST");
                    myURLConnection.setConnectTimeout(15000);
                    myURLConnection.setReadTimeout(10000);
                    myURLConnection.setDoInput(true);
                    myURLConnection.setDoOutput(true);

                    //Définiton des données à envoyer
                    Uri.Builder builder01 = new Uri.Builder();

                    builder01.appendQueryParameter("username", sUsername);
                    builder01.appendQueryParameter("last_name", sFirstName);
                    builder01.appendQueryParameter("first_name", sLastName);
                    builder01.appendQueryParameter("email", sEmail);
                    builder01.appendQueryParameter("password", sPassword);
                    builder01.appendQueryParameter("confirm_password", sConfirmPassword);

                    String query01 = builder01.build().getEncodedQuery();
                    OutputStream os01 = myURLConnection.getOutputStream();
                    BufferedWriter writer01 = new BufferedWriter(new OutputStreamWriter(os01, "UTF-8"));
                    writer01.write(query01);
                    writer01.flush();
                    writer01.close();
                    os01.close();

                    //Récupération du code réponse
                    myURLConnection.connect();
                    int iHttpStatus = myURLConnection.getResponseCode();

                    if (iHttpStatus == HttpURLConnection.HTTP_OK) {

                        //récupération de la réponse
                        InputStreamReader inStream = new InputStreamReader((myURLConnection.getInputStream()));
                        BufferedReader buReader = new BufferedReader(inStream);
                        try {

                            StringBuilder sb = new StringBuilder();
                            String output;
                            while ((output = buReader.readLine()) != null) {
                                sb.append(output);
                            }
                            String sReponse = sb.toString();
                            JSONObject jObject = new JSONObject(sReponse);

                            try { setPreferences("sUser_sUsername", jObject.getString("username")); }
                            catch (JSONException e) {setPreferences("sUser_sUsername", "None");}

                            try { setPreferences("sUser_sNom", jObject.getString("last_name")); }
                            catch (JSONException e) {setPreferences("sUser_sNom", "None");}

                            try { setPreferences("sUser_sPrenom", jObject.getString("first_name")); }
                            catch (JSONException e) {setPreferences("sUser_sPrenom", "None");}

                            try { setPreferences("sUser_sMail", jObject.getString("email")); }
                            catch (JSONException e) {setPreferences("sUser_sMail", "None");}

                            try { setPreferences("sUser_sAccesToken", jObject.getString("access_token")); }
                            catch (JSONException e) {setPreferences("sUser_sAccesToken", "None");}

                            try { setPreferences("sUser_sRefresh_token", jObject.getString("refresh_token")); }
                            catch (JSONException e) {setPreferences("sUser_sRefresh_token", "None");}

                            // Log.e("TAG", "Activity_02_Connexion - sUser_sUsername - username : " + getPreferences("sUser_sUsername","...") );

                        } finally {
                            buReader.close();
                            inStream.close();
                        }

                        //On lance les WS et on redirige vers l'background_accueil connecté
                        toAccueilConnectee();

                    } else if (iHttpStatus == HttpURLConnection.HTTP_BAD_REQUEST) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv10), Toast.LENGTH_LONG).show();

                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv8), Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("TAG", "Activity_02_Register - Login - Exception : " + e);

                } finally {

                    if (myURLConnection != null) {
                        try {
                            myURLConnection.disconnect();
                        } catch (Exception ex) {
                            Log.e("TAG", "Activity_02_Register - Login - Exception disconnect : " + ex);
                        }
                    }

                }

            }
        };

        t.start();

    }

    private void toAccueilConnectee() {

        //Récupération de la liste d'histo bon d achat
        //          ---------------------- ------------------------ ---------------

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent inMenu = new Intent(mContext, Activity_03_Menu.class);
                inMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(inMenu);
                Activity_02_Register.this.finish();

            }
        });

    }

    public void popupConnexionKO() {

        final Dialog dialog = new Dialog(mContext, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_information);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int fWidthDp = dm.widthPixels;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (fWidthDp);
        dialog.getWindow().setAttributes(lp);

        TextView tTitre = (TextView) dialog.findViewById(R.id.tv_titre);
        tTitre.setText(mContext.getResources().getString(R.string.popup_titre));
        TextView tDescription = (TextView) dialog.findViewById(R.id.tv_description);
        tDescription.setText(mContext.getResources().getString(R.string.activity_connexion_tv9));

        TextView valider = (TextView) dialog.findViewById(R.id.tv_appliquer);
        valider.setText(mContext.getString(R.string.popup_button_OK));

        valider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        ImageView iv_annuler = (ImageView) dialog.findViewById(R.id.iv_annuler);
        iv_annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        try {
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public boolean validation() {

        boolean valid = true;

        String username = edit_username.getText().toString();
        String firstname = edit_firstname.getText().toString();
        String lastname = edit_lastname.getText().toString();
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();
        String confirmpassword = edit_confirm_password.getText().toString();

        // verif username :
        if (username.isEmpty()) {
            edit_username.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_username.setError(null);
        }

        // verif firstname :
        if (firstname.isEmpty()) {
            edit_firstname.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_firstname.setError(null);
        }

        // verif lastname
        if (lastname.isEmpty()) {
            edit_lastname.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_lastname.setError(null);
        }

        // verif edLogin
        if (email.isEmpty()) {
            edit_email.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_email.setError(null);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError(getResources().getString(R.string.activity_connexion_tv12));
            valid = false;
        } else {
            edit_email.setError(null);

        }

        // verif password insertion
        if (password.isEmpty()) {
            edit_password.setError(getResources().getString(R.string.activity_connexion_tv5));
            valid = false;
        } else {
            edit_password.setError(null);
        }

        // verif confirmpassword :
        if (confirmpassword.isEmpty()) {
            edit_confirm_password.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_confirm_password.setError(null);
        }


        return valid;
    }

    public void setPreferences(String sPreference, String sValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(sPreference, sValue);
        prefEditor.commit();
    }

    public String getPreferences(String sPreference, String defValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return preferences.getString(sPreference, defValue);
    }

    public void setIntPreferences(String sPreference, int sValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(sPreference, sValue);
        prefEditor.commit();
    }

    public int getIntPreferences(String sPreference, int defValue) {
        preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return preferences.getInt(sPreference, defValue);
    }


}
