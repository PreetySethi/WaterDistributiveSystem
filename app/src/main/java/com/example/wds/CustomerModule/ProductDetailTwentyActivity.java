package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wds.R;
import com.example.wds.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailTwentyActivity extends AppCompatActivity {
    RatingBar ratingBar;
    private static final String TAG = ProductDetailTwentyActivity.class.getSimpleName();
    TextView cus_rating, productname, productbrand, productprice, productbatch, productpacking,productmanufacture;
    Button btnSubmit;
    String getId;
    EditText cus_feedback;
    SessionManager sessionManager;
    private String product_url_read= "";
    private String product_url_edit= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_twenty);
        addListenerOnRatingBar();
        addListenerOnButton();

       // HashMap<String, String> user = sessionManager.getProductDetail();
      //  getId = user.get(sessionManager.ID);

        productname = (TextView)findViewById(R.id.txt_details_product_20);
        productbrand = (TextView) findViewById(R.id.txt_product_brand);
        productprice = (TextView) findViewById(R.id.txt_product_price);
        productbatch = (TextView) findViewById(R.id.txt_product_batch);
        productpacking= (TextView) findViewById(R.id.txt_available_packing);
        productmanufacture= (TextView) findViewById(R.id.txt_manufacture);
        cus_feedback = (EditText) findViewById(R.id.lblFeedback);
    }

    private void getProductDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, product_url_read,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("productDetail", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String product_name = object.getString("product_name").trim();
                                    String product_brand = object.getString("product_brand").trim();
                                    String product_batch = object.getString("product_batch").trim();
                                    String product_packing = object.getString("product_packing").trim();
                                    String product_price =  object.getString("product_price").trim();
                                    String product_manufacture =  object.getString("product_manufacture").trim();

                                    productname.setText(product_name);
                                    productbrand.setText(product_brand);
                                    productbatch.setText(product_batch);
                                    productpacking.setText(product_packing);
                                    productprice.setText(product_price);
                                    productmanufacture.setText(product_manufacture);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailTwentyActivity.this, "Error Reading Detail "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProductDetailTwentyActivity.this, "Error Reading Detail "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("cus_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    @Override
    protected void onResume() {
        super.onResume();
        getProductDetail();
    }
    private void RatingFeedbackDetail() {
        final String cus_rating = this.cus_rating.getText().toString().trim();
        final String cus_feedback = this.cus_feedback.getText().toString().trim();
        final String cus_id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cus_rating",cus_rating);
        params.put("cus_feedback",cus_feedback);


        JsonObjectRequest req = new JsonObjectRequest(product_url_edit, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, product_url_edit,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ProductDetailTwentyActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                //sessionManager.createSession(cus_fname, cus_email_id, cus_id);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailTwentyActivity.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProductDetailTwentyActivity.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cus_rating", cus_rating);
                params.put("cus_feedback", cus_feedback);
                params.put("cus_id", cus_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.add(req);

    }
    public void addListenerOnRatingBar() {

         ratingBar = (RatingBar) findViewById(R.id.ratingBar_twenty);
        cus_rating = (TextView) findViewById(R.id.txtRatingValue_twenty_litre);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                cus_rating.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar_twenty);
        btnSubmit = (Button) findViewById(R.id.btn_rat__twenty_Submit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(ProductDetailTwentyActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
                    RatingFeedbackDetail();

            }

        });
    }
}
