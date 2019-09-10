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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.AllUser.UserProfile;
import com.freechetwithyounme.chettingapp.Chat.ChatActivity;
import com.freechetwithyounme.chettingapp.Model.FriendList;
import com.freechetwithyounme.chettingapp.Model.ViewChatList;
import com.freechetwithyounme.chettingapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ChatList extends Fragment {

    private DatabaseReference mChatRef;
    private DatabaseReference userReference;
    private FirebaseAuth userAuth;

    private String currentUserID;

    private RecyclerView chatListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        chatListView= view.findViewById(R.id.chatListView);
        chatListView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatListView.setLayoutManager(linearLayoutManager);

        userAuth= FirebaseAuth.getInstance();
        currentUserID= userAuth.getCurrentUser().getUid();

        /*tryal= FirebaseDatabase.getInstance().getReference("Chats");
        String chatKey= tryal.getKey();*/
        mChatRef= FirebaseDatabase.getInstance().getReference("Chats").child(currentUserID);
        userReference= FirebaseDatabase.getInstance().getReference("User");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ViewChatList> options = new FirebaseRecyclerOptions.Builder<ViewChatList>()
                .setQuery(mChatRef, ViewChatList.class)
                .build();
        final FirebaseRecyclerAdapter<ViewChatList, ChatViewHolder> adapter =
                new FirebaseRecyclerAdapter<ViewChatList, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull final ViewChatList model) {
                        final String GetUid = getRef(position).getKey();

                        userReference.child(GetUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                final String disply_name= dataSnapshot.child("name").getValue().toString();
                                final String disply_status= dataSnapshot.child("status").getValue().toString();
                                final String disply_image= dataSnapshot.child("imageuri").getValue().toString();

                                holder.dName.setText(disply_name);
                                holder.dMessage.setText(disply_status);
                                Picasso.with(getActivity())
                                        .load(disply_image)
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .into(holder.pImage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }
                                            @Override
                                            public void onError() {
                                                Picasso.with(getActivity())
                                                        .load(disply_image)
                                                        .into(holder.pImage);
                                            }
                                        });

                                if (dataSnapshot.hasChild("online")){
                                    String online_status= (String) dataSnapshot.child("online").getValue().toString();
                                    holder.setUserIcon(online_status);
                                }

                                holder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (dataSnapshot.child("online").exists()){

                                            Intent intent= new Intent(getActivity(), ChatActivity.class);
                                            intent.putExtra("passuID", GetUid);
                                            intent.putExtra("passNAME", disply_name);
                                            //intent.putExtra("passIMAGE", disply_image);
                                            startActivity(intent);
                                        }
                                        else {
                                            userReference.child(GetUid).child("online").setValue(ServerValue.TIMESTAMP)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent= new Intent(getActivity(), ChatActivity.class);
                                                            intent.putExtra("passuID", GetUid);
                                                            intent.putExtra("passNAME", disply_name);
                                                            //intent.putExtra("passIMAGE", disply_image);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
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
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.friendlist_smpl, null);
                        return new ChatViewHolder(view);
                    }
                };
        chatListView.setAdapter(adapter);
        adapter.startListening();

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView dName;
        TextView dMessage;
        ImageView pImage;
        ImageView online_icon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            mView= itemView;
            dName= mView.findViewById(R.id.frndlist_display_name);
            dMessage= mView.findViewById(R.id.frndlist_status);
            pImage= mView.findViewById(R.id.frndlist_profile_image);
        }

        public void setUserIcon(String online_status) {
            online_icon= mView.findViewById(R.id.online_icon);
            if (online_status.equals("true")){
                online_icon.setVisibility(View.VISIBLE);
            } else {
                online_icon.setVisibility(View.INVISIBLE);
            }
        }
    }
}
