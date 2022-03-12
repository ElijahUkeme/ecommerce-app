package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elijah.ukeme.ecommerceapp.Activity.User.ProductDetailsActivity;
import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Cart;
import prevalent.Prevalent;
import viewHolder.CartViewHolder;

public class AdminUserProductActivtity extends AppCompatActivity {

    private RecyclerView productList;
    private DatabaseReference cartListRef;
    RecyclerView.LayoutManager layoutManager;
    String userID,route;
    private int eachQuantity,eachPrice, eachTotal, overAllTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product_activtity);
        productList = findViewById(R.id.recycler_view_product_list);
        userID = getIntent().getStringExtra("uid");
        route = getIntent().getStringExtra("route");
        layoutManager = new LinearLayoutManager(this);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(layoutManager);

        if (route.equalsIgnoreCase("User")){
            cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                    .child("User View").child(Prevalent.currentUser.getPhone())
                    .child("Products");
        }else if (route.equalsIgnoreCase("Admin")){

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View")
                .child(userID).child("Products");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> option = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                eachQuantity = Integer.parseInt(model.getQuantity());
                eachPrice = Integer.parseInt(model.getPrice());
                eachTotal = eachPrice * eachQuantity;
                overAllTotalPrice = overAllTotalPrice + eachTotal;

                holder.productName.setText(model.getPname());
                holder.productPrice.setText("Price N"+model.getPrice());
                holder.productQuantity.setText("Quantity "+model.getQuantity());
                holder.productSubTotal.setText("Sub Total N"+eachTotal);

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();


    }
}