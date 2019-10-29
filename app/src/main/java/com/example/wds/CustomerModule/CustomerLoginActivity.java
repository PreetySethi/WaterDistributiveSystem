package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wds.PreferenceHelper;
import com.example.wds.R;
import com.example.wds.SessionManager;
import com.example.wds.Adapter.VolleySingleton;
import com.example.wds.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class CustomerLoginActivity extends AppCompatActivity {
    private static final String KEY_EMPTY = "";
    private EditText cus_email_id, cus_password;
    private ProgressBar progressBar;
    private Button btncusregister, btncussignin;
    SessionManager sessionManager;
    private PreferenceHelper preferenceHelper;
    private RequestQueue rQueue;
    private String email;
    private String password;
    TextView forgetpassword;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static String login_url = "https://zovvo.in/wds/WebserviceCustomer/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        sessionManager = new SessionManager(this);
        preferenceHelper = new PreferenceHelper(this);

   /*  if(preferenceHelper.getIsLogin()){
            Intent intent = new Intent(CustomerLoginActivity.this,CustomerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }*/

        btncusregister= (Button) findViewById(R.id.btn_cus_register);
        btncussignin=(Button) findViewById(R.id.btn_cus_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBarlogin);

        cus_email_id = (EditText) findViewById(R.id.et_login_emailid);
        cus_password = (EditText) findViewById(R.id.et_login_password);

        forgetpassword= (TextView) findViewById(R.id.click_forget_password);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iinent= new Intent(CustomerLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(iinent);
            }
        });

        btncusregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iinent= new Intent(CustomerLoginActivity.this, CustomerRegisterActivity.class);
                startActivity(iinent);
            }
        });

        pref = getSharedPreferences("login", 0);
        // get editor to edit in file
        editor = pref.edit();

        btncussignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = cus_email_id.getText().toString().trim();
                password = cus_password.getText().toString().trim();

                if (validateInputs()) {
                   Login();
                }
            }
        });
    }

/*-------------------------------------------------------------------------------------------------------------------*/
    private void Login(){
        final String email = cus_email_id.getText().toString().trim();
        final String password = cus_password.getText().toString().trim();

        editor = pref.edit();
        editor.putString("Email", email);
        editor.commit();

        StringRequest request = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  rQueue.getCache().clear();
                Toast.makeText(CustomerLoginActivity.this,response,Toast.LENGTH_LONG).show();
                Toast.makeText(CustomerLoginActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    parseData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("checkLogin", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerLoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.e("checkLoginError", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);

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
                String authid = (String) SharedPref.getUSER_auth(CustomerLoginActivity.this);
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
            data.putOpt("email",cus_email_id .getText().toString());
            data.putOpt("password", cus_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

  /*  private update()
    {
        JSONObject jObject = new JSONObject();
        try {
            if (jObject.has("error")) {
                String aJsonString = jObject.getString("error");
                Toast.makeText(CustomerLoginActivity.this, aJsonString, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CustomerLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return jObject;
    }*/

    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("success")){

            saveInfo(response);

            Toast.makeText(CustomerLoginActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CustomerLoginActivity.this, CustomerActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
            this.finish();
        }else {

           // Toast.makeText(CustomerLoginActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveInfo(String response){

        preferenceHelper.putIsLogin(true);
        try {

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                        //preferenceHelper.putName(dataobj.getString("name"));
                        //preferenceHelper.putHobby(dataobj.getString("hobby"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*---------------------------------------------------------------------------------------------------------------------*/


    private boolean validateInputs() {
        if(KEY_EMPTY.equals(email)){
            cus_email_id.setError("email cannot be empty");
            cus_email_id.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            cus_password.setError("Password cannot be empty");
            cus_password.requestFocus();
            return false;
        }
        return true;
    }


}
