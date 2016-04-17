package com.example.ttpm.game_on.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Data model for a session
 */
@ParseClassName("GameOnSession")
public class GameOnSession extends ParseObject {

    public GameOnSession () {
        // default no-args constructor
    }

    public String getSessionId() {
        return getString("objectId");
    }

    public void setSessionId(String sessionId) {
        put("objectId", sessionId);
    }

    public  String getHostName(){
        return getString("Name");
    }

    public void  setHostName(String hostName){
        put("Name",hostName);
    }

    public void setHostEmail(String hostEmail) {
        put("email", hostEmail);
    }

    public  void setNumberOfParticipants(String numberOfParticipants) {
        put("participants",numberOfParticipants);
    }

    public String getAllPlayerAndHostCount() {
        int getParticipantsNumber = getAllPlayers().length() + 1;
        return String.valueOf(getParticipantsNumber);
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

    public JSONArray getAllPlayers() { return getJSONArray("participants"); }

    public String getPlayer(int position) {
        try {
            return getAllPlayers().get(position).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPlayers(JSONArray participants) {
        put("participants", participants);
    }

    public void addPlayer(String userId)  {
        JSONArray participants = this.getAllPlayers();
        try {
            if (!userIdIsAlreadyIn(participants, userId)) {
                participants.put(userId);
            }
        } catch (org.json.JSONException jsonException) {
            jsonException.printStackTrace();
        }
        this.put("participants", participants);
    }

    private boolean userIdIsAlreadyIn(JSONArray participants, String userId) throws org.json.JSONException {
        int numOfParticipants = participants.length();
        for (int i = 0; i < numOfParticipants; i++) {
            if (participants.get(i).equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public void removePlayer(String userId) throws org.json.JSONException {
        JSONArray oldListOfParticipants = this.getAllPlayers();
        JSONArray newListOfParticipants = new JSONArray();

        int oldListLength = oldListOfParticipants.length();
        for (int i = 0; i < oldListLength; i++) {
            if (!oldListOfParticipants.getString(i).equals(userId)) {
                newListOfParticipants.put(oldListOfParticipants.getString(i));
            }
        }

        this.remove("participants");
        this.put("participants", newListOfParticipants);
    }

    public boolean isOpen() {
        return (this.getBoolean("open") == true);
    }

    public void setOpenStatus(boolean status) {
        this.put("open", status);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public static ParseQuery<GameOnSession> getQuery() {
        return ParseQuery.getQuery(GameOnSession.class);
    }
}