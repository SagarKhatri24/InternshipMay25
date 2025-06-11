package internship.may;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import internship.may.ui.home.ProductAdapter;
import internship.may.ui.home.ProductList;

public class CartActivity extends AppCompatActivity {

    ArrayList<CartList> cartArrayList;
    RecyclerView recyclerView;
    SharedPreferences sp;
    ImageView defaultImage;

    TextView clearCart;
    public static TextView checkout;
    RelativeLayout dataLayout;

    SQLiteDatabase db;

    public static int iTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

        defaultImage = findViewById(R.id.cart_image);
        Glide
                .with(CartActivity.this)
                .load("https://jalongi.com/public/assets/images/product_not_found.jpeg")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

        recyclerView = findViewById(R.id.cart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        clearCart = findViewById(R.id.cart_clear);
        checkout = findViewById(R.id.cart_checkout);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.BUY_NOW,"").commit();
                sp.edit().putString(ConstantSp.CART_TOTAL, String.valueOf(iTotal)).commit();
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        dataLayout = findViewById(R.id.cart_data_layout);

        cartArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM CART WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND ORDERID='0'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                CartList list = new CartList();
                list.setCartId(Integer.parseInt(cursor.getString(0)));
                list.setQty(Integer.parseInt(cursor.getString(4)));

                String selectProductQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='"+cursor.getString(3)+"'";
                Cursor cursorProduct = db.rawQuery(selectProductQuery,null);
                if(cursorProduct.getCount()>0){
                    while (cursorProduct.moveToNext()){
                        list.setProductId(Integer.parseInt(cursorProduct.getString(0)));
                        list.setName(cursorProduct.getString(2));
                        list.setImage(cursorProduct.getString(3));
                        list.setOldPrice(cursorProduct.getString(4));
                        list.setNewPrice(cursorProduct.getString(5));
                        list.setDiscount(cursorProduct.getString(6));
                        list.setUnit(cursorProduct.getString(7));
                        list.setDescription(cursorProduct.getString(8));
                        iTotal += Integer.parseInt(cursorProduct.getString(5)) * Integer.parseInt(cursor.getString(4));
                    }
                }
                cartArrayList.add(list);
            }
            CartAdapter adapter = new CartAdapter(CartActivity.this,cartArrayList, db);
            recyclerView.setAdapter(adapter);

            checkout.setText("Checkout ("+ConstantSp.PRICE_SYMBOL+iTotal+")");

        }

        if(cartArrayList.size()>0){
            dataLayout.setVisibility(VISIBLE);
            defaultImage.setVisibility(GONE);
        }
        else{
            dataLayout.setVisibility(GONE);
            defaultImage.setVisibility(VISIBLE);
        }

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM CART WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND ORDERID='0'";
                db.execSQL(deleteQuery);
                cartArrayList.clear();
                //notifyAll();
                dataLayout.setVisibility(GONE);
                defaultImage.setVisibility(VISIBLE);
            }
        });

    }
}