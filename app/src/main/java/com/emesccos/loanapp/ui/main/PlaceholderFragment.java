package com.emesccos.loanapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emesccos.loanapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    public static EditText inputFirstName;
    public static EditText inputIDnumber;
    public DatePickerEditText dateOfBirthEditText;
    public static EditText inputLastName;
    public static EditText inputPhoneN;
    public static EditText inputPassword;
    public static EditText inputEmailAddress;
    public static MaterialBetterSpinner gender;
    public static MaterialBetterSpinner howLong;
    private RadioGroup phoneBelongsToYou;
    public RadioGroup newOrUsedRadioGroup;
    private String[] genderString = {"Male", "Female", "Other"};
    private String[] howLongPhoneUsed = {"less than 1 month", "more than 1 month", "More than 3 months", "6 months", "1 year", "2 years"};

    private RadioButton phoneBelongsToYouButton;
    private RadioButton newOrUsedRadioButton;


    public static String dateOfBirth;

    public static boolean phoneBelongsToYouBool = false;
    public static boolean newOrUsed = false;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        inputFirstName = root.findViewById(R.id.inputFirstName);
        inputLastName = root.findViewById(R.id.inputLastName);
        inputIDnumber = root.findViewById(R.id.inputIDnumber);
        inputPhoneN = root.findViewById(R.id.inputPhoneN);
        dateOfBirthEditText = root.findViewById(R.id.dateOfBirth);
        gender = root.findViewById(R.id.gender);
        howLong = root.findViewById(R.id.hlongSpinner);
        inputEmailAddress = root.findViewById(R.id.inputEmailAddress);
        newOrUsedRadioGroup = root.findViewById(R.id.newOrUsedRadioGroup);
        phoneBelongsToYou = root.findViewById(R.id.phoneBelongsToYou);
        inputPassword = root.findViewById(R.id.inputPassword);

        dateOfBirth = dateOfBirthEditText.getText().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, genderString);
        gender.setAdapter(adapter);

        ArrayAdapter<String> howLongAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, howLongPhoneUsed);
        howLong.setAdapter(howLongAdapter);

        /*
        int selectedId = phoneBelongsToYou.getCheckedRadioButtonId();
        phoneBelongsToYouButton = root.findViewById(selectedId);
*/
        RadioButton belongsToYou = root.findViewById(R.id.yes);
        RadioButton doesntBelngToYou = root.findViewById(R.id.no);

/*
        if (belongsToYou.isChecked()) {

            phoneBelongsToYouBool = true;

        } else {

            phoneBelongsToYouBool = false;

        }


        int selectedRadioButton = newOrUsedRadioGroup.getCheckedRadioButtonId();
        newOrUsedRadioButton = root.findViewById(selectedRadioButton);
*/
        RadioButton gotItNew = root.findViewById(R.id.gotItNew);


        if (gotItNew.isChecked()) {

            newOrUsed = true;
        } else {

            newOrUsed = false;
        }

        return root;
    }
}