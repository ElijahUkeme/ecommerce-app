package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import interfaces.ItemClickListener;
import model.Product;
import viewHolder.ProductViewHolder;

public class AdminCheckNewProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_new_product);
        recyclerView = findViewById(R.id.recycler_view_new_product_list);
        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(unverifiedProductsRef.orderByChild("productStatus")
                        .equalTo("Not Approved"),Product.class).build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.productName.setText(model.getProduct_name());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price N"+model.getPrice());
                //Picasso.get().load(product.getImage()).placeholder(R.drawable.marah).into(productViewHolder.productImage);
                Picasso.get().load(model.getImage()).into(holder.productImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productID = model.getPid();
                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckNewProduct.this);
                        builder.setTitle("Do you want to Approve this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    productApproved(productID);

                                }else if (i==1){

                                }
                            }
                        });
                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void productApproved(String productId){

        unverifiedProductsRef.child(productId).child("productStatus").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminCheckNewProduct.this,"Product Approved Successfully",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}