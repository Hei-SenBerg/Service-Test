package com.example.service_test.project.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Services.DialogExitApplication;
import com.example.service_test.project.Services.ResizeAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Activity_02_Login extends AppCompatActivity {
    private ImageView backImageView;
    private TextView textView;
    private TextView galerie;

    private Context mContext;
    private SharedPreferences preferences;
    private static final String TAG = "Gestion des dossiers";

    private EditText edit_username;
    private EditText edit_password;
    private TextView btn_connexion;
    private ProgressBar pg_Progresse ;
    private int iWidthTextView;
    private FrameLayout layout_cnx_btn;
    private RecyclerView recyclerView;
    final int duration = 10;
    final int pixelsToMove = 30;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            recyclerView.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };
    private LinearLayoutManager horizontalLayoutManager;
    private ArrayList<String> viewLogos;
    private ResizeAnimation resizeAnim ;

    private TextView signUp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_02__login);

        //Déclaration du Context
        mContext = Activity_02_Login.this;

        // Register --- Inscription :
        signUp = (TextView) findViewById(R.id.btn_register);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, Activity_02_Register.class));
            }
        });

        //Gestion de la couleur de la status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.black));
        }



        backImageView = (ImageView) findViewById(R.id.btn_back);
        backImageView.setClickable(true);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish();}});

        //Gestion des dimmensions du visuel
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Gestion du progress
        pg_Progresse = (ProgressBar) findViewById(R.id.pg_Progresse);
        pg_Progresse.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(pg_Progresse.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(mContext, R.color.redd));
            pg_Progresse.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));

        }  else pg_Progresse.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.redd), PorterDuff.Mode.SRC_IN);

        //Gestion des objets
        edit_username = (EditText) findViewById(R.id.ed_user);
        edit_password = (EditText) findViewById(R.id.ed_psw);
        btn_connexion = (TextView) findViewById(R.id.btn_connexion);
        layout_cnx_btn = (FrameLayout) findViewById(R.id.layout_cnx_btn);

        //Gestion de btn connexion
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On ferme le clavier
                View view = Activity_02_Login.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                iWidthTextView = btn_connexion.getWidth();

                if (validation()) {

                    if (isNetworkAvailable()) {

                        btn_connexion.setText("");
                        resizeAnim = new ResizeAnimation(btn_connexion,btn_connexion.getWidth(),btn_connexion.getHeight(),btn_connexion.getHeight(),btn_connexion.getHeight());
                        btn_connexion.startAnimation(resizeAnim);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                loginWS(edit_username.getText().toString(), edit_password.getText().toString());

                            }
                        }, 450);

                    } else {

                        popupConnexionKO();

                    }

                }

            }

        });

        // Recuperation des logos et leurs affichage
        //               - ------- - -- - -- - -  -- -            :::::    getDataEnseigne();



    }

    public void loginWS(final String sLogin, final String sPassword) {

        pg_Progresse.setVisibility(View.VISIBLE);

        Thread t = new Thread() {

            @Override
            public void run() {

                HttpURLConnection myURLConnection = null;
                try {

                    //Configuration de la toAccueilConnectee
                    URL url = new URL(mContext.getResources().getString(R.string.environement_url) + "/users/login/");
                    myURLConnection = (HttpURLConnection) url.openConnection();
                    myURLConnection.setRequestMethod("POST");

                    myURLConnection.setConnectTimeout(30000);
                    myURLConnection.setReadTimeout(15000);



                    myURLConnection.setDoInput(true);
                    myURLConnection.setDoOutput(true);


                    Log.e(TAG, "Activity_02_Connexion - Login - URL : " + url);


                    Log.e(TAG, "Activity_02_Connexion - Login - sLogin : " + sLogin);
                    Log.e(TAG, "Activity_02_Connexion - Login - sPassword : " + sPassword);

                    //Définiton des données à envoyer
                    Uri.Builder builder01 = new Uri.Builder();
                    builder01.appendQueryParameter("username", sLogin);
                    builder01.appendQueryParameter("password", sPassword);

                    String query01 = builder01.build().getEncodedQuery();
                    Log.e(TAG, "Activity_02_Connexion - Login - query01 : " + query01);

                    OutputStream os01 = myURLConnection.getOutputStream();
                    Log.e(TAG, "Activity_02_Connexion - Login - os01 : " + os01);

                    BufferedWriter writer01 = new BufferedWriter(new OutputStreamWriter(os01, "UTF-8"));
                    writer01.write(query01);
                    writer01.flush();
                    writer01.close();
                    os01.close();

                    //Récupération du code réponse
                    myURLConnection.connect();

                    int iHttpStatus = myURLConnection.getResponseCode();
                    Log.e(TAG, "Activity_02_Connexion - Login - iHttpStatus : " + iHttpStatus);

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
                            Log.e(TAG, "Activity_02_Connexion - Login - sb : " + sb.toString() );
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

                            Log.e(TAG, "Activity_02_Connexion - sUser_sUsername - username : " + getPreferences("sUser_sUsername","...") );

                        } finally {

                            buReader.close();
                            inStream.close();
                        }

                        //On lance les WS et on redirige vers l'background_accueil connecté
                        toAccueilConnectee();

                    } else if (iHttpStatus == HttpURLConnection.HTTP_BAD_REQUEST) {

                        // recu de la valeur msg

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnClearAnimation();
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv10), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnClearAnimation();
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv8), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e) {

                    btnClearAnimation();
                    e.printStackTrace();
                    Log.e(TAG, "Activity_02_Login - Login - Exception : " + e);

                } finally {

                    if (myURLConnection != null) {
                        try {
                            myURLConnection.disconnect();
                        } catch (Exception ex) {
                            Log.e(TAG, "Activity_02_Connexion - Login - Exception disconnect : " + ex);
                        }
                    }
                }
            }
        };

        t.start();
    }

    public void btnClearAnimation(){

        layout_cnx_btn.setVisibility(View.VISIBLE);
        btn_connexion.clearAnimation();
        btn_connexion.setText(R.string.activity_connexion_tv11);

        pg_Progresse.setVisibility(View.INVISIBLE);

        /*
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) btn_connexion.getLayoutParams();
        lp.width = iWidthTextView;
        btn_connexion.setLayoutParams(lp);
         */
    }

    private void toAccueilConnectee() {
/*
        //Récupération de la liste d'histo bon d achat
        try {
            myHisto_BonAchat myHisto_bonAchat = new myHisto_BonAchat(mContext);
            myHisto_bonAchat.getBonAchatHistoList();
        } catch (NoClassDefFoundError e) {
            Log.e(TAG, "Activity_01_Main - getBonAchats - NoClassDefFoundError");
        }

        //recuperation de la liste des tickets
        try {
            myTicketAssociation myTicketAssociation = new myTicketAssociation(mContext);
            myTicketAssociation.getTicketsHistoListWS();
        } catch (NoClassDefFoundError e) {
            Log.e(TAG, "Activity_01_Main - getTicketsHistoListWS - NoClassDefFoundError");
        }

 */

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent inMenu = new Intent(mContext, Activity_03_Menu.class);
                inMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(inMenu);
                Activity_02_Login.this.finish();

            }
        });

    }

    public boolean validation() {

        boolean valid = true;
        String username = edit_username.getText().toString();
        String password = edit_password.getText().toString();

        // verif edLogin
        if (username.isEmpty()) {
            edit_username.setError(getResources().getString(R.string.activity_connexion_tv4));
            valid = false;
        } else {
            edit_username.setError(null);
        }
/*
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_username.setError(getResources().getString(R.string.activity_connexion_tv12));
            valid = false;
        } else {
            edit_username.setError(null);

        }


 */
        // verif password insertion
        if (password.isEmpty()) {
            edit_password.setError(getResources().getString(R.string.activity_connexion_tv5));
            valid = false;
        } else {
            edit_password.setError(null);
        }

        return valid;
    }

    //Gestion des retours du service de géolocalisation
    private BroadcastReceiver mMessageReceiverEnseigneUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String state = intent.getExtras().getString("status");
            if (state.equals("EnseignestChanged OK")) {
/*
                getDataEnseigne();
                adapter.notifyDataSetChanged();
*/
            }

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMessageReceiverEnseigneUpdated);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DialogExitApplication dialogExit = new DialogExitApplication(mContext, Activity_02_Login.this);
            dialogExit.ExiteApplicationPopUp();
            return true;

        } else return super.onKeyDown(keyCode, event);

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

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