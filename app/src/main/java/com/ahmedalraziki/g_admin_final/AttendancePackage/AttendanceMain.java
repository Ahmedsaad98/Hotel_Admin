package com.ahmedalraziki.g_admin_final.AttendancePackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ahmedalraziki.g_admin_final.Attendance;
import com.ahmedalraziki.g_admin_final.MainActivity;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class AttendanceMain extends Fragment {

    TextView txtMain;
    ImageView imgMain;
    private FusedLocationProviderClient fusedLocationClient;

    SharedPreferences prefs = null;

    public AttendanceMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendace_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = view.getContext().getSharedPreferences("prefs1", Context.MODE_PRIVATE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        Assigner(view);
        GetQrToAttend();

    }

    private void Assigner(View view) {
        txtMain = view.findViewById(R.id.fam_txtM1);
        imgMain = view.findViewById(R.id.fam_imgM);
    }

    private void GetQrToAttend() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            return;
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    double curLat = location.getLatitude();
                    double curLon = location.getLongitude();
                    double baseLat = 33.400071;
                    double baseLon = 44.405957;

                    atWork(curLat, curLon, baseLat, baseLon);
                }
            });
        }
    }

    private void atWork(double curLat, double curLon, double baseLat, double baseLon){

        String staffCki = prefs.getString("uid","error");

        if (checkDistance(curLat, curLon, baseLat, baseLon)){
            txtMain.setText("Great now go and Sign In");
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref2 = ref1.child("attendance").child(staffCki).child("code");
            String code = UUID.randomUUID().toString();
            ref2.setValue(code);
            encodeQr(code);
        } else{
            txtMain.setText("Go To Work Please.");
        }
    }

    private boolean checkDistance(double currentLocLat, double currentLocLong, double placeLocLat, double placeLocLong) {
        float radius =  200F;
        float[] results = new float[3];
        Location.distanceBetween(currentLocLat, currentLocLong, placeLocLat, placeLocLong, results);
        return (results[0] <= radius);
    }

    private void encodeQr(String qrCode){
        BitMatrix resultQR ;
        try {
            resultQR = new MultiFormatWriter().encode(qrCode , BarcodeFormat.QR_CODE , 250 ,250 , null
            );
        } catch (WriterException e){
            e.printStackTrace();
            return ;
        }
        int width = resultQR.getWidth();
        int height = resultQR.getHeight();
        int[] pixels = new int[width * height];
        for (int x  = 0 ; x < height ; x++){
            int offset = x * width;
            for (int k = 0 ; k < width ; k++){
                pixels[offset + k] = resultQR.get(k , x) ? BLACK : WHITE ;
            }
        }
        Bitmap myQR = Bitmap.createBitmap(width , height , Bitmap.Config.ARGB_8888);
        myQR.setPixels(pixels, 0 , width , 0 , 0 , width , height);
        imgMain.setImageBitmap(myQR);

    }

}