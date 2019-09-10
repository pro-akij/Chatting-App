package com.freechetwithyounme.chettingapp.MyAccount;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private Toolbar toolbar;

    private ImageView my_profile_imageView;
    private TextView my_Name, my_status, my_location, myNum, myEmail, myPro,
            myIns, myBio, myGender, myAge;
    private Button edit_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Initialize();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("User Profile");


        try {
            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            String getUID= firebaseUser.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference("User").child(getUID);
            databaseReference.keepSynced(true);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String getName= dataSnapshot.child("name").getValue().toString();
                    String getStatus= dataSnapshot.child("status").getValue().toString();
                    String getLocation= dataSnapshot.child("location").getValue().toString();
                    String getGender= dataSnapshot.child("gender").getValue().toString();
                    String getAge= dataSnapshot.child("age").getValue().toString();
                    String getNumber= dataSnapshot.child("number").getValue().toString();
                    String getEmail= dataSnapshot.child("email").getValue().toString();
                    String getProfession= dataSnapshot.child("profession").getValue().toString();
                    String getInstitute= dataSnapshot.child("institute").getValue().toString();
                    String getBio= dataSnapshot.child("bio").getValue().toString();
                    String getImage= dataSnapshot.child("imageuri").getValue().toString();

                    my_Name.setText(getName);
                    my_status.setText(getStatus);
                    my_location.setText(getLocation);
                    //my_occupation.setText(getOccupation);
                    myGender.setText(getGender);
                    myAge.setText(getAge);
                    myNum.setText(getNumber);
                    myEmail.setText(getEmail);
                    myPro.setText(getProfession);
                    myIns.setText(getInstitute);
                    myBio.setText(getBio);
                    Picasso.with(MyProfile.this).load(getImage).into(my_profile_imageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e){
            Toast.makeText(MyProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MyProfile.this, ProSetting.class);
                startActivity(intent);
            }
        });

    }

    public void Initialize(){
        toolbar= findViewById(R.id.toolbar_myprofile);
        my_profile_imageView= findViewById(R.id.myimage);
        my_Name= findViewById(R.id.displyname);
        my_status= findViewById(R.id.status);
        my_location= findViewById(R.id.location);
        //my_occupation= findViewById(R.id.occupation);
        edit_profile= findViewById(R.id.edit_profile);
        myGender= findViewById(R.id.my_gender);
        myAge= findViewById(R.id.my_age);
        myNum= findViewById(R.id.my_num);
        myEmail= findViewById(R.id.my_email);
        myPro= findViewById(R.id.my_profession);
        myIns= findViewById(R.id.my_institute);
        myBio= findViewById(R.id.my_bio);
    }

}


/*

<LinearLayout
                        android:layout_width="0sp"
                        android:layout_height="match_parent"
                        android:layout_weight="1.5"
                        android:orientation="vertical"
                        android:layout_marginTop="5sp">

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:text="@string/name" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/gender"/>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/birthdate" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/status" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/location"/>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/occupation"/>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/p_number" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/email" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/company" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/institute" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/profilebio" />

                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="0sp"
                        android:layout_height="match_parent"
                        android:layout_weight="3"
                        android:orientation="vertical"
                        android:layout_marginTop="5sp">

                        <TextView
                            android:id="@+id/displyname"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:text="@string/d_name" />

                        <TextView
                            android:id="@+id/my_gender"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/gender" />

                        <TextView
                            android:id="@+id/my_age"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/birthdate" />

                        <TextView
                            android:id="@+id/status"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/d_status" />

                        <TextView
                            android:id="@+id/location"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/location"/>

                        <TextView
                            android:id="@+id/occupation"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/occupation"/>

                        <TextView
                            android:id="@+id/my_num"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/p_number" />

                        <TextView
                            android:id="@+id/my_email"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/email" />

                        <TextView
                            android:id="@+id/my_company"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/company" />

                        <TextView
                            android:id="@+id/my_institute"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/institute" />

                        <TextView
                            android:id="@+id/my_bio"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:textColor="@color/colorWhite"
                            android:textSize="18sp"
                            android:layout_marginTop="5sp"
                            android:text="@string/profilebio" />

                    </LinearLayout>



 */