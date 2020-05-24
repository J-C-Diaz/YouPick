package com.jcd.youpick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Vector<String> options = new Vector();
    int optionCount = 0;
    int previouslySelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (previouslySelected >= 0){
                    int id = getResources().getIdentifier("cardView" + Integer.toString(previouslySelected+ 1), "id", getPackageName());
                    CardView newCardView = (CardView) findViewById(id);
                    newCardView.setCardBackgroundColor(Color.WHITE);
                    previouslySelected = -1;
                }
                if (options.size() == 0) {
                    Snackbar.make(view, "Enter at least one dining option", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, options.size());
                    previouslySelected = randomNum;
                    int id = getResources().getIdentifier("cardView" + Integer.toString(randomNum + 1), "id", getPackageName());
                    CardView newCardView = (CardView) findViewById(id);
                    newCardView.setCardBackgroundColor(Color.GREEN);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addRestaurant(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        editText = findViewById(R.id.editText);
        final String newOption = editText.getText().toString();
        editText.setText("");
        if (newOption.trim().isEmpty()){
            Toast.makeText(this, "Restaurant Name Missing" , Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if (options.contains(newOption)){
                Toast.makeText(this, "Restaurant already added" , Toast.LENGTH_SHORT).show();
                return;
            }
            options.add(newOption);
            optionCount = options.size();
            if (optionCount == 7){
                Toast.makeText(this, "Max Number of options added" , Toast.LENGTH_SHORT).show();
                options.removeElementAt(optionCount - 1);
                return;
            }

            //iterate through all cardviews to see what is the first invisible one
            for(int i = 1; i < 7; i++){
                int id = getResources().getIdentifier("cardView" + Integer.toString(i), "id", getPackageName());
                CardView newCardView = (CardView) findViewById(id);
                if (newCardView.getVisibility() == View.INVISIBLE){
                    optionCount = i;
                    break;
                }
            }

            final int cardID = getResources().getIdentifier("cardView" + Integer.toString(optionCount), "id", getPackageName());
            CardView newCardView = (CardView) findViewById(cardID);
            newCardView.setVisibility(View.VISIBLE);
            int textID = getResources().getIdentifier("textView" + Integer.toString(optionCount), "id", getPackageName());
            TextView newTextView = (TextView) findViewById(textID);
            newTextView.setText(newOption);
            newCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog(v, newOption);
                }
            });

        }
    }

    private void alertDialog(final View v, final String s) { //displayed when a user selects a visible card
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Would you like to remove \"" + s + "\" from the list of options?" );
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("DELETE", // User is given the option of deleting their created card
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        v.setVisibility(View.INVISIBLE);
                        options.remove(s);
                        optionCount--;
                        Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void toMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
