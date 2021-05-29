package com.ahmedalraziki.g_admin_final.LogsPackage;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmedalraziki.g_admin_final.Classes.DatePickerFragment;
import com.ahmedalraziki.g_admin_final.Classes.FListFinAdapter;
import com.ahmedalraziki.g_admin_final.Classes.FileUtils;
import com.ahmedalraziki.g_admin_final.Classes.Income;
import com.ahmedalraziki.g_admin_final.Classes.Outlay;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ahmedalraziki.g_admin_final.Classes.LogUtils.LOGE;

public class Logs_Financial extends Fragment implements FListFinAdapter.OnItemListener {

    DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    List<Income> syncLogsFai = new ArrayList<>();
    List<Income> recLogsFai = new ArrayList<>();
    List<Income> recLogsFil = new ArrayList<>();
    List<Outlay> recLogsFaiO = new ArrayList<>();

    ConstraintLayout rootLayout;

    int mode = 0;
    String des;

    Context mContext;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button btnSync;
    Button btnSearch;
    Button btnDateFrom;
    Button btnDateTo;
    Button btnFilter;
    Button btnGetPDF;
    EditText etSearch;
    TextView tvDateFrom;
    TextView tvDateTo;

    SharedPreferences prefs = null ;

    public Logs_Financial() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logs__financial, container, false);
        rootLayout = rootView.findViewById(R.id.flf_root);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        Clicker(view);
        RecFiller1(view);



        mContext = getActivity().getApplicationContext();
        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
    }

    private void Assigner(View view){
        btnSync = view.findViewById(R.id.flf_btnSync);
        recyclerView = view.findViewById(R.id.flf_recView);
        btnSearch = view.findViewById(R.id.flf_btnSearch);
        etSearch = view.findViewById(R.id.flf_etSearch);
        btnDateFrom = view.findViewById(R.id.flf_btnDateFrom);
        btnDateTo = view.findViewById(R.id.flf_btnDateTo);
        btnFilter = view.findViewById(R.id.flf_btnFilter);
        btnGetPDF = view.findViewById(R.id.flf_btnGetPdf);
        tvDateFrom = view.findViewById(R.id.flf_TVDateFrom);
        tvDateTo = view.findViewById(R.id.flf_TVDateTo);

    }

    private void Clicker(View view){

        btnSync.setOnLongClickListener(v -> {
            Syncer1();
        return false;});

        btnSearch.setOnClickListener(v -> {
            String searchFor = etSearch.getText().toString();
            Search(view, searchFor);
        });

        btnDateFrom.setOnClickListener(v -> {
            mode = 1;
            showDatePicker();
        });

        btnDateTo.setOnClickListener(v -> {
            mode = 2;
            showDatePicker();
        });

        btnFilter.setOnClickListener(v -> {
            Filter(view);
        });

        btnGetPDF.setOnClickListener(v -> {
            int check = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            GetPermission(check);
        });
    }

    private void Syncer1(){
        DatabaseReference faiRef = mainRef.child("financial");
        faiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d1 : snapshot.getChildren()){

                    for (DataSnapshot d2 : d1.getChildren()){
                        String amount = "";
                        String date = "";
                        String fromAny = "";
                        String id = "";
                        String staffID = "";
                        String type = "";
                        int year = 0;
                        int month = 0;
                        int day = 0;
                        //
                        for (DataSnapshot d3 : d2.getChildren()){

                            if (d3.getKey().equals("amount")) {
                                amount = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("date")) {
                                date = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("fromAny")) {
                                fromAny = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("id")) {
                                id = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("staffID")) {
                                staffID = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("type")) {
                                type = d3.getValue().toString();
                            }
                            if (d3.getKey().equals("year")) {
                                year = Integer.parseInt(d3.getValue().toString());
                            }
                            if (d3.getKey().equals("month")) {
                                month = Integer.parseInt(d3.getValue().toString());
                            }
                            if (d3.getKey().equals("day")) {
                                day = Integer.parseInt(d3.getValue().toString());
                            }
                        }
                        //
                        Income tmpInc = new Income(id, date, amount, fromAny, staffID, type);
                        tmpInc.setYear(year);
                        tmpInc.setMonth(month);
                        tmpInc.setDay(day);
                        AddLog(tmpInc, 1);
                    }
                    Syncer2();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
    }

    private void Syncer2(){
        for (Income i : syncLogsFai){
            DatabaseReference logRef = mainRef.child("logs").child("financial")
                    .child(i.getId());
            logRef.setValue(i);
        }
    }

    private void AddLog(Income inc, int mode){
        if (mode == 1){
        syncLogsFai.add(inc);
        } else if (mode == 2){
            recLogsFai.add(inc);
        }
    }

    private void RecFiller1(View view){
        DatabaseReference logRef = mainRef.child("logs").child("financial");

        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot d1 : snapshot.getChildren()){
                    String amount = "";
                    String date = "";
                    String fromAny = "";
                    String id = "";
                    String staffID = "";
                    String type = "";
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    //
                    for (DataSnapshot d2 : d1.getChildren()){

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
                    Income tmpLog = new Income(id, date, amount, fromAny, staffID, type);
                    tmpLog.setYear(year);
                    tmpLog.setMonth(month);
                    tmpLog.setDay(day);
                    AddLog(tmpLog, 2);
                }
                setRecView(view, recLogsFai);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
    }

    private void setRecView(View view, List<Income> LTA){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new FListFinAdapter(view.getContext(), 1, LTA, recLogsFaiO , this);
        recyclerView.setAdapter(adapter);
    }

    private void Search(View view, String serID){
        recLogsFil.clear();

        for (Income i : recLogsFai){
            if (i.getId().contains(serID)){
                recLogsFil.add(i);
            }
        }
        setRecView(view, recLogsFil);
    }

    private void GetPermission(int check) {
        if (check == PackageManager.PERMISSION_GRANTED) {
            //If Permission Granted Create PDF
            String dateS = Calendar.getInstance().getTime().toString().trim();
            des = FileUtils.getAppPath(mContext) +"Logs_Fin"+ dateS + ".pdf";

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    createPdf(des);
                }
            }, 500);

        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);
        }
    }

    private void Filter(View view){
        String stDateFrom = tvDateFrom.getText().toString();
        String stDateTo = tvDateTo.getText().toString();

        Date daDateFrom = ConvertToDate(stDateFrom);
        Date daDateTo = ConvertToDate(stDateTo);

        long lonDateFrom = daDateFrom.getTime();
        long lonDateTo = daDateTo.getTime();

        for (Income i : recLogsFai){
            String dateI = i.getDate();
            Date daDateI = ConvertToDate(dateI);
            long lonDateI = daDateI.getTime();

            long diff1 = lonDateI - lonDateFrom;
            long diff2 = lonDateTo - lonDateI;

            if (diff1 > 0 && diff2 > 0){
                recLogsFil.add(i);
            }
        }

        setRecView(view, recLogsFil);
    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }


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

            if (mode == 1) {
                tvDateFrom.setText(datePicked);
            } else if (mode == 2){
                tvDateTo.setText(datePicked);
            }
        }
    };

    public void createPdf(String dest) {

        if (recLogsFil.isEmpty()){
            recLogsFil = recLogsFai;
        }

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        try {
            /**
             * Creating Document
             */
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();
            String staffID = prefs.getString("uid","error");

            info.setTitle("Financial Analysis");
            info.setAuthor("Hotel App" + staffID);
            info.setSubject("Analysis");
            info.setKeywords("Financial, Analysis, Income, Outlay");
            info.setCreator("Hotel App");

            Document document = new Document(pdfDocument, PageSize.A4, true);

            /***
             * Variables for further use....
             */
            Color mColorAccent = new DeviceRgb(153, 204, 255);
            Color mColorBlack = new DeviceRgb(0, 0, 0);
            float mHeadingFontSize = 16.0f;
            float mValueFontSize = 20.0f;

            /**
             * How to USE FONT....
             */
            PdfFont font = PdfFontFactory.createFont("assets/fonts/brandon_medium.otf", "UTF-8", true);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator(new DottedLine());
            lineSeparator.setStrokeColor(new DeviceRgb(0, 0, 68));

            // Title Order Details...
            // Adding Title....
            Text mOrderDetailsTitleChunk = new Text("Financial Logs").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Text mOrderIdChunk = new Text("Date").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            String dateS = Calendar.getInstance().getTime().toString();
            Text mOrderIdValueChunk = new Text(dateS).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
            document.add(mOrderIdValueParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(lineSeparator);
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Text mOrderDateChunk = new Text("Staff ID:").setFont(font).setFontSize(mHeadingFontSize).setFontColor(mColorAccent);
            Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
            document.add(mOrderDateParagraph);

            Text mOrderDateValueChunk = new Text(staffID).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);

            document.add(new Paragraph(""));
            document.add(lineSeparator);
            document.add(new Paragraph(""));

            float [] pcW = {150f, 150f, 150f, 150f, 150f};
            Table table = new Table(pcW);
            table.addCell("ID");
            table.addCell("Amount");
            table.addCell("Date");
            table.addCell("Type");
            table.addCell("Source/To");

                for (Income i : recLogsFil){
                    table.addCell(i.getId());
                    table.addCell(i.getAmount());
                    table.addCell(i.getDate());
                    table.addCell(i.getType());
                    table.addCell(i.getFromAny());
                }

            document.add(table);

            Text footerText = new Text("By Hotel_App, Ahmed Alraziki").setFont(font).setFontSize(14.0f).setFontColor(mColorBlack);
            Paragraph footerTextParagraph = new Paragraph(footerText)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(footerTextParagraph);


            document.close();

            Toast.makeText(mContext, "Created... :)", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                try {
                    FileUtils.openFile(mContext, new File(dest));
                } catch (Exception e) {
                    Log.d("TAG", "run: ERror");
                }
            }, 1000);


        } catch (IOException e) {
            LOGE("createPdf: Error " + e.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {
        Income selI = recLogsFai.get(position);
        String ARG_Log_ID = "ALI";

        Bundle bundle = new Bundle();
        bundle.putString(ARG_Log_ID, selI.getId());
        Fragment fragment = new LogDetails();
        fragment.setArguments(bundle);
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.logs, fragment).commit();
    }
}