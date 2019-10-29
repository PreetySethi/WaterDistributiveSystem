package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wds.R;
import com.example.wds.SessionManager;

public class CustomerActivity extends AppCompatActivity {
    ImageView btn_product, btn_purchase, btn_profile, btn_promotion, btn_points;
    private TextView tv_email;
    SessionManager sessionManager;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        tv_email = (TextView) findViewById(R.id.tv_namesh);
        // btnlogout = (Button) findViewById(R.id.btn);
        pref = getSharedPreferences("login", 0);
        // retrieving value from Registration
        String name = pref.getString("Email", null);
        // Now set these value into textview of second activity
        tv_email.setText("Hii... "+name);

       // sessionManager = new SessionManager(this);
       // sessionManager.checkLogin();

        /** for showing name of customer*/
       // cus_fname= findViewById(R.id.login_fname);
       // cus_email_id = findViewById(R.id.login_lname);

     /*   HashMap<String, String> user= sessionManager.getUserDetail();
        String mFname= user.get(sessionManager.FNAME);
        String mEmail = user.get(sessionManager.Email);

        cus_fname.setText(mFname);
        cus_email_id.setText(mEmail);*/



       /*  before session management
       Intent intent = getIntent();
        String extrafname = intent.getStringExtra("cus_fname");
        String extralname = intent.getStringExtra("cus_lname");
         cus_fname.setText(extrafname);
         cus_lname.setText(extralname);*/
         /** for showing name of customer*/


        btn_product= (ImageButton) findViewById(R.id.btn_product);
        btn_purchase  = (ImageButton) findViewById(R.id.btn_purchase);
        btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        btn_promotion = (ImageButton) findViewById(R.id.btn_promotion);
        btn_points = (ImageButton) findViewById(R.id.btn_point);


        btn_product.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iinent= new Intent(CustomerActivity.this, ProductActivity.class);
                startActivity(iinent);
            }
        });
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, CustomerProfileActivity.class);
                startActivity(intent);
            }
        });
        btn_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, PromotionActivity.class);
                startActivity(intent);
            }
        });
        btn_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, PointsActivity.class);
                startActivity(intent);

            }
        });

       /* btnlogout
                onclick
                sessionManager.logout();*/
    }
}
