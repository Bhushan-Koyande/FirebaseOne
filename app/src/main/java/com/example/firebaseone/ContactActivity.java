package com.example.firebaseone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactActivity extends AppCompatActivity {

    private Button addToDataBase;
    public EditText firstNumber;
    public EditText secondNumber;
    public EditText thirdNumber;

    private String TAG="";

    //Firebase vars
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        firstNumber=findViewById(R.id.phone1);
        secondNumber=findViewById(R.id.phone2);
        thirdNumber=findViewById(R.id.phone3);
        addToDataBase=findViewById(R.id.addToDB);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        myRef=mFirebaseDatabase.getReference();

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

        addToDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((firstNumber.getText().toString()).equals("")
                        &&(secondNumber.getText().toString()).equals("")
                        &&(thirdNumber.getText().toString()).equals(""))){
                    FirebaseUser user=mFirebaseAuth.getCurrentUser();
                    String userID=user.getUid();
                    myRef.child(userID).child("firstPhone").setValue(firstNumber.getText().toString());
                    myRef.child(userID).child("secondPhone").setValue(secondNumber.getText().toString());
                    myRef.child(userID).child("thirdPhone").setValue(thirdNumber.getText().toString());
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
