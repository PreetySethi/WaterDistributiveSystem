package com.example.wds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wds.CustomerModule.CustomerLoginActivity;
import com.example.wds.DistributionModule.driverLoginActivity;

public class login_Activity extends AppCompatActivity {
        Button btn_cus_login, btn_dri_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_cus_login= (Button) findViewById(R.id.btn_customer);
        btn_dri_login= (Button) findViewById(R.id.btn_driver);
        btn_cus_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iinent= new Intent(login_Activity.this, CustomerLoginActivity.class);
                startActivity(iinent);
            }
        });
        btn_dri_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iinent= new Intent(login_Activity.this, driverLoginActivity.class);
                startActivity(iinent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
