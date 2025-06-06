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

public class ProductActivity extends AppCompatActivity {

    int[] productIdArray = {
            1,2,3,4,5
    };

    int[] subCategoryIdArray = {
            1,
            6,
            2,
            3,
            1
    };

    String[] productNameArray = {
            "AASHIRVAAD Shudh Chakki Atta (Akha Ghauno Lot)",
            "Amul Pure Ghee Ghee Plastic Bottle",
            "OM KAJU Budget Cashews",
            "FORTUNE Everyday Basmati Rice (Basmati Chokha)",
            "Classic Cumin Seeds by Flipkart Grocery"
    };
    String[] productImageArray = {
            "https://rukminim2.flixcart.com/image/280/280/xif0q/flour/j/n/v/-original-imagm7w8jfn29hp2.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/280/280/kkec4280/ghee/1/x/b/1-ghee-12x1-ltr-pet-jar-mason-jar-amul-original-imafzqv6gggbhygv.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/280/280/xif0q/nut-dry-fruit/4/o/1/500-budget-cashew-1-pouch-om-kaju-original-imagr7wfwjmrztjp.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/280/280/kqidx8w0/rice/v/b/l/white-everyday-na-basmati-rice-vacuum-pack-fortune-original-imag4gb3wahjk9yw.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/280/280/kwtkxow0/spice-masala/e/l/v/-original-imag9euhnetnup9c.jpeg?q=70"
    };
    String[] productOldPriceArray = {
            "542",
            "660",
            "600",
            "845",
            "280"
    };
    String[] productNewPriceArray = {
            "463",
            "596",
            "426",
            "845",
            "169"
    };
    String[] productDiscountArray = {
            "14",
            "9",
            "28",
            "0",
            "39"
    };
    String[] productUnitArray = {
            "10 KG",
            "1 L Bottle",
            "500 g",
            "5 kg",
            "500 g"
    };

    String[] productDescArray = {
            "Aashirvaad Atta with Multigrains is made with 6 nutritious grains - wheat, maize, oats, soya, channa and psyllium husk. This unique grain blend helps in maintaining normal digestion due to its high fibre content. This multigrain atta also is a source of many essential nutrients. It is high in protein which contributes to the maintenance of muscle mass. It is a source of Vitamin B1 (Thiamine) which contributes to normal nerve and heart function. It also has iron which aids in the formation of haemoglobin. The nutrition benefits are not the only reason to switch to this multigrain flour. Aashirvaad understands the importance of taste and hence makes no compromise on that front as well. Behind every Aashirvaad pack are the Aashirvaad experts who've made the atta with extra care. The careful selection of grains and its proportions in the flour results in the rotis being soft, tasty and fluffy!",
            "Amul Pure Ghee is a vegetarian product made from fresh cream, characterized by a rich aroma and granular texture. It's a good source of energy, vitamins A, D, E, and K, and is often used for various culinary purposes and traditional Ayurvedic practices.",
            "Cashew nuts, are a popular and nutritious dry fruit, widely consumed as a snack, in cooking, and in confectionery. They are known for their kidney-shaped kernel, which is attached to a fleshy, edible structure called the cashew apple. Cashews are a good source of vitamins (E, K, and B6), minerals (phosphorous, zinc, magnesium), and heart-healthy fats, contributing to various health benefits, according to 1mg",
            "Basmati rice is a type of long-grain aromatic rice, known for its distinct nutty flavor, floral aroma, and fluffy texture when cooked. It's a staple in Indian and Pakistani cuisine, popular for its use in dishes like biryani and rice pilaf.",
            "Classic cumin seeds, also known as Jeera, are small, oval, yellowish-brown seeds with a distinctive earthy, warm, and slightly nutty flavor. They are a key ingredient in many cuisines, particularly Indian, Middle Eastern, and Latin American. Cumin seeds are rich in iron and have various culinary and medicinal benefits."
    };

    ArrayList<ProductList> productArrayList;
    RecyclerView recyclerView;
    SharedPreferences sp;
    ImageView defaultImage;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
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

        defaultImage = findViewById(R.id.product_image);
        Glide
                .with(ProductActivity.this)
                .load("https://jalongi.com/public/assets/images/product_not_found.jpeg")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

        recyclerView = findViewById(R.id.product_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));

        productArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM PRODUCT WHERE SUBCATEGORYID='"+sp.getInt(ConstantSp.SUB_CATEGORY_ID,0)+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                ProductList list = new ProductList();
                list.setProductId(Integer.parseInt(cursor.getString(0)));
                list.setSubCategoryId(Integer.parseInt(cursor.getString(1)));
                list.setName(cursor.getString(2));
                list.setImage(cursor.getString(3));
                list.setOldPrice(cursor.getString(4));
                list.setNewPrice(cursor.getString(5));
                list.setDiscount(cursor.getString(6));
                list.setUnit(cursor.getString(7));
                list.setDescription(cursor.getString(8));

                String selectWishlistQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+cursor.getString(0)+"'";
                Cursor cursorWishlist = db.rawQuery(selectWishlistQuery,null);
                if(cursorWishlist.getCount()>0){
                    list.setWishlist(true);
                }
                else{
                    list.setWishlist(false);
                }

                String selectCartQuery = "SELECT * FROM CART WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' AND PRODUCTID='"+cursor.getString(0)+"' AND ORDERID='0'";
                Cursor cursorCart = db.rawQuery(selectCartQuery,null);
                if(cursorCart.getCount()>0){
                    while (cursorCart.moveToNext()){
                        list.setCartId(Integer.parseInt(cursorCart.getString(0)));
                        list.setQty(Integer.parseInt(cursorCart.getString(4)));
                    }
                }
                else{
                    list.setCartId(0);
                    list.setQty(0);
                }

                productArrayList.add(list);
            }
            ProductAdapter adapter = new ProductAdapter(ProductActivity.this,productArrayList, db);
            recyclerView.setAdapter(adapter);
        }

        if(productArrayList.size()>0){
            recyclerView.setVisibility(VISIBLE);
            defaultImage.setVisibility(GONE);
        }
        else{
            recyclerView.setVisibility(GONE);
            defaultImage.setVisibility(VISIBLE);
        }

    }
}