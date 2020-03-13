package com.example.service_test.project.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service_test.R;
import com.example.service_test.project.Activities.Activity_03_Menu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    private Context mContext;
    private SharedPreferences preferences;
    private static final String TAG = "Gestion des dossiers";
    private EditText edit_username;
    private EditText edit_password;
    private Button btn_connexion;
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


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getActivity() ;

        //Gestion des objets
        edit_username = (EditText) view.findViewById(R.id.et_user);
        edit_password = (EditText) view.findViewById(R.id.et_password);
        btn_connexion = (Button) view.findViewById(R.id.btn_login);

        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On ferme le clavier
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (validation()) {

                    if (isNetworkAvailable()) {
/*
                        btn_connexion.setText("");
                        resizeAnim = new ResizeAnimation(btn_connexion,btn_connexion.getWidth(),btn_connexion.getHeight(),btn_connexion.getHeight(),btn_connexion.getHeight());
                        btn_connexion.startAnimation(resizeAnim);
 */
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


        return view ;
    }

    public void loginWS(final String sLogin, final String sPassword) {
        Thread t = new Thread() {

            @Override
            public void run() {

                HttpURLConnection myURLConnection = null;
                try {

                    //Configuration de la toAccueilConnectee
                    URL url = new URL(getContext().getResources().getString(R.string.environement_url) + "/users/login/");
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

                            try { setPreferences("sUser_sRole", jObject.getString("user_type")); }
                            catch (JSONException e) {setPreferences("sUser_sRole", "None");}

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

                    }
                    else if (iHttpStatus == HttpURLConnection.HTTP_BAD_REQUEST) {
                        // recu de la valeur msg

                                getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv10), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.activity_connexion_tv8), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e) {

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
                Intent inMenu = new Intent(getActivity(), Activity_03_Menu.class);
                inMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(inMenu);
                // finish() login fragment  :
                if(getActivity() != null) { getActivity().finish(); }

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

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public void popupConnexionKO() {

        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_information);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int fWidthDp = dm.widthPixels;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (fWidthDp);
        dialog.getWindow().setAttributes(lp);

        TextView tTitre = (TextView) dialog.findViewById(R.id.tv_titre);
        tTitre.setText(getActivity().getResources().getString(R.string.popup_titre));
        TextView tDescription = (TextView) dialog.findViewById(R.id.tv_description);
        tDescription.setText(getActivity().getResources().getString(R.string.activity_connexion_tv9));

        TextView valider = (TextView) dialog.findViewById(R.id.tv_appliquer);
        valider.setText(getActivity().getString(R.string.popup_button_OK));

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
        preferences = this.getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(sPreference, sValue);
        prefEditor.commit();
    }

    public String getPreferences(String sPreference, String defValue) {
        preferences = this.getActivity().getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return preferences.getString(sPreference, defValue);
    }

}