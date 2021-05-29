package com.ahmedalraziki.g_admin_final.StaffPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.ahmedalraziki.g_admin_final.Classes.Staff;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class StaffMemDetails extends Fragment {

    DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    static final String Staff_ID = "idS";
    String sID = "";

    EditText username;
    EditText password;
    EditText name;
    EditText phone;
    EditText address;
    EditText position;
    EditText salary;
    EditText email;
    EditText lvl;

    Button UpdateInfo;
    Button DeleteInfo;
    ConstraintLayout rootLayout;

    Staff selectedMem;
    boolean readDone = false;
    int uPressed = 1;

    public StaffMemDetails() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sID = getArguments().getString(Staff_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_mem_details, container, false);
        rootLayout = root.findViewById(R.id.fsmd_root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        DataFiller0();
        Clicker();
    }

    private void Assigner(View view){
        username = view.findViewById(R.id.fsmd_txtUN);
        password = view.findViewById(R.id.fsmd_txtPA);
        name = view.findViewById(R.id.fsmd_txtNA);
        phone = view.findViewById(R.id.fsmd_txtPH);
        address = view.findViewById(R.id.fsmd_txtAD);
        position = view.findViewById(R.id.fsmd_txtPO);
        salary = view.findViewById(R.id.fsmd_txtSA);
        email = view.findViewById(R.id.fsmd_txtEM);
        lvl = view.findViewById(R.id.fsmd_txtLVL);

        DeleteInfo = view.findViewById(R.id.fsmd_btnDel);
        UpdateInfo = view.findViewById(R.id.fsmd_btnUP);
    }

    private void Clicker(){
        UpdateInfo.setOnClickListener(v ->{
            UpdateClicked();
        });

        DeleteInfo.setOnClickListener(v -> {
            Snackbar.make(rootLayout, "Long Press To Delete", Snackbar.LENGTH_LONG).show();
        });

        DeleteInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeleteMem();
                return false;
            }
        });
    }

    private void DataFiller0(){
        if (!readDone){
            new Handler().postDelayed(this::DataFiller1, 500);
        }
    }

    private void DataFiller1(){
        if (!sID.equals("")){
            DatabaseReference memRef = mainRef.child("management").child("staff").child(sID);
            memRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String id = "";
                    String username = "";
                    String password = "";
                    String name = "";
                    String phone = "";
                    String address = "";
                    String position = "";
                    String salary = "";
                    String email = "";
                    int lvl = 99;
                    //
                    for (DataSnapshot d1 : snapshot.getChildren()){
                        if (d1.getKey().equals("id")){ id = d1.getValue().toString();}
                        if (d1.getKey().equals("username")){ username = d1.getValue().toString();}
                        if (d1.getKey().equals("password")){ password = d1.getValue().toString();}
                        if (d1.getKey().equals("name")){ name = d1.getValue().toString();}
                        if (d1.getKey().equals("phone")){ phone = d1.getValue().toString();}
                        if (d1.getKey().equals("address")){ address = d1.getValue().toString();}
                        if (d1.getKey().equals("position")){ position = d1.getValue().toString();}
                        if (d1.getKey().equals("salary")){ salary = d1.getValue().toString();}
                        if (d1.getKey().equals("email")){ email = d1.getValue().toString();}
                        if (d1.getKey().equals("lvl")){ lvl = Integer.parseInt(d1.getValue().toString());}
                    }
                    Staff tempMem = new Staff(""+id, ""+username, ""+password, ""+name, ""+phone,""+address,""+position
                            ,""+salary,""+email, lvl);
                    tempMem.setStaRef(memRef.getRef());
                    selectedMem = tempMem;
                    DataFiller2( tempMem, memRef ,this);

                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) { }});
        }
    }

    private void DataFiller2(Staff selMem, DatabaseReference ref, ValueEventListener vel){
        username.setText(selMem.getUsername());
        password.setText(selMem.getPassword());
        name.setText(selMem.getName());
        phone.setText(selMem.getPhone());
        address.setText(selMem.getAddress());
        position.setText(selMem.getPosition());
        salary.setText(selMem.getSalary());
        email.setText(selMem.getEmail());
        lvl.setText(String.valueOf(selMem.getLvl()));
        readDone = true;

        ref.removeEventListener(vel);
    }

    private void UpdateClicked(){
        if (uPressed < 2){
            uPressed++;
            Snackbar.make(rootLayout, "Press Twice To Update", Snackbar.LENGTH_LONG).show();
        } else {
            UpdateDetails();
            uPressed = 1;
            Snackbar.make(rootLayout, "Done", Snackbar.LENGTH_LONG).show();
        }
    }

    private void UpdateDetails(){
        if(!username.getText().toString().equals("") ||!password.getText().toString().equals("")
                ||!name.getText().toString().equals("") ||!phone.getText().toString().equals("") ||
                !address.getText().toString().equals("") ||!position.getText().toString().equals("")
                ||!salary.getText().toString().equals("") ||!email.getText().toString().equals("")||
        !lvl.getText().toString().equals("")){
            //
            String varUN = username.getText().toString();
            String varPa = password.getText().toString();
            String varNA = name.getText().toString();
            String varPH = phone.getText().toString();
            String varAD = address.getText().toString();
            String varPO = position.getText().toString();
            String varSa = salary.getText().toString();
            String varEM = email.getText().toString();
            int    varLV = Integer.parseInt(lvl.getText().toString());
            Staff newDetails = new Staff(sID, varUN, varPa, varNA, varPH, varAD, varPO, varSa,
                    varEM, varLV);
            selectedMem.getStaRef().setValue(newDetails);

        } else{
            Snackbar.make(rootLayout, "Please Fill In All Fields", Snackbar.LENGTH_LONG).show();
        }
    }

    private void DeleteMem(){
        DatabaseReference memRef = mainRef.child("management").child("staff").child(sID);
        memRef.removeValue();
    }

}