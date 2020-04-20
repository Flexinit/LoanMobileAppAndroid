package com.emesccos.loanapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emesccos.loanapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class Placeholder2Fragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    public static EditText mainIncome;
    public static EditText lastIncome;
    public static DatePickerEditText dateOfEmployment;
    public static MaterialBetterSpinner purposeSpinner;
    public static RadioGroup useLoanFor;
    public static RadioGroup haveOutstandingLoanRadioGoup;
    private String[] purpose = {"Starting a Business", "Upgrading my Business", "Paying fees and other Bills",
            "Shopping", "Education", "Medical", "Emergency"};



    public static Placeholder2Fragment newInstance(int index) {
        Placeholder2Fragment fragment = new Placeholder2Fragment();
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
        View root = inflater.inflate(R.layout.fragment_main2, container, false);

        mainIncome = root.findViewById(R.id.mainIncome);
        dateOfEmployment = root.findViewById(R.id.dateOfEmployment);
        lastIncome = root.findViewById(R.id.lastIncome);
        purposeSpinner = root.findViewById(R.id.purposeSpinner);

        ArrayAdapter<String> purposeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, purpose);
        purposeSpinner.setAdapter(purposeAdapter);
        useLoanFor = root.findViewById(R.id.purposeForLoanRadioGroup);
        haveOutstandingLoanRadioGoup = root.findViewById(R.id.haveOutstandingLoanRadioGroup);

        return root;
    }
}