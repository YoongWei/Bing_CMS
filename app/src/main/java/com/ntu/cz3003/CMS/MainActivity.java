package com.ntu.cz3003.CMS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (user != null) {
            Log.d(TAG, "onCreate: User Sign-in Checked");
            //startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
            //run other methods
                //onResume();
        }
        else{
            Log.d(TAG, "onCreate: Redirecting to LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
        }
    }




}
