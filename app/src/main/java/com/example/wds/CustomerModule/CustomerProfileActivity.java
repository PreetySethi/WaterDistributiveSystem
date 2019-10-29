package com.example.wds.CustomerModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wds.R;
import com.example.wds.SessionManager;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.wds.Adapter.VolleySingleton;
import com.example.wds.SharedPref;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ViewPager pager;
    private static final String TAG = CustomerProfileActivity.class.getSimpleName(); //getting the info
    private EditText et_firstname, et_lastname, et_email, et_phone, et_zone, et_floor, et_buildno, et_buildname, et_flat, et_street;
    private Button btn_logout, btn_photo_upload;
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "https://zovvo.in/wds/WebserviceCustomer/get_profile";
    private static String URL_EDIT = "https://zovvo.in/wds/WebserviceCustomer/editprofile";
    private static String URL_UPLOAD = "https://zovvo.in/wds/WebserviceCustomer/editphoto";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView cus_profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(null);
        setSupportActionBar(toolbar);

        et_firstname = (EditText) findViewById(R.id.et_update_firstname);
        et_lastname =  (EditText)findViewById(R.id.et_update_lastname);
        et_email = (EditText) findViewById(R.id.et_update_email);
        et_phone = (EditText)findViewById(R.id.et_update_mobile);
        et_zone= (EditText)findViewById(R.id.et_upzone);
        et_floor= (EditText)findViewById(R.id.et_upfloor);
        et_buildno= (EditText)findViewById(R.id.et_upbuilding_no);
        et_buildname= (EditText)findViewById(R.id.et_upbuilding_office_name);
        et_flat= (EditText)findViewById(R.id.et_upflat);
        et_street= (EditText)findViewById(R.id.et_upstreet);


        cus_profile_image = (CircleImageView) findViewById(R.id.profile_image);
        btn_photo_upload = (Button) findViewById(R.id.btn_photo);


        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void getDetails(){
        final StringRequest request = new StringRequest(Request.Method.GET, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Success", response.toString());
                    VolleyLog.v("Response:%n %s", response.toString());

                    Toast.makeText(CustomerProfileActivity.this,response,Toast.LENGTH_LONG).show();
                    Toast.makeText(CustomerProfileActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                    parseviewData(response);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("checkresponse", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(CustomerProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.e("checkerror", "onErrorResponse: " + error.getMessage());
            }
        }) {

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
                String authid = (String) SharedPref.getUSER_auth(CustomerProfileActivity.this);
                headers.put("Content-Type", "application/json");
                headers.put("auth_token", authid);
                return headers;
                // params.put("auth_token", "ffcd450a68d036f67ca7f71cced6b4e7");
                // return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // getUserDetail();
        getDetails();
    }
    private void parseviewData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("success")){
            String first_name = jsonObject.getString("first_name").trim();
            String last_name = jsonObject.getString("last_name").trim();
            String email = jsonObject.getString("email").trim();
            String zone_no = jsonObject.getString("zone_no").trim();
            String street_no = jsonObject.getString("street_no").trim();
            String bulding_no = jsonObject.getString("bulding_no").trim();
            String building_name = jsonObject.getString("building_name").trim();
            String floor_no = jsonObject.getString("floor_no").trim();
            String unit_no = jsonObject.getString("unit_no").trim();
            String phone = jsonObject.getString("phone").trim();



            et_firstname.setText(first_name);
            et_lastname.setText(last_name);
            et_email.setText(email);
            et_phone.setText(phone);
            et_zone.setText(zone_no);
            et_street.setText(street_no);
            et_buildno.setText(bulding_no);
            et_buildname.setText(building_name);
            et_floor.setText(floor_no);
            et_flat.setText(unit_no);


        }else {

            Toast.makeText(CustomerProfileActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /** if (id == R.id.action_settings) {
            return true;
        }**/

        switch (item.getItemId()){
            case R.id.menu_edit:

                et_firstname.setFocusableInTouchMode(true);
                et_firstname.setTextColor(Color.GREEN);

                et_lastname.setFocusableInTouchMode(true);
                et_lastname.setTextColor(Color.GREEN);

                et_phone.setFocusableInTouchMode(true);
                et_phone.setTextColor(Color.GREEN);

                et_email.setFocusableInTouchMode(true);
                et_email.setTextColor(Color.GREEN);

                et_street.setFocusableInTouchMode(true);
                et_street.setTextColor(Color.GREEN);

                et_buildno.setFocusableInTouchMode(true);
                et_buildno.setTextColor(Color.GREEN);

                et_buildname.setFocusableInTouchMode(true);
                et_buildname.setTextColor(Color.GREEN);

                et_zone.setFocusableInTouchMode(true);
                et_zone.setTextColor(Color.GREEN);

                et_floor.setFocusableInTouchMode(true);
                et_floor.setTextColor(Color.GREEN);

                et_flat.setFocusableInTouchMode(true);
                et_flat.setTextColor(Color.GREEN);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_firstname, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_lastname, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_email, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_phone, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_street, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_buildno, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_buildname, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_zone, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_floor, InputMethodManager.SHOW_IMPLICIT);
                imm.showSoftInput(et_flat, InputMethodManager.SHOW_IMPLICIT);


                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                updateProfile();

                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                et_firstname.setFocusableInTouchMode(false);
                et_firstname.setTextColor(Color.GRAY);

                et_lastname.setFocusableInTouchMode(false);
                et_lastname.setTextColor(Color.GRAY);

                et_email.setFocusableInTouchMode(false);
                et_email.setTextColor(Color.GRAY);

                et_phone.setFocusableInTouchMode(false);
                et_phone.setTextColor(Color.GRAY);

                et_zone.setFocusable(false);
                et_zone.setTextColor(Color.GRAY);

                et_street.setFocusable(false);
                et_street.setTextColor(Color.GRAY);

                et_flat.setFocusable(false);
                et_flat.setTextColor(Color.GRAY);

                et_buildno.setFocusable(false);
                et_buildno.setTextColor(Color.GRAY);

                et_buildname.setFocusable(false);
                et_buildname.setTextColor(Color.GRAY);

                et_floor.setFocusable(false);
                et_floor.setTextColor(Color.GRAY);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }


    private void updateProfile(){

        final String first_name = this.et_firstname.getText().toString();
        final String last_name = this.et_lastname.getText().toString();
        final String email = this.et_email.getText().toString();
        final String phone = this.et_phone.getText().toString();
        final String zone_no = this.et_zone.getText().toString();
        final String street_no = this.et_street.getText().toString();
        final String bulding_no = this.et_buildno.getText().toString();
        final String building_name = this.et_buildname.getText().toString();
        final String floor_no = this.et_floor.getText().toString();
        final String unit_no = this.et_flat.getText().toString();


        StringRequest request = new StringRequest(Request.Method.POST, URL_EDIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("Success", response.toString());
                    VolleyLog.v("Response:%n %s", response.toString());
                    Toast.makeText(CustomerProfileActivity.this,response,Toast.LENGTH_LONG).show();
                    Toast.makeText(CustomerProfileActivity.this,response.toString(),Toast.LENGTH_LONG).show();

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
                Toast.makeText(CustomerProfileActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.e("checkerror", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("email",email);
                params.put("phone",phone);
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
                Map<String, String> headers = new HashMap<>();
                String authid = (String) SharedPref.getUSER_auth(CustomerProfileActivity.this);
                headers.put("Content-Type", "application/json");
                headers.put("auth_token", authid);
                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
    private JSONObject getLoginData() {
        JSONObject data = new JSONObject();
        try {
            data.putOpt("first_name", et_firstname.getText().toString());
            data.putOpt("last_name", et_lastname.getText().toString());
            data.putOpt("email", et_email.getText().toString());
            data.putOpt("phone", et_phone.getText().toString());
            data.putOpt("zone_no", et_zone.getText().toString());
            data.putOpt("street_no", et_street.getText().toString());
            data.putOpt("bulding_no", et_buildno.getText().toString());
            data.putOpt("building_name", et_buildname.getText().toString());
            data.putOpt("floor_no", et_floor.getText().toString());
            data.putOpt("unit_no", et_flat.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("success")){
            //Intent intent = new Intent(CustomerProfileActivity.this, CustomerActivity.class);
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           // startActivity(intent);
           // this.finish();
        }else {

            Toast.makeText(CustomerProfileActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
        }
    }

   /* private void saveInfo(String response){

        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("success")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                   // preferenceHelper.putFName(dataobj.getString("first_name"));
                   // preferenceHelper.putLName(dataobj.getString("last_name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                cus_profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            UploadPicture(getId, getStringImage(bitmap));

        }
    }

    private void UploadPicture(final String id, final String photo) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(CustomerProfileActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(CustomerProfileActivity.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(CustomerProfileActivity.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(" cus_id", id);
                params.put("photo", photo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
        } else if (id == R.id.nav_address) {


        } else if (id == R.id.nav_contact) {


        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(CustomerProfileActivity.this, HistoryActivity.class);
            startActivity(intent);

        }else if(id == R.id.nav_logout)
        {
             Intent intent = new Intent(CustomerProfileActivity.this, CustomerLoginActivity.class);
             startActivity(intent);

        }else if(id == R.id.nav_points)
        {

        }
        else if(id == R.id.nav_view)
        {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
