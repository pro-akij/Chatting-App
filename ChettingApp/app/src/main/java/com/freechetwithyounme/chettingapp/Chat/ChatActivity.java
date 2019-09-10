package com.freechetwithyounme.chettingapp.Chat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freechetwithyounme.chettingapp.Adapter.MessageAdapter;
import com.freechetwithyounme.chettingapp.Model.Chat;
import com.freechetwithyounme.chettingapp.Model.Message;
import com.freechetwithyounme.chettingapp.Model.User;
import com.freechetwithyounme.chettingapp.MyAccount.MyProfile;
import com.freechetwithyounme.chettingapp.MyAccount.ProSetting;
import com.freechetwithyounme.chettingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String recieveID, recieveNAME;
    private Toolbar toolbar;
    private RecyclerView listOfMessage;
    private TextView name, last_seen;
    private CircleImageView circleImageView;

    private DatabaseReference rootRef;
    private DatabaseReference messageRef;
    private DatabaseReference UserRef;
    private StorageReference chatImageStore;
    private FirebaseAuth mAuth;
    private String currentUser;

    private ImageButton selectImage;
    private EditText enterMessage;
    private FloatingActionButton sendMessage;
    private ArrayList<Chat> mList= new ArrayList<>();
    private MessageAdapter messageAdapte;
    private static final int GELLERY_PICK= 1;
    private Uri imageUri;
    private Uri GetImageUri;
    public static String getLastSeenTime;
    private ProgressDialog progressDialog;
    private int count= 0;
    private int imageCount= 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageCount++;
        if (requestCode == GELLERY_PICK && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            imageUri= data.getData();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listOfMessage= findViewById(R.id.list_of_messages);
        listOfMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        listOfMessage.setLayoutManager(linearLayoutManager);

        mAuth= FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.message_app_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth= FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        recieveID = getIntent().getExtras().get("passuID").toString();
        recieveNAME = getIntent().getExtras().get("passNAME").toString();
        setTitle(recieveNAME);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionVew = layoutInflater.inflate(R.layout.chat_id, null);
        actionBar.setCustomView(actionVew);
        chatImageStore= FirebaseStorage.getInstance().getReference("ChatImage");
        progressDialog= new ProgressDialog(this);

        rootRef.child("User").child(recieveID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                final String get_last_seen= dataSnapshot.child("online").getValue().toString();
                final String get_image= dataSnapshot.child("imageuri").getValue().toString();

                /*Picasso.with(ChatActivity.this)
                        .load(get_image)
                        .into(circleImageView);*/

                if (get_last_seen.equals("true")){
                    //last_seen.setText(R.string.online);
                    actionBar.setSubtitle(R.string.online);
                }
                else {
                    LastSeenTime getTime= new LastSeenTime();
                    long last_time= Long.parseLong(get_last_seen);
                    getLastSeenTime= getTime.getTimeAgo(last_time, getApplicationContext());
                    //last_seen.setText(getLastSeenTime);
                    actionBar.setSubtitle(getLastSeenTime);

                }
                ReadMessage(currentUser, recieveID, user.getImageuri());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        selectImage= findViewById(R.id.select_file);
        enterMessage= findViewById(R.id.input_your_message);
        sendMessage= findViewById(R.id.fab);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= enterMessage.getText().toString();
                if (count>0){
                    ChatImageUpload();
                    ImageChatMessage(currentUser, recieveID, GetImageUri.toString());
                    ShowImage();
                    count--;
                } else if (!msg.isEmpty()){
                    ChatMessage(currentUser, recieveID, msg);
                    Toast.makeText(ChatActivity.this, "message send successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "you can't send empty message", Toast.LENGTH_SHORT).show();
                }
                enterMessage.setText("");
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gellery= new Intent();
                gellery.setAction(Intent.ACTION_GET_CONTENT);
                gellery.setType("image/*");
                startActivityForResult(gellery, GELLERY_PICK);
                count++;
            }
        });
    }

    private void ShowImage() {

    }

    private void ChatImageUpload() {
        progressDialog.setTitle("Image Sending");
        progressDialog.setMessage("please wait while sending your image");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        StorageReference reference= chatImageStore.child(System.currentTimeMillis()+"."+getFileExtantionChat(imageUri));
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        GetImageUri= uriTask.getResult();

                        Toast.makeText(ChatActivity.this, "upload successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

    }

    public void ChatMessage(final String msgSenderID,final String msgRecieveID,final String message){

        messageRef= FirebaseDatabase.getInstance().getReference("Chats");
        messageRef.child(currentUser).child(recieveID).push().child("chat list").setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                HashMap<String, Object> msgHashMap= new HashMap<>();
                msgHashMap.put("senderID", msgSenderID);
                msgHashMap.put("receiverID", msgRecieveID);
                msgHashMap.put("message", message);
                msgHashMap.put("type", "text");
                messageRef.child("Chats").push().setValue(msgHashMap);

            }
        });
    }

    public void ImageChatMessage(final String msgSenderID,final String msgRecieveID,final String imageMessage){

        messageRef= FirebaseDatabase.getInstance().getReference("Chats");
        messageRef.child(currentUser).child(recieveID).push().child("chat list").setValue(imageMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                HashMap<String, Object> msgHashMap= new HashMap<>();
                msgHashMap.put("senderID", msgSenderID);
                msgHashMap.put("receiverID", msgRecieveID);
                msgHashMap.put("chatimage", GetImageUri);
                msgHashMap.put("type", "image");
                messageRef.child("Chats").push().setValue(msgHashMap);

            }
        });
    }


    public void ReadMessage(final String myID, final String userID, final String imaguURI){

        mList= new ArrayList<>();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Chats").child("Chats");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot shote : dataSnapshot.getChildren()){
                    Chat chat= shote.getValue(Chat.class);
                    if (chat.getReceiverID().equals(myID) && chat.getSenderID().equals(userID) ||
                    chat.getReceiverID().equals(userID) && chat.getSenderID().equals(myID)) {
                        mList.add(chat);
                    }
                    messageAdapte= new MessageAdapter(ChatActivity.this, mList, imaguURI);
                    listOfMessage.setAdapter(messageAdapte);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getFileExtantionChat(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}