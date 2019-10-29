package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import com.example.wds.PreferenceHelper;
import com.example.wds.R;
import com.example.wds.SessionManager;
import com.example.wds.Adapter.VolleySingleton;
import com.example.wds.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;



public class CustomerAddressActivity extends AppCompatActivity {
    private TextView tv_fname, authid;
    private PreferenceHelper preferenceHelper;
    private EditText  et_cuszone,et_cusstreet, et_cusbuilding_no, et_cusbuildingname, et_cusfloor, et_cus_unit;
    private Button btn_cus_address_reg, btnlogout;
    private static String address_url = "https://zovvo.in/wds/WebserviceCustomer/addressUpdation";
    SessionManager sessionManager;
    SharedPreferences pref;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_address);





        tv_fname = (TextView) findViewById(R.id.tvfname);
       // btnlogout = (Button) findViewById(R.id.btn);
        pref = getSharedPreferences("Registration", 0);
        // retrieving value from Registration
        String name = pref.getString("Name", null);
        // Now set these value into textview of second activity
        tv_fname.setText("Hii... "+name);


        HashMap<String, String> user = sessionManager.getUserDetail();
        et_cuszone = (EditText) findViewById(R.id.et_zone);
        et_cusstreet = (EditText) findViewById(R.id.et_street);
        et_cusbuilding_no = (EditText) findViewById(R.id.et_building_no);
        et_cusbuildingname = (EditText) findViewById(R.id.et_building_office_name);
        et_cusfloor = (EditText) findViewById(R.id.et_floor);
        et_cus_unit = (EditText) findViewById(R.id.et_flat);
        btn_cus_address_reg= (Button) findViewById(R.id.cus_address_submit) ;
        //authid = (TextView) findViewById(R.id.auth);

         btn_cus_address_reg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    inputValidation();
                 registerWithVolley();

             }
         });
    }
    private void inputValidation() {
        String str_zone = et_cuszone.getText().toString();
        String str_street = et_cusstreet.getText().toString();
        String str_buildno = et_cusbuilding_no.getText().toString();
        String str_buildname = et_cusbuildingname.getText().toString();
        String str_floor = et_cusfloor.getText().toString();
        String str_unit = et_cus_unit.getText().toString();

        if (TextUtils.isEmpty(str_zone)) {
            et_cuszone.setError("Please enter your Zone no");
            et_cuszone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_street)) {
            et_cusstreet.setError("Please enter your Stret no");
            et_cusstreet.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_buildno)) {
            et_cusbuilding_no.setError("Please enter your building no");
            et_cusbuilding_no.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_buildname)) {
            et_cusbuildingname.setError("Please enter your building name");
            et_cusbuildingname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_floor)) {
            et_cusfloor.setError("Please provide your floor no");
            et_cusfloor.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_unit)) {
            et_cus_unit.setError("Please provide your flat/unit no");
            et_cus_unit.requestFocus();
            return;
        }
    }



    private void registerWithVolley(){


        final String zone_no = this.et_cuszone.getText().toString().trim();
        final String street_no = this.et_cusstreet.getText().toString().trim();
        final String bulding_no = this.et_cusbuilding_no.getText().toString().trim();
        final String building_name = this.et_cusbuildingname.getText().toString().trim();
        final String unit_no = this.et_cus_unit.getText().toString().trim();
        final String floor_no = this.et_cusfloor.getText().toString().trim();


        final StringRequest request = new StringRequest(Request.Method.POST, address_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Success", response.toString());
                    VolleyLog.v("Response:%n %s", response.toString());

                    Toast.makeText(CustomerAddressActivity.this,response,Toast.LENGTH_LONG).show();
                    Toast.makeText(CustomerAddressActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                    parseData(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("checkresponse", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(CustomerAddressActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.e("checkerror", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("zone_no",zone_no);
                params.put("street_no",street_no);
                params.put("bulding_no",bulding_no);
                params.put("building_name",building_name);
                params.put("floor_no",floor_no);
                params.put("unit_no",unit_no);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                return getLoginData().toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String, String> params = new HashMap<String, String>();
                Map<String, String> headers = new HashMap<>();
                String authid = (String) SharedPref.getUSER_auth(CustomerAddressActivity.this);
                headers.put("Content-Type", "application/json");
                headers.put("auth_token", authid);
                return headers;
               // params.put("auth_token", "ffcd450a68d036f67ca7f71cced6b4e7");
               // return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
    private JSONObject getLoginData() {
        JSONObject data = new JSONObject();
        try {
            data.putOpt("zone_no", et_cuszone.getText().toString());
            data.putOpt("street_no", et_cusstreet.getText().toString());
            data.putOpt("bulding_no", et_cusbuilding_no.getText().toString());
            data.putOpt("building_name", et_cusbuildingname.getText().toString());
            data.putOpt("floor_no", et_cusfloor.getText().toString());
            data.putOpt("unit_no", et_cus_unit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("success")){
            Intent intent = new Intent(CustomerAddressActivity.this, CustomerLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else {

            Toast.makeText(CustomerAddressActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
        }
    }

   /* You can access this data from other class by

SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(this);
    mResponse = m.getString("Response", "");*/

    /* btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(CustomerAddressActivity.this,CustomerRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CustomerAddressActivity.this.finish();
            }
        });*/
}


