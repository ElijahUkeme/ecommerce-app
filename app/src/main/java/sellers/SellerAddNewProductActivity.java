package sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import interfaces.showAbleMessage;
import model.SellersModel;

public class SellerAddNewProductActivity extends AppCompatActivity implements showAbleMessage {
    private String categoryName,description,price,pName;
    private Button addNewProduct;
    private EditText productName, productDescription, productPrice;
    private ImageView productImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    boolean cancel = false;
    String saveCurrentDate, saveCurrentTime,productRandomKey,downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productRef,sellerRef;
    private ProgressDialog loadingDialog;
    String sName,sEmail,sPhone,sAddress,sid;
    //private FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
    //String sellerPresent = currentUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);
        addNewProduct = findViewById(R.id.add_new_product);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productImage = findViewById(R.id.select_product_image);
        loadingDialog = new ProgressDialog(this);

        categoryName = getIntent().getExtras().get("category").toString();
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("sellers");

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               validateProductDetails();

            }
        });
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            //SellersModel sellersModel = snapshot.getValue(SellersModel.class);
                            sName = snapshot.child("name").getValue(String.class);
                            sEmail = snapshot.child("email").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sid = snapshot.child("sid").getValue().toString();
                            Log.d("Seller","the seller name"+sName);
                            Log.d("Seller","the seller email"+sEmail);
                            Log.d("Seller","the seller phone"+sPhone);
                            Log.d("Seller","the seller Address"+sAddress);
                            Log.d("Seller","the seller id"+sid);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Seller","Error message"+error);

                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }
    private void openGallery() {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent,PICK_IMAGE_REQUEST);
    }
    private void validateProductDetails(){
        description = productDescription.getText().toString();
        pName = productName.getText().toString();
        price = productPrice.getText().toString();

        if (imageUri == null){
            showMessage("Error","Product Image Mandatory");
        }else if (description.isEmpty()) {
            productDescription.setError("Product Description required");
            productDescription.requestFocus();
            cancel = true;
        }else if (pName.isEmpty()){
            productName.setError("Product Name required");
            productName.requestFocus();
            cancel = true;
        }else if (price.isEmpty()){
            productPrice.setError("Product price required");
            productPrice.requestFocus();
            cancel = true;
        }else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        loadingDialog.setTitle("Add Product Processing...");
        loadingDialog.setMessage("Please wait while we are checking your product details");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss &");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;
        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(SellerAddNewProductActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
              loadingDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this,"Product Image uploaded successfully",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this,"Got Product Image Url successfully",Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }


                });
            }
        });
    }
    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",price);
        productMap.put("product_name",pName);
        productMap.put("sellerName",sName);
        productMap.put("sellerEmail",sEmail);
        productMap.put("sellerAddress",sAddress);
        productMap.put("sellerPhone",sPhone);
        productMap.put("sid",sid);
        productMap.put("productStatus","Not Approved");
        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingDialog.dismiss();
                            Intent intent = new Intent(SellerAddNewProductActivity.this, SellerHomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(SellerAddNewProductActivity.this,"Product added successfully",Toast.LENGTH_SHORT).show();
                        }else {
                            loadingDialog.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }

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