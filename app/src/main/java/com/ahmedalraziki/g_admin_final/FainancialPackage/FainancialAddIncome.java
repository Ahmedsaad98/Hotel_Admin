package com.ahmedalraziki.g_admin_final.FainancialPackage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Classes.DatePickerFragment;
import com.ahmedalraziki.g_admin_final.Classes.Income;
import com.ahmedalraziki.g_admin_final.Classes.Reservation;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FainancialAddIncome extends Fragment {

    EditText amountEt;
    TextView dateTv;
    Button   dateBtn;
    EditText sourceEt;
    Button   aiBtn;
    Button   syncResBtn;
    Button   backBtn;
    Spinner  typeSpn;
    ConstraintLayout rootLayout;
    List<Reservation> reservations;
    SharedPreferences prefs = null ;

    public FainancialAddIncome() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fainancial_add_income, container, false);
        rootLayout = root.findViewById(R.id.ffai_View);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
        reservations = new ArrayList<>();
        Assigner(view);
        Clicker();
        setUpSpinner();
    }

    private void Assigner(View view){
        amountEt = view.findViewById(R.id.ffai_txtInAm);
        dateTv = view.findViewById(R.id.ffai_txtInDa);
        dateBtn = view.findViewById(R.id.ffai_btnInDate);
        sourceEt = view.findViewById(R.id.ffai_txtInFa);
        aiBtn = view.findViewById(R.id.ffai_btnInAdd);
        syncResBtn = view.findViewById(R.id.ffai_btnSync);
        backBtn = view.findViewById(R.id.ffai_btnBack);
        typeSpn = view.findViewById(R.id.ffai_spnInTy);
    }

    private void Clicker(){
        dateBtn.setOnClickListener(v -> {
            showDatePicker();
        });

        aiBtn.setOnClickListener(v -> {
            AddIncome();
        });

        syncResBtn.setOnClickListener(v -> {
            resSync();
        });

        backBtn.setOnClickListener(v ->{
            Fragment fragment = new FainancialDash();
            FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fainancial, fragment).commit();
        });
    }

    //Add Income !
    private void AddIncome(){
        String staffId = prefs.getString("uid","error");
        String it = typeSpn.getSelectedItem().toString();

        if (ETR()){
        Income income = new Income(UUID.randomUUID().toString(), dateTv.getText().toString(),
                amountEt.getText().toString(), sourceEt.getText().toString(),staffId,it);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = ref1.child("financial").child("income").child(income.getId());
        ref2.setValue(income);
        clearAfterAdding();
        } else {
            Snackbar.make(rootLayout, "Please Fill In All Fields", Snackbar.LENGTH_LONG).show();
        }
    }

    //Every Thing Ready.
    private Boolean ETR(){
        String staffId = prefs.getString("uid","error");
        if (!staffId.equals("error") && !dateTv.getText().toString().equals("date")
                && !amountEt.getText().toString().equals("")){
            return true;
        } else {
            return false;
        }
    }

    private void clearAfterAdding() {
        dateTv.setText("date");
        amountEt.setText("");
        sourceEt.setText("");
    }

    //Choose Type.
    private void setUpSpinner(){
        List<String> incomeTypes = new ArrayList<>();
        incomeTypes.add("Reservation");
        incomeTypes.add("Conference");
        incomeTypes.add("PackageDeal");

        ArrayAdapter<String> adapterTypes = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item_custom1, incomeTypes);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpn.setAdapter(adapterTypes);
    }

    //Choose Date.
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear ,
                              int dayOfMonth) {
            int month = monthOfYear + 1;
            String datePicked = year + "/" + month  + "/" + dayOfMonth;
            dateTv.setText(datePicked);
        }
    };

    private void resSync(){
        //1-read All reservations.
        readAllRes();
        //2-Add Them To Income.
    }
    private void readAllRes(){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = ref1.child("reservations");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d1 : snapshot.getChildren()){
                    for (DataSnapshot d2 : d1.getChildren()){
                        for (DataSnapshot d3 : d2.getChildren()){
                            String id = "";
                            String name = "";
                            String phone = "";
                            String email = "";
                            String nob = "";
                            String dci = "";
                            String dco = "";
                            String total = "";
                            String roomNo = "";
                            String staffCki = "";
                            String staffCko = "";
                            for (DataSnapshot d4 : d3.getChildren()){
                                if(Objects.requireNonNull(d4.getKey()).equals("id")){ id = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("name")){ name = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("phone")){ phone = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("email")){ email = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("nob")){ nob = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("dci")){ dci = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("dco")){ dco = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("total")){ total = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("roomNo")){ roomNo = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("staffCki")){ staffCki = Objects.requireNonNull(d4.getValue()).toString();}
                                if(Objects.requireNonNull(d4.getKey()).equals("staffCko")){ staffCko = Objects.requireNonNull(d4.getValue()).toString();}

                            }
                            Reservation tempRes = new Reservation(id, name, phone, email, dci, dco, nob,
                                    total);
                            tempRes.setStaffCki(staffCki);
                            tempRes.setRoomNo(roomNo);
                            tempRes.setStaffCko(staffCko);
                            addReservationToList(tempRes);
                        }
                    }
                    addAllResToIn();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void addReservationToList(Reservation res){
        reservations.add(res);
    }


    private void addAllResToIn(){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = ref1.child("financial").child("income");
        String staffId = prefs.getString("uid","error");

        for (int i = 0; i < reservations.size(); i++){
            String resId = reservations.get(i).getId();
            Reservation resTmp = reservations.get(i);
            Income inTmp = new Income(resTmp.getId(), resTmp.getDci(), resTmp.getTotal(),
                    "Reservation Sync", staffId, "Reservation");
            String[] DateSep = resTmp.getDci().split("/");
            inTmp.setYear(Integer.parseInt(DateSep[0]));
            inTmp.setMonth(Integer.parseInt(DateSep[1]));
            inTmp.setDay(Integer.parseInt(DateSep[2]));

            ref2.child(resId).setValue(inTmp);
        }
    }
}