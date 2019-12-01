package com.jcd.youpick.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jcd.youpick.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;



public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    Button signOut;
    Button mealButton;
    TextView welcomeMsg;
    String[] defaultFood = new String[]{"Pizza", "Burgers", "Salad", "Tacos", "Chinese", "Sandwiches", "Chicken"};
    Vector<String> foodOptions = new Vector<>();
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        welcomeMsg = findViewById(R.id.textView);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            welcomeMsg.setText("Welcome " + personName);
            linearLayout = findViewById(R.id.linearLayout);


            for(int i = 0; i < defaultFood.length; i++){
                final ToggleButton newButton = new ToggleButton(this);
                newButton.setText(defaultFood[i]);
                newButton.setId(View.generateViewId());
                newButton.setTextOff(defaultFood[i]);
                newButton.setTextOn(defaultFood[i]);
                newButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            foodOptions.add(buttonView.getText().toString());
                            buttonView.setBackgroundColor(Color.GREEN);
                        }
                        else{
                            foodOptions.remove(buttonView.getText().toString());
                            buttonView.setBackgroundColor(Color.RED);
                        }
                    }
                });
                linearLayout.addView(newButton);
            }



        }

        mealButton = findViewById(R.id.meal_button);
        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, foodOptions.size());
                Toast.makeText(MainActivity.this, "Today's meal should be: " + foodOptions.elementAt(randomNum), Toast.LENGTH_LONG).show();
            }
        });

        signOut = findViewById(R.id.sign_out_button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id. sign_out_button:
                        signOut();
                        break;// ...
                }

            }
        });

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

}
