package com.freechetwithyounme.chettingapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.AllUser.UserProfile;
import com.freechetwithyounme.chettingapp.Chat.ChatActivity;
import com.freechetwithyounme.chettingapp.Model.FriendList;
import com.freechetwithyounme.chettingapp.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Friends extends Fragment {

    private RecyclerView mRecyclerView_frnd_list;
    private DatabaseReference friendsReference;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;

    private String friendsUserID;

    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mRecyclerView_frnd_list = view.findViewById(R.id.my_frnd_list);
        mRecyclerView_frnd_list.setHasFixedSize(true);
        mRecyclerView_frnd_list.setLayoutManager(new LinearLayoutManager(getContext()));

        userAuth = FirebaseAuth.getInstance();
        friendsUserID = userAuth.getCurrentUser().getUid();

        friendsReference = FirebaseDatabase.getInstance().getReference("Friends").child(friendsUserID);
        userReference = FirebaseDatabase.getInstance().getReference("User");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<FriendList> options = new FirebaseRecyclerOptions.Builder<FriendList>()
                .setQuery(friendsReference, FriendList.class)
                .build();
        final FirebaseRecyclerAdapter<FriendList, FriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<FriendList, FriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull final FriendList model) {
                        final String GetUid = getRef(position).getKey();
                        holder.textView2.setText(model.getDate());

                        userReference.child(GetUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                final String disply_name = dataSnapshot.child("name").getValue().toString();
                                String disply_status = dataSnapshot.child("status").getValue().toString();
                                final String disply_image = dataSnapshot.child("imageuri").getValue().toString();

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

                                if (dataSnapshot.hasChild("online")) {
                                    String online_status = (String) dataSnapshot.child("online").getValue().toString();
                                    holder.setUserIcon(online_status);
                                }

                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence charSequence[] = new CharSequence[]{
                                                disply_name + "'s Profile",
                                                "Message"
                                        };
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Select");

                                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (which == 0) {
                                                    Intent intent = new Intent(getActivity(), UserProfile.class);
                                                    intent.putExtra("passuID", GetUid);
                                                    startActivity(intent);
                                                } else if (which == 1) {

                                                    if (dataSnapshot.child("online").exists()) {

                                                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                        intent.putExtra("passuID", GetUid);
                                                        intent.putExtra("passNAME", disply_name);
                                                        //intent.putExtra("passIMAGE", disply_image);
                                                        startActivity(intent);
                                                    } else {
                                                        userReference.child(GetUid).child("online").setValue(ServerValue.TIMESTAMP)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                                        intent.putExtra("passuID", GetUid);
                                                                        intent.putExtra("passNAME", disply_name);
                                                                        //intent.putExtra("passIMAGE", disply_image);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                    }
                                                }

                                            }
                                        });
                                        builder.create();
                                        builder.show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.friendlist_smpl, null);
                        return new FriendsViewHolder(view);
                    }
                };
        mRecyclerView_frnd_list.setAdapter(adapter);
        adapter.startListening();
    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView;
        TextView textView1;
        TextView textView2;
        ImageView imageView;
        ImageView icon;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            textView = mView.findViewById(R.id.frndlist_display_name);
            textView1 = mView.findViewById(R.id.frndlist_status);
            textView2 = mView.findViewById(R.id.frndlist_date);
            imageView = mView.findViewById(R.id.frndlist_profile_image);
        }

        public void setUserIcon(String online_status) {
            icon = mView.findViewById(R.id.online_icon);
            if (online_status.equals("true")) {
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.INVISIBLE);
            }
        }
    }

}
