package com.example.ttpm.game_on;

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

        for(int i = 0; i < 10; i++) {
            BoardGame b = new BoardGame();
            mBoardGames.add(b);
        }
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
