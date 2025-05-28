package internship.may;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import internship.may.ui.home.CategoryAdapter;
import internship.may.ui.home.CategoryList;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
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

    ArrayList<CategoryList> arrayList;

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

        recyclerView = findViewById(R.id.sub_category_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        arrayList = new ArrayList<>();
        for (int i = 0; i < kilosNameArray.length; i++) {
            CategoryList list = new CategoryList();
            list.setName(kilosNameArray[i]);
            list.setImage(kilosImageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter adapter = new CategoryAdapter(SubCategoryActivity.this, arrayList,"SubCategory");
        recyclerView.setAdapter(adapter);

    }
}