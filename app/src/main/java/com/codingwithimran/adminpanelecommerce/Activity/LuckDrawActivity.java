package com.codingwithimran.adminpanelecommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codingwithimran.adminpanelecommerce.Adapters.LuckyDrawAdapter;
import com.codingwithimran.adminpanelecommerce.Modals.AllProductModal;
import com.codingwithimran.adminpanelecommerce.Modals.LuckDrawModal;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LuckDrawActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<LuckDrawModal> list;
    LuckyDrawAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_draw);
        recyclerView = findViewById(R.id.rec_luckydaw);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        list = new ArrayList<>();

        adapter = new LuckyDrawAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        db.collection("Luck Draw").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LuckDrawModal modal = document.toObject(LuckDrawModal.class);
                                String documentId = document.getId();
                                modal.setId(documentId);
                                list.add(modal);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}