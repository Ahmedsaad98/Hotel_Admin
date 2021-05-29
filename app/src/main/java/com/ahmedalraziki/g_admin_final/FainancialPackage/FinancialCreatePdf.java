package com.ahmedalraziki.g_admin_final.FainancialPackage;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ahmedalraziki.g_admin_final.Classes.FileUtils;
import com.ahmedalraziki.g_admin_final.Classes.Income;
import com.ahmedalraziki.g_admin_final.Classes.Outlay;
import com.ahmedalraziki.g_admin_final.R;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ahmedalraziki.g_admin_final.Classes.LogUtils.LOGE;

public class FinancialCreatePdf extends Fragment {

    private static final String ARG_PARAM1 = "lio";

    private int lio;


    Context mContext;
    String des;

    List<Income> incomes;
    List<Outlay> outlays;

    SharedPreferences prefs = null ;

    public FinancialCreatePdf() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lio = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_financial_create_pdf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incomes = new ArrayList<>();
        outlays = new ArrayList<>();

        mContext = getActivity().getApplicationContext();

        int check = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        GetPermission(check, view);

        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
    }

    private void GetPermission(int check, View view) {
        if (check == PackageManager.PERMISSION_GRANTED) {
            //If Permission Granted Create PDF
            String dateS = Calendar.getInstance().getTime().toString().trim();
            des = FileUtils.getAppPath(mContext) +"Income_Analysis" + dateS + ".pdf";

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inOuReader(view, des);
                    }
                }, 2000);

        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);
        }
    }

    public void createPdf(String dest) {

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
            Text mOrderDetailsTitleChunk = new Text("FINANCIAL ANALYSIS").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack);
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

            if (lio == 1 ){
                for (Income i : incomes){
                    table.addCell(i.getId());
                    table.addCell(i.getAmount());
                    table.addCell(i.getDate());
                    table.addCell(i.getType());
                    table.addCell(i.getFromAny());
                }
            } else if (lio == 2 ){
                for (Outlay o : outlays){
                    table.addCell(o.getId());
                    table.addCell(o.getAmount());
                    table.addCell(o.getDate());
                    table.addCell(o.getType());
                    table.addCell(o.getFromAny());
                }
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

    private void inOuReader(View view, String dest){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = null;
        if (lio == 1){
            ref2 = ref1.child("financial").child("income");
        } else {
            ref2 = ref1.child("financial").child("outlay");
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
                        //Making A temp Income Object.
                        if (lio == 1) {
                            Income tmpInc = new Income(id, date, amount, fromAny, staffID, type);
                            tmpInc.setYear(year);
                            tmpInc.setMonth(month);
                            tmpInc.setDay(day);
                            AddIn(tmpInc);
                        } else {
                            Outlay tmpOut = new Outlay(id, date, amount, fromAny, staffID, type);
                            tmpOut.setYear(year);
                            tmpOut.setMonth(month);
                            tmpOut.setDay(day);
                            AddOu(tmpOut);
                        }

                    }
                    createPdf(dest);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
        }
    }

    private void AddIn(Income income){
        incomes.add(income);
    }
    private void AddOu(Outlay outlay){
        outlays.add(outlay);
    }

}