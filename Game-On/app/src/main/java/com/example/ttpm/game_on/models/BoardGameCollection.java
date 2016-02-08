package com.example.ttpm.game_on.models;

import android.content.Context;

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

    // grab board game list from parse
    public List<BoardGame> getBoardGames() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BoardGames");

        for(int i = 0; i < 10; i++) {
            BoardGame b = new BoardGame();
            b.setBoardName("Scrabble");
            if (i % 2 == 0) {
                b.setBoardName("Connect 4");
            }
            if (i % 3 == 0) {
                b.setBoardName("Chess");
            }
            if (i % 5 == 0) {
                b.setBoardName("Settlers of Catan");
            }
            mBoardGames.add(b);
        }

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
