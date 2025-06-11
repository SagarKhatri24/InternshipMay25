package internship.may;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    TextView name,newPrice,oldPrice,discount,unit,description,buyNow;
    ImageView imageView,wishlist;

    TextView addItem,qty;
    ImageView plus,minus;
    RelativeLayout cartLayout;

    SharedPreferences sp;
    SQLiteDatabase db;

    boolean isWishlist = false;

    int iQty = 1;
    int iCartId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        db = openOrCreateDatabase("InternshipMay.db",MODE_PRIVATE,null);

        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS (USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),COUNTRY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY (CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(255))";
        db.execSQL(categoryTableQuery);

        String subCategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY (SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(255))";
        db.execSQL(subCategoryTableQuery);

        String productTableQuery = "CREATE TABLE IF NOT EXISTS PRODUCT (PRODUCTID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10), NAME VARCHAR(100),IMAGE VARCHAR(255), OLDPRICE VARCHAR(10),NEWPRICE VARCHAR(10),DISCOUNT VARCHAR(20),UNIT VARCHAR(20),DESCRIPTION TEXT)";
        db.execSQL(productTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST (WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10) , PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART (CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID VARCHAR(10), USERID VARCHAR(10), PRODUCTID VARCHAR(10), QTY VARCHAR(10), PRICE VARCHAR(10), TOTALPRICE VARCHAR(10))";
        db.execSQL(cartTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.product_detail_name);
        newPrice = findViewById(R.id.product_detail_new_price);
        oldPrice = findViewById(R.id.product_detail_old_price);
        discount = findViewById(R.id.product_detail_discount);
        unit = findViewById(R.id.product_detail_unit);
        imageView = findViewById(R.id.product_detail_image);
        description = findViewById(R.id.product_detail_description);

        buyNow = findViewById(R.id.product_detail_buy_now);
        wishlist = findViewById(R.id.product_detail_wishlist);

        addItem = findViewById(R.id.product_detail_add_item);
        qty = findViewById(R.id.product_detail_qty);
        plus = findViewById(R.id.product_detail_plus);
        minus = findViewById(R.id.product_detail_minus);
        cartLayout = findViewById(R.id.product_detail_cart_layout);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME,""));
        newPrice.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_NEW_PRICE,""));
        oldPrice.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_OLD_PRICE,""));
        oldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        discount.setText(sp.getString(ConstantSp.PRODUCT_DISCOUNT,"")+ConstantSp.PERCENTAGE_OFF);
        unit.setText(sp.getString(ConstantSp.PRODUCT_UNIT,""));

        description.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION,""));

        Glide.with(ProductDetailActivity.this).load(sp.getString(ConstantSp.PRODUCT_IMAGE,"")).placeholder(R.mipmap.ic_launcher).into(imageView);

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startPayment();
                sp.edit().putString(ConstantSp.CART_TOTAL, sp.getString(ConstantSp.PRODUCT_NEW_PRICE,"")).commit();
                sp.edit().putString(ConstantSp.BUY_NOW,"Yes").commit();
                Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            isWishlist = true;
            wishlist.setImageResource(R.drawable.wishlist_fill);
        }
        else{
            isWishlist = false;
            wishlist.setImageResource(R.drawable.wishlist_blank);
        }

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWishlist){
                    String deletQuery = "DELETE FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                    db.execSQL(deletQuery);
                    wishlist.setImageResource(R.drawable.wishlist_blank);
                    isWishlist = false;
                }
                else {
                    String insertWishlist = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "')";
                    db.execSQL(insertWishlist);
                    wishlist.setImageResource(R.drawable.wishlist_fill);
                    isWishlist = true;
                }
            }
        });

        String selectCartQuery = "SELECT * FROM CART WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' AND ORDERID='0'";
        Cursor cartCursor = db.rawQuery(selectCartQuery,null);
        if(cartCursor.getCount()>0){
            addItem.setVisibility(GONE);
            cartLayout.setVisibility(VISIBLE);
            while (cartCursor.moveToNext()){
                iCartId = Integer.parseInt(cartCursor.getString(0));
                iQty = Integer.parseInt(cartCursor.getString(4));
                qty.setText(String.valueOf(iQty));
            }
        }
        else{
            addItem.setVisibility(VISIBLE);
            cartLayout.setVisibility(GONE);
        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iTotaPrice = iQty * Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEW_PRICE,""));
                String insertQuery = "INSERT INTO CART VALUES(NULL,'0','"+sp.getString(ConstantSp.USERID,"")+"','"+sp.getString(ConstantSp.PRODUCT_ID,"")+"','"+iQty+"','"+sp.getString(ConstantSp.PRODUCT_NEW_PRICE,"")+"','"+iTotaPrice+"')";
                db.execSQL(insertQuery);

                String cartNewQuery = "SELECT * FROM CART ORDER BY CARTID DESC LIMIT 1";
                Cursor cartNewCursor = db.rawQuery(cartNewQuery,null);
                if(cartNewCursor.getCount()>0){
                    while (cartNewCursor.moveToNext()){
                        iCartId = Integer.parseInt(cartNewCursor.getString(0));
                        Log.d("RESPONSE_CART", String.valueOf(iCartId));
                    }
                }
                qty.setText(String.valueOf(iQty));

                addItem.setVisibility(GONE);
                cartLayout.setVisibility(VISIBLE);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iQty +=1;
                int iTotalPrice = iQty * Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEW_PRICE,""));
                String updateQuery = "UPDATE CART SET QTY = '"+iQty+"',TOTALPRICE='"+iTotalPrice+"' WHERE CARTID = '"+iCartId+"'";
                db.execSQL(updateQuery);
                qty.setText(String.valueOf(iQty));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iQty -=1;
                if(iQty<=0){
                    String deleteQuery = "DELETE FROM CART WHERE CARTID='"+iCartId+"'";
                    db.execSQL(deleteQuery);

                    cartLayout.setVisibility(GONE);
                    addItem.setVisibility(VISIBLE);
                }
                else{
                    int iTotalPrice = iQty * Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEW_PRICE,""));
                    String updateQuery = "UPDATE CART SET QTY = '"+iQty+"',TOTALPRICE='"+iTotalPrice+"' WHERE CARTID = '"+iCartId+"'";
                    db.execSQL(updateQuery);
                    qty.setText(String.valueOf(iQty));
                }
            }
        });

    }

    private void startPayment() {
        Activity activity = this;
        Checkout co = new Checkout();
        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",getResources().getString(R.string.app_name));
            jsonObject.put("description","Product Purchase");
            jsonObject.put("send_sms_hash",true);
            jsonObject.put("allow_rotation",true);
            jsonObject.put("image",R.mipmap.ic_launcher);
            jsonObject.put("currency","INR");
            jsonObject.put("amount",String.valueOf(Integer.parseInt(sp.getString(ConstantSp.PRODUCT_NEW_PRICE,""))*100));

            JSONObject prefill = new JSONObject();
            prefill.put("email",sp.getString(ConstantSp.EMAIL,""));
            prefill.put("contact",sp.getString(ConstantSp.CONTACT,""));

            jsonObject.put("prefill",prefill);
            co.open(activity,jsonObject);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d("RESPONSE_PAYMENT",s);
        Toast.makeText(this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("RESPONSE_PAYMENT_ERROR",s);
        Toast.makeText(this, "Order Place Unsuccessfully", Toast.LENGTH_SHORT).show();
    }
}