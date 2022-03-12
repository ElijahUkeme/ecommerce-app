package viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijah.ukeme.ecommerceapp.R;

public class DeliveredViewHolder extends RecyclerView.ViewHolder {
    public TextView shipperName, phoneNumber,totalPrice,addressCity,dateTime,dateTime2,receiver;
    public Button deliveredProductBtn;
    public DeliveredViewHolder(@NonNull View itemView) {
        super(itemView);
        shipperName = itemView.findViewById(R.id.delivered_order_user_name);
        phoneNumber = itemView.findViewById(R.id.delivered_order_phone_number);
        totalPrice = itemView.findViewById(R.id.delivered_order_total_price);
        addressCity = itemView.findViewById(R.id.delivered_order_address_and_city);
        dateTime = itemView.findViewById(R.id.ordered_date_and_time);
        dateTime2 = itemView.findViewById(R.id.delivered_order_date_and_time);
        receiver = itemView.findViewById(R.id.receivers_name_textview);
        deliveredProductBtn = itemView.findViewById(R.id.btn_show_all_products_delivered);
    }
}
