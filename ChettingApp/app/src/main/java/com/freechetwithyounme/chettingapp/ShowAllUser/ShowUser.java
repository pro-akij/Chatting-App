package com.freechetwithyounme.chettingapp.ShowAllUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.AllUser.FindUser;
import com.freechetwithyounme.chettingapp.AllUser.UserProfile;
import com.freechetwithyounme.chettingapp.Model.GridModel;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ShowUser extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String uID;
    //private FirebaseRecyclerAdapter<GridModel, GridViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.user_recyclerView);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("User");

    }


    @Override
    protected void onStart() {
        super.onStart();

        /*Query searctQuery = searchFrndRef.orderByChild("name")
                .startAt(getSearchData).endAt(getSearchData + "\uf8ff");*/

        FirebaseRecyclerOptions<GridModel> options = new FirebaseRecyclerOptions.Builder<GridModel>()
                .setQuery(databaseReference, GridModel.class)
                .build();
        final FirebaseRecyclerAdapter<GridModel, GridViewHolder> adapter =
                new FirebaseRecyclerAdapter<GridModel, GridViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final GridViewHolder holder, int position, @NonNull final GridModel model) {
                        final String passUid = getRef(position).getKey();
                        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        holder.gName.setText(model.getName());
                        holder.gLocation.setText(model.getLocation());

                        Picasso.with(ShowUser.this)
                                .load(model.getImageuri())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.gImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(ShowUser.this)
                                                .load(model.getImageuri())
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(holder.gImageView);
                                    }
                                });

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShowUser.this, UserProfile.class);
                                intent.putExtra("passuID", passUid);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.gridsample, null);
                        return new GridViewHolder(view);
                    }
                };
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView gName;
        TextView gLocation;
        ImageView gImageView;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            gName = itemView.findViewById(R.id.gridName);
            gLocation = itemView.findViewById(R.id.gridLocation);
            gImageView = itemView.findViewById(R.id.gridImage);
        }
    }
}
