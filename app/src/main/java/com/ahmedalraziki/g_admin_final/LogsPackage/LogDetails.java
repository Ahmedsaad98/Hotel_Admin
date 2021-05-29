package com.ahmedalraziki.g_admin_final.LogsPackage;

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
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Calendar;

import static com.ahmedalraziki.g_admin_final.Classes.LogUtils.LOGE;

public class LogDetails extends Fragment {

    private static final String ARG_Log_ID = "ALI";

    private String ALI;

    TextView tvID;
    TextView tvDate;
    TextView tvAmount;
    TextView tvSource;
    TextView tvType;
    TextView tvSID;

    Button btnPdf;

    String des;
    Context mContext;

    SharedPreferences prefs = null ;

    DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    public LogDetails() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ALI = getArguments().getString(ARG_Log_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);
        mContext = getActivity().getApplicationContext();

        Assigner(view);
        DataFiller();
        Clicker();


    }

    private void Assigner(View view){
        tvID = view.findViewById(R.id.fld_txtID);
        tvDate = view.findViewById(R.id.fld_txtDate);
        tvAmount = view.findViewById(R.id.fld_txtAmo);
        tvSource = view.findViewById(R.id.fld_txtSou);
        tvType = view.findViewById(R.id.fld_txtTy);
        tvSID = view.findViewById(R.id.fld_txtSta);
        btnPdf = view.findViewById(R.id.fld_btnPdf);
    }

    private void Clicker(){
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                GetPermission(check);
            }
        });
    }

    private void DataFiller(){
        if (ALI != null){
            DatabaseReference selRef = mainRef.child("logs").child("financial").child(ALI);

            selRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String amount = "";
                    String date = "";
                    String fromAny = "";
                    String id = "";
                    String staffID = "";
                    String type = "";
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    for (DataSnapshot d2 : snapshot.getChildren()){

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
                    Income tmpInc = new Income(id, date, amount, fromAny, staffID, type);
                    setValues(tmpInc);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
        }
    }

    private void setValues(Income income){
        tvID.setText(income.getId());
        tvDate.setText(income.getDate());
        tvAmount.setText(income.getAmount());
        tvSource.setText(income.getFromAny());
        tvType.setText(income.getType());
        tvSID.setText(income.getStaffID());
    }


    private void GetPermission(int check) {
        if (check == PackageManager.PERMISSION_GRANTED) {
            //If Permission Granted Create PDF
            String dateS = Calendar.getInstance().getTime().toString().trim();
            des = FileUtils.getAppPath(mContext) +"Log_Details" + dateS + ".pdf";

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    createPdf(des);
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
             * Setting Variables
             */

            String logAmount = tvAmount.getText().toString();
            String logDate = tvDate.getText().toString();
            String logFromAny = tvSource.getText().toString();
            String logId = tvID.getText().toString();
            String logStaffID = tvSID.getText().toString();
            String logType = tvType.getText().toString();

            /**
             * Creating Document
             */
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();
            String staffID = prefs.getString("uid","error");

            info.setTitle("Log "+ logId +" Details");
            info.setAuthor("Hotel App" + staffID);
            info.setSubject("Analysis");
            info.setKeywords("Logging System");
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
            Text mOrderDetailsTitleChunk = new Text("Log_details").setFont(font).setFontSize(36.0f).setFontColor(mColorBlack);
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

            Text logAmountT = new Text("Amount : " + logAmount).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Text logDateT = new Text("Date : " + logDate).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Text logFromAnyT = new Text("Source : " + logFromAny).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Text logIdT = new Text("ID : " + logId).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Text logStaffIDT = new Text("Staff ID : " + logStaffID).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);
            Text logTypeT = new Text("Type : " + logType).setFont(font).setFontSize(mValueFontSize).setFontColor(mColorBlack);

            Paragraph logAmountP = new Paragraph(logAmountT);
            Paragraph logDateP = new Paragraph(logDateT);
            Paragraph logFromAnyP = new Paragraph(logFromAnyT);
            Paragraph logIdP = new Paragraph(logIdT);
            Paragraph logStaffIDP = new Paragraph(logStaffIDT);
            Paragraph logTypeP = new Paragraph(logTypeT);

            document.add(logAmountP);
            document.add(logDateP);
            document.add(logFromAnyP);
            document.add(logIdP);
            document.add(logStaffIDP);
            document.add(logTypeP);

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
                    Log.d("TAG", "run: Error");
                }
            }, 500);


        } catch (IOException e) {
            LOGE("createPdf: Error " + e.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }


}