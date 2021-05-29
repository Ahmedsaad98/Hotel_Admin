package com.ahmedalraziki.g_admin_final.receptionPackage;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmedalraziki.g_admin_final.R;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.Objects;

public class Reception_Scanner extends Fragment {

    private static final String ARG_PARAM1 = "qrData";
    private static final String ARG_PARAM2 = "fromFrag";

    private CodeScanner mCodeScanner;

    private String qrData;
    private String fromFrag;

    public Reception_Scanner() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrData = getArguments().getString(ARG_PARAM1);
            fromFrag = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_reception__scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        returnQrData(result);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    private void returnQrData(Result result){
        qrData = result.toString();
        switch (fromFrag){
            case "cki":
                Bundle bundle = new Bundle();
                bundle.putString(ARG_PARAM1, qrData);
                bundle.putString(ARG_PARAM2,"scanner");
                Fragment fragment = new ReceptionCki();
                fragment.setArguments(bundle);
                FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.reception, fragment).commit();
                break;

            case "cko":
                Bundle bundle2 = new Bundle();
                bundle2.putString(ARG_PARAM1, qrData);
                bundle2.putString(ARG_PARAM2,"scanner");
                Fragment fragment2 = new ReceptionCko();
                fragment2.setArguments(bundle2);
                FragmentManager fm2 = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.reception, fragment2).commit();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}