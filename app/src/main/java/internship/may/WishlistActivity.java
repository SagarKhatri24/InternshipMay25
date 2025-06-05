package internship.may;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

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

public class WishlistActivity extends AppCompatActivity {

    ArrayList<Wishlist> wishlistArrayList;
    RecyclerView recyclerView;
    SharedPreferences sp;
    ImageView defaultImage;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);
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

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        defaultImage = findViewById(R.id.wishlist_image);
        Glide
                .with(WishlistActivity.this)
                .load("https://jalongi.com/public/assets/images/product_not_found.jpeg")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

        recyclerView = findViewById(R.id.wishlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));

        wishlistArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                Wishlist list = new Wishlist();
                list.setWishlistId(Integer.parseInt(cursor.getString(0)));
                list.setProductId(Integer.parseInt(cursor.getString(2)));

                String productQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='"+cursor.getString(2)+"'";
                Cursor productCursor = db.rawQuery(productQuery,null);
                if(productCursor.getCount()>0){
                    while (productCursor.moveToNext()){
                        list.setName(productCursor.getString(2));
                        list.setImage(productCursor.getString(3));
                        list.setOldPrice(productCursor.getString(4));
                        list.setNewPrice(productCursor.getString(5));
                        list.setDiscount(productCursor.getString(6));
                        list.setUnit(productCursor.getString(7));
                        list.setDescription(productCursor.getString(8));
                    }
                }
                else{
                    list.setName("");
                    list.setImage("");
                    list.setOldPrice("0");
                    list.setNewPrice("0");
                    list.setDiscount("0");
                    list.setUnit("");
                    list.setDescription("");
                }

                wishlistArrayList.add(list);
            }
            WishlistAdapter adapter = new WishlistAdapter(WishlistActivity.this,wishlistArrayList, db);
            recyclerView.setAdapter(adapter);
        }

        if(wishlistArrayList.size()>0){
            recyclerView.setVisibility(VISIBLE);
            defaultImage.setVisibility(GONE);
        }
        else{
            recyclerView.setVisibility(GONE);
            defaultImage.setVisibility(VISIBLE);
        }

    }
}