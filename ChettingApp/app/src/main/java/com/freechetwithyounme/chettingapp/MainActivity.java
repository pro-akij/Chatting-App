package com.freechetwithyounme.chettingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.AllUser.FindUser;
import com.freechetwithyounme.chettingapp.Fragment.ChatList;
import com.freechetwithyounme.chettingapp.Fragment.FriendRequests;
import com.freechetwithyounme.chettingapp.Fragment.Friends;
import com.freechetwithyounme.chettingapp.MyAccount.MyProfile;

import com.freechetwithyounme.chettingapp.ShowAllUser.ShowUser;
import com.freechetwithyounme.chettingapp.ViewRequest.ViewSendReq;
import com.freechetwithyounme.chettingapp.Welcome.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView profile_image;
    private TextView profile_name;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout= findViewById(R.id.tablayout);
        ViewPager viewPager= findViewById(R.id.viewpage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        viewpagerAdapter viewpagerAdapter= new viewpagerAdapter(getSupportFragmentManager());
        viewpagerAdapter.addFragment(new ChatList(),"ChatList");
        viewpagerAdapter.addFragment(new Friends(),"Friends");
        viewpagerAdapter.addFragment(new FriendRequests(),"Request");
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        auth= FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();

        if (currentUser!= null){
            String onLineUserID= auth.getCurrentUser().getUid();
            userReference= FirebaseDatabase.getInstance().getReference().child("User").child(onLineUserID);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        /*final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Exit !");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_online){

            Toast.makeText(this, "until now oky.. edit try to after", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.action_frnd_req){
            //Intent intent= new Intent(this, FriendRequests.class);
            FriendRequests friendRequests= new FriendRequests();
            FragmentManager fm= getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.main_activity_ac, friendRequests).commit();
            Toast.makeText(this,"your are now frnd req fragment", Toast.LENGTH_LONG).show();

        } else if (id == R.id.action_find_people){
            Intent intent= new Intent(this, FindUser.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.action_logout) {
            if (currentUser != null){
                userReference.child("online").setValue(ServerValue.TIMESTAMP);
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent= new Intent(this,Splash.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem nItem) {
        int id = nItem.getItemId();

        if (id == R.id.acc_setting) {
            Intent intent= new Intent(MainActivity.this, MyProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_findPeople) {
            Intent intent= new Intent(MainActivity.this, FindUser.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_viewSendRequest) {
            Intent intent= new Intent(MainActivity.this, ViewSendReq.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_alluser) {
            Intent intent= new Intent(MainActivity.this, ShowUser.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser== null){
            Intent intent= new Intent(MainActivity.this, Splash.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null){
            userReference.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (currentUser != null){
            userReference.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

    ///---- Fragment view pager---------------

    class viewpagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;
        private ArrayList<String> title;

        viewpagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments= new ArrayList<>();
            this.title= new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            this.fragments.add(fragment);
            this.title.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}
