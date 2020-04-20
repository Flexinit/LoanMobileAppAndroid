package com.emesccos.loanapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.emesccos.loanapp.ui.main.Placeholder1Fragment;
import com.emesccos.loanapp.ui.main.Placeholder2Fragment;
import com.emesccos.loanapp.ui.main.PlaceholderFragment;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.emesccos.loanapp.ui.main.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    public static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 100;
    public static String deviceID;
    public CoordinatorLayout codLayout;
    ProgressBar progressBar;

    public String firstName;
    public String lastName;
    public String id_number;
    public String phoneNumber;
    public String date_Of_Birth;
    public String gender;
    public String how_long;
    public String email;
    public String password;
    public String averageIncome;
    public String purposeOfLoanDescription;
    public boolean phoneBelongsToYou = false;
    private boolean newOrUsedBool = false;
    public boolean iHaveAjob = false;
    public boolean selfEmployed = false;
    public boolean student = false;
    public boolean noIncome = false;
    public boolean haveOutstandingLoan = false;
    public boolean loanForPersonalUse = false;
    public boolean loanForBusinessUse = false;

    private RadioButton phoneBelongsToYouButton;

    private RadioButton outstandingLoan;
    private RadioButton purposeOfLoan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        codLayout = findViewById(R.id.codLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Personal Data
                    firstName = PlaceholderFragment.inputFirstName.getText().toString();
                    lastName = PlaceholderFragment.inputLastName.getText().toString();
                    id_number = PlaceholderFragment.inputIDnumber.getText().toString();
                    phoneNumber = PlaceholderFragment.inputPhoneN.getText().toString();
                    date_Of_Birth = PlaceholderFragment.dateOfBirth;

                    gender = PlaceholderFragment.gender.getText().toString();
                    how_long = PlaceholderFragment.howLong.getText().toString();
                    email = PlaceholderFragment.inputEmailAddress.getText().toString();
                    password = PlaceholderFragment.inputPassword.getText().toString();

                    if (PlaceholderFragment.dateOfBirth == null) {

                        Toast.makeText(MainActivity.this, "Please provide Date of Birth", Toast.LENGTH_LONG).show();

                        return;

                    }

                    if (firstName == null) {

                        Toast.makeText(MainActivity.this, "Please provide Your First Name", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (password == null) {

                        Toast.makeText(MainActivity.this, "Please provide Your Password", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (lastName == null) {

                        Toast.makeText(MainActivity.this, "Please provide Your Last Name", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (id_number == null) {

                        Toast.makeText(MainActivity.this, "Please provide Your ID Number", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (phoneNumber == null) {

                        Toast.makeText(MainActivity.this, "Please provide Your Phone Number", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (gender == null) {

                        Toast.makeText(MainActivity.this, "Please specify Gender", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (how_long == null) {

                        Toast.makeText(MainActivity.this, "How Long have you used the Phone?", Toast.LENGTH_LONG).show();

                        return;

                    }


                    phoneBelongsToYou = PlaceholderFragment.phoneBelongsToYouBool;

/*
                    if (phoneBelongsToYouButton == null) {

                        Toast.makeText(MainActivity.this, "Does the phone belong to You?", Toast.LENGTH_LONG).show();

                        return;

                    }
*/
                    newOrUsedBool = PlaceholderFragment.newOrUsed;


                    //Income Data
                    averageIncome = Placeholder1Fragment.averageIncomeSpinner.getText().toString();

                    if (Placeholder1Fragment.iHaveAjob.isChecked()) {
                        iHaveAjob = true;

                    }

                    if (Placeholder1Fragment.amSelfEmployed.isChecked()) {
                        selfEmployed = true;

                    }

                    if (Placeholder1Fragment.amAStudent.isChecked()) {
                        student = true;

                    }
                    if (Placeholder1Fragment.iHaveNoIncome.isChecked()) {
                        noIncome = true;

                    }


                    int selectedRadio = Placeholder1Fragment.haveOutstandingLoanRadioGoup.getCheckedRadioButtonId();
                    outstandingLoan = findViewById(selectedRadio);

                    if (outstandingLoan.getText() == "YES") {

                        haveOutstandingLoan = true;
                    }

                    if (averageIncome == null) {

                        Toast.makeText(MainActivity.this, "Please give your average Income", Toast.LENGTH_LONG).show();

                        return;

                    }
                    if (student && noIncome && selfEmployed) {

                        Toast.makeText(MainActivity.this, "You cannot select more than 2 Options", Toast.LENGTH_LONG).show();

                        return;

                    }

                    //Purpose of Loan
                    int selectedRadioPurpose = Placeholder2Fragment.useLoanFor.getCheckedRadioButtonId();
                    purposeOfLoan = findViewById(selectedRadioPurpose);

                    if (purposeOfLoan.isChecked()) {

                        loanForPersonalUse = true;
                    } else {

                        loanForBusinessUse = true;
                    }

                    purposeOfLoanDescription = Placeholder2Fragment.purposeSpinner.getText().toString();

                    deviceID = getDeviceIMEI();

                    Log.e("Device IMEI: ", deviceID);

                    final HashMap<String, Object> params = new HashMap<>();
                    params.put("FirstName", firstName);
                    params.put("LastName", lastName)  ;
                    params.put("MobileNumber", phoneNumber);
                    params.put("deviceIMEI", deviceID);
                    params.put("IDNumber", id_number);
                    params.put("DateOfBirth", date_Of_Birth);
                    params.put("Gender", gender);
                    params.put("EmailAddress", email);
                    params.put("phoneIsMine", phoneBelongsToYou);
                    params.put("phoneIsNew", newOrUsedBool);
                    params.put("howLongHaveUsedPhone", how_long);
                    params.put("averageIncome", averageIncome);
                    params.put("haveAjob", iHaveAjob);
                    params.put("selfEmployed", selfEmployed);
                    params.put("haveNoIncome", noIncome);
                    params.put("purposeOfLoan", loanForPersonalUse);
                    params.put("kindOfExpenseForLoan", purposeOfLoanDescription);
                    params.put("anyOutstandingLoan", haveOutstandingLoan);
                    params.put("password", password);


                    new MaterialStyledDialog.Builder(MainActivity.this)
                            .setTitle("Please Confirm.")
                            .setDescription(

                                    "First Name  : " + firstName + "\n" +
                                            "LastName    : " + lastName + "\n" +
                                            "MobileNumber : " + phoneNumber + "\n" +
                                            "IDNumber     : " + id_number + "\n" +
                                            "DOB     : " + date_Of_Birth + "\n" +
                                            "Phone Is Mine : " + phoneBelongsToYou + "\n" +
                                            "Got It new     : " + newOrUsedBool + "\n" +
                                            "Duration     : " + how_long + "\n" +
                                            "Average Income     : " + averageIncome + "\n" +
                                            "Employed     : " + iHaveAjob + "\n" +
                                            "Self Employed     : " + selfEmployed + "\n" +
                                            "No Income     : " + noIncome + "\n" +
                                            "Use     : " + loanForPersonalUse + "\n" +
                                            "Expense   : " + purposeOfLoanDescription + "\n" +
                                            "Outstanding Loan :" + haveOutstandingLoan + "\n" +
                                            "Email: " + email)


                            .setIcon(R.mipmap.emesccos_logo)
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                    if (isNetworkAvailable(getApplicationContext())) {
                                        makeJsonObjectRequest(params);
                                    } else {

                                        Toast.makeText(MainActivity.this, "Sorry,NoInternet Connection", Toast.LENGTH_LONG).show();

                                        Snackbar snackbar = Snackbar
                                                .make(codLayout, "Sorry...No Internet Connectivity!", Snackbar.LENGTH_LONG)
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

                                }


                            }).setNegativeText("Cancel")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                }
                            }).show();


                } catch (Exception ex) {

                    Toast.makeText(MainActivity.this, "Please fill in All the Details", Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }


            }
        });
    }

    private void makeJsonObjectRequest(HashMap<String, Object> data) {
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        String createdBy = prefs.getString("CreatedBy", null);
        String Office = prefs.getString("Office", null);
        String MobileNumber = prefs.getString("MobileNumber", null);

        Log.e("REQUEST to SERVER", "LoanApplicant Data: " + data.toString());
        JsonObjectRequest req = new JsonObjectRequest(Config.LOAN_APPLICATION, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(MainActivity.class.getName(), response.toString());

                        String mStatusCode;
                        String message;

                        try {
                            mStatusCode = response.getString("response");
                            message = response.getString("message");
                            String loanLimit = response.getString("loan_limit");


                            if (mStatusCode.equals("200")) {

                                SharedPreferences preferences = getDefaultSharedPreferences(MainActivity.this);
                                preferences.edit().putString("loanLimit", loanLimit).apply();
                                Log.e("Loan Limit returned ", loanLimit);

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
                                new MaterialStyledDialog.Builder(MainActivity.this)
                                        .setTitle("Successful")
                                        .setDescription(message)
                                        .setIcon(R.mipmap.emesccos_logo)
                                        .setPositiveText("OK")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                                                startActivity(new Intent(MainActivity.this, LoanApplicationActivity.class));
                                            }

                                        })

                                        .setNegativeText("Cancel")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            }
                                        }).show();

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
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
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