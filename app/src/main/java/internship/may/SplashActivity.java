package internship.may;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    SharedPreferences sp;

    String[] nameArray = {"Kilos", "Mobiles", "Fashion", "Electronics", "Home & Furniture", "Appliances", "Flight Bookings", "Beauty, Toys & More", "Two Wheelers"};
    String[] imageArray = {
            "https://rukminim2.flixcart.com/flap/80/80/image/29327f40e9c4d26b.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/22fddf3c7da4c4f4.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0d75b34f7d8fbcb3.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/69c6589653afdb9a.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/ab7e2b022a4587dd.jpg?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/0139228b2f7eb413.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/71050627a56b4693.png?q=100",
            "https://rukminim2.flixcart.com/flap/80/80/image/dff3f7adcf3a90c6.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/80/80/image/05d708653beff580.png?q=100"
    };

    int[] categoryIdArray = {
            1,
            1,
            1,
            1,
            1,
            1,
            1
    };

    String[] kilosNameArray = {
            "Staples",
            "Snacks & Beverages",
            "Packaged Food",
            "Personal & Baby Care",
            "Household Care",
            "Dairy & Eggs",
            "Home & Kitchen"
    };

    String[] kilosImageArray = {
            "https://rukminim2.flixcart.com/flap/128/128/image/50474c.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/9fbd36.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/ac8550.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/7670e2.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/b7ade9.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/8014b1.jpg?q=100",
            "https://rukminim2.flixcart.com/flap/128/128/image/e6e0ecc56771471a.png?q=100"
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
            "Aashirvaad Atta with Multigrains is made with 6 nutritious grains - wheat, maize, oats, soya, channa and psyllium husk. This unique grain blend helps in maintaining normal digestion due to its high fibre content. This multigrain atta also is a source of many essential nutrients. It is high in protein which contributes to the maintenance of muscle mass. It is a source of Vitamin B1 (Thiamine) which contributes to normal nerve and heart function. It also has iron which aids in the formation of haemoglobin. The nutrition benefits are not the only reason to switch to this multigrain flour. Aashirvaad understands the importance of taste and hence makes no compromise on that front as well. Behind every Aashirvaad pack are the Aashirvaad experts who have made the atta with extra care. The careful selection of grains and its proportions in the flour results in the rotis being soft, tasty and fluffy!",
            "Amul Pure Ghee is a vegetarian product made from fresh cream, characterized by a rich aroma and granular texture. It is a good source of energy, vitamins A, D, E, and K, and is often used for various culinary purposes and traditional Ayurvedic practices.",
            "Cashew nuts, are a popular and nutritious dry fruit, widely consumed as a snack, in cooking, and in confectionery. They are known for their kidney-shaped kernel, which is attached to a fleshy, edible structure called the cashew apple. Cashews are a good source of vitamins (E, K, and B6), minerals (phosphorous, zinc, magnesium), and heart-healthy fats, contributing to various health benefits, according to 1mg",
            "Basmati rice is a type of long-grain aromatic rice, known for its distinct nutty flavor, floral aroma, and fluffy texture when cooked. It is a staple in Indian and Pakistani cuisine, popular for its use in dishes like biryani and rice pilaf.",
            "Classic cumin seeds, also known as Jeera, are small, oval, yellowish-brown seeds with a distinctive earthy, warm, and slightly nutty flavor. They are a key ingredient in many cuisines, particularly Indian, Middle Eastern, and Latin American. Cumin seeds are rich in iron and have various culinary and medicinal benefits."
    };

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
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

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.splash_image);
        Glide
                .with(SplashActivity.this)
                .asGif()
                .load("https://cdn.dribbble.com/userupload/32060265/file/original-a3540107913aef547529adf058fece03.gif")
                .placeholder(R.drawable.nature)
                .into(imageView);

        doDataStore();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString(ConstantSp.USERID, "").equals("")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }

    private void doDataStore() {
        for(int i=0;i<nameArray.length;i++) {
            String selectQuery = "SELECT * FROM CATEGORY WHERE NAME='"+nameArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){

            }
            else {
                String insertquery = "INSERT INTO CATEGORY VALUES(NULL,'" + nameArray[i] + "','" + imageArray[i] + "')";
                db.execSQL(insertquery);
            }
        }

        for(int i=0;i<kilosNameArray.length;i++) {
            String selectQuery = "SELECT * FROM SUBCATEGORY WHERE NAME='"+kilosNameArray[i]+"' AND CATEGORYID='"+categoryIdArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){

            }
            else {
                String insertquery = "INSERT INTO SUBCATEGORY VALUES(NULL, '"+categoryIdArray[i]+"' ,'" + kilosNameArray[i] + "','" + kilosImageArray[i] + "')";
                db.execSQL(insertquery);
            }
        }

        for(int i=0;i<productNameArray.length;i++) {
            String selectQuery = "SELECT * FROM PRODUCT WHERE NAME='"+productNameArray[i]+"' AND SUBCATEGORYID='"+subCategoryIdArray[i]+"'";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()>0){

            }
            else {
                String insertquery = "INSERT INTO PRODUCT VALUES(NULL, '"+subCategoryIdArray[i]+"' ,'" + productNameArray[i] + "','" + productImageArray[i] + "', '"+ productOldPriceArray[i] +"', '"+productNewPriceArray[i]+"', '"+productDiscountArray[i]+"', '"+productUnitArray[i]+"','"+productDescArray[i]+"')";
                db.execSQL(insertquery);
            }
        }

    }
}