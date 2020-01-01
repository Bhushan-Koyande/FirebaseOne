package com.example.firebaseone;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private EditText newMail,oldMail,oldPass;
    private Button submitButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String currentMail,currentPassword,changedMail;
    private static final String TAG="EditProfileActivity/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        mFirebaseAuth=FirebaseAuth.getInstance();

        oldMail=findViewById(R.id.oldemail);
        oldPass=findViewById(R.id.oldpassword);
        newMail=findViewById(R.id.newemail);
        submitButton=findViewById(R.id.continueButton);

        currentMail=oldMail.getText().toString();
        currentPassword=oldPass.getText().toString();
        changedMail=newMail.getText().toString();

        mAuthListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (user!=null){
                    Log.d(TAG,"onAuthStateChanged:signed_in"+user.getUid());
                    //ToastMessage("Successfully signed in with :"+user.getEmail());
                }else {
                    Log.d(TAG,"onAuthStateChanged:signed_out");
                    //ToastMessage("Successfully signed out");
                }
            }
        };

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!currentMail.equals(""))&&(!currentPassword.equals(""))&&(!changedMail.equals(""))) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(currentMail, currentPassword);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "user re-authenticated");
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            firebaseUser.updateEmail(changedMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "user e-mail updated");
                                    }
                                }
                            });
                        }
                    });
                }else {
                    ToastMessage("enter all details");
                }
            }
        });

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
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
