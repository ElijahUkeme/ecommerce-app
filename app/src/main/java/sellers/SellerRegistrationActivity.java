package sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private EditText name,phone,email,password,address;
    private Button registerButton;
    private TextView textViewLogin;
    boolean cancel = false;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        name= findViewById(R.id.seller_name);
        phone = findViewById(R.id.seller_phone);
        email = findViewById(R.id.seller_email);
        password = findViewById(R.id.seller_password);
        address = findViewById(R.id.seller_address);
        registerButton = findViewById(R.id.seller_registration_button);
        textViewLogin = findViewById(R.id.seller_already_registered_textview);
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new ProgressDialog(this);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {
        String sellerName = name.getText().toString();
        String phoneNumber = phone.getText().toString();
        String sellerEmail = email.getText().toString();
        String sellerPassword = password.getText().toString();
        String sellerAddress = address.getText().toString();

        if (sellerName.isEmpty()){
            name.setError("Please Enter Your Name");
            cancel = true;
            name.requestFocus();
        }else if (phoneNumber.isEmpty()){
            phone.setError("Please Enter Your Phone Number");
            cancel = true;
            phone.requestFocus();
        }
        else if (sellerEmail.isEmpty()){
            email.setError("Please Enter Your Email Address");
            cancel = true;
            email.requestFocus();
        }else if (sellerPassword.isEmpty()){
            password.setError("Please Enter Your Password");
            cancel = true;
            password.requestFocus();
        }else if (sellerAddress.isEmpty()){
            address.setError("Please Enter Your Shop Address");
            cancel = true;
            address.requestFocus();
        }else {
            loadingDialog.setTitle("Registration Processing...");
            loadingDialog.setMessage("Please wait while we are checking your credentials");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            mAuth.createUserWithEmailAndPassword(sellerEmail,sellerPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                final DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user=mAuth.getCurrentUser();
                                String sid = mAuth.getCurrentUser().getUid();

                                HashMap<String,Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("name",sellerName);
                                sellerMap.put("phone",phoneNumber);
                                sellerMap.put("email",sellerEmail);
                                sellerMap.put("password",sellerPassword);
                                sellerMap.put("address",sellerAddress);
                                sellerRef.child("sellers").child(sid)
                                        .updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                    loadingDialog.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this,"Seller Registered Successfully",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                                                    startActivity(intent);
                                                }else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                                                }



                                            }
                                        });

                            }else {

                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    loadingDialog.dismiss();
                                    e.printStackTrace();
                                    Toast.makeText(SellerRegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });

        }
    }
}