package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrandActivity extends AppCompatActivity {

    private LinearLayout addBrand;
    private RecyclerView brandRecyclerView;
    public static BrandAdapter brandAdapter;
    private Dialog loaddialog;
    private int position;

    public static List<BrandModel> brandModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        position = getIntent().getIntExtra("position", -1);
        final CategoryModel categoryModel = DBqueries.categoryModelList.get(position);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thương hiệu của "+categoryModel.getCategoryName());
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        brandModelList = categoryModel.getBrandModelList();

        brandRecyclerView = (RecyclerView) findViewById(R.id.brand_recyclerview);
        addBrand = (LinearLayout) findViewById(R.id.add_new_brand_btn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        brandRecyclerView.setLayoutManager(linearLayoutManager);

        brandAdapter = new BrandAdapter(brandModelList);
        brandRecyclerView.setAdapter(brandAdapter);
        brandAdapter.notifyDataSetChanged();

        addBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddBrandIntent = new Intent(getApplicationContext(), AddBrandActivity.class);
                startActivity(AddBrandIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}