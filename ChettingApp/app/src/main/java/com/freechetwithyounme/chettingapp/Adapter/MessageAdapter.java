package com.freechetwithyounme.chettingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.Model.Chat;
import com.freechetwithyounme.chettingapp.Model.Message;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.R;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static final int MSG_LEFT= 0;
    private static final int MSG_RIGHT= 1;
    private Context context;
    private List<Chat> chatList;
    private String imageuri;

    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chatList, String imageuri) {
        this.context = context;
        this.chatList = chatList;
        this.imageuri= imageuri;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_RIGHT) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_right, null);
            return new MessageViewHolder(view);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_left, null);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i) {

        Chat chat = chatList.get(i);
        String msg_type= chat.getType().toString();
        //if (msg_type== "text"){
            //messageViewHolder.msg_imageView.setVisibility(View.INVISIBLE);
            messageViewHolder.textView.setText(chat.getMessage());
            Picasso.with(context)
                    .load(imageuri)
                    .into(messageViewHolder.imageView);
         /*else {
            messageViewHolder.textView.setVisibility(View.INVISIBLE);
            Picasso.with(context)
                    .load(imageuri)
                    .into(messageViewHolder.imageView);
            Picasso.with(context)
                    .load(chat.getChatimage())
                    .into(messageViewHolder.msg_imageView);
        }*/
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView textView;
        ImageView imageView;
        ImageView msg_imageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            textView = itemView.findViewById(R.id.message_chatID);
            imageView = itemView.findViewById(R.id.message_profle_image);
            msg_imageView= itemView.findViewById(R.id.message_imageID);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSenderID().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        } else {
            return MSG_LEFT;
        }
    }
}