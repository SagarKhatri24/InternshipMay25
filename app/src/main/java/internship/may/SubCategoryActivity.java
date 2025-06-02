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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import internship.may.ui.home.CategoryAdapter;
import internship.may.ui.home.CategoryList;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int[] subCategoryIdArray = {1,2,3,4,5,6,7};
    /*int[] categoryIdArray = {
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
    };*/

    ArrayList<CategoryList> arrayList;
    SharedPreferences sp;
    ImageView defaultImage;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub_category);
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

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        defaultImage = findViewById(R.id.sub_category_image);
        Glide
                .with(SubCategoryActivity.this)
                .load("https://cdn.dribbble.com/userupload/20939313/file/original-4f28ca50cef0c3d6068d504eb1adb77b.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(defaultImage);

        recyclerView = findViewById(R.id.sub_category_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM SUBCATEGORY WHERE CATEGORYID='"+sp.getInt(ConstantSp.CATEGORY_ID,0)+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                CategoryList list = new CategoryList();
                list.setSubCategoryId(Integer.parseInt(cursor.getString(0)));
                list.setCategoryId(Integer.parseInt(cursor.getString(1)));
                list.setName(cursor.getString(2));
                list.setImage(cursor.getString(3));
                arrayList.add(list);
            }
            CategoryAdapter adapter = new CategoryAdapter(SubCategoryActivity.this, arrayList,"SubCategory");
            recyclerView.setAdapter(adapter);
        }

        if(arrayList.size()>0){
            recyclerView.setVisibility(VISIBLE);
            defaultImage.setVisibility(GONE);
        }
        else{
            recyclerView.setVisibility(GONE);
            defaultImage.setVisibility(VISIBLE);
        }

    }
}