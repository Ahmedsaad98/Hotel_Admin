package com.ahmedalraziki.g_admin_final.StaffPackage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ahmedalraziki.g_admin_final.Classes.Staff;
import com.ahmedalraziki.g_admin_final.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class StaffAddMember extends Fragment {

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etAddress;
    EditText etPosition;
    EditText etUserName;
    EditText etPassword;
    EditText etSalary;
    EditText etLevel;
    Button btnAdd;
    ConstraintLayout rootLayout;

    public StaffAddMember() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_staff_add_member, container, false);
        rootLayout = root.findViewById(R.id.fasm_root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Assigner(view);
        AddClicked();

    }

    private void Assigner(View view){
        etName = view.findViewById(R.id.fasm_txtName);
        etPhone = view.findViewById(R.id.fasm_txtPhone);
        etEmail = view.findViewById(R.id.fasm_txtEmail);
        etAddress = view.findViewById(R.id.fasm_txtAddress);
        etPosition = view.findViewById(R.id.fasm_txtPosition);
        etUserName = view.findViewById(R.id.fasm_txtUN);
        etPassword = view.findViewById(R.id.fasm_txtPassword);
        etSalary = view.findViewById(R.id.fasm_txtSalary);
        etLevel = view.findViewById(R.id.fasm_txtLevel);
        btnAdd = view.findViewById(R.id.fasm_btnAdd);
    }

    private void AddClicked(){
        btnAdd.setOnClickListener(v ->{
            if (CheckForAllFields()){ AddMember(); }
        });
    }

    private Boolean CheckForAllFields(){

        if (etName.getText().toString().equals("") ||etPhone.getText().toString().equals("")
                ||etEmail.getText().toString().equals("") ||etAddress.getText().toString().equals("")
                ||etPosition.getText().toString().equals("") ||etUserName.getText().toString().equals("")
                ||etPassword.getText().toString().equals("") ||etSalary.getText().toString().equals("")
                ||etLevel.getText().toString().equals("")){
            Snackbar.make(rootLayout, "Please Fill In All Fields", Snackbar.LENGTH_LONG).show();
            return false;
        }
        else { return true; }

    }

    private void AddMember(){
        String id = UUID.randomUUID().toString();
        String userName = etUserName.getText().toString().trim();
        String passWord = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String Email = etEmail.getText().toString().trim();
        String Address = etAddress.getText().toString().trim();
        String Position = etPosition.getText().toString().trim();
        String Salary = etSalary.getText().toString().trim();
        int level = Integer.parseInt(etLevel.getText().toString());

        Staff tempMem = new Staff(id, userName, passWord);
        tempMem.setName(name);
        tempMem.setPhone(phone);
        tempMem.setEmail(Email);
        tempMem.setAddress(Address);
        tempMem.setPosition(Position);
        tempMem.setSalary(Salary);
        tempMem.setLvl(level);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = ref1.child("management").child("staff").child(id);
        ref2.setValue(tempMem);
    }

}