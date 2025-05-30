package internship.may;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    ImageView imageView;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.product_detail_name);
        newPrice = findViewById(R.id.product_detail_new_price);
        oldPrice = findViewById(R.id.product_detail_old_price);
        discount = findViewById(R.id.product_detail_discount);
        unit = findViewById(R.id.product_detail_unit);
        imageView = findViewById(R.id.product_detail_image);
        description = findViewById(R.id.product_detail_description);

        buyNow = findViewById(R.id.product_detail_buy_now);

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
                startPayment();
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