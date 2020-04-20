package com.jcd.youpick;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class ScrollingActivity extends AppCompatActivity {
    EditText editText;
    Vector<String> options = new Vector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
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
                if (options.size() == 0) {
                    Snackbar.make(view, "Enter at least one dining option", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else {
                    int randomNum = ThreadLocalRandom.current().nextInt(0, options.size());
                    Snackbar.make(view, "Today's meal should be: " + options.elementAt(randomNum), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
        String newOption = editText.getText().toString();
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
            Button myButton = new Button(this);
            myButton.setText(newOption);

            LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayoutVertical);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, lp);
        }
    }
}
