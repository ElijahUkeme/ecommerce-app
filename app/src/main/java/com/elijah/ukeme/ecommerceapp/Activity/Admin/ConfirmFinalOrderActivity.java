package com.elijah.ukeme.ecommerceapp.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.Activity.User.HomeActivity;
import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import prevalent.Prevalent;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone,editTextHomeAddress,editTextCityName;
    private Button buttonConfirmOrder;
    private String totalPrice;
    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        editTextName = findViewById(R.id.shipping_name);
        editTextHomeAddress = findViewById(R.id.shipping_address);
        editTextPhone = findViewById(R.id.shipping_phone_number);
        editTextCityName = findViewById(R.id.shipping_city_name);
        buttonConfirmOrder = findViewById(R.id.confirm_final_order_button);
        totalPrice = getIntent().getStringExtra("total_price");
        Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price"+totalPrice,Toast.LENGTH_SHORT).show();

        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

    }
    public void checkFields(){
        if (editTextName.getText().toString().isEmpty()){
            editTextName.setError("Please Enter Your Name");
            cancel = true;
            editTextName.requestFocus();
        }else if (editTextPhone.getText().toString().isEmpty()){
            editTextPhone.setError("Please Enter Your Phone Number");
            cancel = true;
            editTextPhone.requestFocus();
        }else if (editTextHomeAddress.getText().toString().isEmpty()){
            editTextHomeAddress.setError("Please Enter Your Home Address");
            cancel = true;
            editTextHomeAddress.requestFocus();
        }else if (editTextCityName.getText().toString().isEmpty()){
            editTextCityName.setError("Please Enter Your City Name");
            cancel = true;
            editTextCityName.requestFocus();
        }else {
            confirmOrder();
        }

    }
    private void confirmOrder(){
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss ");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference confirmOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentUser.getPhone());
        HashMap<String,Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount",totalPrice);
        ordersMap.put("name",editTextName.getText().toString());
        ordersMap.put("phone",editTextPhone.getText().toString());
        ordersMap.put("homeAddress",editTextHomeAddress.getText().toString());
        ordersMap.put("city",editTextCityName.getText().toString());
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("status","Not Shipped");

        confirmOrderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your final Order has been placed Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}