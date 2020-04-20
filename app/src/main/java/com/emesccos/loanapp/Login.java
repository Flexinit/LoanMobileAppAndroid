package com.emesccos.loanapp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.transition.Fade;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.emesccos.loanapp.MainActivity.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE;

public class Login extends AppCompatActivity {

    public EditText username;
    EditText password;
    TextInputLayout input_username;
    TextInputLayout inputPass;
    Button login;
    String createdby;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private SQLiteDatabase db;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        input_username = findViewById(R.id.input_layout_username);
        inputPass = findViewById(R.id.input_layout_password);
        login = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progress_update);
        coordinatorLayout = findViewById(R.id.codLayout);

        // setUpWindowAnimations();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   startActivity(new Intent(Login.this, LoanApplicationActivity.class));
                //slideDown(Login.this);

                try {
                    submitForm();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        // getWindow().setExitTransition(fade);

    }

    private void submitForm() throws JSONException, IOException {
        if (validateName() && validatePassword()) {

            createdby = username.getText().toString();

            if (isNetworkAvailable(this)) {

                makeJsonObjectRequest(createdby, password.getText().toString());

            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Authentication Failure...No Internet Connectivity", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                snackbar.setActionTextColor(Color.RED);

                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.GREEN);
                snackbar.show();
            }


        } else {
            return;
        }
    }

    private boolean validateName() {
        if (username.getText().toString().trim().isEmpty()) {
            input_username.setError("Please Enter a Valid Username");
            requestFocus(username);
            return false;
        } else {
            input_username.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputPass.setError("Please Enter a Valid Password");
            requestFocus(password);
            return false;
        } else {
            inputPass.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void makeJsonObjectRequest(final String user, final String pass) {
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);

        String encryptedPass = encrypt("da0k188qL5OiY3eX", "_VSUrIqGV2pHSye1", pass).trim();
        // user = username.getText().toString();

        HashMap<String, String> params = new HashMap<>();
        params.put("deviceIMEI", getDeviceIMEI());
        params.put("username", user);
        params.put("password", pass);

        JsonObjectRequest req = new JsonObjectRequest(Config.LOGIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ServerLogin Response", response.toString());
                        try {
                            String responseCode = response.getString("response");

                            if (responseCode.equals("200")) {
                                username.setText("");
                                password.setText("");

                                startActivity(new Intent(Login.this, LoanApplicationActivity.class));
                                //  slideDown(Login.this);

                                 /*
                                String Office = response.getString("+Office");
                                String phoneNumber = response.getString("MobileNumber");


                                SharedPreferences preferences = getDefaultSharedPreferences(Login.this);
                                preferences.edit().putString("Office", Office).apply();
                                preferences.edit().putString("MobileNumber", phoneNumber).apply();
                                preferences.edit().putString("CreatedBy", user).apply();


                                dialog.dismiss();



                                if (CredentialsCount(getApplicationContext()) > 0) {
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    username.setText("");
                                    password.setText("");
                                } else {
                                    username.setText("");
                                    password.setText("");
                                    Bundle animation = ActivityOptions.makeCustomAnimation(getApplicationContext(),
                                            R.anim.slide_first_activity, R.anim.translate).toBundle();
                                    startActivity(new Intent(Login.this, ChangePass.class), animation);

                                }
*/

                            } else if (responseCode.equals("101")) {

                                showErrorSnackbar("Wrong Username and or Password");

                                progressBar.setVisibility(View.INVISIBLE);

                                username.setText("");
                                password.setText("");
                            }
                        } catch (JSONException e) {

                            progressBar.setVisibility(View.INVISIBLE);

                            showErrorSnackbar("Authentication Failure");
                            username.setText("");
                            password.setText("");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Login.class.getName(), "Server Error  " + error.getMessage());

                progressBar.setVisibility(View.INVISIBLE);

                showErrorSnackbar("Sorry...There is a technical hitch. Please try again later");

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", "yetu", "p4ssw0rd").getBytes(), Base64.DEFAULT)));
                return headers;
            }


        };

        // Adding request to request queue

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }


    public void showErrorSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            makeJsonObjectRequest(username.getText().toString(), password.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.GREEN);
        snackbar.show();
    }


    private static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public String getDeviceIMEI() {

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(Login.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(Login.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                    // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                deviceUniqueIdentifier = tm.getDeviceId();

            }
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    public void register(View v) {

        startActivity(new Intent(this, MainActivity.class));
      //  AddBeneficiary.slideUp(this);

    }
  /*
    public static void slideDown(Activity activity) {

        activity.overridePendingTransition(R.anim.activity_slide_to_top, R.anim.activity_nothing);

    }

   */
}
