package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import sellers.SellerProductCategoryActivity;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChanges,deleteProduct;
    private EditText productName,productPrice,productDescription;
    private ImageView productImage;
    String productId,pName,pPrice,pDescription;
    private ProgressDialog loadingDialog;
    boolean cancel = false;

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        productDescription = findViewById(R.id.editText_item_product_description);
        productName = findViewById(R.id.editText_item_product_name);
        productPrice = findViewById(R.id.editText_item_product_price);
        productImage = findViewById(R.id.maintTain_item_product_image);
        applyChanges = findViewById(R.id.button_apply_changes);
        deleteProduct = findViewById(R.id.button_delete_product);
        loadingDialog = new ProgressDialog(this);
        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        displaySpecificProductDetails();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyProductChange();
            }
        });
        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }
    private void displaySpecificProductDetails(){
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("product_name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();
                    productName.setText(name);
                    productPrice.setText(price);
                    productDescription.setText(description);
                    Picasso.get().load(image).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void applyProductChange(){
         pName = productName.getText().toString();
         pPrice = productPrice.getText().toString();
         pDescription = productDescription.getText().toString();

        if (pName.isEmpty()){
            productName.setError("Please Enter the Product Name");
            cancel = true;
            productName.requestFocus();
        }else if (pPrice.isEmpty()){
            productPrice.setError("Please Enter the Product Price");
            cancel = true;
            productPrice.requestFocus();
        }else if (pDescription.isEmpty()){
            productDescription.setError("Please Enter the Product Description");
            cancel = true;
            productDescription.requestFocus();
        }else {
            acceptChanges();
        }

    }
    private void acceptChanges(){
        loadingDialog.setTitle("Order Processing...");
        loadingDialog.setMessage("Please wait while we are checking your delivery order");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productId);
        productMap.put("description",pDescription);
        productMap.put("price",pPrice);
        productMap.put("product_name",pName);
        productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loadingDialog.dismiss();
                    Toast.makeText(AdminMaintainProductsActivity.this,"Product Updated Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void deleteThisProduct(){
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductsActivity.this,"Product Deleted Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                
            }
        });
    }
}