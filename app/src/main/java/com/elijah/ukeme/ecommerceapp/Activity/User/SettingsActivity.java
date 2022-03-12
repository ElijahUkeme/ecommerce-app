package com.elijah.ukeme.ecommerceapp.Activity.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import prevalent.Prevalent;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private EditText fullName,address,phoneNumber;
    private TextView saveProfile, closeProfile,profileChange;
    private Uri imageUri;
    private String myUri = "";
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    boolean cancel = false;
    String userFulName,userAddress,userPhoneNumber;
    private StorageTask uploadTask;
    private Button securityQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImage = findViewById(R.id.settings_profile_image_circle);
        fullName = findViewById(R.id.settings_full_name);
        address = findViewById(R.id.settings_address);
        phoneNumber = findViewById(R.id.settings_phone_number);
        saveProfile = findViewById(R.id.update_settings_textview);
        closeProfile = findViewById(R.id.close_settings_textview);
        profileChange = findViewById(R.id.settings_profile_image_change);
        securityQuestionButton = findViewById(R.id.security_questions_button);

        userInfoDisplay(profileImage,fullName,phoneNumber,address);
        closeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        securityQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else {
                    updateOnlyUserInfo();
                }
            }
        });
        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data !=null && data.getData()!=null){
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //imageUri = result.getUri();
           // profileImage.setImageURI(imageUri);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();
                    profileImage.setImageURI(imageUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(SettingsActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }else {
            Toast.makeText(SettingsActivity.this,"Error, Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
        }
        }catch (Exception e){
            Toast.makeText(SettingsActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }


    private void userInfoSaved(){
        userPhoneNumber=phoneNumber.getText().toString();
        userFulName=fullName.getText().toString();
        userAddress=address.getText().toString();
        if (userPhoneNumber.isEmpty()){
            phoneNumber.setError("Please Enter your Phone Number");
            cancel = true;
            phoneNumber.requestFocus();
        }else if (userFulName.isEmpty()){
            fullName.setError("Please Enter your full Name");
            cancel= true;
            fullName.requestFocus();
        }
            else if (userAddress.isEmpty()){
            address.setError("Please Enter your Address");
            cancel = true;
            address.requestFocus();

        }else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri !=null){
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentUser.getPhone()+".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri>task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name",userFulName);
                        userMap.put("address",userAddress);
                        userMap.put("phoneOrder",userPhoneNumber);
                        userMap.put("image",myUri);
                        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profile Information Updated Successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(SettingsActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }else {
            Toast.makeText(SettingsActivity.this,"Profile Image Not Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name",userFulName);
        userMap.put("address",userAddress);
        userMap.put("phoneOrder",userPhoneNumber);
        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Information Updated Successfully",Toast.LENGTH_SHORT).show();
        finish();


    }

    private void userInfoDisplay(CircleImageView profileImage, EditText fullName, EditText phoneNumber, EditText userAddress) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImage);
                        fullName.setText(name);
                        phoneNumber.setText(phone);
                        userAddress.setText(address);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}