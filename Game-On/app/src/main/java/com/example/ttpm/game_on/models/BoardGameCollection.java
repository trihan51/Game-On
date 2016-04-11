package com.example.ttpm.game_on.models;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tony Nguyen on 2/7/2016.
 */
public class BoardGameCollection {

    private static BoardGameCollection sBoardGameCollection;

    private ArrayList<BoardGame> mBoardGames;

    public static BoardGameCollection get(Context context) {
        if(sBoardGameCollection == null) {
            sBoardGameCollection = new BoardGameCollection();
        }

        return sBoardGameCollection;
    }

    public BoardGameCollection() {
        mBoardGames = new ArrayList<>();
    }

    public void addBoardGame(BoardGame b) {
        mBoardGames.add(b);
    }

    public List<BoardGame> getBoardGames() {
        return mBoardGames;
    }

    public BoardGame getBoardGame(UUID id) {
        for (BoardGame boardGame : mBoardGames) {
            if(boardGame.getBoardId().equals(id)) {
                return boardGame;
            }
        }
        return null;
    }
}
