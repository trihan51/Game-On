package com.example.ttpm.game_on.adapter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tony Nguyen on 2/7/2016.
 */
public class BoardGame {

    private UUID mBoardId;
    private String mBoardName;
    private int mOpenSessions;
    private List<Integer> mNumPlayers;

    public BoardGame() {
        this(UUID.randomUUID());
    }

    public BoardGame(UUID id) {
        mBoardId = id;
        mNumPlayers = new ArrayList<>();
    }

    public UUID getBoardId() {
        return mBoardId;
    }

    public String getBoardName() {
        return mBoardName;
    }

    public void setBoardName(String boardName) {
        mBoardName = boardName;
    }

    public List<Integer> getNumPlayers() {
        return mNumPlayers;
    }

    public void setNumPlayers(List<Integer> numPlayers) {
        mNumPlayers = numPlayers;
    }

    // will call db to get count of sessions open
    public int getOpenSessions() {
        return mOpenSessions;
    }
}
