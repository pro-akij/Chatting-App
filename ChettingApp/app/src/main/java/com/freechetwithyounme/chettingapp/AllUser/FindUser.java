package com.freechetwithyounme.chettingapp.AllUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class FindUser extends AppCompatActivity {
    //private DatabaseReference databaseReference;
    private DatabaseReference searchFrndRef;
    private String uID;

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EditText searchText;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        toolbar = findViewById(R.id.toolbar_allfinder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.finder_recylarView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //databaseReference = FirebaseDatabase.getInstance().getReference("User");
        searchFrndRef = FirebaseDatabase.getInstance().getReference("User");

        searchText= findViewById(R.id.enter_search_text);
        searchButton= findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSearchData= searchText.getText().toString();
                if (getSearchData.isEmpty()){
                    Toast.makeText(FindUser.this, "you can't search null data", Toast.LENGTH_SHORT).show();
                } else {
                    SearchYourValue(getSearchData);
                }
            }
        });
    }


    public void SearchYourValue(String getSearchData) {

        Query searctQuery = searchFrndRef.orderByChild("name")
                .startAt(getSearchData).endAt(getSearchData + "\uf8ff");

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(searctQuery, User.class)
                .build();
        final FirebaseRecyclerAdapter<User, UserViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final UserViewHolder holder, int position, @NonNull final User model) {
                        final String passUid = getRef(position).getKey();
                        uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        holder.textView.setText(model.getName());
                        holder.textView1.setText(model.getStatus());

                        Picasso.with(FindUser.this)
                                .load(model.getImageuri())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(FindUser.this)
                                                .load(model.getImageuri())
                                                .placeholder(R.mipmap.ic_launcher)
                                                .into(holder.imageView);
                                    }
                                });

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FindUser.this, UserProfile.class);
                                intent.putExtra("passuID", passUid);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.user_sample, null);
                        return new UserViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView;
        TextView textView1;
        ImageView imageView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            textView = itemView.findViewById(R.id.all_user_display_name);
            textView1 = itemView.findViewById(R.id.all_user_status);
            imageView = itemView.findViewById(R.id.all_user_profile_image);
        }
    }

}