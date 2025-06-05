package internship.may;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import internship.may.ui.home.ProductAdapter;
import internship.may.ui.home.ProductList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyHolder> {

    Context context;
    ArrayList<Wishlist> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public WishlistAdapter(Context context, ArrayList<Wishlist> productArrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = productArrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
    }

    @NonNull
    @Override
    public WishlistAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wishlist, parent, false);
        return new WishlistAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name, oldPrice, newPrice, discount, unit;
        ImageView image, wishlist;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_wishlist_name);
            oldPrice = itemView.findViewById(R.id.custom_wishlist_old_price);
            newPrice = itemView.findViewById(R.id.custom_wishlist_new_price);
            discount = itemView.findViewById(R.id.custom_wishlist_discount);
            unit = itemView.findViewById(R.id.custom_wishlist_unit);
            image = itemView.findViewById(R.id.custom_wishlist_image);
            wishlist = itemView.findViewById(R.id.custom_wishlist_wishlist);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getNewPrice());
        holder.discount.setText(arrayList.get(position).getDiscount() + ConstantSp.PERCENTAGE_OFF);
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deletQuery = "DELETE FROM WISHLIST WHERE WISHLISTID='" + arrayList.get(position).getWishlistId() + "'";
                db.execSQL(deletQuery);
                arrayList.remove(position);
                notifyItemChanged(position);
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

