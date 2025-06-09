package internship.may;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import internship.may.ui.home.ProductAdapter;
import internship.may.ui.home.ProductList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;
    int iQty, iCartId;

    public CartAdapter(Context context, ArrayList<CartList> productArrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = productArrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart, parent, false);
        return new CartAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name, oldPrice, newPrice, discount, unit, qty;
        ImageView image, delete, plus, minus;
        RelativeLayout cartLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_cart_name);
            oldPrice = itemView.findViewById(R.id.custom_cart_old_price);
            newPrice = itemView.findViewById(R.id.custom_cart_new_price);
            discount = itemView.findViewById(R.id.custom_cart_discount);
            unit = itemView.findViewById(R.id.custom_cart_unit);
            image = itemView.findViewById(R.id.custom_cart_image);
            delete = itemView.findViewById(R.id.custom_cart_delete);

            cartLayout = itemView.findViewById(R.id.custom_cart_cart_layout);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            plus = itemView.findViewById(R.id.custom_cart_plus);
            minus = itemView.findViewById(R.id.custom_cart_minus);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getNewPrice());
        holder.discount.setText(arrayList.get(position).getDiscount() + ConstantSp.PERCENTAGE_OFF);
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        Log.d("RESPONSE_CART", arrayList.get(position).getCartId() + "\n" + arrayList.get(position).getQty());

        holder.qty.setText(String.valueOf(arrayList.get(position).getQty()));

        iQty = arrayList.get(position).getQty();
        iCartId = arrayList.get(position).getCartId();

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCartId = arrayList.get(position).getCartId();
                iQty = arrayList.get(position).getQty();
                iQty += 1;
                int iTotaPrice = iQty * Integer.parseInt(arrayList.get(position).getNewPrice());
                String updateQuery = "UPDATE CART SET QTY='" + iQty + "',TOTALPRICE='" + iTotaPrice + "' WHERE CARTID='" + iCartId + "'";
                db.execSQL(updateQuery);

                CartActivity.iTotal += Integer.parseInt(arrayList.get(position).getNewPrice());
                CartActivity.checkout.setText("Checkout ("+ConstantSp.PRICE_SYMBOL+CartActivity.iTotal+")");
                setCartData(position, iCartId, iQty);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCartId = arrayList.get(position).getCartId();
                iQty = arrayList.get(position).getQty();
                iQty -= 1;
                int iTotaPrice = iQty * Integer.parseInt(arrayList.get(position).getNewPrice());

                CartActivity.iTotal -= Integer.parseInt(arrayList.get(position).getNewPrice());
                CartActivity.checkout.setText("Checkout ("+ConstantSp.PRICE_SYMBOL+CartActivity.iTotal+")");

                if (iQty <= 0) {
                    String deleteQuery = "DELETE FROM CART WHERE CARTID='" + iCartId + "'";
                    db.execSQL(deleteQuery);
                    setCartData(position, 0, 0);
                } else {
                    String updateQuery = "UPDATE CART SET QTY='" + iQty + "',TOTALPRICE='" + iTotaPrice + "' WHERE CARTID='" + iCartId + "'";
                    db.execSQL(updateQuery);
                    setCartData(position, iCartId, iQty);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.iTotal -= (Integer.parseInt(arrayList.get(position).getNewPrice()) * arrayList.get(position).getQty());
                CartActivity.checkout.setText("Checkout ("+ConstantSp.PRICE_SYMBOL+CartActivity.iTotal+")");

                String deleteQuery = "DELETE FROM CART WHERE CARTID='" + iCartId + "'";
                db.execSQL(deleteQuery);
                setCartData(position, 0, 0);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, String.valueOf(arrayList.get(position).getProductId())).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NAME, arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_OLD_PRICE, arrayList.get(position).getOldPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NEW_PRICE, arrayList.get(position).getNewPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DISCOUNT, arrayList.get(position).getDiscount()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_UNIT, arrayList.get(position).getUnit()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE, arrayList.get(position).getImage()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESCRIPTION, arrayList.get(position).getDescription()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    private void setCartData(int position, int iCartId, int iQty) {
        if (iCartId == 0) {
            arrayList.remove(position);
            notifyDataSetChanged();
        } else {
            CartList list = new CartList();
            list.setCartId(iCartId);
            list.setQty(iQty);
            list.setProductId(arrayList.get(position).getProductId());
            list.setName(arrayList.get(position).getName());
            list.setImage(arrayList.get(position).getImage());
            list.setOldPrice(arrayList.get(position).getOldPrice());
            list.setNewPrice(arrayList.get(position).getNewPrice());
            list.setDiscount(arrayList.get(position).getDiscount());
            list.setUnit(arrayList.get(position).getUnit());
            list.setDescription(arrayList.get(position).getDescription());
            arrayList.set(position, list);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

