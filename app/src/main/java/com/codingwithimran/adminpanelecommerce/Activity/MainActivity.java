package com.codingwithimran.adminpanelecommerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codingwithimran.adminpanelecommerce.R;

public class MainActivity extends AppCompatActivity {
    TextView all_category, all_product, new_product, luckdraw_users, directOrder, cartOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        all_category = findViewById(R.id.All_Category_id);
        all_product = findViewById(R.id.all_product_id);
        new_product = findViewById(R.id.new_product_id);
        luckdraw_users = findViewById(R.id.luckyDrawusers);
        directOrder = findViewById(R.id.directOrder_id);
        cartOrder = findViewById(R.id.cartOrder_id);

        all_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(new Intent(MainActivity.this, AllCategoryActivity.class)));
            }
        });
        all_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllProductActivity.class));
            }
        });
        new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewProductActivity.class));
            }
        });
        luckdraw_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LuckDrawActivity.class));
            }
        });
        directOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DirectOrderActivity.class));
            }
        });
        cartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartOrderActivity.class));
            }
        });
    }
}