package sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.elijah.ukeme.ecommerceapp.R;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, sportsTshirts, femaleDresses,sweathers,glasses,hatsCap,labtops,mobilePhones,
    headsets,watches,bags,shoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);

        tShirts = findViewById(R.id.tshirts);
        sportsTshirts = findViewById(R.id.sports_wear);
        femaleDresses = findViewById(R.id.female_dresses);
        sweathers = findViewById(R.id.sweaters);
        glasses = findViewById(R.id.glasses);
        hatsCap = findViewById(R.id.hats);
        labtops = findViewById(R.id.laptops);
        mobilePhones = findViewById(R.id.mobile_phones);
        headsets = findViewById(R.id.headphones);
        watches = findViewById(R.id.watches);
        bags = findViewById(R.id.purses_bags);
        shoes = findViewById(R.id.shoes);

        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","tShirts");
            startActivity(intent);
        });
        sportsTshirts.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","sportShirts");
            startActivity(intent);
        });
        femaleDresses.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","femaleDresses");
            startActivity(intent);
        });
        sweathers.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","sweaters");
            startActivity(intent);
        });
        glasses.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","glasses");
            startActivity(intent);
        });
        hatsCap.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","hatsCap");
            startActivity(intent);
        });
        labtops.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","labtops");
            startActivity(intent);
        });
        mobilePhones.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","mobilePhones");
            startActivity(intent);
        });
        headsets.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","headSets");
            startActivity(intent);
        });
        watches.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","watches");
            startActivity(intent);
        });
        bags.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","bags");
            startActivity(intent);
        });
        shoes.setOnClickListener(v -> {
            Intent intent = new Intent(SellerProductCategoryActivity.this, SellerAddNewProductActivity.class);
            intent.putExtra("category","shoes");
            startActivity(intent);
        });
    }

}