package com.elijah.ukeme.ecommerceapp.Activity.User;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.Activity.Admin.ConfirmFinalOrderActivity;
import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Cart;
import prevalent.Prevalent;
import viewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView totalPrice,textViewMessage;
    private Button nextButton;
    private int eachQuantity,eachPrice, eachTotal, overAllTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recycler_view_cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        totalPrice = findViewById(R.id.total_price);
        textViewMessage = findViewById(R.id.message1);
        nextButton = findViewById(R.id.next_process_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("total_price",String.valueOf(overAllTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentUser.getPhone()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int position, @NonNull Cart model) {

                eachQuantity = Integer.parseInt(model.getQuantity());
                eachPrice = Integer.parseInt(model.getPrice());
                eachTotal = eachPrice * eachQuantity;
                overAllTotalPrice = overAllTotalPrice + eachTotal;

                cartViewHolder.productName.setText(model.getPname());
                cartViewHolder.productPrice.setText("Price N"+model.getPrice());
                cartViewHolder.productQuantity.setText("Quantity "+model.getQuantity());
                cartViewHolder.productSubTotal.setText("Sub Total N"+eachTotal);
                totalPrice.setText("Total Price N"+ overAllTotalPrice);
                if (overAllTotalPrice==0){
                    nextButton.setEnabled(false);
                }

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0){
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }else if (i == 1){
                                    AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(CartActivity.this);
                                    deleteBuilder.setTitle("Delete");
                                    deleteBuilder.setMessage("Are you sure that you want to remove the item?");
                                    deleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            cartListRef.child("User View")
                                                    .child(Prevalent.currentUser.getPhone())
                                                    .child("Products")
                                                    .child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this,"Item Removed Successfully",Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                                    deleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    deleteBuilder.show();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }
    private void checkOrderStatus(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String statusStament = snapshot.child("status").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();

                    if (statusStament.equalsIgnoreCase("Shipped")){
                        totalPrice.setText("Dear "+name+"\nYour Order is Shipped Successfully\nYou can purchase more product once you receive your first order");
                        recyclerView.setVisibility(View.GONE);
                        textViewMessage.setText("Congratulations, Your final order has been Shipped Successfully, you will receive your product in the next 24 hours at your home Address\nThanks for your Patronage\nElijah Ukeme Udo, the Technical Officer");
                        textViewMessage.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);
                    }else if (statusStament.equalsIgnoreCase("Not Shipped")){

                        totalPrice.setText("Product Not Yet Ship");
                        recyclerView.setVisibility(View.GONE);
                        textViewMessage.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}