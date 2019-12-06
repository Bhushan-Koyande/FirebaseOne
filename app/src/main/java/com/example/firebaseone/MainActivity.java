package com.example.firebaseone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment());
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment=null;
                FragmentManager fragmentManager=getSupportFragmentManager();
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        selectedFragment=new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment=new SearchFragment();
                        break;
                    case R.id.nav_account:
                        selectedFragment=new AccountFragment();
                        break;
                }
                fragmentManager.popBackStack("ConnectionError", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                assert selectedFragment!=null;
                fragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment());

    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
    }

    //@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Fragment selectedFragment=null;
        FragmentManager fragmentManager=getSupportFragmentManager();
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                selectedFragment=new HomeFragment();
                break;
            case R.id.nav_search:
                selectedFragment=new SearchFragment();
                break;
            case R.id.nav_account:
                selectedFragment=new AccountFragment();
                break;
        }
        fragmentManager.popBackStack("ConnectionError", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        assert selectedFragment!=null;
        fragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        return true;
    }

}
