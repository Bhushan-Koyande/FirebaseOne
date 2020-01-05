package com.example.firebaseone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName,secondName,inputEmailID,inputPasswd,confirmPasswd;
    Button registerBtn;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName=findViewById(R.id.firstName);
        secondName=findViewById(R.id.secondName);
        inputEmailID=findViewById(R.id.inputEmailID);
        inputPasswd=findViewById(R.id.inputPasswd);
        confirmPasswd=findViewById(R.id.confirmPasswd);
        registerBtn=findViewById(R.id.registerButton);

        mAuth= FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=inputEmailID.getText().toString();
                String password=inputPasswd.getText().toString();
                String nameOne=firstName.getText().toString();
                String nameTwo=secondName.getText().toString();
                String confirm=confirmPasswd.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nameOne)
                || TextUtils.isEmpty(nameTwo) || TextUtils.isEmpty(confirm)){
                    ToastMessage("Please enter all details");
                    return;
                }
                if(!confirm.equals(password)){
                    ToastMessage("Enter correct password");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                ToastMessage("createUserWithEmail:OnComplete"+task.isSuccessful());
                                if(!task.isSuccessful()){
                                    ToastMessage(task.getException().getMessage());
                                }else{
                                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });

    }

    private void ToastMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
