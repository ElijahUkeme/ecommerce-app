package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import sellers.SellerProductCategoryActivity;

public class DeliveredOrderActivity extends AppCompatActivity {

    private String name,phone,address,city,totalAmount,orderedTime,orderedDate,deliveredTime,deliveredDate,userId;
    private EditText receiverName;
    private Button btnSubmitOrder;
    boolean cancel = false;
    private ProgressDialog loadingDialogBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delevered_order);
        receiverName = findViewById(R.id.receivers_name);
        btnSubmitOrder = findViewById(R.id.submit_button);
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        totalAmount = getIntent().getStringExtra("total");
        orderedDate =  getIntent().getStringExtra("date");
        orderedTime = getIntent().getStringExtra("time");
        userId = getIntent().getStringExtra("userID");
        loadingDialogBar = new ProgressDialog(this);

        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    saveDeliveredOrder();
                    

                }catch (Exception e){
                    Toast.makeText(DeliveredOrderActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void saveDeliveredOrder(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        deliveredDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss ");
        deliveredTime = currentTime.format(calendar.getTime());
        if (receiverName.getText().toString().isEmpty()){
            receiverName.setError("Please Enter the Receiver's Name");
            cancel = true;
            receiverName.requestFocus();
        }else {

            loadingDialogBar.setTitle("Order Processing...");
            loadingDialogBar.setMessage("Please wait while we are checking your delivery order");
            loadingDialogBar.setCanceledOnTouchOutside(false);
            loadingDialogBar.show();
            final DatabaseReference deliveredRef = FirebaseDatabase.getInstance()
                    .getReference().child("Delivered List");
            HashMap<String,Object> deliveredMap = new HashMap<>();
            deliveredMap.put("name",name);
            deliveredMap.put("phone",phone);
            deliveredMap.put("address",address);
            deliveredMap.put("city",city);
            deliveredMap.put("totalAmount",totalAmount);
            deliveredMap.put("orderedDate",orderedDate);
            deliveredMap.put("orderedTime",orderedTime);
            deliveredMap.put("deliveredDate",deliveredDate);
            deliveredMap.put("deliveredTime",deliveredTime);
            deliveredMap.put("receiversName",receiverName.getText().toString());

            deliveredRef.updateChildren(deliveredMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        loadingDialogBar.dismiss();
                        Toast.makeText(DeliveredOrderActivity.this,"Order Added Successfully to the Delivered Order List",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DeliveredOrderActivity.this, SellerProductCategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(DeliveredOrderActivity.this,"Error Occurred,Try Again",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}