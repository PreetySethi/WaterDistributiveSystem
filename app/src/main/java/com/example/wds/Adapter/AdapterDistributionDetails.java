package com.example.wds.Adapter;
//Adapter for the contactsFragment
//inf
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wds.DistributionModule.DistributionDetails;
import com.example.wds.R;

import java.util.ArrayList;


public class AdapterDistributionDetails extends RecyclerView.Adapter<AdapterDistributionDetails.MyViewHolder>{

    Context context;
    ArrayList<DistributionDetails> profiles;

    public AdapterDistributionDetails(Context c, ArrayList<DistributionDetails> p)
    {
        context = c;
        profiles= p;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.distribution_cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(profiles.get(i).getName());
        myViewHolder.id.setText(profiles.get(i).getId());
        myViewHolder.number.setText(profiles.get(i).getNumber());
       // myViewHolder.btn.setVisibility(View.VISIBLE);

        if(profiles.get(i).getPermission()) {
            myViewHolder.btn.setVisibility(View.VISIBLE);
          //  myViewHolder.onClick(i);
        }

    }

    @Override
    public int getItemCount()
    {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,id,number,quantity,password;
        Button btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           // name = (TextView) itemView.findViewById(R.id.txt_name);
            //password = (TextView) itemView.findViewById(R.id.txt_password);
           // number = (TextView) itemView.findViewById(R.id.txt_number);


           // id = (TextView) itemView.findViewById(R.id.t);

           // btn = (Button) itemView.findViewById(R.id.butCheck);

        }
    }
}
