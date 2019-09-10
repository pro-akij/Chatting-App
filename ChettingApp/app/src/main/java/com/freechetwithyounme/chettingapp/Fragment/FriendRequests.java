package com.freechetwithyounme.chettingapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.DateFormat;
import java.util.Date;


public class FriendRequests extends Fragment {

    private DatabaseReference friendReqReference;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;
    private DatabaseReference frndReqDatabase;
    private DatabaseReference frndDatabase;

    private RecyclerView recyclerView_reqstData;
    private String currentUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);

        recyclerView_reqstData= view.findViewById(R.id.my_frnd_request_list);
        recyclerView_reqstData.setHasFixedSize(true);
        recyclerView_reqstData.setLayoutManager(new LinearLayoutManager(getContext()));

        userAuth= FirebaseAuth.getInstance();
        currentUserID= userAuth.getCurrentUser().getUid();

        friendReqReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child(currentUserID);
        friendReqReference.keepSynced(true);
        userReference= FirebaseDatabase.getInstance().getReference("User");
        //userReference.keepSynced(true);
        frndReqDatabase= FirebaseDatabase.getInstance().getReference("FriendRequest");
        //frndReqDatabase.keepSynced(true);
        frndDatabase= FirebaseDatabase.getInstance().getReference("Friends");
        //frndDatabase.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(friendReqReference, User.class)
                .build();
        final FirebaseRecyclerAdapter<User, UserFRViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, UserFRViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserFRViewHolder holder, int position, @NonNull final User model) {
                        final String GetUid = getRef(position).getKey();
                        DatabaseReference getReceiveType= getRef(position).child("request_type").getRef();

                        getReceiveType.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    String requestType= dataSnapshot.getValue().toString();
                                    if (requestType.equals("receive")){

                                        userReference.child(GetUid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String disply_name= dataSnapshot.child("name").getValue().toString();
                                                String disply_status= dataSnapshot.child("status").getValue().toString();
                                                final String disply_image= dataSnapshot.child("imageuri").getValue().toString();

                                                holder.textView.setText(disply_name);
                                                holder.textView1.setText(disply_status);

                                                Picasso.with(getActivity())
                                                        .load(disply_image)
                                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                                        .into(holder.imageView, new Callback() {
                                                            @Override
                                                            public void onSuccess() {

                                                            }

                                                            @Override
                                                            public void onError() {
                                                                Picasso.with(getActivity())
                                                                        .load(disply_image)
                                                                        .into(holder.imageView);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        holder.add.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                final String getTime= DateFormat.getDateTimeInstance().format(new Date());

                                                frndDatabase.child(currentUserID).child(GetUid).child("date").setValue(getTime)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                frndDatabase.child(GetUid).child(currentUserID).child("date").setValue(getTime)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                frndReqDatabase.child(currentUserID).child(GetUid).removeValue()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                frndReqDatabase.child(GetUid).child(currentUserID).removeValue()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        });
                                                                            }
                                                                        });
                                                            }
                                                        });

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
                                                                                Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });
                                    } else {
                                        recyclerView_reqstData.setVisibility(View.INVISIBLE);
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
                    public UserFRViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.frndreqst_smpl, null);
                        return new UserFRViewHolder(view);
                    }
                };
        recyclerView_reqstData.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserFRViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView;
        TextView textView1;
        ImageView imageView;
        Button add, delete;

        public UserFRViewHolder(@NonNull View itemView) {
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