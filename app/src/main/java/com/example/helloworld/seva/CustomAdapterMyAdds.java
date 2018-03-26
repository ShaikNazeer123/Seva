package com.example.helloworld.seva;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.seva.ListModel;
import com.example.helloworld.seva.MainActivity;
import com.example.helloworld.seva.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Shaik Nazeer on 20-02-2018.
 */

public class CustomAdapterMyAdds extends RecyclerView.Adapter<CustomAdapterMyAdds.ViewHolder>{
    private ArrayList data;
    Context context;
    public DatabaseReference mDatabase;

    public CustomAdapterMyAdds(ArrayList d,Context ct) {
        data = d;
        context=ct;
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        public ImageView t_post_image;
        public TextView t_item_post_date;
        public ImageView t_like_image;
        public ImageView t_edit;
        public ImageView t_delete;
        public  ImageView t_complete;

        public String uId;
        public String postId;
        public String categoryType;

        public Boolean isLiked;
        public Boolean isCompleted;

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
            this.t_item_post_date = v.findViewById(R.id.item_post_date);
            this.t_like_image = v.findViewById(R.id.like);
            this.t_edit = v.findViewById(R.id.edit);
            this.t_delete = v.findViewById(R.id.delete);
            this.t_complete = v.findViewById(R.id.complete);
        }
    }



    @Override
    public CustomAdapterMyAdds.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //Context doubt
        final View v =  LayoutInflater.from(context).inflate(R.layout.list_item_myadds, parent, false);
        return new ViewHolder(v);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListModel tempValues = (ListModel) data.get(position);
        if(tempValues != null) {
            holder.t_name.setText(tempValues.getItemName());
            holder.t_title.setText(tempValues.getItemTitle());
            holder.t_phoneNumber.setText(tempValues.getItemPhoneNumber());
            holder.t_description.setText(tempValues.getItemDescription());
            holder.t_location.setText(tempValues.getItemLocation());
            holder.t_expiryDate.setText(tempValues.getItemExpiryDate());
            holder.t_item_post_date.setText(tempValues.getItemPostDate());
            holder.postId = tempValues.getPostId();
            holder.categoryType = tempValues.getCategoryType();
            holder.uId = tempValues.getuId();


            Picasso.with(context).load(tempValues.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.t_post_image, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.with(context).load(tempValues.getImage()).into(holder.t_post_image);
                }

                @Override
                public void onError() {
                    Picasso.with(context).load(tempValues.getImage()).into(holder.t_post_image);
                }
            });

            if(tempValues.getLikeStatus() == true){
                holder.t_like_image.setImageResource(R.mipmap.ic_red);
                holder.t_like_image.setTag("1");
            }
            else{
                holder.t_like_image.setImageResource(R.mipmap.like);
                holder.t_like_image.setTag("0");
            }

            //holder.t_post_image.setImageURI(tempValues.getImageUri());
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

        holder.t_like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.t_like_image.getTag().equals("0")){
                    //state unlike --> like
                    holder.t_like_image.setImageResource(R.mipmap.ic_red);
                    holder.t_like_image.setTag("1");
                    mDatabase.child("Users").child(holder.uId).child("myinterests").child(holder.categoryType).child(holder.postId).setValue("true");
                    mDatabase.child(holder.categoryType).child(holder.postId).child("interested").child(holder.uId).setValue("true");
                }
                else{
                    holder.t_like_image.setImageResource(R.mipmap.like);
                    holder.t_like_image.setTag("0");
                    mDatabase.child("Users").child(holder.uId).child("myinterests").child(holder.categoryType).child(holder.postId).setValue(null);
                    mDatabase.child(holder.categoryType).child(holder.postId).child("interested").child(holder.uId).setValue(null);
                }
            }
        });

        holder.t_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "Edit button pressed", Toast.LENGTH_SHORT).show();
                Intent editIntent = new Intent(context,EditActivity.class);
                editIntent.putExtra("postId",holder.postId);
                editIntent.putExtra("categoryType",holder.categoryType);
                context.startActivity(editIntent);
            }
        });



        holder.t_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup_window,null);

                final PopupWindow popupWindow;
                LinearLayout linearLayout1;
                TextView closePopupBtn;
                TextView deleteBtn;
                final ProgressDialog progressDialog  = new ProgressDialog(view.getContext());

                closePopupBtn = (TextView) customView.findViewById(R.id.cancel);
                deleteBtn = (TextView) customView.findViewById(R.id.delete);

                //instantiate popup window
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //display the popup window
                popupWindow.showAsDropDown(view);

                //close the popup window on button click
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss delete
                        popupWindow.dismiss();
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        progressDialog.show();
                        //Log.e("pg bar","started");
                        progressDialog.setMessage("Deleting...");
                        mDatabase.child(holder.categoryType).child(holder.postId).setValue(null);
                        progressDialog.dismiss();
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
                //Toast.makeText(context, "Delete button pressed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.t_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("Transactions").child(holder.categoryType).child(holder.postId).setValue("true");
                mDatabase.child(holder.categoryType).child(holder.postId).child("iscompleted").setValue("true");
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
