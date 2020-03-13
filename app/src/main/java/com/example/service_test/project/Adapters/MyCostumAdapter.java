package com.example.service_test.project.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.service_test.R;
import com.example.service_test.project.Classes.Data;
import com.example.service_test.project.Databases.DB_Data;

import java.util.List;

public class MyCostumAdapter extends BaseAdapter {

    Context context;
    List<Data> datas ;
    private DB_Data db;

    public MyCostumAdapter(Context context, List<Data> datas){
        super();
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        db = new DB_Data(context);

        convertView = LayoutInflater.from(context).inflate(R.layout.service_objects_list, parent, false);



         TextView mid = convertView.findViewById(R.id.mid);
         TextView userId = convertView.findViewById(R.id.userId);
         TextView title = convertView.findViewById(R.id.title);


        Log.d("tag","data - datas.get(position).getId() " + datas.get(position).getId() );
        Log.d("tag","data - datas.get(position).getId() " + datas.get(position).getUserId() );
        Log.d("tag","data - datas.get(position).getId() " + datas.get(position).getTitle() );


        mid.setText(String.valueOf(datas.get(position).getId()));
        userId.setText(String.valueOf(datas.get(position).getUserId()) );
        title.setText(String.valueOf(datas.get(position).getTitle()));

        return convertView;
    }
}
