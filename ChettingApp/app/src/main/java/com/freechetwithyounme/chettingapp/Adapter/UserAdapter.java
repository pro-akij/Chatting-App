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

import com.freechetwithyounme.chettingapp.Fragment.FriendRequests;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.frndreqst_smpl, null);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
       final User user = userList.get(i);
        userViewHolder.textView.setText(user.getName());
        userViewHolder.textView1.setText(user.getStatus());
        Picasso.with(context)
                .load(user.getImageuri())
                .into(userViewHolder.imageView);

        userViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"added frnd list",Toast.LENGTH_LONG).show();
            }
        });
        userViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"delete this frnd",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView textView, textView1;
        ImageView imageView;
        Button add, delete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            textView = itemView.findViewById(R.id.my_frnd_display_name);
            textView1 = itemView.findViewById(R.id.my_frnd_status);
            imageView = itemView.findViewById(R.id.my_frnd_profile_image);
            add= itemView.findViewById(R.id.add_my_frnd_list);
            delete= itemView.findViewById(R.id.delete_frnd_list);
        }
    }
}

