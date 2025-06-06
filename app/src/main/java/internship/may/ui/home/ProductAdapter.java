package internship.may.ui.home;

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

import internship.may.ConstantSp;
import internship.may.ProductDetailActivity;
import internship.may.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;
    int iQty,iCartId;

    public ProductAdapter(Context context, ArrayList<ProductList> productArrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = productArrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name,oldPrice,newPrice,discount,unit,addItem,qty;
        ImageView image,wishlist,plus,minus;
        RelativeLayout cartLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_product_name);
            oldPrice = itemView.findViewById(R.id.custom_product_old_price);
            newPrice = itemView.findViewById(R.id.custom_product_new_price);
            discount = itemView.findViewById(R.id.custom_product_discount);
            unit = itemView.findViewById(R.id.custom_product_unit);
            image = itemView.findViewById(R.id.custom_product_image);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);

            cartLayout = itemView.findViewById(R.id.custom_product_cart_layout);
            addItem = itemView.findViewById(R.id.custom_product_add_item);
            qty = itemView.findViewById(R.id.custom_product_qty);
            plus = itemView.findViewById(R.id.custom_product_plus);
            minus = itemView.findViewById(R.id.custom_product_minus);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.oldPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.newPrice.setText(ConstantSp.PRICE_SYMBOL+arrayList.get(position).getNewPrice());
        holder.discount.setText(arrayList.get(position).getDiscount()+ConstantSp.PERCENTAGE_OFF);
        holder.unit.setText(arrayList.get(position).getUnit());

        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        Log.d("RESPONSE_CART",arrayList.get(position).getCartId()+"\n"+arrayList.get(position).getQty());

        if(arrayList.get(position).getCartId()==0){
            holder.cartLayout.setVisibility(GONE);
            holder.addItem.setVisibility(VISIBLE);
        }
        else{
            holder.cartLayout.setVisibility(VISIBLE);
            holder.addItem.setVisibility(GONE);
        }

        holder.qty.setText(String.valueOf(arrayList.get(position).getQty()));

        iQty = arrayList.get(position).getQty();
        iCartId = arrayList.get(position).getCartId();

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iQty = 1;
                int iTotaPrice = iQty * Integer.parseInt(arrayList.get(position).getNewPrice());
                String insertQuery = "INSERT INTO CART VALUES(NULL,'0','"+sp.getString(ConstantSp.USERID,"")+"','"+arrayList.get(position).getProductId()+"','"+iQty+"','"+arrayList.get(position).getNewPrice()+"','"+iTotaPrice+"')";
                db.execSQL(insertQuery);

                String cartNewQuery = "SELECT * FROM CART ORDER BY CARTID DESC LIMIT 1";
                Cursor cartNewCursor = db.rawQuery(cartNewQuery,null);
                if(cartNewCursor.getCount()>0){
                    while (cartNewCursor.moveToNext()){
                        iCartId = Integer.parseInt(cartNewCursor.getString(0));
                        Log.d("RESPONSE_CART", String.valueOf(iCartId));
                    }
                }
                setCartData(position,iCartId,iQty);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCartId = arrayList.get(position).getCartId();
                iQty = arrayList.get(position).getQty();
                iQty +=1;
                int iTotaPrice = iQty * Integer.parseInt(arrayList.get(position).getNewPrice());
                String updateQuery = "UPDATE CART SET QTY='"+iQty+"',TOTALPRICE='"+iTotaPrice+"' WHERE CARTID='"+iCartId+"'";
                db.execSQL(updateQuery);
                setCartData(position,iCartId,iQty);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCartId = arrayList.get(position).getCartId();
                iQty = arrayList.get(position).getQty();
                iQty -=1;
                int iTotaPrice = iQty * Integer.parseInt(arrayList.get(position).getNewPrice());
                if(iQty<=0){
                    String deleteQuery= "DELETE FROM CART WHERE CARTID='"+iCartId+"'";
                    db.execSQL(deleteQuery);
                    setCartData(position, 0 ,0);
                }
                else {
                    String updateQuery = "UPDATE CART SET QTY='" + iQty + "',TOTALPRICE='" + iTotaPrice + "' WHERE CARTID='" + iCartId + "'";
                    db.execSQL(updateQuery);
                    setCartData(position, iCartId, iQty);
                }
            }
        });

        if(arrayList.get(position).isWishlist){
            holder.wishlist.setImageResource(R.drawable.wishlist_fill);
        }
        else{
            holder.wishlist.setImageResource(R.drawable.wishlist_blank);
        }

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isWishlist = false;
                if(arrayList.get(position).isWishlist){
                    String deletQuery = "DELETE FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+arrayList.get(position).getProductId()+"'";
                    db.execSQL(deletQuery);
                    isWishlist = false;
                }
                else{
                    String insertWishlist = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + arrayList.get(position).getProductId() + "')";
                    db.execSQL(insertWishlist);
                    isWishlist = true;
                }
                ProductList list = new ProductList();
                list.setProductId(arrayList.get(position).getProductId());
                list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
                list.setName(arrayList.get(position).getName());
                list.setImage(arrayList.get(position).getImage());
                list.setOldPrice(arrayList.get(position).getOldPrice());
                list.setNewPrice(arrayList.get(position).getNewPrice());
                list.setDiscount(arrayList.get(position).getDiscount());
                list.setUnit(arrayList.get(position).getUnit());
                list.setDescription(arrayList.get(position).getDescription());
                list.setWishlist(isWishlist);
                list.setCartId(arrayList.get(position).getCartId());
                list.setQty(arrayList.get(position).getQty());
                arrayList.set(position,list);
                notifyItemChanged(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, String.valueOf(arrayList.get(position).getProductId())).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NAME,arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_OLD_PRICE,arrayList.get(position).getOldPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NEW_PRICE,arrayList.get(position).getNewPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DISCOUNT,arrayList.get(position).getDiscount()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_UNIT,arrayList.get(position).getUnit()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE,arrayList.get(position).getImage()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESCRIPTION,arrayList.get(position).getDescription()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    private void setCartData(int position, int iCartId, int iQty) {
        ProductList list = new ProductList();
        list.setProductId(arrayList.get(position).getProductId());
        list.setSubCategoryId(arrayList.get(position).getSubCategoryId());
        list.setName(arrayList.get(position).getName());
        list.setImage(arrayList.get(position).getImage());
        list.setOldPrice(arrayList.get(position).getOldPrice());
        list.setNewPrice(arrayList.get(position).getNewPrice());
        list.setDiscount(arrayList.get(position).getDiscount());
        list.setUnit(arrayList.get(position).getUnit());
        list.setDescription(arrayList.get(position).getDescription());
        list.setWishlist(arrayList.get(position).isWishlist);
        list.setCartId(iCartId);
        list.setQty(iQty);
        arrayList.set(position,list);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
