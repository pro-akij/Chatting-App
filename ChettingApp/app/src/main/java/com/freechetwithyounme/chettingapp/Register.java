package com.freechetwithyounme.chettingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText mUser_name, mEmail, mPassword, mRepassword;
    private Button mRegister;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_check;

    private String get_user, get_email, get_password, get_re_password;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar= findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Register");

        mUser_name= findViewById(R.id.enter_user_name);
        mEmail= findViewById(R.id.enter_email);
        mPassword= findViewById(R.id.enter_password);
        mRepassword= findViewById(R.id.enter_re_password);
        mRegister= findViewById(R.id.set_register);
        progressDialog= new ProgressDialog(this);

        mAuth= FirebaseAuth.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_user= mUser_name.getText().toString();
                get_email= mEmail.getText().toString();
                get_password= mPassword.getText().toString();
                get_re_password= mRepassword.getText().toString();

                if (TextUtils.isEmpty(get_user) || TextUtils.isEmpty(get_email) || TextUtils.isEmpty(get_password)){
                    Toast.makeText(Register.this,"All field are required !", Toast.LENGTH_LONG).show();
                } else if(get_password.length()< 6){
                    Toast.makeText(Register.this,"Password must at least 6 char long", Toast.LENGTH_LONG).show();
                } else if (!get_password.equals(get_re_password)){
                    Toast.makeText(Register.this, "Password not match", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Registering");
                    progressDialog.setMessage("please wait while we create your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    cls_register(get_email, get_user, get_password);
                }
            }
        });

    }

    public void cls_register(final String email, final String user_name, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                            String uId= currentUser.getUid();
                            databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(uId);
                            databaseReference.keepSynced(true);

                            databaseReference_check= FirebaseDatabase.getInstance().getReference("Check_User_Key");
                            databaseReference_check.keepSynced(true);

                            String myKey= databaseReference.getKey();

                            HashMap<String, String> hashMap= new HashMap<>();
                            hashMap.put("name", user_name);
                            hashMap.put("status", "Hi everyone i am here");
                            hashMap.put("location", "Dhaka, Bangladesh");
                            hashMap.put("occupation", "Jobs Holders");
                            hashMap.put("gender", "default");
                            hashMap.put("age", "default");
                            hashMap.put("number", "default");
                            hashMap.put("email", "default");
                            hashMap.put("profession", "default");
                            hashMap.put("institute", "default");
                            hashMap.put("bio", "default");
                            hashMap.put("imageuri", "default");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this,"Register successfully !!", Toast.LENGTH_LONG).show();
                                        Intent intent= new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                            databaseReference_check.setValue(myKey).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this,"your check key is uploaded",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.hide();
                            Toast.makeText(Register.this, "invalid Username or Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}