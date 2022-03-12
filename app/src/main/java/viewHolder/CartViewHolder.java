package viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijah.ukeme.ecommerceapp.R;

import interfaces.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productName, productPrice,productQuantity,productSubTotal;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.cart_product_name);
        productPrice = itemView.findViewById(R.id.cart_product_price);
        productQuantity =itemView.findViewById(R.id.cart_product_quantity);
        productSubTotal = itemView.findViewById(R.id.cart_product_price_sub_total);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
