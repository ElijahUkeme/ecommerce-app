package com.elijah.ukeme.ecommerceapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import interfaces.showAbleMessage;

public class RegisterActivity extends AppCompatActivity implements showAbleMessage {

    private Button btnCreate;
    private EditText userName, phoneNumber, password;
    boolean cancel = false;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingDialog = new ProgressDialog(this);

        userName = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.register_phone_number);
        password = findViewById(R.id.register_password);
        btnCreate = findViewById(R.id.register_button);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        String name = userName.getText().toString();
        String phone = phoneNumber.getText().toString();
        String pass = password.getText().toString();
        if (name.isEmpty()){
            userName.setError("Username required");
            userName.requestFocus();
            cancel = true;
        }else if (pass.isEmpty()){
            password.setError("password Required");
            password.requestFocus();
            cancel = true;
        }else if (phone.isEmpty()){
            phoneNumber.setError("Phone number required");
            phoneNumber.requestFocus();
            cancel = true;
        }else {

            loadingDialog.setTitle("Account Creation....");
            loadingDialog.setMessage("Please wait while we are checking your credentials");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            validatePhoneNumber(name,phone,pass);

        }
    }

    private void validatePhoneNumber(String name, String phoneNumber, String password){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phoneNumber).exists())){

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name",name);
                    userDataMap.put("phone",phoneNumber);
                    userDataMap.put("password",password);
                    databaseReference.child("Users").child(phoneNumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulation, you have successfully created an account",Toast.LENGTH_SHORT).show();
                                        loadingDialog.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActiviy.class);
                                        startActivity(intent);
                                    }else {
                                        loadingDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this,"Networ Error, Please try Again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    showMessage("Error","Phone Number Already Exist\nPlease try again using another phone number");
                    loadingDialog.dismiss();

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