package com.codingwithimran.adminpanelecommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codingwithimran.adminpanelecommerce.Adapters.AllProductAdapter;
import com.codingwithimran.adminpanelecommerce.Modals.AllProductModal;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllProductActivity extends AppCompatActivity {
    ArrayList<AllProductModal> allproductlist;
    AllProductAdapter allProductAdapter;
    FirebaseFirestore database;
    RecyclerView rec_allproduct;
    Button addproduct;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product);
        rec_allproduct = findViewById(R.id.rec_all_product);
        addproduct= findViewById(R.id.add_product);

        database = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite...");
        progressDialog.show();
        allproductlist = new ArrayList<>();
        allProductAdapter = new AllProductAdapter(AllProductActivity.this, allproductlist);
        rec_allproduct.setAdapter(allProductAdapter);
        rec_allproduct.setLayoutManager(new GridLayoutManager(this, 2));

        showAllProduct();
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllProductActivity.this, AddAllProductActivity.class));
            }
        });

    }

    private void showAllProduct() {
        database.collection("AllProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AllProductModal allpromodal = document.toObject(AllProductModal.class);
                                String documentId = document.getId();
                                allpromodal.setProductId(documentId);
                                allproductlist.add(allpromodal);
                                allProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}