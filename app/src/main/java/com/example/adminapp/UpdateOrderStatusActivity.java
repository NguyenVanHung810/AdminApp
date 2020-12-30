package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class UpdateOrderStatusActivity extends AppCompatActivity {

    private Spinner statusList;
    private Button update, cancel;
    private ArrayList<String> order_status;
    private String status;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_status);

        statusList = findViewById(R.id.spinner_status);
        update = findViewById(R.id.update_btn);
        cancel = findViewById(R.id.cancel_btn);
        order_status = new ArrayList<>();
        intent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cập nhật tình trạng đơn hàng");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        order_status = getIntent().getStringArrayListExtra("statusList");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, order_status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusList.setAdapter(adapter);

        statusList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("ORDERS").document(intent.getStringExtra("id"))
                        .update("Order_Status", status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toasty.success(getApplicationContext(), "Cập nhật thành công !!!", Toasty.LENGTH_SHORT).show();
                        }
                        else {
                            Toasty.error(getApplicationContext(), "Cập nhật không thành công !!!", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}