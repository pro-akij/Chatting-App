package com.freechetwithyounme.chettingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LonIn extends AppCompatActivity {
    private EditText mlog_user, mlog_password;
    private Button mlogin;
    private TextView goRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lon_in);

        toolbar= findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        setTitle("Sign In");

        mlog_user= findViewById(R.id.log_username);
        mlog_password= findViewById(R.id.log_password);
        mlogin= findViewById(R.id.log_in);
        goRegister= findViewById(R.id.logIN_register);
        progressDialog= new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Log in User_Update");
                progressDialog.setMessage("please wait while we create your account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String email= mlog_user.getText().toString();
                String password= mlog_password.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Fill properly",Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent= new Intent(LonIn.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Longd Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LonIn.this, Register.class);
                startActivity(intent);
            }
        });
    }

}
