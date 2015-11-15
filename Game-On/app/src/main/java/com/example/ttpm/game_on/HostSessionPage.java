package com.example.ttpm.game_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
//import com.parse.ParseQueryAdapter;

public class HostSessionPage extends AppCompatActivity {

    GameOnSession currentGameOnSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_session_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("host", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> list, ParseException e) {
                if (e == null) {
                    currentGameOnSession = list.get(0);
                    TextView sessionInfoOutput = (TextView) findViewById(R.id.sessionInfoOutputArea);
                    sessionInfoOutput.setText("");
                    sessionInfoOutput.append("The game is: " + currentGameOnSession.getGameTitle() + "\n");
                    sessionInfoOutput.append("The host is: " + currentGameOnSession.getHost().getUsername());
                } else {
                    Toast.makeText(HostSessionPage.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button leaveButton = (Button) findViewById(R.id.leaveButton);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentGameOnSession.deleteInBackground();

                Intent takeToNearbySessionsScreen = new Intent(HostSessionPage.this, NearbySessionsMain.class);
                startActivity(takeToNearbySessionsScreen);
            }
        });

    }

}
