package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    private List<OrderItemModel> orderItemModelList;
    private Dialog loadingDialog;

    public OrderItemAdapter(List<OrderItemModel> orderItemModelList, Dialog loadingDialog) {
        this.orderItemModelList = orderItemModelList;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {

        String orderId = orderItemModelList.get(position).getOrderId();
        String orderStatus=orderItemModelList.get(position).getOrderStatus();
        Date orderDate = orderItemModelList.get(position).getOrderedDate();
        Boolean cr = orderItemModelList.get(position).isCancellationrequested();
        String cus = orderItemModelList.get(position).getFullName();
        holder.setdata(orderId, orderStatus,orderDate, cr, cus, position);
    }

    @Override
    public int getItemCount() {
        return orderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView orderId, orderStatus, ordered_date;
        private ImageView update_order_status;
        private TextView cus;
        private ConstraintLayout order_item_layout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            orderId = (TextView) itemView.findViewById(R.id.order_id);
            orderStatus = (TextView) itemView.findViewById(R.id.order_status);
            ordered_date = (TextView) itemView.findViewById(R.id.ordered_date);
            cus = (TextView) itemView.findViewById(R.id.cus);
            update_order_status = (ImageView) itemView.findViewById(R.id.update_order_status);
            order_item_layout = (ConstraintLayout) itemView.findViewById(R.id.order_item_layout);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setdata(String id, String status, Date ordereddate, Boolean cr, String customer, final int position){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");

            orderId.setText("Mã đơn hàng: "+id);
            orderStatus.setText(status);
            ordered_date.setText("Ngày đặt hàng: "+simpleDateFormat.format(ordereddate));
            cus.setText("Khách hàng: "+customer);


            ArrayList<String> statusList = new ArrayList<>();

            if(status.equals("Ordered")){
                statusList.add("Packed");
                statusList.add("Cancelled");
            }
            else if(status.equals("Packed")){
                statusList.add("Shipped");
                statusList.add("Cancelled");
            }
            else if(status.equals("Shipped")){
                statusList.add("Delivery");
            }
            else if(status.equals("Delivered")){
                order_item_layout.setBackgroundResource(R.color.md_green_300);
                update_order_status.setVisibility(View.GONE);
            }
            else if(status.equals("Cancelled")){
                order_item_layout.setBackgroundResource(R.color.md_red_300);
                update_order_status.setVisibility(View.GONE);
            }


            update_order_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent updateOrderStatusIntent = new Intent(itemView.getContext(), UpdateOrderStatusActivity.class);
                    updateOrderStatusIntent.putExtra("id", id);
                    updateOrderStatusIntent.putStringArrayListExtra("statusList", statusList);
                    itemView.getContext().startActivity(updateOrderStatusIntent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent OrderDetailsIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);
                    OrderDetailsIntent.putExtra("position", position);
                    itemView.getContext().startActivity(OrderDetailsIntent);
                }
            });
        }

    }
}
