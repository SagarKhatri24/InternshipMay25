package internship.may.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import internship.may.ConstantSp;
import internship.may.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    Context context;
    ArrayList<OrderList> arrayList;

    public OrderAdapter(Context context, ArrayList<OrderList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView orderNo,name,email,contact,address,city,total;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.custom_order_no);
            name = itemView.findViewById(R.id.custom_order_name);
            email = itemView.findViewById(R.id.custom_order_email);
            contact = itemView.findViewById(R.id.custom_order_contact);
            address = itemView.findViewById(R.id.custom_order_address);
            city = itemView.findViewById(R.id.custom_order_city);
            total = itemView.findViewById(R.id.custom_order_total);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.orderNo.setText("Order No : "+arrayList.get(position).getOrderId());
        holder.name.setText(arrayList.get(position).getName());
        holder.email.setText(arrayList.get(position).getEmail());
        holder.contact.setText(arrayList.get(position).getContact());
        holder.address.setText(arrayList.get(position).getAddress());
        holder.city.setText(arrayList.get(position).getCity());
        if(arrayList.get(position).getMode().equals("Cash")){
            holder.total.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getTotal()+"-"+arrayList.get(position).getMode());
        }
        else{
            holder.total.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getTotal()+"-"+arrayList.get(position).getMode()+" ("+ arrayList.get(position).getTransactionId() +")");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
