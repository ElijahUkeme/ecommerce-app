package com.elijah.ukeme.ecommerceapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import interfaces.showAbleMessage;
import model.Product;
import prevalent.Prevalent;

public class ProductDetailsActivity extends AppCompatActivity implements showAbleMessage {

    private Button addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName, productPrice, productDescription;
    private String productId,status="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addToCartBtn = findViewById(R.id.add_product_to_cart);
        productImage = findViewById(R.id.product_details_image);
        numberButton = findViewById(R.id.number_button);
        productName = findViewById(R.id.product_details_name);
        productPrice = findViewById(R.id.product_details_price);
        productDescription = findViewById(R.id.product_details_description);
        productId = getIntent().getStringExtra("pid");
        
        getProductDetails(productId);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equalsIgnoreCase("Orders placed")|| status.equalsIgnoreCase("Orders Shipped")){
                    showMessage("Error","You Already have one Order Placed\nPlease wait for the Admin Approval and Delivery before making another");
                }else {
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();
    }

    private void addingToCartList(){
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss &");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentUser.getPhone())
                .child("Products")
                .child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentUser.getPhone())
                                    .child("Products")
                                    .child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this,"Product Added to cart",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });





    }

    private void getProductDetails(String productId) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Product product = snapshot.getValue(Product.class);
                    productName.setText(product.getProduct_name());
                    productDescription.setText(product.getDescription());
                    productPrice.setText(product.getPrice());
                    Picasso.get().load(product.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkOrderStatus(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String statusStament = snapshot.child("status").getValue().toString();

                    if (statusStament.equalsIgnoreCase("Shipped")){
                        status = "Orders Shipped";

                    }else if (statusStament.equalsIgnoreCase("Not Shipped")){

                        status = "Orders Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    }
