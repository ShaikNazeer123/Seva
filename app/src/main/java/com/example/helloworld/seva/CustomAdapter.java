package com.example.helloworld.seva;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.seva.ListModel;
import com.example.helloworld.seva.MainActivity;
import com.example.helloworld.seva.R;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Shaik Nazeer on 20-02-2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private ArrayList data;
    ListModel tempValues=null;
    Context context;

    public CustomAdapter(ArrayList d,Context ct) {
        data = d;
        context=ct;
    }
    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data,Context ct) {
        context=ct;  //remove if it creates any null pointer problem - nazeer
        this.data = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView t_name;
        public TextView t_phoneNumber;
        public TextView t_title;
        public TextView t_description;
        public TextView t_location;
        public TextView t_expiryDate;
        public ImageView t_call_image;
        public ImageView t_location_image;
        public ImageButton t_post_image;


        public ViewHolder(View v){
            super(v);
            this.t_name = v.findViewById(R.id.item_name);
            this.t_title = v.findViewById(R.id.item_title);
            this.t_phoneNumber = v.findViewById(R.id.item_phone_number);
            this.t_description = v.findViewById(R.id.item_description);
            this.t_location = v.findViewById(R.id.item_location);
            this.t_expiryDate = v.findViewById(R.id.item_expiry_date);
            this.t_call_image = v.findViewById(R.id.call);
            this.t_location_image = v.findViewById(R.id.location);
            this.t_post_image = v.findViewById(R.id.postImage);
        }
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        //Context doubt
        final View v =  LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        tempValues = null;
        tempValues = (ListModel) data.get(position);
        if(tempValues != null) {
            holder.t_name.setText(tempValues.getItemName());
            holder.t_title.setText(tempValues.getItemTitle());
            holder.t_phoneNumber.setText(tempValues.getItemPhoneNumber());
            holder.t_description.setText(tempValues.getItemDescription());
            holder.t_location.setText(tempValues.getItemLocation());
            holder.t_expiryDate.setText(tempValues.getItemExpiryDate());
            holder.t_post_image.setImageURI(tempValues.getImageUri());
        }

        holder.t_call_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+tempValues.getItemPhoneNumber()));
                context.startActivity(intent);
            }
        });

        holder.t_location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map = "http://maps.google.co.in/maps?daddr=" + tempValues.getItemLocation();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                intent.setPackage("com.google.android.apps.maps");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
