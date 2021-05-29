package com.ahmedalraziki.g_admin_final.FainancialPackage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Classes.DatePickerFragment;
import com.ahmedalraziki.g_admin_final.Classes.Outlay;
import com.ahmedalraziki.g_admin_final.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class FainancialAddOutlay extends Fragment {

    Button btnBack;
    Button btnDate;
    Button btnAddOutlay;

    TextView txtAmount;
    TextView txtDate;
    TextView txtSource;

    Spinner spnTy;

    SharedPreferences prefs = null ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FainancialAddOutlay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fainancial_add_outlay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
        Assigner(view);
        setUpSpinner();
        Clicker();

    }

    private void Assigner(View view){
        btnBack      = view.findViewById(R.id.ffao_btnBack);
        btnDate      = view.findViewById(R.id.ffao_BtnOuDa);
        btnAddOutlay = view.findViewById(R.id.ffao_btnOuAdd);
        txtAmount    = view.findViewById(R.id.ffao_txtOuAm);
        txtDate      = view.findViewById(R.id.ffao_txtOuDa);
        txtSource    = view.findViewById(R.id.ffao_txtOuSo);
        spnTy        = view.findViewById(R.id.ffao_spnOuTy);
    }

    private void Clicker(){
        btnAddOutlay.setOnClickListener(v -> AddOutlay());
        btnDate.setOnClickListener(v -> showDatePicker());
        btnBack.setOnClickListener(v -> {
            Fragment fragment = new FainancialDash();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });
    }

    //Choose Date.
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear ,
                              int dayOfMonth) {
            int month = monthOfYear + 1;
            String datePicked = year + "/" + month  + "/" + dayOfMonth;
            txtDate.setText(datePicked);
        }
    };

    // Adding This Outlay To Database.
    private void AddOutlay(){
    if(ETR()){
        String id      = UUID.randomUUID().toString();
        String date    = txtDate.getText().toString();
        String amount  = txtAmount.getText().toString();
        String fromAny = txtSource.getText().toString();
        String StaffID = prefs.getString("uid","error");
        String Type    = spnTy.getSelectedItem().toString();
        Outlay tempOutlay = new Outlay(id, date, amount, fromAny, StaffID, Type);
        String[] DateSep = txtDate.getText().toString().split("/");
        tempOutlay.setYear(Integer.parseInt(DateSep[0]));
        tempOutlay.setMonth(Integer.parseInt(DateSep[1]));
        tempOutlay.setDay(Integer.parseInt(DateSep[2]));
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = ref1.child("financial").child("outlay");
        ref2.child(id).setValue(tempOutlay);
        }
    }

    // Choose Type.
    private void setUpSpinner(){
        List<String> incomeTypes = new ArrayList<>();
        incomeTypes.add("Store");
        incomeTypes.add("Staff");
        incomeTypes.add("Services");
        incomeTypes.add("Contracted Co.");

        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item_custom1, incomeTypes);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTy.setAdapter(adapterTypes);
    }

    //Every Thing Ready.
    private Boolean ETR(){
        String amount = txtAmount.getText().toString();
        String date   = txtDate.getText().toString();
        String source = txtSource.getText().toString();

        if (!amount.equals("") && !date.equals("date") && !source.equals("")){
            return true;
        } else {
            return false;
        }
    }

}