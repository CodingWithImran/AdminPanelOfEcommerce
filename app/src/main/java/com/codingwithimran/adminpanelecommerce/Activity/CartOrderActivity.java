package com.codingwithimran.adminpanelecommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codingwithimran.adminpanelecommerce.Adapters.OrderAdapter;
import com.codingwithimran.adminpanelecommerce.Modals.OrderModals;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartOrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    FirebaseFirestore database;
    ArrayList<OrderModals> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        recyclerView = findViewById(R.id.rec_cartOrder);

        database = FirebaseFirestore.getInstance();

        list = new ArrayList<>();

        orderAdapter = new OrderAdapter(this, list);
        recyclerView.setAdapter(orderAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.collection("Cart orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        OrderModals modal = document.toObject(OrderModals.class);
                        String orderId = document.getId();
                        modal.setOrderId(orderId);
                        list.add(modal);
                        orderAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

    }
}