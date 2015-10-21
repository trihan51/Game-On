package com.example.ttpm.game_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    protected Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    loginbutton = (Button)findViewById(R.id.LoginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketologinScreen = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(taketologinScreen);
            }
        });

        registerbutton = (Button)findViewById(R.id.RegisterButton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketoRegisterScreen = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(taketoRegisterScreen);
            }
        });

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "duemHXnG4aocoONNNIEQLevZ7MyLAvqWSSFlBnpW", "Conlzrgvh0WbBVQgV7c0VIjqlEIcxUNSi4iwmzyW");





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
}
