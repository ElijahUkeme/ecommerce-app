package com.elijah.ukeme.ecommerceapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import prevalent.Prevalent;

public class ResetPasswordActivity extends AppCompatActivity implements showAbleMessage {
    private String check = "";
    private Button verifyButton;
    private EditText inputPhoneNumber,question1,question2;
    private TextView pageTitle,questionTitle;
    boolean cancel = false;
    private ProgressDialog loadingDialogBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        verifyButton = findViewById(R.id.verify_button);
        inputPhoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        questionTitle = findViewById(R.id.question_title);
        pageTitle = findViewById(R.id.page_title);
        loadingDialogBar = new ProgressDialog(this);
        check = getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();
        inputPhoneNumber.setVisibility(View.GONE);
        if (check.equalsIgnoreCase("settings")){
            pageTitle.setText("Set Questions");
            questionTitle.setText("Please Set Answers to the Following Questions");
            verifyButton.setText("Set");
            displayPreviousAnswers();
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setQuestions();
                }
            });


        }else if (check.equalsIgnoreCase("login")){

            inputPhoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }
    private void setQuestions(){
        String quest1 = question1.getText().toString();
        String quest2 = question2.getText().toString();
        if (quest1.isEmpty()){
            question1.setError("Please Enter Question One");
            cancel = true;
            question1.requestFocus();
        }else if (quest2.isEmpty()){
            question2.setError("Please Enter Question Two");
            cancel = true;
            question2.requestFocus();
        }else {
            loadingDialogBar.setTitle("Security Question");
            loadingDialogBar.setMessage("Please wait while we are setting your Security Questions");
            loadingDialogBar.setCanceledOnTouchOutside(false);
            loadingDialogBar.show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentUser.getPhone());

            HashMap<String,Object> userMap = new HashMap<>();
            userMap.put("question1",quest1);
            userMap.put("question2",quest2);

            reference.child("Security Questions").updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        loadingDialogBar.dismiss();
                        Toast.makeText(ResetPasswordActivity.this,"Security Questions Set Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    private void displayPreviousAnswers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentUser.getPhone());

        reference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String answer1 = snapshot.child("question1").getValue().toString();
                    String answer2 = snapshot.child("question2").getValue().toString();
                    question1.setText(answer1);
                    question2.setText(answer2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void verifyUser(){
        String phone = inputPhoneNumber.getText().toString();
        String quest1 = question1.getText().toString();
        String quest2 = question2.getText().toString();
        if (phone.isEmpty()|| phone.equals("")){
            inputPhoneNumber.setError("Please Enter the phone Number");
            cancel = true;
            inputPhoneNumber.requestFocus();
        }else if (quest1.isEmpty()|| quest1.equals("")){
            question1.setError("Please Enter the Answer to the first Question");
            cancel = true;
            question1.requestFocus();
        }else if (quest2.isEmpty()||quest2.equals("")){
            question2.setError("Please Enter the Answer to the second Question");
            cancel = true;
            question2.requestFocus();
        }else {

            loadingDialogBar.setTitle("Security Question");
            loadingDialogBar.setMessage("Please wait while we are verifying your Security Questions");
            loadingDialogBar.setCanceledOnTouchOutside(false);
            loadingDialogBar.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(phone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    if (snapshot.hasChild("Security Questions")){
                        String answer1 = snapshot.child("Security Questions").child("question1").getValue().toString();
                        String answer2 = snapshot.child("Security Questions").child("question2").getValue().toString();
                        if (!quest1.equalsIgnoreCase(answer1) ) {
                            loadingDialogBar.dismiss();
                            showMessage("Error","Your Answer One does not Correspond with the Answer in the Database");
                        }else if (quest2.equalsIgnoreCase(answer2)) {
                            loadingDialogBar.dismiss();
                            showMessage("Error","Your Answer Two does not  Correspond with the Answer in the Database");
                        }else {


                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("New Password");

                            final EditText newPassword = new EditText(ResetPasswordActivity.this);
                            newPassword.setHint("Enter Your New Password Here");
                            builder.setView(newPassword);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (newPassword.getText().toString().equals("")){
                                        loadingDialogBar.dismiss();
                                        newPassword.setError("Please Enter the New Password");
                                        cancel = true;
                                        newPassword.requestFocus();
                                    }else {
                                        reference.child("password").setValue(newPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            loadingDialogBar.dismiss();
                                                            Toast.makeText(ResetPasswordActivity.this,"Password Reset Successfully",Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ResetPasswordActivity.this,LoginActiviy.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder.show();
                                    }

                                }
                            });

                        }
                    }

                }
                    else {
                        loadingDialogBar.dismiss();
                    showMessage("Error","User with the Phone Number not exist or you did not set security question to your Account. Contact Us on ukemedmet@gmail.com");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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