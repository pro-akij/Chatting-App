package com.freechetwithyounme.chettingapp.ViewRequest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ViewSendReq extends AppCompatActivity {

    private DatabaseReference friendReqReference;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;
    private DatabaseReference frndReqDatabase;
    private DatabaseReference frndDatabase;
    private String currentUserID;

    private RecyclerView viewSendFriendReqVew;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_send_req);

        viewSendFriendReqVew= findViewById(R.id.viewSendFriendReq);
        viewSendFriendReqVew.setHasFixedSize(true);
        viewSendFriendReqVew.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Vew Send Request");
        toolbar = findViewById(R.id.toolbar_vewSendReq);
        setSupportActionBar(toolbar);

        userAuth= FirebaseAuth.getInstance();
        currentUserID= userAuth.getCurrentUser().getUid();

        friendReqReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child(currentUserID);
        friendReqReference.keepSynced(true);
        userReference= FirebaseDatabase.getInstance().getReference("User");
        userReference.keepSynced(true);
        frndReqDatabase= FirebaseDatabase.getInstance().getReference("FriendRequest");
        frndReqDatabase.keepSynced(true);
        frndDatabase= FirebaseDatabase.getInstance().getReference("Friends");
        frndDatabase.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(friendReqReference, User.class)
                .build();
        final FirebaseRecyclerAdapter<User, UserSendFRViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, UserSendFRViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserSendFRViewHolder holder, int position, @NonNull final User model) {
                        final String GetUid = getRef(position).getKey();
                        DatabaseReference getSendType= getRef(position).child("request_type").getRef();

                        getSendType.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    String requestType= dataSnapshot.getValue().toString();
                                    if (requestType.equals("send")){

                                        holder.add.setVisibility(View.INVISIBLE);
                                        userReference.child(GetUid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String disply_name= dataSnapshot.child("name").getValue().toString();
                                                String disply_status= dataSnapshot.child("status").getValue().toString();
                                                final String disply_image= dataSnapshot.child("imageuri").getValue().toString();

                                                holder.textView.setText(disply_name);
                                                holder.textView1.setText(disply_status);

                                                Picasso.with(ViewSendReq.this)
                                                        .load(disply_image)
                                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                                        .into(holder.imageView, new Callback() {
                                                            @Override
                                                            public void onSuccess() {

                                                            }

                                                            @Override
                                                            public void onError() {
                                                                Picasso.with(ViewSendReq.this)
                                                                        .load(disply_image)
                                                                        .into(holder.imageView);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        holder.delete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                frndReqDatabase.child(currentUserID).child(GetUid).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                frndReqDatabase.child(GetUid).child(currentUserID).removeValue()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                Toast.makeText(ViewSendReq.this, "Request Removed", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });

                                    } else {
                                        viewSendFriendReqVew.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ViewSendReq.this, "No Request Found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public UserSendFRViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.frndreqst_smpl, null);
                        return new UserSendFRViewHolder(view);
                    }
                };
        viewSendFriendReqVew.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserSendFRViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView;
        TextView textView1;
        ImageView imageView;
        Button add, delete;

        public UserSendFRViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            textView = itemView.findViewById(R.id.my_frnd_display_name);
            textView1 = itemView.findViewById(R.id.my_frnd_status);
            imageView = itemView.findViewById(R.id.my_frnd_profile_image);
            add = itemView.findViewById(R.id.add_my_frnd_list);
            delete = itemView.findViewById(R.id.delete_frnd_list);
        }
    }
}
