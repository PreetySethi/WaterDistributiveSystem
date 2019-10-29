package com.example.wds.DistributionModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wds.R;

public class driverLoginActivity extends AppCompatActivity {
    Button btn_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
       // btn_signin= (Button) findViewById(R.id.btn_dri_login);
       /* btn_signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iinent= new Intent(driverLoginActivity.this,DistributionActivity.class);
                startActivity(iinent);
            }
        });*/
    }
}
