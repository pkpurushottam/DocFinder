package com.example.root.docfinder;

/**
 * Created by root on 4/12/16.
 */



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private Context context;
    // As the name suggests, its the context of current state of the application/object. It lets newly created objects understand what has been going on.
    // Typically you call it to get information regarding another part of your program (activity, package/application)
    private ArrayList<MainActivity.Users> usersArrayList;

    public CustomAdapter(Context context, ArrayList<MainActivity.Users> usersArrayList) {
        this.context=context;
        this.usersArrayList=usersArrayList;

        /* Layout Inflater to call external xml layout () */
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Basically it is needed to create (or fill) View based on XML file in runtime.
    }

    @Override
    public int getCount() {
        if(usersArrayList.size()<=0)
            return 1;
        return usersArrayList.size();
    }

    @Override
    public Object getItem(int  position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        //convertView is used to reuse old view.
        View vi = convertView;
        ViewHolder holder;
        if(convertView==null){

            /****** Inflate card_view.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item, null);

            /****** View Holder Object to contain card_view.xml file elements ******/

            holder = new ViewHolder();
            holder.id = (TextView) vi.findViewById(R.id.user_id);
            holder.name=(TextView)vi.findViewById(R.id.user_name);
            holder.icon_path=(TextView)vi.findViewById(R.id.user_email);
           holder.icon = (ImageView)vi.findViewById(R.id.icon);
          //  Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(icon);
            //  holder.icon_path=(TextView)vi.findViewById(R.id.iconPath);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }else {
            holder = (ViewHolder) vi.getTag();
        }

        if(usersArrayList.size()<=0)
        {
            holder.id.setText("No Data");
            holder.name.setVisibility(View.GONE);
            holder.icon_path.setVisibility(View.GONE);
            holder.icon.setVisibility(View.GONE);

        }
        else {

            MainActivity.Users document=usersArrayList.get(position);
            holder.id.setText("File ID: "+document.id);
            holder.name.setText("Name: "+document.name);
            holder.icon_path.setText("Description: "+document.icon_path);
         //   holder.icon.setImageURI(icon_path.getText().toString());
           // Glide.with().load("http://i.imgur.com/DvpvklR.png").into(icon);
         //   HttpURLConnection connection= (HttpURLConnection) holder.icon_path.openConnection();
//            Glide.with(holder.icon.getContext()).load(holder.icon_path).into(holder.icon);

        }

        return vi;
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView id;
        public TextView name;
        public TextView icon_path;
        public ImageView icon;

    }

}
