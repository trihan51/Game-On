package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.adapter.HostSearchAdapter;
import com.example.ttpm.game_on.models.BoardGame;
import com.example.ttpm.game_on.models.BoardGameCollection;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostSearchFragment extends android.support.v4.app.Fragment {

    private RecyclerView mSearchRecyclerView;
    private HostSearchAdapter mSearchAdapter;
    private List<BoardGame> mBoardGames;

    public HostSearchFragment() {
    }

    public static HostSearchFragment newInstance(String text)
    {
        HostSearchFragment f = new HostSearchFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_search, container, false);

        mSearchRecyclerView = (RecyclerView) view
                .findViewById(R.id.host_search_recycler_view);

        SearchView mGameSearchView = (SearchView) view
                .findViewById(R.id.host_search_search_view);
        mGameSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<BoardGame> filteredBoardGameList = filter(mBoardGames, newText);
                mSearchAdapter.animateTo(filteredBoardGameList);
                mSearchRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        BoardGameCollection boardGameCollection = new BoardGameCollection();
        mBoardGames = boardGameCollection.getBoardGames();

        mSearchAdapter = new HostSearchAdapter(getActivity(), mBoardGames);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    private List<BoardGame> filter(List<BoardGame> boardGames, String query) {
        query = query.toLowerCase();

        List<BoardGame> filteredBoardGame = new ArrayList<>();
        for (BoardGame boardGame : boardGames) {
            String text = boardGame.getBoardName().toLowerCase();
            if (text.contains(query)) {
                filteredBoardGame.add(boardGame);
            }
        }

        return filteredBoardGame;
    }
}





