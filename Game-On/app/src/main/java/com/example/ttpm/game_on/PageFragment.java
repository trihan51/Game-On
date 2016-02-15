package com.example.ttpm.game_on;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ttpm.game_on.activities.SessionActivity;
import com.example.ttpm.game_on.models.BoardGamesAvailable;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    TextView textView;
    protected TextView greetinguser;
    protected Button logoutbutton;
    protected Button swipeinterfacebutton;
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ParseQueryAdapter<ParseObject> GameSessionsAdapter;
    private CustomAdapter chessopensessionAdapter;
    private CustomSettlersOfCatan settlersofcatansessionAdapter;
    private CustomSplendorAdapter splendorsessionAdapter;
    private CustomOneNightUltimateWerewolfAdapter onenightultimatewerewolfsessionAdapter;
    private CustomMonopolyAdapter monopolysessionAdapter;

    private ListView listView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);


       // Bundle bundle = getArguments();
        //String message = Integer.toString(bundle.getInt("count"));
        mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), BoardGamesAvailable.class);
        mainAdapter.setTextKey("boardName");

        listView = (ListView) view.findViewById(R.id.listView2);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();
        chessopensessionAdapter = new CustomAdapter(getActivity());
        splendorsessionAdapter =  new CustomSplendorAdapter(getActivity());
        settlersofcatansessionAdapter = new CustomSettlersOfCatan(getActivity());
        onenightultimatewerewolfsessionAdapter = new CustomOneNightUltimateWerewolfAdapter(getActivity());
        monopolysessionAdapter = new CustomMonopolyAdapter(getActivity());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject clickedone = (ParseObject) listView.getItemAtPosition(position);
                if (clickedone != null)
                {
                    String clickedones = clickedone.get("boardName").toString();
                    Log.d("CLICKED?", clickedone.toString());
                    if(listView.getAdapter() == mainAdapter)
                    {
                        if (clickedones.equals("Chess"))
                        {
                            listView.setAdapter(chessopensessionAdapter);
                            chessopensessionAdapter.loadObjects();

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ParseObject clickedonEE = (ParseObject) listView.getItemAtPosition(position);
                                    Log.d("ObjectID of this game", clickedonEE.getObjectId().toString());
                                    /*PageFragment fordata = new PageFragment();
                                    Bundle args = new Bundle();
                                    args.putString("SessionIDToJoin", clickedonEE.getObjectId().toString());
                                    fordata.setArguments(args);*/
                                    Intent intent = new Intent(getActivity(), SessionActivity.class);
                                    intent.putExtra("SessionIDToJoin", clickedonEE.getObjectId().toString());
                                    getActivity().startActivity(intent);

                                }
                            });
                        }

                        if (clickedones.equals("Settlers Of Catan"))
                        {
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

                        if (clickedones.equals("Monopoly"))
                        {
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

                        if (clickedones.equals("Splendor"))
                        {
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

                        if (clickedones.equals("One Night Ultimate Werewolf"))
                        {
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












               /* ParseQuery<ParseObject> query = ParseQuery.getQuery("GameOnSession");
                query.whereEqualTo("gameTitle", clickedones);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null)
                        {
                            for ( ParseObject opengames : objects)
                            {
                                //mainAdapter.setTextKey(objects.get(i).toString());
                                Log.d("it works", "no");
                            }

                        }else{
                            Log.d("didnt work", "no");

                        }
                    }
                }); */

            }
        });



        /*//logoutbutton = (Button) view.findViewById(R.id.logouttbutton);

        ParseUser currentUser = ParseUser.getCurrentUser();

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser1 = ParseUser.getCurrentUser();
                String currentuses = currentUser1.getUsername();
                Toast.makeText(getActivity(), currentuses + " has logged out.", Toast.LENGTH_LONG).show();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();// this will now be null
                if (currentUser != null) {
                    Toast.makeText(getActivity(), "Error logging out!", Toast.LENGTH_LONG).show();
                } else {
                    Intent gohome = new Intent(getActivity(), MainActivity.class);
                    startActivity(gohome);
                    getActivity().finish();
                }

            }
        });*/

        return view;
    }

    public static PageFragment newInstance(String text)
    {
        PageFragment f = new PageFragment();

        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }



    public void QuerySessions(String clickedones)
    {

    }


}
