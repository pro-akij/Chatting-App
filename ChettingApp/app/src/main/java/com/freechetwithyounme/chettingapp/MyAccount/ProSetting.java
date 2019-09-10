package com.freechetwithyounme.chettingapp.MyAccount;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.Model.User_Update;
import com.freechetwithyounme.chettingapp.R;
import com.freechetwithyounme.chettingapp.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProSetting extends AppCompatActivity {
    private static final int GELLARY_REQUEST_CODE= 1;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private Uri imageUri;
    int count=0, count1=0;

    private ProgressDialog progressDialog;
    private Button image_choose, update;
    private MaterialEditText edit_displayName, edit_status, edit_location,
            edit_number, edit_email, edit_company, edit_institute, edit_bio, enter_Age;

    private Spinner mUserGender;
    private Toolbar toolbar;
    private RadioButton Std, Jobs;
    private RadioGroup radioGroup;

    private String[] mUser_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_setting);
        Initialize();
        toolbar= findViewById(R.id.toolbar_prositting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Profile Setting");

        radioGroup= findViewById(R.id.radiobtnGroup);
        Std= findViewById(R.id.selectStudents);
        Jobs= findViewById(R.id.selectJobs);

        Std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProSetting.this, "Selected"+Std.getText().toString(), Toast.LENGTH_SHORT).show();
                edit_institute.setVisibility(View.VISIBLE);
                edit_company.setVisibility(View.INVISIBLE);
                count++;
                count1=0;
            }
        });
        Jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProSetting.this, "Selected"+Jobs.getText().toString(), Toast.LENGTH_SHORT).show();
                edit_company.setVisibility(View.VISIBLE);
                edit_institute.setVisibility(View.INVISIBLE);
                count1++;
                count=0;
            }
        });

        mUser_gender= getResources().getStringArray(R.array.user_gander);
        ArrayAdapter<String> userGender= new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, mUser_gender);
        mUserGender.setAdapter(userGender);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uId= currentUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(uId);

        storageReference= FirebaseStorage.getInstance().getReference("update");

        progressDialog= new ProgressDialog(this);

        image_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallary= new Intent();
                gallary.setType("image/*");
                gallary.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallary, GELLARY_REQUEST_CODE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d_n= edit_displayName.getText().toString().trim();
                String st= edit_status.getText().toString().trim();
                String lo= edit_location.getText().toString().trim();
                String num= edit_number.getText().toString().trim();
                String email= edit_email.getText().toString().trim();
                String bio= edit_bio.getText().toString().trim();

                if (d_n.isEmpty()){
                    edit_displayName.setError("Enter status");
                    edit_displayName.requestFocus();
                    return;
                } else if (st.isEmpty()){
                    edit_status.setError("Enter status");
                    edit_status.requestFocus();
                    return;
                } else if(lo.isEmpty()) {
                    edit_location.setError("Enter location");
                    edit_location.requestFocus();
                    return;
                } else if (num.isEmpty()){
                    edit_number.setError("Enter Contact Number");
                    edit_number.requestFocus();
                    return;
                } else if (email.isEmpty()){
                    edit_email.setError("Enter Contact Email");
                    edit_email.requestFocus();
                    return;
                } else if (bio.isEmpty()){
                    edit_bio.setError("Enter somethings about you...");
                    edit_bio.requestFocus();
                    return;
                } else{
                    progressDialog.setTitle("Updating User Info");
                    progressDialog.setMessage("please wait while we update your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    saveProfileData();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GELLARY_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            imageUri= data.getData();
        }
    }

    public String getFileExtantion(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void saveProfileData(){

        final String dis_Name= edit_displayName.getText().toString().trim();
        final String sta= edit_status.getText().toString().trim();
        final String loc= edit_location.getText().toString().trim();
        final String gender= mUserGender.getSelectedItem().toString();
        final String age= enter_Age.getText().toString();
        final String num= edit_number.getText().toString().trim();
        final String email= edit_email.getText().toString().trim();
        final String com= edit_company.getText().toString().trim();
        final String ins= edit_institute.getText().toString().trim();
        final String bio= edit_bio.getText().toString().trim();

        if (dis_Name.isEmpty()){
            edit_displayName.setError("Enter status");
            edit_displayName.requestFocus();
            return;
        } else if (sta.isEmpty()){
            edit_status.setError("Enter status");
            edit_status.requestFocus();
            return;
        } else if(loc.isEmpty()) {
            edit_location.setError("Enter location");
            edit_location.requestFocus();
            return;
        } else if (num.isEmpty()){
            edit_number.setError("Enter Contact Number");
            edit_number.requestFocus();
            return;
        } else if (email.isEmpty()){
            edit_email.setError("Enter Contact Email");
            edit_email.requestFocus();
            return;
        } else if (bio.isEmpty()){
            edit_bio.setError("Enter somethings about you...");
            edit_bio.requestFocus();
            return;
        }

        StorageReference reference= storageReference.child(System.currentTimeMillis()+"."+getFileExtantion(imageUri));

        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri getUri= uriTask.getResult();

                        HashMap<String, String> updateHashMap= new HashMap<>();
                        updateHashMap.put("name", dis_Name);
                        updateHashMap.put("status", sta);
                        updateHashMap.put("location", loc);
                        updateHashMap.put("gender", gender);
                        updateHashMap.put("age", age);
                        updateHashMap.put("number", num);
                        updateHashMap.put("email", email);
                        if (count1>0){
                            updateHashMap.put("profession", "Job Holder");
                            updateHashMap.put("institute", com);
                        }
                        if (count>0){
                            updateHashMap.put("profession", "Studests");
                            updateHashMap.put("institute", ins);
                        }
                        updateHashMap.put("bio", bio);
                        updateHashMap.put("imageuri", getUri.toString());

                        databaseReference.setValue(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(ProSetting.this,"Update successfully !!", Toast.LENGTH_LONG).show();
                                    Intent intent= new Intent(ProSetting.this, MyProfile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ProSetting.this,"Update not successfully",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void Initialize(){
        edit_displayName= findViewById(R.id.edit_display_name);
        image_choose= findViewById(R.id.choose_profile_image);
        edit_status= findViewById(R.id.edit_status);
        edit_location= findViewById(R.id.edit_location);
        update= findViewById(R.id.update_info);
        mUserGender= findViewById(R.id.select_gender);
        enter_Age= findViewById(R.id.enter_age);
        edit_number= findViewById(R.id.contact_number);
        edit_email= findViewById(R.id.contact_email);
        edit_company= findViewById(R.id.your_company);
        edit_institute= findViewById(R.id.your_institute);
        edit_bio= findViewById(R.id.profile_bio);
    }

    @Override
    public void onBackPressed() {
    }
}
