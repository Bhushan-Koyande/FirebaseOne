package com.example.firebaseone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEmail,mPassword;
    private Button signInBtn,signOutBtn;
    private TextView signUpTextView,forgotTextView;
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmail=findViewById(R.id.inputMail);
        mPassword=findViewById(R.id.inputPassword);
        signInBtn=findViewById(R.id.signInButton);
        signOutBtn=findViewById(R.id.signOutButton);
        signUpTextView=findViewById(R.id.signUpTextView);
        forgotTextView=findViewById(R.id.forgotPasswdTextView);
        mAuth=FirebaseAuth.getInstance();
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
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString();
                String passwd=mPassword.getText().toString();
                if (!email.equals("") && !passwd.equals("")){
                    mAuth.signInWithEmailAndPassword(email,passwd);
                }
                else {
                    ToastMessage("You didn't fill in all the required data");
                }
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                ToastMessage("Signing out");
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ToastMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
