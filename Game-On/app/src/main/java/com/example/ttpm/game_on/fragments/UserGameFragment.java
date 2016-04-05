package com.example.ttpm.game_on.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.models.GameOnSession;
import com.example.ttpm.game_on.QueryPreferences;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SessionActivity;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony on 2/17/2016.
 */
public class UserGameFragment extends android.support.v4.app.Fragment{

    private static final String ARG_BOARD_GAME_NAME_ID = "com.example.ttpm.game_on.board_game_name_id";
    private static final String ARG_SEARCH_RADIUS = "com.example.ttpm.game_on.search_radius";
    private static final String ARG_CURRENT_LOCATION = "com.example.ttpm.game_on.current_location";

    private RecyclerView mSearchRecyclerView;
    private SessionSearchAdapter mSearchAdapter;
    private List<GameOnSession> mGameOnSessions;
    private TextView mBoardGameTextView;
    private ImageView mBoardGameImageView;

    private String boardGameName;
    private String mSearchRadius;
    private Location mCurrentLocation;

    public static UserGameFragment newInstance(String boardGameName, String searchRadius, Location currentLocation) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOARD_GAME_NAME_ID, boardGameName);
        args.putSerializable(ARG_SEARCH_RADIUS, searchRadius);
        args.putParcelable(ARG_CURRENT_LOCATION, currentLocation);

        UserGameFragment fragment = new UserGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        boardGameName = (String) getArguments().getSerializable(ARG_BOARD_GAME_NAME_ID);
        mSearchRadius = (String) getArguments().getSerializable(ARG_SEARCH_RADIUS);
        mCurrentLocation = (Location) getArguments().getParcelable(ARG_CURRENT_LOCATION);
        Toast.makeText(getActivity(), boardGameName, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_game, container, false);

        mSearchRecyclerView = (RecyclerView) view.findViewById(R.id.user_game_recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBoardGameImageView = (ImageView) view.findViewById(R.id.user_game_image_view);
        mBoardGameTextView = (TextView) view.findViewById(R.id.user_game_game_name);
        mBoardGameTextView.setText(boardGameName);

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
        query.whereEqualTo("Open", true);
        /*
         //name of the seconday table
       query.include("User");
        query.whereEqualTo("host","objectID");
         */



      // query.include("email");
       //query.include("participants");


        if (!mSearchRadius.equals(getResources().getString(R.string.radio_na))) {
            query.whereWithinMiles(
                    "location",
                    new ParseGeoPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                    Double.valueOf(mSearchRadius));
        }

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
        if (QueryPreferences.getStoredSessionId(getActivity()) == null) {
            MenuItem currentSessionMenuItem = menu.findItem(R.id.menu_action_current_session);
            currentSessionMenuItem.setVisible(false);
        }
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
            case R.id.menu_action_current_session:
                Intent intent = SessionActivity.newIntent(getActivity(), mCurrentLocation);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SessionSearchViewHolder extends RecyclerView.ViewHolder {

        private GameOnSession mSession;
        private ImageView mHostImageView;
        private TextView mHostNameTextView;
        private TextView mNumOfParticipantsTextView;
        private ButtonRectangle mJoinButton;

        public SessionSearchViewHolder(View itemView) {
            super(itemView);

            mHostImageView = (ImageView) itemView.findViewById(R.id.list_item_user_image_view);
            mHostNameTextView = (TextView) itemView.findViewById(R.id.list_item_host_name);
            mNumOfParticipantsTextView =
                    (TextView) itemView.findViewById(R.id.list_item_participant_count);
            mJoinButton = (ButtonRectangle) itemView.findViewById(R.id.list_item_join_button);

            mJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add user to current session
                    mSession.addParticipant(ParseUser.getCurrentUser().getObjectId());
                    mSession.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Saved Successfully
                                QueryPreferences.setStoredSessionId(getActivity(), mSession.getObjectId());
                                Intent intent = SessionActivity.newIntent(getActivity(), mCurrentLocation);
                                startActivity(intent);
                            } else {
                                // The save failed
                                Log.d("ERROR", "Unable to add current user to session: " + e);
                            }
                        }
                    });
                }
            });
        }


        public void bindSession(GameOnSession session) {
            mSession = session;

            bindHostName(mSession);
            bindNumOfPlayers(mSession);

            mNumOfParticipantsTextView.setText(session.getNumberOfParticipants());
        }

        public void bindHostName(GameOnSession session) {
            mSession = session;

            ParseUser host = mSession.getHost();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", host.getObjectId());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        mHostNameTextView.setText(objects.get(0).getUsername());
                    } else {
                        mHostNameTextView.setText("No host found");
                    }
                }
            });
        }

        public void bindNumOfPlayers(GameOnSession session) {
            mSession = session;

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BoardGames");
            query.whereEqualTo("boardName", mSession.getGameTitle());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        JSONArray arr = objects.get(0).getJSONArray("maxPlayers");
                        String maxPlayers = "";
                        try {
                            maxPlayers = arr.getString(arr.length()-1);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        mNumOfParticipantsTextView.append("/" + maxPlayers + " players");
                    } else {
                        Log.e("GAMEONSESSION", "No array of maximum participants found");
                    }
                }
            });
            Log.d("GAMEONSESSION", mSession.getGameTitle());
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
