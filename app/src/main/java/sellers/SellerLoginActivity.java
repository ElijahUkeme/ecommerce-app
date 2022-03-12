package sellers;

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

import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import interfaces.showAbleMessage;

public class SellerLoginActivity extends AppCompatActivity implements showAbleMessage {

    private EditText emailText,passwordText;
    private Button loginButton;
    private TextView registerTextview;
    boolean cancel = false;
    private ProgressDialog loadingDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        emailText = findViewById(R.id.seller_login_email);
        passwordText = findViewById(R.id.seller_login_password);
        loginButton = findViewById(R.id.seller_login_button);
        registerTextview = findViewById(R.id.seller_not_registered_textview);
        loadingDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerLoginActivity.this,SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void login(){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.trim().equals("")){
            emailText.setError("Please Enter Your Email Address");
            cancel = true;
            emailText.requestFocus();
        }else if (password.trim().equals("")){
            passwordText.setError("Please Enter Your Password");
            cancel = true;
            passwordText.requestFocus();
        }else {

            loadingDialog.setTitle("Login Processing...");
            loadingDialog.setMessage("Please wait while we are checking your credentials");
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loadingDialog.dismiss();
                                Toast.makeText(SellerLoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerLoginActivity.this,SellerHomeActivity.class);
                                startActivity(intent);
                            }else {
                                loadingDialog.dismiss();
                                showMessage("Error","Incorrect Credentials");
                            }
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