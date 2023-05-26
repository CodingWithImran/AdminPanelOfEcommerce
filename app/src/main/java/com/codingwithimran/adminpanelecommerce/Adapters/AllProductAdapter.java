package com.codingwithimran.adminpanelecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingwithimran.adminpanelecommerce.Modals.AllProductModal;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.productViewHolder>{

    Context context;
    ArrayList<AllProductModal> list;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public AllProductAdapter(Context context, ArrayList<AllProductModal> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new productViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        AllProductModal productModal = list.get(position);
        Glide.with(context).load(productModal.getProduct_img()).into(holder.product_img);
        holder.product_name.setText(productModal.getName());
        holder.product_price.setText(String.valueOf(productModal.getPrice()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                db.collection("AllProducts").document(productModal.getProductId())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    list.remove(productModal);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Product has been deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                db.collection("NewProducts").document(productModal.getProductId())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    list.remove(productModal);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Product has been deleted", Toast.LENGTH_SHORT).show();
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

    class productViewHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_price;
        ImageView product_img;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = (itemView).findViewById(R.id.product_name);
            product_price = (itemView).findViewById(R.id.prodcut_price);
            product_img = (itemView).findViewById(R.id.product_img);
        }
    }
}
