package com.example.ttpm.game_on;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostSearchFragment extends android.support.v4.app.Fragment {

    private RecyclerView mHostSearchRecyclerView;
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
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_host_search, container, false);
        RelativeLayout contentView = new RelativeLayout( getActivity() );
        // add all your stuff

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
                if(hasFocus) {
                    initList();
                }
            }
        });
//        host-search end

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
            listView.setAdapter(adapter);
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
                        if(!tempListItems.contains(boardGameName.getString("boardName"))) {
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

    private void post() {
        GameOnSession session = new GameOnSession();
        session.setGameTitle("chess");
        session.setHost(ParseUser.getCurrentUser());

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        session.setACL(acl);

        session.saveInBackground();
    }

    public static HostSearchFragment newInstance(String text)
    {
        HostSearchFragment f = new HostSearchFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    private class HostSearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public HostSearchHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class HostSearchAdapter extends RecyclerView.Adapter<HostSearchHolder> {

        @Override
        public HostSearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(HostSearchHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}





