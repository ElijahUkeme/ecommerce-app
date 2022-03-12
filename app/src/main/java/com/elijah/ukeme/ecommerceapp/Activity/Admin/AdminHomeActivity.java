package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.elijah.ukeme.ecommerceapp.Activity.User.HomeActivity;
import com.elijah.ukeme.ecommerceapp.Activity.User.MainActivity;
import com.elijah.ukeme.ecommerceapp.R;

public class AdminHomeActivity extends AppCompatActivity {
    private Button logout,confirmNewOrders,checkDeliveredOrder,maintainProduct,approveNewProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logout = findViewById(R.id.button_admin_logout_category);
        confirmNewOrders = findViewById(R.id.button_check_new_order);
        checkDeliveredOrder = findViewById(R.id.button_check_delivered_order_category);
        maintainProduct = findViewById(R.id.button_maintain_products);
        approveNewProduct = findViewById(R.id.button_check_and_approve_products);

        approveNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this,AdminCheckNewProduct.class);
                startActivity(intent);
            }
        });

        maintainProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        checkDeliveredOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, DeliveredListActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        confirmNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrderConfirm.class);
                startActivity(intent);
            }
        });
    }
}