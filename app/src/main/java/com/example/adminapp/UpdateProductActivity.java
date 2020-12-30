package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static java.security.AccessController.getContext;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText title, price, cutted_price, desc;
    private Button save, cancel;
    private ImageView image;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageButton delete_img, change_img;

    private Boolean updatePhoto = false;
    private Uri uri;

    private Dialog loadingDialog;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

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

        //////////loading dialog
        loadingDialog = new Dialog(getApplicationContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //////////loading dialog

        Intent intent = getIntent();

        title = (EditText) findViewById(R.id.product_title);
        price = (EditText) findViewById(R.id.product_price);
        cutted_price = (EditText) findViewById(R.id.product_cutted_price);
        desc = (EditText) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.product_image);
        delete_img = (ImageButton) findViewById(R.id.delete_img_btn);
        change_img = (ImageButton) findViewById(R.id.change_img_btn);

        title.setText(intent.getStringExtra("title"));
        price.setText(intent.getStringExtra("price"));
        cutted_price.setText(intent.getStringExtra("cutted_price"));
        desc.setText(intent.getStringExtra("desc"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("image")).apply(new RequestOptions().placeholder(R.drawable.no_img)).into(image);

        String id = intent.getStringExtra("id");

        save = (Button) findViewById(R.id.add_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePic(id);

                Map<String,Object> productData = new HashMap<>();
                productData.put("product_price",price.getText().toString());
                productData.put("product_title", title.getText().toString());
                productData.put("product_description", desc.getText().toString());
                productData.put("cutted_price", cutted_price.getText().toString());
                productData.put("product_image", uri.toString());

                db.collection("PRODUCTS").document(id)
                        .update(productData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DBqueries.productModelList.clear();
                                Toasty.success(getApplicationContext(),"Cập nhật thành công !!!", Toasty.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.success(getApplicationContext(),"Cập nhật thất bại !!!", Toasty.LENGTH_SHORT).show();
                                finish();
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

        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(R.drawable.no_img).into(image);
            }
        });

        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);

                    }else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                }else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    updatePhoto = true;
                    Glide.with(getApplicationContext()).load(uri).into(image);
                } else {
                    Toasty.error(getApplicationContext(), "Image not found!", Toast.LENGTH_SHORT, true).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            }else {
                Toasty.error(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT,true).show();
            }
        }
    }

    private void updatePic(String product_id) {
        final StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("pi/"+product_id+".jpg");
        Glide.with(getApplicationContext()).asBitmap().load(uri).centerCrop().into(new ImageViewTarget<Bitmap>(image) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        uri=task.getResult();
                                    }else {
                                        loadingDialog.dismiss();
                                        String error=task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            loadingDialog.dismiss();
                            String error=task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return;
            }

            @Override
            protected void setResource(@Nullable Bitmap resource) {
                image.setImageResource(R.drawable.no_img);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}