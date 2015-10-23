package com.example.ttpm.game_on;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;

public class Gameon extends android.app.Application {


    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "duemHXnG4aocoONNNIEQLevZ7MyLAvqWSSFlBnpW", "Conlzrgvh0WbBVQgV7c0VIjqlEIcxUNSi4iwmzyW");
    }
}
