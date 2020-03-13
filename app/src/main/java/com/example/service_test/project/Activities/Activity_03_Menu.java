package com.example.service_test.project.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.service_test.R;
import com.example.service_test.project.Adapters.EmployeeAdapter;
import com.example.service_test.project.Adapters.ViewPagerAdapter;
import com.example.service_test.project.Databases.DB_Data;
import com.example.service_test.project.Models.Employee;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.service_test.project.Adapters.MyAdapter.mPos;

public class  Activity_03_Menu extends AppCompatActivity {
    private static Bitmap bitmap;
    private Toolbar toolbar ;
    private ViewPager viewPager ;
    private ViewPagerAdapter adapter ;
    private TabLayout tabLayout ;
    private ListView listView ;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Context mContext;
    private DB_Data db;

    private EmployeeAdapter myCostumAdapter;
    private List<Employee> employees;

    final static int SELECT_PICTURE = 1;
    private ImageView image;
    private Uri filePath;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_03__menu);

        mContext = Activity_03_Menu.this;
        db = new DB_Data(mContext);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        navigationView = (NavigationView) findViewById(R.id.menu);

        // Set user && email
        View header = navigationView.getHeaderView(0);

        TextView user = header.findViewById(R.id.menu_user);
        TextView email = header.findViewById(R.id.menu_email);

        user.setText(getPreferences( "sUser_sUsername", "..."));
        email.setText(getPreferences("sUser_sMail", "..."));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.nav_message) {
                    // startActivity(new Intent(Activity_03_Menu.this, Activity_02_Login.class));
                } else if (id == R.id.nav_chat) {

                } else if (id == R.id.nav_profile) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        employees =new ArrayList<>();
        employees.clear();

        db.open();
        employees = db.getAllBonAchats();
        db.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer_menu, menu);

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_chat) {
            Toast.makeText(Activity_03_Menu.this, "Nav Chat Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data!= null && data.getData() != null ){

            filePath = data.getData();

            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();

                setBitmapImage(bitmap);

                if ( mPos != -1 ){
                    Employee employee = employees.get(mPos);
                    employee.setImage(byteArray);

                    db.open();
                    db.updateEmployee(employee);
                    db.close();

                }

            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public void setBitmapImage(Bitmap bitmap){ this.bitmap = bitmap; }

    public static Bitmap getBitmapImage(){ return bitmap; }

    /** ---------   BitMapToString  ---------- */

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /** ---------   StringToBitMap  ---------- */

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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

    public void selectFragment(int position){
        viewPager.setCurrentItem(position, true);
// true is to animate the transaction
    }

}

