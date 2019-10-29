package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wds.R;

public class ProductActivity extends AppCompatActivity {
    Button producttwenty, product_one, product_fiveml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        producttwenty = (Button) findViewById(R.id.btn_twentylittre);
       // product_one =(Button) findViewById(R.id.btn_onelittre);
       // product_fiveml =(Button) findViewById(R.id.btn_fiveml);

        producttwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, ProductDetailTwentyActivity.class);
                startActivity(intent);
            }
        });
        /* product_one.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(ProductActivity.this, product_detail_onelitreActivity.class);
                 startActivity(intent);
             }
         });
          product_fiveml.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(ProductActivity.this, ProductDetail_fivemiliActivity.class);
                  startActivity(intent);
              }
          });*/
    }
}
