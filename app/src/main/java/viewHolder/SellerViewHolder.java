package viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijah.ukeme.ecommerceapp.R;

import interfaces.ItemClickListener;

public class SellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription,productPrice,productStatus;
    public ImageView productImage;
    public ItemClickListener listener;

    public SellerViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.item_product_name_seller);
        productDescription = itemView.findViewById(R.id.item_product_description_seller);
        productImage = itemView.findViewById(R.id.item_product_image_seller);
        productPrice = itemView.findViewById(R.id.item_product_price_seller);
        productStatus=itemView.findViewById(R.id.item_product_status_seller);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.listener = listener;
    }
}
