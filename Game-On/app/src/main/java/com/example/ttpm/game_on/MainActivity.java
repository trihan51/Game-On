package com.example.ttpm.game_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button loginbutton;
    protected Button registerbutton;
    //    host-search
    //String[] items;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;
    //    host-search end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Intent gomainscreen = new Intent(MainActivity.this, NearbySessionsMain.class);
            startActivity(gomainscreen);
        } else {
            // show the signup or login screen
        }

        loginbutton = (Button)findViewById(R.id.LoginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketologinScreen = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(taketologinScreen);
                finish();
            }
        });

        registerbutton = (Button) findViewById(R.id.RegisterButton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taketoRegisterScreen = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(taketoRegisterScreen);
            }
        });

        //        host-search
        listView=(ListView)findViewById(R.id.listview);
        editText=(EditText)findViewById(R.id.txtsearch);
        initList();     //FOR TESTING PURPOSE
        editText.addTextChangedListener(new TextWatcher() {
            int length;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length = s.toString().length();
                initList();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    // reset listview
                    initList();
                } else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Checks for deletion
                if (s.toString().length() < length) {
                    initList();
                    for (String item : listItems) {
                        if (!item.toLowerCase().contains(s.toString().toLowerCase())) {
                            listItems.remove(item);
                        }
                    }
                }
            }
        });
        //      host-search end
    }

//    host-search
    public void searchItem(String txtToSearch) {
        for(String item : listItems) {
            if(!item.toLowerCase().contains(txtToSearch.toString().toLowerCase())) {
                listItems.remove(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
    //    We don't have to call BoardGames so many times...
    public void initList(){
        listItems = new ArrayList<String>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BoardGames");
        query.orderByAscending("boardName");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for(ParseObject boardGameName : list) {
                        listItems.add(boardGameName.getString("boardName"));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, listItems);
        listView.setAdapter(adapter);
    }
//    host-search end

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
