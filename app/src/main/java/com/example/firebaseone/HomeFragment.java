package com.example.firebaseone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import com.example.firebaseone.ContactActivity;

public class HomeFragment extends Fragment {

    private String TAG="";
    private String userID;

    private String contactOne;
    private String contactTwo;
    private String contactThree;

    private TextView myLocation;
    private TextView firstPhone;
    private TextView secondPhone;
    private TextView thirdPhone;
    private Button panicButton;

    //Firebase vars
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    final int SEND_SMS_PERMISSION_REQUEST_CODE=1;
    private static final int REQUEST_CALL=1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        userID=user.getUid();
        myRef=myRef.child(userID);

        firstPhone=v.findViewById(R.id.firstPhone);
        secondPhone=v.findViewById(R.id.secondPhone);
        thirdPhone=v.findViewById(R.id.thirdPhone);
        myLocation=v.findViewById(R.id.locationTextView);
        panicButton=v.findViewById(R.id.panicButton);

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                    ToastMessage("Successfully signed in with :"+user.getEmail());
                }else {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                    ToastMessage("Successfully signed out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.hasChild("firstPhone"))
                        &&(dataSnapshot.hasChild("secondPhone"))
                        &&(dataSnapshot.hasChild("thirdPhone"))) {
                    contactOne = dataSnapshot.child("firstPhone").getValue().toString();
                    contactTwo = dataSnapshot.child("secondPhone").getValue().toString();
                    contactThree = dataSnapshot.child("thirdPhone").getValue().toString();
                    Log.d(TAG, contactOne + ", " + contactTwo + ", " + contactThree);
                    firstPhone.setText(contactOne);
                    secondPhone.setText(contactTwo);
                    thirdPhone.setText(contactThree);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,databaseError.getMessage());
            }
        });


        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String message="I'm in urgent need of help.I'm at "+myLocation.getText().toString();
                String message="Hello...Good Morning";
                if((contactOne==null || contactOne.length()==0)&&(contactTwo==null || contactTwo.length()==0)&&(contactThree==null || contactThree.length()==0)){
                    return;
                }
                if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(contactOne,null,message,null,null);
                    smsManager.sendTextMessage(contactTwo,null,message,null,null);
                    smsManager.sendTextMessage(contactThree,null,message,null,null);
                }
            }
        });

        firstPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(contactOne);
            }
        });

        secondPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(contactTwo);
            }
        });

        thirdPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(contactThree);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener!=null){
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void ToastMessage(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }

    public boolean checkPermission(String permission){
        int check= ContextCompat.checkSelfPermission(getActivity(),permission);
        return (check==PackageManager.PERMISSION_GRANTED);
    }

    private void makePhoneCall(String number){
        if(number.trim().length()>0 ){
            if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial="tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else {
            ToastMessage("Enter phone number");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               ToastMessage("Permission Granted : Try now");
            }else {
                ToastMessage("Permission Denied");
            }
        }
    }

}
