package com.sobag.beaconplayground;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.view.View.OnClickListener;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Created by bdesh on 7/25/2017.
 */

public class RegisterActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = "RegisterActivity";
    TextView txtRegisterEmail, txtRegisterPassword, txtRegisterPhoneNumber, txtConfirmPassword;
    Button btnRegister;
    String url = "http://reacon.azurewebsites.net/api/Account/Register";
    Person person = new Person();



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtRegisterEmail = (TextView) findViewById(R.id.txtRegisterEmail);
        txtRegisterPassword = (TextView) findViewById(R.id.txtRegisterPassword);
        txtConfirmPassword = (TextView) findViewById(R.id.txtConfirmPassword);
        txtRegisterPhoneNumber = (TextView) findViewById(R.id.txtRegisterPhoneNumber);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);

       // String email = txtRegisterEmail.getText().toString();
       // String password = txtRegisterPassword.getText().toString();
       // String phonenumber = txtRegisterPhoneNumber.getText().toString();
       // String username = txtRegisterUsername.getText().toString();
    }

    public static String POST(String url, Person person){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("UserName", person.getEmail());
            jsonObject.accumulate("Password", person.getPassword());
            jsonObject.accumulate("ConfirmPassword", person.getConfirmpassword());
            jsonObject.accumulate("PhoneNumber", person.getPhonenumber());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();


            // 10. convert inputstream to string
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            }
            else {
                result = "Did not work!";
            }
            Log.d("InputStream", result);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phonenumber) {
        String PHONE_PATTERN = "^(947|07)(\\d{8})$";

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phonenumber);
        return matcher.matches();
    }



    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnRegister:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();

                    person = new Person();
                    person.setPassword(txtRegisterPassword.getText().toString()); //password
                    person.setConfirmPassword(txtConfirmPassword.getText().toString()); // confrrm password
                    person.setEmail(txtRegisterEmail.getText().toString().trim()); //email
                    person.setPhonenumber(txtRegisterPhoneNumber.getText().toString()); // phoneNumber

                    String email = txtRegisterEmail.getText().toString().trim();
                    String phonenumber = txtRegisterPhoneNumber.getText().toString().trim();
                    String password = txtRegisterPassword.getText().toString().trim();
                    String confirmPassword = txtConfirmPassword.getText().toString().trim();

                    if (!isValidEmail(email)) {
                        txtRegisterEmail.setError("Invalid Email");
                    }
                    if (!isValidPhoneNumber(phonenumber)) {
                        txtRegisterPhoneNumber.setError("Invalid Phone Number");
                    }
                    if (!password.equals(confirmPassword)) {
                        txtConfirmPassword.setError("Passwords do not match");
                    }



            else {
                    //call AsynTask to perform network operation on separate thread
                    new HttpAsyncTask().execute(url);

                }
                break;
        }

    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0], person);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
          Intent myIntent = new Intent(RegisterActivity.this,
                    NavigationActivity.class);
            startActivity(myIntent);

        }
    }


    private boolean validate(){
        if(txtRegisterEmail.getText().toString().trim().equals(""))
            return false;
        else if(txtRegisterPassword.getText().toString().trim().equals(""))
            return false;
        else if(txtRegisterPhoneNumber.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
      /*  btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String email = txtRegisterEmail.getText().toString();
                String password = txtRegisterPassword.getText().toString();
                String phonenumber = txtRegisterPhoneNumber.getText().toString();
                String username = txtRegisterUsername.getText().toString();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }



            }
        });
    }
}
*/