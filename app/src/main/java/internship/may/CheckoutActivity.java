package internship.may;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText name,email,contact,address;
    Spinner city;
    Button payNow;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String[] cityArray = { "Select City", "Ahmedabad", "Vadodara", "Surat", "Rajkot" };
    String sCity;

    RadioGroup paymentType;
    String sPaymentType;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
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

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS ORDER_TABLE (ORDERID INTEGER PRIMARY KEY AUTOINCREMENT,USERID VARCHAR(10),NAME VARCHAR(10),EMAIL VARCHAR(50),CONTACT VARCHAR(10),ADDRESS TEXT,CITY VARCHAR(10),PAYMENT_MODE VARCHAR(10),TRANSACTIONID VARCHAR(20))";
        db.execSQL(orderTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.checkout_name);
        email = findViewById(R.id.checkout_email);
        contact = findViewById(R.id.checkout_contact);
        address = findViewById(R.id.checkout_address);
        city = findViewById(R.id.checkout_city);

        ArrayAdapter adapter = new ArrayAdapter(CheckoutActivity.this, android.R.layout.simple_list_item_1,cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCity = cityArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        payNow = findViewById(R.id.checkout_paynow);

        paymentType = findViewById(R.id.checkout_payment);
        paymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sPaymentType = radioButton.getText().toString();
            }
        });

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().trim().equals("")){
                    name.setError("Name Required");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(contact.getText().toString().trim().equals("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(address.getText().toString().trim().equals("")){
                    address.setError("Address Required");
                }
                else if(city.getSelectedItemPosition()==0){
                    Toast.makeText(CheckoutActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(sp.getString(ConstantSp.BUY_NOW,"").equalsIgnoreCase("")) {
                        if (sPaymentType.equalsIgnoreCase("Cash")) {
                            doOrder(sPaymentType, "");
                        } else {
                            startPayment();
                        }
                    }
                    else{
                        String insertQuery = "INSERT INTO CART VALUES(NULL,'0','"+sp.getString(ConstantSp.USERID,"")+"','"+sp.getString(ConstantSp.PRODUCT_ID,"")+"','1','"+sp.getString(ConstantSp.PRODUCT_NEW_PRICE,"")+"','"+sp.getString(ConstantSp.PRODUCT_NEW_PRICE,"")+"')";
                        db.execSQL(insertQuery);

                        if (sPaymentType.equalsIgnoreCase("Cash")) {
                            doOrder(sPaymentType, "");
                        } else {
                            startPayment();
                        }

                    }
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
            jsonObject.put("amount",String.valueOf(Integer.parseInt(sp.getString(ConstantSp.CART_TOTAL,""))*100));

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
        //Toast.makeText(this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
        doOrder(sPaymentType,s);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("RESPONSE_PAYMENT_ERROR",s);
        Toast.makeText(this, "Order Place Unsuccessfully", Toast.LENGTH_SHORT).show();
    }

    private void doOrder(String sPaymentType, String sTransactionId) {
        String insertQuery = "INSERT INTO ORDER_TABLE VALUES(NULL,'"+sp.getString(ConstantSp.USERID,"")+"','"+name.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+address.getText().toString()+"','"+sCity+"','"+sPaymentType+"','"+sTransactionId+"')";
        db.execSQL(insertQuery);
        Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

        int iOrderId = 0;

        String cartNewQuery = "SELECT * FROM ORDER_TABLE ORDER BY ORDERID DESC LIMIT 1";
        Cursor cartNewCursor = db.rawQuery(cartNewQuery,null);
        if(cartNewCursor.getCount()>0){
            while (cartNewCursor.moveToNext()){
                iOrderId = Integer.parseInt(cartNewCursor.getString(0));
                Log.d("RESPONSE_CART", String.valueOf(iOrderId));
            }
        }

        String updateCartQuery = "UPDATE CART SET ORDERID='"+iOrderId+"' WHERE ORDERID='0' AND USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        db.execSQL(updateCartQuery);

        Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}