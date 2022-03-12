package com.elijah.ukeme.ecommerceapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sellers.SellerRegistrationActivity;
import interfaces.showAbleMessage;
import io.paperdb.Paper;
import model.Users;
import prevalent.Prevalent;

public class MainActivity extends AppCompatActivity implements showAbleMessage {

    Button loginButton,SignUpButton;
    private ProgressDialog loadingDialog;
    private ProductDetailsActivity productDetailsActivity;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productDetailsActivity = new ProductDetailsActivity();

        Paper.init(this);
        loadingDialog = new ProgressDialog(this);

        loginButton = findViewById(R.id.main_login_button);
        SignUpButton = findViewById(R.id.join_now_button);
        textView = findViewById(R.id.to_seller_registration);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(v -> login());

        SignUpButton.setOnClickListener(v -> join());

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPassword = Paper.book().read(Prevalent.userPasswordKey);

        if (userPhoneKey !="" && userPassword !=""){
            if (!TextUtils.isEmpty(userPhoneKey)&& !TextUtils.isEmpty(userPassword)){
                allowAccess(userPhoneKey,userPassword);
                loadingDialog.setTitle("Already login");
                loadingDialog.setMessage("Please wait.........");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
            }
        }
    }

    private void allowAccess(String phone, String pass) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()){

                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equalsIgnoreCase(pass)) {
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentUser = usersData;
                            startActivity(intent);
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
    }

    public void login(){
        Intent intent = new Intent(MainActivity.this, LoginActiviy.class);
        startActivity(intent);
    }

    public void join(){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
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