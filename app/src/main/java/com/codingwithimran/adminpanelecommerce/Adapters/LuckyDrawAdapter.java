package com.codingwithimran.adminpanelecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.adminpanelecommerce.Modals.LuckDrawModal;
import com.codingwithimran.adminpanelecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LuckyDrawAdapter extends RecyclerView.Adapter<LuckyDrawAdapter.viewHolder>{
    Context context;
    ArrayList<LuckDrawModal> list;
    FirebaseFirestore db;
    FirebaseAuth auth;
    public LuckyDrawAdapter(Context context, ArrayList<LuckDrawModal> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_luckydraw, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LuckDrawModal modal = list.get(position);
        holder.name.setText(modal.getMy_name());
        holder.email.setText(modal.getMy_email());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String id = modal.getId();
                db.collection("Luck Draw").document(id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    list.remove(modal);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Lucky draw has been deleted", Toast.LENGTH_SHORT).show();
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

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView name, email;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = (itemView).findViewById(R.id.nameId);
            email = (itemView).findViewById(R.id.emailId);
        }
    }
}
