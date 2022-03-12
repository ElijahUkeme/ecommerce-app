package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.AdminOrders;
import viewHolder.AdminOrderViewHolder;

public class AdminNewOrderConfirm extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order_confirm);
        recyclerView = findViewById(R.id.recycler_view_order_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.shipperName.setText("Name: "+model.getName());
                holder.phoneNumber.setText("Phone Number "+model.getPhone());
                holder.totalPrice.setText("Total Price: "+model.getTotalAmount());
                holder.addressCity.setText("Address: "+model.getHomeAddress()+","+model.getCity());
                holder.dateTime.setText("Order Placed On: "+model.getDate()+" at "+model.getTime());

                holder.viewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Uid = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderConfirm.this, AdminUserProductActivtity.class);
                        intent.putExtra("uid",Uid);
                        intent.putExtra("route","User");
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderConfirm.this);
                        builder.setTitle("Is this Order Shipped?");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if (i==0){
                                    String Uid = getRef(position).getKey();
                                   Intent intent = new Intent(AdminNewOrderConfirm.this, DeliveredOrderActivity.class);
                                    intent.putExtra("name",model.getName());
                                    intent.putExtra("phone",model.getPhone());
                                    intent.putExtra("total",model.getTotalAmount());
                                    intent.putExtra("address",model.getHomeAddress());
                                    intent.putExtra("city",model.getCity());
                                    intent.putExtra("date",model.getDate());
                                    intent.putExtra("time",model.getTime());
                                    intent.putExtra("userID",Uid);
                                    removeOrder(Uid);
                                    startActivity(intent);




                                }else {
                                    finish();
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public void removeOrder(String userId){

        orderRef.child(userId).removeValue();
        Toast.makeText(getApplicationContext(),"Order removed Successfully from the Order List",Toast.LENGTH_SHORT).show();
    }
}