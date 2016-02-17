package com.example.ttpm.game_on.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ttpm.game_on.CustomChessAdapter;
import com.example.ttpm.game_on.CustomMonopolyAdapter;
import com.example.ttpm.game_on.CustomOneNightUltimateWerewolfAdapter;
import com.example.ttpm.game_on.CustomSettlersOfCatanAdapter;
import com.example.ttpm.game_on.CustomSplendorAdapter;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.models.BoardGamesAvailable;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


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
}
