package com.example.ttpm.game_on;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment2 extends android.support.v4.app.Fragment {

    TextView textView;
    GridView gridview;
    public PageFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_page_fragment2, container, false);

        textView = (TextView)view.findViewById(R.id.textView4);

        //Bundle bundle = getArguments();
        //String message = Integer.toString(bundle.getInt("count"));
        textView.setText(getArguments().getString("msg"));

        gridview = (GridView)view.findViewById(R.id.gridView);
        gridview.setAdapter(new MyAdapter(view.getContext()));


        return view;
    }

    public static PageFragment2 newInstance(String text)
    {
        PageFragment2 f = new PageFragment2();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }


}
