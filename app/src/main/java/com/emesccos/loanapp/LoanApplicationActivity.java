package com.emesccos.loanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.emesccos.loanapp.MainActivity.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE;

public class LoanApplicationActivity extends AppCompatActivity {
    public CoordinatorLayout codLayout;
    public Button btnApplyForLoan;
    public Button btnRepayloan;
    ProgressBar progressBar;
    public static String amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);
        codLayout = findViewById(R.id.codLayout);
        btnApplyForLoan = findViewById(R.id.btnApplyForLoan);
        btnRepayloan = findViewById(R.id.btnRepayLoan);

        btnApplyForLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(LoanApplicationActivity.this)) {

                    getAmountFromDialog(LoanApplicationActivity.this);

                } else {

                    Toast.makeText(LoanApplicationActivity.this, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        btnRepayloan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(LoanApplicationActivity.this)) {

                    getAmountToBeRepaid(LoanApplicationActivity.this);

                } else {

                    Toast.makeText(LoanApplicationActivity.this, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        ;

    }

    private String getAmountFromDialog(Context c) {

        try {

            final EditText taskEditText = new EditText(c);
            AlertDialog dialog = new AlertDialog.Builder(c)
                    .setIcon(R.mipmap.emesccos_logo)
                    .setTitle("Loan Application")
                    .setMessage("Please Enter Amount.")
                    .setView(taskEditText)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//
                            try {
                                amount = String.valueOf(taskEditText.getText());

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String loan_limit = prefs.getString("loanLimit", null);

                                Log.e("Loan Limit from Prefs: ", loan_limit);

                                if (Integer.parseInt(amount) > Integer.parseInt(loan_limit)) {

                                    Toast.makeText(LoanApplicationActivity.this, "Sorry, You Can't borrow beyond: " + loan_limit, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                makeJsonObjectRequest(amount);

                            } catch (Exception ex) {

                                ex.printStackTrace();

                            }

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

            return amount;
        } catch (Exception ex) {

            ex.printStackTrace();
            return "";


        }
    }

    private String getAmountToBeRepaid(Context c) {

        try {

            final EditText taskEditText = new EditText(c);
            AlertDialog dialog = new AlertDialog.Builder(c)
                    .setIcon(R.mipmap.emesccos_logo)
                    .setTitle("Loan Repayment")
                    .setMessage("Please Enter Amount.")
                    .setView(taskEditText)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                amount = String.valueOf(taskEditText.getText());

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String loan_limit = prefs.getString("loanLimit", null);

                                Log.e("Loan Limit from Prefs: ", loan_limit);
                                makeJsonObjectRequestRepayLoan(amount);

                            } catch (Exception ex) {

                                ex.printStackTrace();

                            }

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

            return amount;
        } catch (Exception ex) {

            ex.printStackTrace();
            return "";


        }
    }

    private void makeJsonObjectRequest(String amount) {
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loan_limit = prefs.getString("loanLimit", null);

        String deviceIMEI = getDeviceIMEI();

        final HashMap<String, String> params = new HashMap<>();
        params.put("deviceIMEI", deviceIMEI);
        //params.put("phoneNumber", phoneNumber);
        params.put("amount", amount);

        Log.e("REQUEST to SERVER", "GET LOAN Data: " + params.toString());
        JsonObjectRequest req = new JsonObjectRequest(Config.GET_LOAN, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(MainActivity.class.getName(), response.toString());
                        String mStatusCode;
                        String message;
                        try {
                            mStatusCode = response.getString("response");
                            message = response.getString("message");

                            if (mStatusCode.equals("200")) {

                                new MaterialStyledDialog.Builder(LoanApplicationActivity.this)
                                        .setTitle("Successful")
                                        .setDescription(message)
                                        .setIcon(R.mipmap.emesccos_logo)
                                        .setPositiveText("Thank You")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                dialog.dismiss();
                                            }

                                        })

                                        .setNegativeText("Cancel")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                dialog.dismiss();

                                            }
                                        }).show();

                                /// clearInputs();
                                progressBar.setVisibility(View.INVISIBLE);

                                Snackbar snackbar = Snackbar
                                        .make(codLayout, message, Snackbar.LENGTH_LONG)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });


                                snackbar.setActionTextColor(Color.RED);

                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();

                                //Move to loan application class
                                // startActivity(new Intent(LoanApplicationActivity.this, LoanApplicationActivity.class));

                            } else {

                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar snackbar = Snackbar
                                        .make(codLayout, "Sorry...Please Try Again Later!", Snackbar.LENGTH_LONG)
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.INVISIBLE);
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

    private void makeJsonObjectRequestRepayLoan(String amount) {
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loan_limit = prefs.getString("loanLimit", null);

        String deviceIMEI = getDeviceIMEI();

        final HashMap<String, String> params = new HashMap<>();
        params.put("deviceIMEI", deviceIMEI);
        params.put("amount", amount);

        Log.e("REQUEST to SERVER", "REPAY LOAN Data: " + params.toString());
        JsonObjectRequest req = new JsonObjectRequest(Config.REPAY_LOAN, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(MainActivity.class.getName(), response.toString());
                        String mStatusCode;
                        String message;
                        try {
                            mStatusCode = response.getString("response");
                            message = response.getString("message");

                            if (mStatusCode.equals("200")) {

                                new MaterialStyledDialog.Builder(LoanApplicationActivity.this)
                                        .setTitle("Successful")
                                        .setDescription(message)
                                        .setIcon(R.mipmap.emesccos_logo)
                                        .setPositiveText("Thank You")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                dialog.dismiss();
                                            }

                                        })

                                        .setNegativeText("Cancel")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                dialog.dismiss();

                                            }
                                        }).show();

                                progressBar.setVisibility(View.INVISIBLE);

                                Snackbar snackbar = Snackbar
                                        .make(codLayout, message, Snackbar.LENGTH_LONG)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                            }
                                        });


                                snackbar.setActionTextColor(Color.RED);

                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();



                            } else {

                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar snackbar = Snackbar
                                        .make(codLayout, "Sorry...Please Try Again Later!", Snackbar.LENGTH_LONG)
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.INVISIBLE);
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

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String getDeviceIMEI() {

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(LoanApplicationActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoanApplicationActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(LoanApplicationActivity.this,
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

}
