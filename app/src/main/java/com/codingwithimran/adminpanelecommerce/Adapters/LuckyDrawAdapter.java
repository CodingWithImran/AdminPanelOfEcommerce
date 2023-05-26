package com.codingwithimran.adminpanelecommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.adminpanelecommerce.Modals.LuckDrawModal;
import com.codingwithimran.adminpanelecommerce.R;

import java.util.ArrayList;

public class LuckyDrawAdapter extends RecyclerView.Adapter<LuckyDrawAdapter.viewHolder>{
    Context context;
    ArrayList<LuckDrawModal> list;

    public LuckyDrawAdapter(Context context, ArrayList<LuckDrawModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_luckydraw, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LuckDrawModal modal = list.get(position);
        holder.name.setText(modal.getName());
        holder.email.setText(modal.getEmail());
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
