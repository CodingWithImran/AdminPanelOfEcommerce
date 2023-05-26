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

public class NewProductActivity extends AppCompatActivity {

    ArrayList<AllProductModal> newproductList;
    AllProductAdapter allProductAdapter;
    FirebaseFirestore database;
    RecyclerView rec_newproduct;
    Button addproduct;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_ativity);
        rec_newproduct = findViewById(R.id.rec_new_product);
        addproduct= findViewById(R.id.add_new_product);

        database = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite...");
        progressDialog.show();

        newproductList = new ArrayList<>();
        allProductAdapter = new AllProductAdapter(NewProductActivity.this, newproductList);
        rec_newproduct.setAdapter(allProductAdapter);
        rec_newproduct.setLayoutManager(new GridLayoutManager(this, 2));

        showNewProduct();
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewProductActivity.this, AddNewProductActivity.class));
            }
        });
    }

    private void showNewProduct() {
        database.collection("NewProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AllProductModal allpromodal = document.toObject(AllProductModal.class);
                                String documentid = document.getId();
                                allpromodal.setProductId(documentid);
                                newproductList.add(allpromodal);
                                allProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}