package com.elijah.ukeme.ecommerceapp.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elijah.ukeme.ecommerceapp.Activity.Admin.AdminMaintainProductsActivity;
import com.elijah.ukeme.ecommerceapp.Activity.Admin.AdminUserProductActivtity;
import com.elijah.ukeme.ecommerceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import model.Product;
import prevalent.Prevalent;
import viewHolder.ProductViewHolder;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle !=null){
            type = getIntent().getStringExtra("Admin");
        }

        Paper.init(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
                if (!type.equalsIgnoreCase("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.user_profile_username);
        CircleImageView profileImage = headerView.findViewById(R.id.user_profile_image);
        if (!type.equalsIgnoreCase("Admin")){
            username.setText(Prevalent.currentUser.getName());
            Picasso.get().load(Prevalent.currentUser.getImage()).into(profileImage);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productRef.orderByChild("productStatus").equalTo("Approved"),Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Product product) {
                productViewHolder.productName.setText(product.getProduct_name());
                productViewHolder.productDescription.setText(product.getDescription());
                productViewHolder.productPrice.setText("Price N"+product.getPrice());
                //Picasso.get().load(product.getImage()).placeholder(R.drawable.marah).into(productViewHolder.productImage);
                Picasso.get().load(product.getImage()).into(productViewHolder.productImage);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type.equalsIgnoreCase("Admin")){

                            Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                            intent.putExtra("pid",product.getPid());
                            startActivity(intent);

                        }else {

                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid",product.getPid());
                        startActivity(intent);
                    }
                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout,viewGroup,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cart:
                if (!type.equalsIgnoreCase("Admin")) {
                    Intent intoCart = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intoCart);

                    break;
                }

            case R.id.nav_search:
                if (!type.equalsIgnoreCase("Admin")) {
                    Intent intoSearch = new Intent(HomeActivity.this, SearchProductsActivity.class);
                    startActivity(intoSearch);

                    break;
                }

            case R.id.nav_categories:

                break;

            case R.id.nav_settings:
                if (!type.equalsIgnoreCase("Admin")) {
                    Intent intentToSetting = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intentToSetting);

                    break;
                }

            case R.id.nav_logout:
                if (!type.equalsIgnoreCase("Admin")) {
                    Paper.book().destroy();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    break;
                }

            default:

                Toast.makeText(HomeActivity.this,"Wrong selection",Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }
}
