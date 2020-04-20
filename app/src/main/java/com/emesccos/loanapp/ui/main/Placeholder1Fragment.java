package com.emesccos.loanapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.emesccos.loanapp.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class Placeholder1Fragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PageViewModel pageViewModel;
    public static MaterialBetterSpinner averageIncomeSpinner;
    public static MaterialBetterSpinner purposeSpinner;
    private String[] averageMonthlyIncome = {"1,000 - 5,000", "5,000 - 10,000",
            "10,000 - 30,000", "30,000 - 50,000", "500,000 - 1,000,000",};
    private String[] purpose = {"Starting a Business", "Upgrading my Business", "Paying fees and other Bills",
            "Shopping", "Education", "Emergency"};
    private DatePickerEditText datePickerEditText;
    public static CheckBox iHaveAjob;
    public static CheckBox amSelfEmployed;
    public static CheckBox amAStudent;
    public static CheckBox iHaveNoIncome;

    public static RadioGroup useLoanFor;
    public static RadioGroup haveOutstandingLoanRadioGoup;
    public static boolean haveAjob = false;
    public static boolean selfEmployed = false;
    public static boolean student = false;
    public static boolean haveNoIncome = false;
    public static boolean haveOutstandingLoan = false;

    public static Placeholder1Fragment newInstance(int index) {
        Placeholder1Fragment fragment = new Placeholder1Fragment();
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
        View root = inflater.inflate(R.layout.fragment_main1, container, false);

        averageIncomeSpinner = root.findViewById(R.id.averageIncomeSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, averageMonthlyIncome);
        averageIncomeSpinner.setAdapter(adapter);

        iHaveAjob = root.findViewById(R.id.haveAjob);
        amSelfEmployed = root.findViewById(R.id.selfEmployed);
        amAStudent = root.findViewById(R.id.student);
        iHaveNoIncome = root.findViewById(R.id.noIncome);

        haveOutstandingLoanRadioGoup = root.findViewById(R.id.haveOutstandingLoanRadioGroup);

        return root;
    }
}