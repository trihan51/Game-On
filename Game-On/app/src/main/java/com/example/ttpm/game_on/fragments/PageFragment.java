package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ttpm.game_on.CustomChessAdapter;
import com.example.ttpm.game_on.CustomMonopolyAdapter;
import com.example.ttpm.game_on.CustomOneNightUltimateWerewolfAdapter;
import com.example.ttpm.game_on.CustomSettlersOfCatanAdapter;
import com.example.ttpm.game_on.CustomSplendorAdapter;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.activities.SplashActivity;
import com.example.ttpm.game_on.models.BoardGamesAvailable;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private CustomChessAdapter chessopensessionAdapter;
    private CustomSettlersOfCatanAdapter settlersofcatansessionAdapter;
    private CustomSplendorAdapter splendorsessionAdapter;
    private CustomOneNightUltimateWerewolfAdapter onenightultimatewerewolfsessionAdapter;
    private CustomMonopolyAdapter monopolysessionAdapter;

    private ListView listView;

    public PageFragment() {
    }

    public static PageFragment newInstance()
    {
        return new PageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_fragment, container, false);

        mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), BoardGamesAvailable.class);
        mainAdapter.setTextKey("boardName");

        listView = (ListView) view.findViewById(R.id.listView2);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
        chessopensessionAdapter = new CustomChessAdapter(getActivity());
        splendorsessionAdapter =  new CustomSplendorAdapter(getActivity());
        settlersofcatansessionAdapter = new CustomSettlersOfCatanAdapter(getActivity());
        onenightultimatewerewolfsessionAdapter = new CustomOneNightUltimateWerewolfAdapter(getActivity());
        monopolysessionAdapter = new CustomMonopolyAdapter(getActivity());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ParseObject clickedone = (ParseObject) listView.getItemAtPosition(position);
            if (clickedone != null) {
                String clickedones = clickedone.get("boardName").toString();
                Log.d("CLICKED?", clickedone.toString());
                if (listView.getAdapter() == mainAdapter) {
                    if (clickedones.equals("Chess")) {
                        listView.setAdapter(chessopensessionAdapter);
                        chessopensessionAdapter.loadObjects();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                            }
                        });
                    }

                    if (clickedones.equals("Settlers Of Catan")) {
                        listView.setAdapter(settlersofcatansessionAdapter);
                        settlersofcatansessionAdapter.loadObjects();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                            }
                        });
                    }

                    if (clickedones.equals("Monopoly")) {
                        listView.setAdapter(monopolysessionAdapter);
                        monopolysessionAdapter.loadObjects();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                            }
                        });
                    }

                    if (clickedones.equals("Splendor")) {
                        listView.setAdapter(splendorsessionAdapter);
                        splendorsessionAdapter.loadObjects();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                            }
                        });
                    }

                    if (clickedones.equals("One Night Ultimate Werewolf")) {
                        listView.setAdapter(onenightultimatewerewolfsessionAdapter);
                        onenightultimatewerewolfsessionAdapter.loadObjects();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                            }
                        });
                    }

                }
            }
            }
        });
        return view;
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
}
