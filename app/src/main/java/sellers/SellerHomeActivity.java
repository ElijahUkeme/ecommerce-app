package sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.Activity.Admin.AdminCheckNewProduct;
import com.elijah.ukeme.ecommerceapp.Activity.User.MainActivity;
import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import model.Product;
import viewHolder.ProductViewHolder;
import viewHolder.SellerViewHolder;

public class SellerHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        recyclerView = findViewById(R.id.recycler_view_unverified_product_list);
        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_add:
                        Intent intoCat = new Intent(SellerHomeActivity.this,SellerProductCategoryActivity.class);
                        intoCat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intoCat);
                        break;

                    case R.id.navigation_logout:
                        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(unverifiedProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid())
                        ,Product.class).build();

        FirebaseRecyclerAdapter<Product, SellerViewHolder> adapter = new FirebaseRecyclerAdapter<Product, SellerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerViewHolder holder, int position, @NonNull Product model) {

                holder.productName.setText(model.getProduct_name());
                holder.productDescription.setText(model.getDescription());
                holder.productPrice.setText("Price N"+model.getPrice());
                holder.productStatus.setText("Product Status: "+model.getProductStatus());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.marah).into(holder.productImage);
                Picasso.get().load(model.getImage()).into(holder.productImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productID = model.getPid();
                        CharSequence[] options = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do you want to delete this product?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    deleteProduct(productID);

                                }else if (i==1){

                                }
                            }
                        });
                        builder.show();
                    }
                });


            }

            @NonNull
            @Override
            public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_list,parent,false);
                SellerViewHolder holder = new SellerViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void deleteProduct(String productID){
        unverifiedProductsRef.child(productID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerHomeActivity.this,"Product deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}