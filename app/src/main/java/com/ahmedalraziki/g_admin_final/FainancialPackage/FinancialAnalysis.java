package com.ahmedalraziki.g_admin_final.FainancialPackage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ahmedalraziki.g_admin_final.Classes.Income;
import com.ahmedalraziki.g_admin_final.Classes.Outlay;
import com.ahmedalraziki.g_admin_final.R;
import com.ahmedalraziki.g_admin_final.reservationPackage.ReservationList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FinancialAnalysis extends Fragment {

    LineChart chart1;
    LineChart chart2;
    RadioGroup char1RadGroup;
    RadioGroup char2RadGroup;
    RadioButton char1RadYear;
    RadioButton char2RadYear;
    RadioButton char1RadMonth;
    RadioButton char2RadMonth;
    RadioButton char1RadDay;
    RadioButton char2RadDay;
    List<Float> xDates1;
    List<Float> xDates2;
    List<Float> yAmounts1;
    List<Float> yAmounts2;
    List<Income> incomesCh1;
    List<Outlay> outlaysCh2;
    Button backBtn;
    Button GLI;
    Button GLO;
    Button PdfI;
    Button PdfO;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FinancialAnalysis() {
        // Required empty public constructor
    }

    public static FinancialAnalysis newInstance(String param1, String param2) {
        FinancialAnalysis fragment = new FinancialAnalysis();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_financial_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        Clicker();
        ListInit();
        setCh1Ph1();
        setCh2Ph1();
    }
    private void Assigner(View view){
        chart1        = view.findViewById(R.id.ffiana_chart1);
        chart2        = view.findViewById(R.id.ffiana_chart2);
        char1RadGroup = view.findViewById(R.id.ffiana_chart1RdGroup);
        char2RadGroup = view.findViewById(R.id.ffiana_chart2RdGroup);
        char1RadYear  = view.findViewById(R.id.ffiana_chart1RdYear);
        char2RadYear  = view.findViewById(R.id.ffiana_chart2RdYear);
        char1RadMonth = view.findViewById(R.id.ffiana_chart1RdMonth);
        char2RadMonth = view.findViewById(R.id.ffiana_chart2RdMonth);
        char1RadDay   = view.findViewById(R.id.ffiana_chart1RdDay);
        char2RadDay   = view.findViewById(R.id.ffiana_chart2RdDay);
        backBtn       = view.findViewById(R.id.ffiana_btnBack);
        GLI           = view.findViewById(R.id.ffiana_btnListIn);
        GLO           = view.findViewById(R.id.ffiana_btnListOu);
        PdfI          = view.findViewById(R.id.ffiana_btnPdfIn);
        PdfO          = view.findViewById(R.id.ffiana_btnPdfOu);
    }

    private void Clicker(){
    backBtn.setOnClickListener(v ->{
        Fragment fragment = new FainancialDash();
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fainancial, fragment).commit();
    });

    GLI.setOnClickListener(v ->{
        Bundle bundle = new Bundle();
        bundle.putInt("flt", 1);
        Fragment fragment = new FinancialLists();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fainancial, fragment).commit();
    });

    GLO.setOnClickListener(v ->{
        Bundle bundle = new Bundle();
        bundle.putInt("flt", 2);
        Fragment fragment = new FinancialLists();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fainancial, fragment).commit();
    });

    PdfI.setOnClickListener(v ->{
        Bundle bundle = new Bundle();
        bundle.putInt("lio", 1);
        Fragment fragment = new FinancialCreatePdf();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fainancial, fragment).commit();
    });
    PdfO.setOnClickListener(v ->{
        Bundle bundle = new Bundle();
        bundle.putInt("lio", 2);
        Fragment fragment = new FinancialCreatePdf();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fainancial, fragment).commit();
    });


    }

    private void ListInit(){
        xDates1 = new ArrayList<>();
        yAmounts1 = new ArrayList<>();
        incomesCh1 = new ArrayList<>();

        outlaysCh2 = new ArrayList<>();
        xDates2 = new ArrayList<>();
        yAmounts2 = new ArrayList<>();
    }

    private void setCh1Ph1() {
        // Initiating Phase One.
        char1RadMonth.setChecked(true);

        chart1.setBackgroundColor(Color.WHITE);
        chart1.getDescription().setEnabled(false);
        chart1.setDrawGridBackground(false);
        chReader(1,2);
        char1RadGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ffiana_chart1RdYear:
                        chReader(1,1);
                        break;

                    case R.id.ffiana_chart1RdMonth:
                        chReader(1,2);
                        break;

                    case R.id.ffiana_chart1RdDay:
                        chReader(1,3);
                        break;
                }
            }
        });

    }

    private void setCh2Ph1(){
        // Initiating Phase One.
        char2RadMonth.setChecked(true);
        chart2.setBackgroundColor(Color.WHITE);
        chart2.getDescription().setEnabled(false);
        chart2.setDrawGridBackground(false);
        chReader(2,2);
        char2RadGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ffiana_chart2RdYear:
                        chReader(2,1);
                        break;

                    case R.id.ffiana_chart2RdMonth:
                        chReader(2,2);
                        break;

                    case R.id.ffiana_chart2RdDay:
                        chReader(2,3);
                        break;
                }
            }
        });    }

    private void chReader(int chC, int durMode){
        //Clearing If Read before.
        if (chC == 1) {
            xDates1.clear();
            yAmounts1.clear();
            incomesCh1.clear();
        } else if (chC == 2){
            xDates2.clear();
            yAmounts2.clear();
            outlaysCh2.clear();
        }
        //Reading All Incomes In Database.
        //1 for income, 2 for outlay, 3 for both.
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = null;
        switch (chC){
            case 1:
            ref2 = ref1.child("financial").child("income");
                break;
            case 2:
                ref2 = ref1.child("financial").child("outlay");
                break;
        }

        if (ref2 != null){
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot d1 : snapshot.getChildren()) {
                            String amount = "";
                            String date = "";
                            String fromAny = "";
                            String id = "";
                            String staffID = "";
                            String type = "";
                            int year = 0;
                            int month = 0;
                            int day = 0;

                            for (DataSnapshot d2 : d1.getChildren()) {
                                if (d2.getKey().equals("amount")) {
                                    amount = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("date")) {
                                    date = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("fromAny")) {
                                    fromAny = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("id")) {
                                    id = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("staffID")) {
                                    staffID = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("type")) {
                                    type = d2.getValue().toString();
                                }
                                if (d2.getKey().equals("year")) {
                                    year = Integer.parseInt(d2.getValue().toString());
                                }
                                if (d2.getKey().equals("month")) {
                                    month = Integer.parseInt(d2.getValue().toString());
                                }
                                if (d2.getKey().equals("day")) {
                                    day = Integer.parseInt(d2.getValue().toString());
                                }

                            }
                        if (chC == 1) {
                            //Making A temp Income Object.
                            Income tmpInc = new Income(id, date, amount, fromAny, staffID, type);
                            tmpInc.setYear(year);
                            tmpInc.setMonth(month);
                            tmpInc.setDay(day);
                            adIn1(tmpInc);
                            } else if (chC == 2){
                            Outlay tmpOtl = new Outlay(id, date, amount, fromAny, staffID, type);
                            tmpOtl.setYear(year);
                            tmpOtl.setMonth(month);
                            tmpOtl.setDay(day);
                            adOu2(tmpOtl);
                            }
                        }
                        //After Reading All Incomes We Initiate Phase 2.
                    if (chC == 1) {
                        setCh1Ph2(durMode);
                    } else if (chC == 2){
                        setCh2Ph2(durMode);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) { }}); }

    }

    private void adIn1(Income income){
        // Adding An Income To The List Of Incomes.
        incomesCh1.add(income);
    }

    private void adOu2(Outlay outlay){
    // Adding an Outlay To The List Of Outlays.
    outlaysCh2.add(outlay);
    }

    private void setCh1Ph2(int durMode){
        // Turning incomes in incomesCh1[] into a date and Amount.
        for (int i = 0; i < incomesCh1.size(); i++){
            if (durMode == 1){
                xDates1.add(Float.parseFloat(String.valueOf(incomesCh1.get(i).getYear())));
                yAmounts1.add(Float.parseFloat(incomesCh1.get(i).getAmount()));
            } else if (durMode == 2){
                xDates1.add(Float.parseFloat(String.valueOf(incomesCh1.get(i).getMonth())));
                yAmounts1.add(Float.parseFloat(incomesCh1.get(i).getAmount()));
            } else if (durMode == 3){
                xDates1.add(Float.parseFloat(String.valueOf(incomesCh1.get(i).getDay())));
                yAmounts1.add(Float.parseFloat(incomesCh1.get(i).getAmount()));
            }

        }
        // Turning the date-Amount pairs into Entries.
        ArrayList<Entry> yVals1 = new ArrayList<>();
        for (int j = 0; j < xDates1.size(); j++){
            yVals1.add(new Entry(xDates1.get(j), yAmounts1.get(j)));
        }
        //Sorting By Date.
        yVals1.sort(new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                String o1X = String.valueOf(o1.getX());
                String o2X = String.valueOf(o2.getX());
                return o1X.compareTo(o2X);
            }
        });
        // Setting The Chart With these Values.
        LineDataSet set1 = new LineDataSet(yVals1 , "Income");
        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        chart1.setData(data);
        chart1.invalidate();
        chart1.notifyDataSetChanged();
    }

    private void setCh2Ph2(int durMode){
        // Turning Outlays in OutlaysCh2[] into a date and Amount.
        if (outlaysCh2 != null){
        for (int i = 0; i < outlaysCh2.size(); i++){
            if (durMode == 1){
                xDates2.add(Float.parseFloat(String.valueOf(outlaysCh2.get(i).getYear())));
                yAmounts2.add(Float.parseFloat(outlaysCh2.get(i).getAmount()));
                } else if (durMode == 2){
                xDates2.add(Float.parseFloat(String.valueOf(outlaysCh2.get(i).getMonth())));
                yAmounts2.add(Float.parseFloat(outlaysCh2.get(i).getAmount()));
                } else if (durMode == 3){
                xDates2.add(Float.parseFloat(String.valueOf(outlaysCh2.get(i).getDay())));
                yAmounts2.add(Float.parseFloat(outlaysCh2.get(i).getAmount()));
                }
            }
        }
        // Turning the date-Amount pairs into Entries.
        ArrayList<Entry> yVals2 = new ArrayList<>();
        for (int j = 0; j < xDates2.size(); j++){
            yVals2.add(new Entry(xDates2.get(j), yAmounts2.get(j)));
        }
        //Sorting By Date.
        yVals2.sort((o1, o2) -> {
            String o1X = String.valueOf(o1.getX());
            String o2X = String.valueOf(o2.getX());
            return o1X.compareTo(o2X);
        });
        // Setting The Chart With these Values.
        LineDataSet set2 = new LineDataSet(yVals2 , "Outlay");
        set2.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        chart2.setData(data);
        chart2.invalidate();
        chart2.notifyDataSetChanged();
    }
}