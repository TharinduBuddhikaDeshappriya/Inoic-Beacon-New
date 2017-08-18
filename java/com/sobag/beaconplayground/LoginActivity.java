package com.sobag.beaconplayground;

/**
 * Created by bdesh on 7/25/2017.
 */

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;





public class LoginActivity extends Activity {


    private static final String LOG_TAG = "LoginActivity";
    private TextView txtEmail, txtPassword;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.acivity_login);

        txtEmail = (TextView) findViewById(R.id.txtLoginEmail);
        txtPassword = (TextView) findViewById(R.id.txtLoginPassword);
        Button ButtonLogin = (Button) findViewById(R.id.btnLogin);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                ProgressDialog dialog = new ProgressDialog(LoginActivity.this); // this = YourActivity
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Logging in... Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if (email.equals("") || password.equals("") ) {
                    Toast.makeText(LoginActivity.this, "Please Fill All The Fields!!!",
                            Toast.LENGTH_LONG).show();
                } else {
                   /* try {
                        dialog.setMessage("Logging in ...");
                        dialog.show();

                        StringRequest strReq = new StringRequest(Method.POST,
                                url, new Response.Listener<String>() {


                            public void onResponse(String response) {
                                dialog.hide();

                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean error = jObj.getBoolean("error");

                                    // Check for error node in json
                                    if (!error) {
                                        // user successfully logged in
                                        // Create login session


                                        // Now store the user in SQLite
                                        String uid = jObj.getString("uid");

                                        JSONObject user = jObj.getJSONObject("user");
                                        String name = user.getString("name");
                                        String email = user.getString("email");
                                        String created_at = user
                                                .getString("created_at");

                                        // Launch main activity
                                        Intent intent = new Intent(LoginActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Error in login. Get the error message
                                        String errorMsg = jObj.getString("error_msg");
                                        Toast.makeText(getApplicationContext(),
                                                errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // JSON error
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Login Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        error.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.hide();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                // Posting parameters to login url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", email);
                                params.put("password", password);

                                return params;
                            }

                        };

                            Intent myIntent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(myIntent);


                            Toast.makeText(LoginActivity.this, "Login Failed !!!",
                                    Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }*/
                }

            }
        });
    }
}
