package com.example.ttpm.game_on;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a session
 */
@ParseClassName("GameOnSession")
public class GameOnSession extends ParseObject {

    public GameOnSession () {
        // default no-args constructor
    }

    public String getGameTitle() {
        return getString("gameTitle");
    }

    public void setGameTitle(String title) {
        put("gameTitle", title);
    }

    public ParseUser getHost() {
        return getParseUser("host");
    }

    public void setHost(ParseUser value) {
        put("host", value);
    }

    // TODO: 11/15/2015 add a participants property. This can be represented using an ArrayList.

    // TODO: 11/15/2015 uncomment the getters and setters below after incorporating location API.
//    public ParseGeoPoint getLocation() {
//        return getParseGeoPoint("location");
//    }
//
//    public void setLocation(ParseGeoPoint value) {
//        put("location", value);
//    }

    // TODO: 11/15/2015 add a createdAt property

    public static ParseQuery<GameOnSession> getQuery() {
        return ParseQuery.getQuery(GameOnSession.class);
    }
}