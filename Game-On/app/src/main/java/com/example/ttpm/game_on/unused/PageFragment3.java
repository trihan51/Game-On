package com.example.ttpm.game_on.unused;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ttpm.game_on.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment3 extends android.support.v4.app.Fragment {

    private ParseQueryAdapter<ParseObject> mainAdapter;

    private ListView listView;
    //private CustomAdapter urgentTodosAdapter;

    public PageFragment3() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_page_fragment3, container, false);

        // Initialize main ParseQueryAdapter
        mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), "BoardGames");
        //mainAdapter1 = new ParseQueryAdapter<ParseObject>(getActivity(), "GameSessionsTest1");

        mainAdapter.setTextKey("boardName");
        mainAdapter.setImageKey("gameLogo");

        // Initialize the subclass of ParseQueryAdapter
        //urgentTodosAdapter = new CustomAdapter(getActivity());

        // Initialize ListView and set initial view to mainAdapter
listView = (ListView)view.findViewById(R.id.listView);
        mainAdapter.loadObjects();
        listView.setAdapter(mainAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                //ParseQuery<ParseObject>innerquery = ParseQuery.getQuery("")

                mainAdapter.clear();
                mainAdapter.notifyDataSetChanged();
                mainAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), "GameSessionsTest1");
                //mainAdapter.getItemView(ParseObject , )
                listView.setAdapter(mainAdapter);
                mainAdapter.loadObjects();





                //mainAdapter.setImageKey("Host.profilePicture");



            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ParseObject gameID = mainAdapter.getItem(position);
                //listView.clearChoices();
                //mainAdapter1 = new ParseQueryAdapter<ParseObject>(getActivity(), "GameSessionsTest1");
                //ParseQuery query2 = new ParseQuery("GameSessonsTest1");
                ParseUser curruser = ParseUser.getCurrentUser();
                ParseObject gameScore = new ParseObject("GameSessionsTest1");
                gameScore.put("gameTitle", ParseObject.createWithoutData("BoardGames", gameID.getObjectId()));
                gameScore.put("Host", ParseObject.createWithoutData("_User", curruser.getObjectId()));
                gameScore.put("Open", true);
                //gameScore.put("random", "less do it");
                try {
                    gameScore.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                return false;
            }
        });
        return view;
    }



    public static PageFragment3 newInstance(String text)
    {
        PageFragment3 f = new PageFragment3();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }




}
