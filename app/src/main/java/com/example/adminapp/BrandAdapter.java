package com.example.adminapp;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.Viewholder>  {

    private List<BrandModel> brandModelList;

    public BrandAdapter(List<BrandModel> brandModelList) {
        this.brandModelList = brandModelList;
    }

    @NonNull
    @Override
    public BrandAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_item_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.Viewholder holder, int position) {

        String id = brandModelList.get(position).getBrand_id();
        String name=brandModelList.get(position).getBrand_name();


        holder.setdata(id, name, position);
    }

    @Override
    public int getItemCount() {
        return brandModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView brand_name;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            brand_name = (TextView) itemView.findViewById(R.id.brandname_item);
        }

        private void setdata(String id, String name, final int position) {
            brand_name.setText(name);
        }
    }
}
