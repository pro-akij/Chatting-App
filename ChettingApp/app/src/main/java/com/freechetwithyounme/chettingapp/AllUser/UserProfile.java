package com.freechetwithyounme.chettingapp.AllUser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class UserProfile extends AppCompatActivity {
    private ImageView mImageView;
    private TextView displayName, displayGender, displayBirthdate, displayStatus, displayLocation,
            displayEmail, displayProfession, displayInstitute, displayBio;
    private Button sendRequest, cancleRequest;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference frndReqDatabase;
    private DatabaseReference frndDatabase;
    private String currentUser;
    private String recieveUid;

    private Toolbar toolbar;
    private String user_state= "not_friend";
    String disply_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar= findViewById(R.id.user_profile_app_ber);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Initialize();

        recieveUid= getIntent().getStringExtra("passuID");
        currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseReference= FirebaseDatabase.getInstance().getReference("User").child(recieveUid);
        mDatabaseReference.keepSynced(true);
        frndReqDatabase= FirebaseDatabase.getInstance().getReference("FriendRequest");
        frndReqDatabase.keepSynced(true);
        frndDatabase= FirebaseDatabase.getInstance().getReference("Friends");
        frndDatabase.keepSynced(true);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                disply_image= dataSnapshot.child("imageuri").getValue().toString();
                String disply_name= dataSnapshot.child("name").getValue().toString();
                String disply_status= dataSnapshot.child("status").getValue().toString();
                String disply_gender= dataSnapshot.child("gender").getValue().toString();
                String disply_birthday= dataSnapshot.child("age").getValue().toString();
                String disply_location= dataSnapshot.child("location").getValue().toString();
                String disply_email= dataSnapshot.child("email").getValue().toString();
                String disply_profession= dataSnapshot.child("profession").getValue().toString();
                String disply_institute= dataSnapshot.child("institute").getValue().toString();
                String disply_bio= dataSnapshot.child("bio").getValue().toString();

                displayName.setText(disply_name);
                displayGender.setText(disply_gender);
                displayBirthdate.setText(disply_birthday);
                displayStatus.setText(disply_status);
                displayLocation.setText(disply_location);
                displayEmail.setText(disply_email);
                displayProfession.setText(disply_profession);
                displayInstitute.setText(disply_institute);
                displayBio.setText(disply_bio);

                Picasso.with(UserProfile.this)
                        .load(disply_image)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(mImageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(UserProfile.this)
                                        .load(disply_image)
                                        .into(mImageView);
                            }
                        });

                ////----------------friend recieve / friend list-----------

                frndReqDatabase.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(recieveUid)){
                            String req_type= dataSnapshot.child(recieveUid).child("request_type").getValue().toString();
                            if (req_type.equals("receive")){
                                user_state= "request_accept";
                                sendRequest.setText("Accept Request");

                                cancleRequest.setVisibility(View.VISIBLE);
                                cancleRequest.setEnabled(true);

                                cancleRequest.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CencelFriendRequest();
                                    }
                                });
                            } else if (req_type.equals("send")){
                                user_state="request_send";
                                sendRequest.setText("cancel request");

                                cancleRequest.setVisibility(View.INVISIBLE);
                                cancleRequest.setEnabled(false);
                            }
                        } else {
                            frndDatabase.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(recieveUid)){
                                        user_state="friend";
                                        sendRequest.setText("Unfriend");

                                        cancleRequest.setVisibility(View.INVISIBLE);
                                        cancleRequest.setEnabled(false);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cancleRequest.setVisibility(View.INVISIBLE);
        cancleRequest.setEnabled(false);


        if (recieveUid.equals(currentUser)){
            Toast.makeText(this, "Hey Man this is you !!", Toast.LENGTH_SHORT).show();
            sendRequest.setEnabled(false);
        } else {
            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequest.setEnabled(false);

                    if (user_state == "not_friend") {
                        SendFriendRequest();
                    }
                    if (user_state == "request_send") {
                        CencelFriendRequest();
                    }
                    if (user_state.equals("request_accept")) {
                        AcceptRequest();
                    }
                    if (user_state.equals("friend")) {
                        Unfriends();
                    }
                }
            });
        }
    }


    public void Initialize() {
        mImageView = findViewById(R.id.display_profile_image);
        displayName = findViewById(R.id.display_profile_name);
        displayGender= findViewById(R.id.display_profile_gender);
        displayBirthdate= findViewById(R.id.display_profile_birth);
        displayStatus = findViewById(R.id.display_profile_status);
        displayLocation = findViewById(R.id.display_profile_location);
        displayEmail = findViewById(R.id.display_profile_email);
        displayProfession = findViewById(R.id.display_profile_profession);
        displayInstitute = findViewById(R.id.display_profile_institute);
        displayBio = findViewById(R.id.display_profile_bio);
        sendRequest = findViewById(R.id.send_frnd_request);
        cancleRequest = findViewById(R.id.cancel_frnd_request);
    }

    public void SendFriendRequest() {
        frndReqDatabase.child(currentUser).child(recieveUid).child("request_type")
                .setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    frndReqDatabase.child(recieveUid).child(currentUser).child("request_type")
                            .setValue("receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendRequest.setEnabled(true);
                            user_state = "request_send";
                            sendRequest.setText("cancel request");

                            cancleRequest.setVisibility(View.INVISIBLE);
                            cancleRequest.setEnabled(false);
                            Toast.makeText(UserProfile.this, "request send successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(UserProfile.this, "failed send request", Toast.LENGTH_LONG).show();
                }
                sendRequest.setEnabled(true);
            }
        });
    }

    public void CencelFriendRequest() {
        frndReqDatabase.child(currentUser).child(recieveUid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        frndReqDatabase.child(recieveUid).child(currentUser).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        sendRequest.setEnabled(true);
                                        user_state = "not_friend";
                                        sendRequest.setText("send friend request");

                                        cancleRequest.setVisibility(View.INVISIBLE);
                                        cancleRequest.setEnabled(false);
                                    }
                                });
                    }
                });
    }

    public void AcceptRequest(){
        final String getTime= DateFormat.getDateTimeInstance().format(new Date());

        frndDatabase.child(currentUser).child(recieveUid).child("date").setValue(getTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        frndDatabase.child(recieveUid).child(currentUser).child("date").setValue(getTime)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        frndReqDatabase.child(currentUser).child(recieveUid).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        frndReqDatabase.child(recieveUid).child(currentUser).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        sendRequest.setEnabled(true);
                                                                        user_state="friend";
                                                                        sendRequest.setText("Unfriend");

                                                                        cancleRequest.setVisibility(View.INVISIBLE);
                                                                        cancleRequest.setEnabled(false);
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public void Unfriends(){
        frndDatabase.child(currentUser).child(recieveUid).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                frndDatabase.child(recieveUid).child(currentUser).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            sendRequest.setEnabled(true);
                            user_state= "not_friend";
                            sendRequest.setText("send request");

                            cancleRequest.setVisibility(View.INVISIBLE);
                            cancleRequest.setEnabled(false);
                        }
                    }
                });
            }
        });
    }
}
