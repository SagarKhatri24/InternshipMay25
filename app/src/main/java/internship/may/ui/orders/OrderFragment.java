package internship.may.ui.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import internship.may.ConstantSp;
import internship.may.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    SQLiteDatabase db;
    SharedPreferences sp;
    ArrayList<OrderList> arrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = getActivity().openOrCreateDatabase("InternshipMay.db", Context.MODE_PRIVATE,null);

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

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS ORDER_TABLE (ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),NAME VARCHAR(10),EMAIL VARCHAR(50),CONTACT VARCHAR(10),ADDRESS TEXT,CITY VARCHAR(10),PAYMENT_MODE VARCHAR(10),TRANSACTIONID VARCHAR(20))";
        db.execSQL(orderTableQuery);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF,Context.MODE_PRIVATE);

        binding.orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        String selectQuery = "SELECT * FROM ORDER_TABLE WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' ORDER BY ORDERID DESC";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                OrderList list = new OrderList();
                list.setOrderId(cursor.getString(0));
                list.setName(cursor.getString(2));
                list.setEmail(cursor.getString(3));
                list.setContact(cursor.getString(4));
                list.setAddress(cursor.getString(5));
                list.setCity(cursor.getString(6));
                list.setMode(cursor.getString(7));
                list.setTransactionId(cursor.getString(8));

                String cartQuery = "SELECT * FROM CART WHERE ORDERID='"+cursor.getString(0)+"'";
                Cursor cartCursor = db.rawQuery(cartQuery,null);
                int iCartTotal = 0;
                if(cartCursor.getCount()>0){
                    while (cartCursor.moveToNext()){
                        iCartTotal += Integer.parseInt(cartCursor.getString(6));
                    }
                }
                list.setTotal(String.valueOf(iCartTotal));
                arrayList.add(list);
            }
            OrderAdapter adapter = new OrderAdapter(getActivity(),arrayList);
            binding.orderRecycler.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}