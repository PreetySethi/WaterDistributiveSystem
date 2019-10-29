package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.wds.PreferenceHelper;
import com.example.wds.R;
import com.example.wds.Adapter.VolleySingleton;
import com.example.wds.SessionManager;
import com.example.wds.SharedPref;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRegisterActivity extends AppCompatActivity {

    PlacesClient placesClient;
    private static  final int REQUEST_LOCATION=1;
    List<Place.Field> placeField = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
    private EditText et_firstname, et_lastname, et_email, et_phone,et_password;
    private Button btn_cus_register;
    private ProgressBar loading;
    TextView loc_latitude;
    TextView loc_longitude;
    TextView active_location;
    LocationManager locationManager;
    private PreferenceHelper preferenceHelper;
    private RequestQueue rQueue;
    SessionManager sessionManager;
    SharedPreferences pref;
    String getId;
    Editor editor;

    private static String register_url = "https://zovvo.in/wds/WebserviceCustomer/registration";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        preferenceHelper = new PreferenceHelper(this);
        sessionManager = new SessionManager(CustomerRegisterActivity.this);

       /* if(preferenceHelper.getIsLogin()){
            Intent intent = new Intent(CustomerRegisterActivity.this, CustomerAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }*/



        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                requestPermission();
                initSearchList();



        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_mobile);
        loc_latitude=(TextView) findViewById(R.id.txt_cus_latitude);
        loc_longitude=(TextView) findViewById(R.id.txt_cus_longitude);
        active_location= (TextView) findViewById(R.id.txt_cus_location);
        btn_cus_register = (Button) findViewById(R.id.btn_cus_signup);

        // creating an shared Preference file for the information to be stored
        // first argument is the name of file and second is the mode, 0 is private mode

        pref = getSharedPreferences("Registration", 0);
        // get editor to edit in file
        editor = pref.edit();

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth_token", "value");
        editor.commit();

      btn_cus_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    inputValidation();
                  RegisterWithVolley();

            }
        });
    }



    //Validating of user not to have null values
    private void inputValidation() {
        String str_fname = et_firstname.getText().toString();
        String str_lname = et_lastname.getText().toString();
        String str_password = et_password.getText().toString();
        String str_email = et_email.getText().toString();
        String str_mob = et_phone.getText().toString();
        String str_loc = active_location.getText().toString();


        if (TextUtils.isEmpty(str_fname)) {
            et_firstname.setError("Please enter your firstname");
            et_firstname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_lname)) {
            et_lastname.setError("Please enter your lastname");
            et_lastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_mob)) {
            et_phone.setError("Please enter your mobile no");
            et_phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_email)) {
            et_email.setError("Please enter your email");
            et_email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_loc)) {
           active_location.setError("Please provide your location");
            active_location.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_password)) {
            et_password.setError("Please provide your location");
            et_password.requestFocus();
            return;
        }
    }
    /*-------------------------------------------------------------------------------------------------------------------*/
    //registration process of user
    private void RegisterWithVolley(){
        final String first_name = this.et_firstname.getText().toString();
        final String last_name = this.et_lastname.getText().toString();
        final String password = this.et_password.getText().toString();
        final String email = this.et_email.getText().toString();
        final String phone = this.et_phone.getText().toString();
        final String loc_latitude =this.loc_latitude.getText().toString();
        final String loc_longitude = this.loc_longitude.getText().toString();
        final String activelocation= this.active_location.getText().toString();

        editor = pref.edit();
        editor.putString("Name", first_name);
        editor.commit();

        StringRequest request = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Success", response.toString());
                    VolleyLog.v("Response:%n %s", response.toString());
                    Toast.makeText(CustomerRegisterActivity.this,response,Toast.LENGTH_LONG).show();
                    Toast.makeText(CustomerRegisterActivity.this,response.toString(),Toast.LENGTH_LONG).show();

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
                Toast.makeText(CustomerRegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put("password",password);
                params.put("active_location",activelocation);
                params.put("loc_latitude", loc_latitude);
                params.put("loc_longitude",loc_longitude);
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
            data.putOpt("password", et_password.getText().toString());
            data.putOpt("active_location", active_location.getText().toString());
            data.putOpt("loc_latitude", loc_latitude.getText().toString());
            data.putOpt("loc_longitude", loc_longitude.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void parseData(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optString("status").equals("success")){
            Toast.makeText(CustomerRegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            String auth = jsonObject.optJSONObject("data").optString("auth_token");

            if (!auth.isEmpty()) {
                //*******Save value through share prefernce*******//
                SharedPref.SaveUSER_auth(auth,CustomerRegisterActivity.this);

            }
            Intent intent = new Intent(CustomerRegisterActivity.this,CustomerAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
            this.finish();
        }else {

            Toast.makeText(CustomerRegisterActivity.this, jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
        }
    }
    private void saveInfo(String response){

        preferenceHelper.putIsLogin(true);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("success")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                   // preferenceHelper.putName(dataobj.getString("name"));
                   // preferenceHelper.putHobby(dataobj.getString("hobby"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

/*-------------------------------------------------------------------------------------------------------------------------------*/


//For Autocomplete Fragment//

    private void requestPermission() {
        Dexter.withActivity(this).withPermissions(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(CustomerRegisterActivity.this,"You must enable this permission",Toast.LENGTH_SHORT);
            }
        }).check();
    }

    private void initSearchList(){
        //Initialize Places. For simplicity, the API key is hard-coded.
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getResources().getString(R.string.places_api_key));
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment=(AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener(){
            @Override
            public void onPlaceSelected(Place place){

                final LatLng latLng = place.getLatLng();
                if(latLng != null) {
                    // TODO: Get info about the selected place.
                    Log.i("placeApi", "Place: " + place.getName() + ", " + place.getId());
                   // Toast.makeText(CustomerRegisterActivity.this, "" + place.getName()+","+place.getId()+","+place.getLatLng(), Toast.LENGTH_SHORT).show();
                    loc_latitude.setText(""+latLng.latitude);
                    loc_longitude.setText(""+latLng.longitude);
                    active_location.setText(place.getName());

                }
            }

            @Override
            public void onError(Status status){
                // TODO: Handle the error.
                Log.i("placeApi","An error occurred: "+status);
            }
        });

        // Create a new Places client instance.
        PlacesClient placesClient=Places.createClient(this);

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token= AutocompleteSessionToken.newInstance();
        // Create a RectangularBounds object.
        RectangularBounds bounds= RectangularBounds.newInstance(
                new LatLng(-33,-168),
                new LatLng(71,136));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request= FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
//                .setLocationRestriction(bounds)
                .setCountry("us")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .build();
    }

}
