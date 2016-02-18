package com.example.ttpm.game_on.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.example.ttpm.game_on.models.BoardGame;
import com.example.ttpm.game_on.models.BoardGameCollection;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 2/17/2016.
 */
public class UserGameFragment extends android.support.v4.app.Fragment{

    private static final String ARG_BOARD_GAME_NAME_ID =
            "com.example.ttpm.game_on.board_game_name_id";

    private RecyclerView mSearchRecyclerView;
    private GameSearchAdapter mSearchAdapter;
    private List<BoardGame> mBoardGames;

    private String boardGameName;

    public static UserGameFragment newInstance(String boardGameName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOARD_GAME_NAME_ID, boardGameName);

        UserGameFragment fragment = new UserGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        boardGameName = (String) getArguments().getSerializable(ARG_BOARD_GAME_NAME_ID);
        Toast.makeText(getActivity(), boardGameName, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_game, container, false);

        mSearchRecyclerView = (RecyclerView) view
                .findViewById(R.id.user_game_recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        BoardGameCollection boardGameCollection = new BoardGameCollection();
        mBoardGames = boardGameCollection.getBoardGames();
        queryForSpecificBoardGames();

        mSearchAdapter = new GameSearchAdapter(getActivity(), mBoardGames);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_log_out:
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(getActivity(), currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(getActivity(), "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void queryForSpecificBoardGames() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameOnSession");
        query.whereEqualTo("gameTitle", boardGameName);
        query.whereNotEqualTo("host", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject boardGameName : objects) {
                        BoardGame b = new BoardGame();
                        b.setBoardName(boardGameName.getString("boardName"));
                        mBoardGames.add(b);
                        mSearchAdapter.addNewGame(b);
                    }
                }
            }
        });
    }

    private class GameSearchViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleTextView;
        private TextView mSessionsTextView;
        private TextView mJoinButton;

        public GameSearchViewHolder(View itemView) {
            super(itemView);

            mTitleTextView =
                    (TextView) itemView.findViewById(R.id.list_item_games_game_pic);
            mSessionsTextView =
                    (TextView) itemView.findViewById(R.id.list_item_games_game_open);
            mJoinButton =
                    (TextView) itemView.findViewById(R.id.list_item_games_button);
            mJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "You joined this open game! Position: "
                            + Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindGame(BoardGame boardGame) {
            mTitleTextView.setText(boardGameName);
            mSessionsTextView.setText(Integer.toString(R.id.list_item_host_games_game_open));
        }
    }

    public class GameSearchAdapter extends RecyclerView.Adapter<GameSearchViewHolder> {

        private LayoutInflater mLayoutInflater;
        private List<BoardGame> mBoardGames;

        public GameSearchAdapter(Context context, List<BoardGame> boardGames) {
            mLayoutInflater = LayoutInflater.from(context);
            mBoardGames = new ArrayList<>(boardGames);
        }

        @Override
        public GameSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_user_game, parent, false);
            return new GameSearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GameSearchViewHolder holder, int position) {
            BoardGame boardGame = mBoardGames.get(position);
            holder.bindGame(boardGame);
        }

        @Override
        public int getItemCount() {
            return mBoardGames.size();
        }

        public void addNewGame(BoardGame boardGame) {
            mBoardGames.add(boardGame);
            notifyDataSetChanged();
        }

        public BoardGame removeGame(int position) {
            BoardGame model = mBoardGames.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addGame(int position, BoardGame boardGame) {
            mBoardGames.add(position, boardGame);
            notifyItemInserted(position);
        }

        public void moveGame(int fromPosition, int toPosition) {
            BoardGame model = mBoardGames.remove(fromPosition);
            mBoardGames.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }
    }
}
