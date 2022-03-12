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
import android.widget.TextView;
import android.widget.Toast;

import sellers.SellerProductCategoryActivity;

import com.elijah.ukeme.ecommerceapp.Activity.Admin.AdminHomeActivity;
import com.elijah.ukeme.ecommerceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import interfaces.showAbleMessage;
import io.paperdb.Paper;
import model.Users;
import prevalent.Prevalent;

public class LoginActiviy extends AppCompatActivity implements showAbleMessage {

    private EditText phoneNumber, password;
    private Button loginButton;
    private TextView forgotPassowrd,adminLink,nonAdminLink;
    private ProgressDialog loadingDialog;
    boolean cancel = false;
    private String parentDbName = "Users";
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);
        loadingDialog = new ProgressDialog(this);

        phoneNumber = findViewById(R.id.input_phone_number);
        password = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login_button);
        forgotPassowrd = findViewById(R.id.forgot_password_textview);
        adminLink = findViewById(R.id.admin_panel_link);
        nonAdminLink = findViewById(R.id.non_admin_panel_link);
        checkBox = findViewById(R.id.remember_me_check_box);


        Paper.init(this);

        loginButton.setOnClickListener(v -> login());
        forgotPassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActiviy.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                nonAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admin";
            }
        });
        nonAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                nonAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }


    private void login(){
        String phone = phoneNumber.getText().toString();
        String pass = password.getText().toString();
        if (phone.isEmpty()){
            phoneNumber.setError("Phone Number required");
            phoneNumber.requestFocus();
            cancel = true;
        }else if (pass.isEmpty()){
            password.setError("password Required");
            password.requestFocus();
            cancel = true;

        }else {
            loadingDialog.setTitle("Login Processing...");
            loadingDialog.setMessage("Please wait while we are checking your credentials");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            performLogin(phone,pass);
        }
    }

    private void performLogin(String phone, String pass) {

        try {


        if (checkBox.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,pass);
        }

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.child(parentDbName).child(phone).exists()){

                   Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);

                   if (usersData.getPhone().equals(phone)) {
                       if (usersData.getPassword().equalsIgnoreCase(pass)) {
                           if (parentDbName.equalsIgnoreCase("Admin")){
                               Toast.makeText(LoginActiviy.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                               loadingDialog.dismiss();
                               Intent intent = new Intent(LoginActiviy.this, AdminHomeActivity.class);
                               startActivity(intent);
                           }else if (parentDbName.equalsIgnoreCase("Users")){
                               Toast.makeText(LoginActiviy.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                               loadingDialog.dismiss();
                               Intent intent = new Intent(LoginActiviy.this, HomeActivity.class);
                               Prevalent.currentUser = usersData;
                               startActivity(intent);
                           }
                       }else {
                           showMessage("Error","Incorrect Password");
                           loadingDialog.dismiss();
                       }
                   }

               }else {
                   showMessage("Error","User Account with the phone Number not exist");
                   loadingDialog.dismiss();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }catch (Exception e){
            Toast.makeText(LoginActiviy.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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