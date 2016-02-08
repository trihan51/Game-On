package com.example.ttpm.game_on.adapter.models;

import android.content.Context;

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
            sBoardGameCollection = new BoardGameCollection(context);
        }

        return sBoardGameCollection;
    }

    private BoardGameCollection(Context context) {
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
