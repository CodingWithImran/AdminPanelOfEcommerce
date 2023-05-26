package com.codingwithimran.adminpanelecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingwithimran.adminpanelecommerce.Modals.Category;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> list;
    FirebaseFirestore db;
    FirebaseAuth auth;
    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category modal = list.get(position);
        holder.cat_name.setText(modal.getCategory_name());
        Glide.with(context).load(modal.getCategory_icon()).into(holder.cat_img);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String id = modal.getCategoryId();
                db.collection("categories").document(id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    list.remove(modal);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Category has been deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView cat_img;
        TextView cat_name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_img = (itemView).findViewById(R.id.product_img);
            cat_name = (itemView).findViewById(R.id.product_name);
        }
    }
}
