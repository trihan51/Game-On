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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.GameOnSession;
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
    private SessionSearchAdapter mSearchAdapter;
    private List<GameOnSession> mGameOnSessions;

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

        mGameOnSessions = new ArrayList<GameOnSession>();
        queryForSpecificGameOnSessions();

        mSearchAdapter = new SessionSearchAdapter(getActivity(), mGameOnSessions);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
    }

    private void queryForSpecificGameOnSessions() {
        ParseQuery<GameOnSession> query = GameOnSession.getQuery();
        query.whereEqualTo("gameTitle", boardGameName);
        query.whereNotEqualTo("host", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<GameOnSession>() {
            @Override
            public void done(List<GameOnSession> objects, ParseException e) {
                if (e == null) {
                    for (GameOnSession session : objects) {
                        mGameOnSessions.add(session);
                        mSearchAdapter.addNewSession(session);
                    }
                }
            }
        });
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

    private class SessionSearchViewHolder extends RecyclerView.ViewHolder {

        private GameOnSession mSession;

        private TextView mSessionIdTextView;
        private TextView mGameTitleTextView;
        private Button mJoinButton;

        public SessionSearchViewHolder(View itemView) {
            super(itemView);

            mSessionIdTextView = (TextView) itemView.findViewById(R.id.list_item_session_id);
            mGameTitleTextView = (TextView) itemView.findViewById(R.id.list_item_game_title);
            mJoinButton = (Button) itemView.findViewById(R.id.list_item_join_button);

            mJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "You clicked me!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindSession(GameOnSession session) {
            mSession = session;
            mSessionIdTextView.setText(session.getObjectId());
            mGameTitleTextView.setText(session.getGameTitle());
        }
    }

    public class SessionSearchAdapter extends RecyclerView.Adapter<SessionSearchViewHolder> {

        private LayoutInflater mLayoutInflater;
        private List<GameOnSession> mGameOnSessions;

        public SessionSearchAdapter(Context context, List<GameOnSession> gameOnSessions) {
            mLayoutInflater = LayoutInflater.from(context);
            mGameOnSessions = new ArrayList<>(gameOnSessions);
        }

        @Override
        public SessionSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_user_game, parent, false);
            return new SessionSearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SessionSearchViewHolder holder, int position) {
            GameOnSession session = mGameOnSessions.get(position);
            holder.bindSession(session);
        }

        @Override
        public int getItemCount() {
            return mGameOnSessions.size();
        }

        public void addNewSession(GameOnSession session) {
            mGameOnSessions.add(session);
            notifyDataSetChanged();
        }

        public GameOnSession removeSession(int position) {
            GameOnSession model = mGameOnSessions.remove(position);
            notifyItemRemoved(position);
            return model;
        }

        public void addSession(int position, GameOnSession session) {
            mGameOnSessions.add(position, session);
            notifyItemInserted(position);
        }

        public void moveSession(int fromPosition, int toPosition) {
            GameOnSession model = mGameOnSessions.remove(fromPosition);
            mGameOnSessions.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        }
    }
}
