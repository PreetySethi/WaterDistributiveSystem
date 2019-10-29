package com.example.wds.CustomerModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wds.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ForgotPasswordOtpActivity extends AppCompatActivity {
    EditText cus_OTP, cus_NewPsw, cus_ConfPsw;
    Button bChange;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);

        cus_OTP = findViewById(R.id.otp);
        cus_NewPsw = findViewById(R.id.new_psw);
        cus_ConfPsw = findViewById(R.id.conf_psw);
        Bundle bundle = getIntent().getExtras();

        email = bundle.getString("email");

        bChange = findViewById(R.id.change);

        bChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cus_NewPsw.getText().toString().equals(cus_ConfPsw.getText().toString())) {

                    class ChangePasswordAsync extends AsyncTask<String, Void, String> {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            if (s.equals("UPDATED")) {
                                Toast.makeText(ForgotPasswordOtpActivity.this,"Password changed Successfully", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(ForgotPasswordOtpActivity.this, CustomerLoginActivity.class);
                                startActivity(i);
                                finish();

                            }
                            else    Toast.makeText(ForgotPasswordOtpActivity.this,"Wrong Otp", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        protected String doInBackground(String... params) {

                            String urls = getResources().getString(R.string.base_url).concat("resetPassword/");
                            try {
                                URL url = new URL(urls);
                                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setRequestMethod("POST");
                                httpURLConnection.setDoInput(true);
                                httpURLConnection.setDoOutput(true);
                                OutputStream outputStream = httpURLConnection.getOutputStream();
                                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                                String post_Data = URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

                                bufferedWriter.write(post_Data);
                                bufferedWriter.flush();
                                bufferedWriter.close();
                                outputStream.close();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                                String result = "", line = "";
                                while ((line = bufferedReader.readLine()) != null) {
                                    result += line;
                                }
                                return result;
                            } catch (Exception e) {

                                return e.toString();
                            }
                        }
                    }

                    //creating asynctask object and executing it
                    ChangePasswordAsync changePasswordAsync = new ChangePasswordAsync();
                    changePasswordAsync.execute(cus_OTP.getText().toString(),email, cus_NewPsw.getText().toString());
                } else {
                    Toast.makeText(ForgotPasswordOtpActivity.this, "Password Did Not Matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
