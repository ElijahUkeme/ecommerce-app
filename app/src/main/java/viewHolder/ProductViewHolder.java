package viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijah.ukeme.ecommerceapp.R;

import interfaces.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription,productPrice;
    public ImageView productImage;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.item_product_name);
        productDescription = itemView.findViewById(R.id.item_product_description);
        productImage = itemView.findViewById(R.id.item_product_image);
        productPrice = itemView.findViewById(R.id.item_product_price);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
}
