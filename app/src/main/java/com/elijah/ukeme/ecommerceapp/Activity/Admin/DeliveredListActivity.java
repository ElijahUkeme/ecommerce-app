package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.DeliveredOrder;
import viewHolder.DeliveredViewHolder;

public class DeliveredListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference deliveredOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_list);
        recyclerView = findViewById(R.id.recycler_view_delivered_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deliveredOrderRef = FirebaseDatabase.getInstance().getReference().child("Delivered List");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DeliveredOrder> options = new FirebaseRecyclerOptions.Builder<DeliveredOrder>()
                .setQuery(deliveredOrderRef,DeliveredOrder.class)
                .build();

        FirebaseRecyclerAdapter<DeliveredOrder, DeliveredViewHolder> adapter = new FirebaseRecyclerAdapter<DeliveredOrder, DeliveredViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DeliveredViewHolder holder, int position, @NonNull DeliveredOrder model) {

                holder.shipperName.setText("Name: "+model.getName());
                holder.phoneNumber.setText("Phone Number "+model.getPhone());
                holder.receiver.setText("Received By: "+model.getReceiversName());
                holder.totalPrice.setText("Total Price: "+model.getTotalAmount());
                holder.addressCity.setText("Address: "+model.getAddress()+", "+model.getCity());
                holder.dateTime.setText("Ordered On: "+model.getOrderedDate()+" at "+model.getOrderedTime());
                holder.dateTime2.setText("Received On: "+model.getDeliveredDate()+" at "+model.getDeliveredTime());

                holder.deliveredProductBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Uid = getRef(position).getKey();
                        Intent intent = new Intent(DeliveredListActivity.this, AdminUserProductActivtity.class);
                        intent.putExtra("uid",Uid);
                        intent.putExtra("route","Admin");
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public DeliveredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivered_order_layout,parent,false);

                return new DeliveredViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}