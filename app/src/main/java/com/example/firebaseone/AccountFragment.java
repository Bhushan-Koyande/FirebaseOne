package com.example.firebaseone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {

    private CardView profile;
    private CardView contact;
    private CardView about;
    private CardView logout;

    public AccountFragment(){
        //Required Empty Constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        profile=v.findViewById(R.id.cardView1);
        contact=v.findViewById(R.id.cardView2);
        about=v.findViewById(R.id.cardView3);
        logout=v.findViewById(R.id.cardView4);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),ContactActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}
