package com.example.ttpm.game_on;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostSearchFragment extends android.support.v4.app.Fragment {

    private RecyclerView mSearchRecyclerView;
    private HostSearchAdapter mSearchAdapter;

    String[] items;
    final ArrayList<String> tempListItems;
    {
        tempListItems = new ArrayList<>();
    }
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_search, container, false);

        mSearchRecyclerView = (RecyclerView) view
                .findViewById(R.id.host_search_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        host-search
        editText=(EditText)view.findViewById(R.id.txtsearch);
        initList();
        editText.addTextChangedListener(new TextWatcher() {
            int length;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length = s.toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // perform search
                searchItem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Checks for deletion
                if (s.toString().length() < length) {
                    initList();
                    searchItem(s.toString());
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //initList();
                }
            }
        });
//        host-search end

        updateUI();

        return view;
    }

    public void searchItem(String txtToSearch) {
        for(String item : items) {
            if(!item.toLowerCase().contains(txtToSearch.toLowerCase())) {
                listItems.remove(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
    //    We don't have to call BoardGames so many times...
    public void initList() {
        if(queryHostBoardGames()){
            items = new String[tempListItems.size()];
            items = tempListItems.toArray(items);
            Arrays.sort(items);

            Log.d("HSearch - initList", "Generate Clean List");
            Log.d("initList - temp size", Integer.toString(tempListItems.size()));
            Log.d("initList - items size", Integer.toString(items.length));
//        items = new String[]{"Chess", "Monopoly", "Settlers of Catan", "Uno", "One Night Ultimate Werewolf", "Splendor"};
//        Arrays.sort(items);
            listItems = new ArrayList<>(Arrays.asList(items));
            adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.host_item, R.id.txtitem, listItems);
            //listView.setAdapter(adapter);
        }
    }

    private boolean queryHostBoardGames() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BoardGames");
        query.orderByAscending("boardName");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject boardGameName : list) {
                        if (!tempListItems.contains(boardGameName.getString("boardName"))) {
                            tempListItems.add(boardGameName.getString("boardName"));
                        }
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        return true;
    }

    private void updateUI() {
        BoardGameCollection boardGameCollection = BoardGameCollection.get(getActivity());
        List<BoardGame> boardGames = boardGameCollection.getBoardGames();

        if (mSearchAdapter == null) {
            mSearchAdapter = new HostSearchAdapter(boardGames);
            mSearchRecyclerView.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.setBoardGames(boardGames);
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    private class HostSearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mSessionsTextView;
        private TextView mJoinButton;

        private BoardGame mBoardGame;

        public HostSearchHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.list_item_host_games_game_pic);
            mSessionsTextView =
                    (TextView) itemView.findViewById(R.id.list_item_host_games_game_open);
            mJoinButton =
                    (TextView) itemView.findViewById(R.id.list_item_host_games_button);
            mJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            "Wow, you QUICK JOIN this game!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindGame(BoardGame boardGame) {
            mBoardGame = boardGame;
            mTitleTextView.setText(mBoardGame.getBoardName());
            mSessionsTextView.setText(Integer.toString(mBoardGame.getOpenSessions()));
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    "Wow, you LONG HELD this game!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class HostSearchAdapter extends RecyclerView.Adapter<HostSearchHolder> {

        private List<BoardGame> mBoardGames;

        public HostSearchAdapter(List<BoardGame> boardGames) {
            mBoardGames = boardGames;
        }

        @Override
        public HostSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            view = layoutInflater.inflate(R.layout.list_item_host_games, parent, false);
            return new HostSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(HostSearchHolder holder, int position) {
            BoardGame boardGame = mBoardGames.get(position);
            holder.bindGame(boardGame);
        }

        @Override
        public int getItemCount() {
            return mBoardGames.size();
        }

        public void setBoardGames(List<BoardGame> boardGames) {
            mBoardGames = boardGames;
        }
    }
}





