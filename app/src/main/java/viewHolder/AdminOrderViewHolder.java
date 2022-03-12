package viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijah.ukeme.ecommerceapp.R;

public class AdminOrderViewHolder extends RecyclerView.ViewHolder {
    public TextView shipperName, phoneNumber,totalPrice,addressCity,dateTime;
    public Button viewOrder;
    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        shipperName = itemView.findViewById(R.id.order_user_name);
        phoneNumber = itemView.findViewById(R.id.order_phone_number);
        totalPrice = itemView.findViewById(R.id.order_total_price);
        addressCity = itemView.findViewById(R.id.order_address_and_city);
        dateTime = itemView.findViewById(R.id.order_date_and_time);
        viewOrder = itemView.findViewById(R.id.btn_show_all_products);
    }
}
