package com.codingwithimran.adminpanelecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.adminpanelecommerce.Modals.OrderModals;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewholder>{
    Context context;
    ArrayList<OrderModals> list;
    FirebaseFirestore db;

    public OrderAdapter(FirebaseFirestore db) {
        this.db = FirebaseFirestore.getInstance();
    }

    public OrderAdapter(Context context, ArrayList<OrderModals> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(context).inflate(R.layout.layout_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        OrderModals modals = list.get(position);
        holder.currentDate.setText(modals.getCurrentDate());
        holder.productNumber.setText(modals.getProductNumber());
        holder.productName.setText(modals.getProductName());
        holder.quantity.setText(String.valueOf(modals.getQuantity()));
        holder.totalPrice.setText(String.valueOf(modals.getTotalPrice()));
        holder.fulladdress.setText(modals.getCustomerFullAddress());
        holder.customerName.setText(modals.getCustomerName());
        holder.deliverOrder.setText(String.valueOf(modals.getTrackingStatus()));
        holder.paymentStatus.setText(modals.getPaymentStatus());
        Map<String, Object> map = new HashMap<>();
        map.put("TrackingStatus", "Ship");

        String orderId = modals.getOrderId();
        db = FirebaseFirestore.getInstance();
//        db.collection("Direct Purchase").
//        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                OrderModals modals1 = documentSnapshot.toObject(OrderModals.class);
//                                if(modals1.getTrackingStatus().equals("ship")){
//                                    Toast.makeText(context, "" + modals1.getTrackingStatus(), Toast.LENGTH_SHORT).show();
//                                    holder.deliverOrder.setText("ship");
//                                }
//                            }
//                        }
//                    }
//                });


        DocumentReference itemRef = db.collection("Direct Purchase").document(orderId);
        DocumentReference orderCart = db.collection("Cart orders").document(orderId);
        holder.deliverOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            Toast.makeText(context, "orders has been shipped successfully" + map, Toast.LENGTH_SHORT).show();
                            holder.deliverOrder.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                orderCart.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            Toast.makeText(context, "orders has been shipped successfully" + map, Toast.LENGTH_SHORT).show();
                            holder.deliverOrder.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView currentDate, currentTime, productNumber, productName, quantity, totalPrice, fulladdress, customerName,
                deliverOrder, paymentStatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            currentDate = (itemView).findViewById(R.id.order_date);
//            currentTime = (itemView).findViewById(R.id.orde)
            productNumber = (itemView).findViewById(R.id.product_number);
            productName = (itemView).findViewById(R.id.productname_id);
            quantity = (itemView).findViewById(R.id.quantity_id);
            totalPrice = (itemView).findViewById(R.id.tprice_id);
            fulladdress = (itemView).findViewById(R.id.customerName_id);
            customerName = (itemView).findViewById(R.id.customerName_id);
            deliverOrder = (itemView).findViewById(R.id.DeliverOrder);
            paymentStatus = (itemView).findViewById(R.id.paymentStatus_id);
        }
    }
}
