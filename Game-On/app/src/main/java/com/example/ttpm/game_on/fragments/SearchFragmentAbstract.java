package com.example.ttpm.game_on.fragments;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.example.ttpm.game_on.models.BoardGame;

import java.util.List;

/**
 * Created by Tony on 2/18/2016.
 */
public abstract class SearchFragmentAbstract extends android.support.v4.app.Fragment
        implements SearchView.OnQueryTextListener {

    protected RecyclerView mSearchRecyclerView;
    protected SearchAdapter mSearchAdapter;
    protected List<BoardGame> mBoardGames;
}
