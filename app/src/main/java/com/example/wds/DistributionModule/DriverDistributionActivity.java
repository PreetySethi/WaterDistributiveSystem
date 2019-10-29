package com.example.wds.DistributionModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.wds.DistributionModule.DistributionDetails;
import com.example.wds.R;

import java.util.ArrayList;

public class DriverDistributionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DistributionDetails> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_distribution);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewdistribution);
    }
   //recyclerView.setLayoutManager( new LinearLayoutManager(()));

}
