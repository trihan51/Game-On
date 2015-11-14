package com.example.ttpm.game_on;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment3 extends android.support.v4.app.Fragment {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView listView;
    private CustomAdapter urgentTodosAdapter;

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
        mainAdapter.setTextKey("boardName");
        mainAdapter.setImageKey("gameLogo");

        // Initialize the subclass of ParseQueryAdapter
        urgentTodosAdapter = new CustomAdapter(getActivity());

        // Initialize ListView and set initial view to mainAdapter
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

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
