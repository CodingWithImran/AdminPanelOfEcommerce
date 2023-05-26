package com.codingwithimran.adminpanelecommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.codingwithimran.adminpanelecommerce.Adapters.CategoryAdapter;
import com.codingwithimran.adminpanelecommerce.Modals.Category;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllCategoryActivity extends AppCompatActivity {
    CategoryAdapter categoryAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore database;
    FirebaseAuth auth;
    ArrayList<Category> categoriesList;
    TextView add_categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        recyclerView = findViewById(R.id.rec_category);
        add_categories = findViewById(R.id.add_category);

        database = FirebaseFirestore.getInstance();

        categoriesList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoriesList);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        showCategories();
//        Add Categories
        add_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(AllCategoryActivity.this, AddCategoryActivity.class));
            }
        });


    }
    void showCategories(){
        database.collection("categories").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                Category categoryModal = document.toObject(Category.class);
                                String documentID = document.getId();
                                categoryModal.setCategoryId(documentID);
                                categoriesList.add(categoryModal);
                                categoryAdapter.notifyDataSetChanged();
//                            progressDialog.dismiss();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }
}