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

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText cus_email_id;
    Button submit;
    //private static String base_url = "https://zovvo.in/wds/Webservice/customerRegistration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        cus_email_id = (EditText) findViewById(R.id.et_forgot_email);
        submit = (Button) findViewById(R.id.submit_request);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class EmailVerify extends AsyncTask<String, Void, String> {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.trim().equals("SENT")) {


                            Intent i = new Intent(ForgotPasswordActivity.this, ForgotPasswordOtpActivity.class);
                            i.putExtra("cus_email_id",cus_email_id.getText().toString());
                            startActivity(i);
                            Toast.makeText(ForgotPasswordActivity.this, "OTP sent successfully, check your Email", Toast.LENGTH_SHORT).show();
                            finish();

                        } else
                            Toast.makeText(ForgotPasswordActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                    }

                    //
                    @Override
                    protected String doInBackground(String... params) {

                       String urls = getResources().getString(R.string.base_url).concat("generateOtpToResetPassword/");
                        try {
                            URL url = new URL(urls);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                            String post_Data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");

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
                EmailVerify loginUsrObj = new EmailVerify();
                loginUsrObj.execute(cus_email_id.getText().toString());
            }
        });
    }
}
