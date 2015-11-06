package com.example.ttpm.game_on;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;


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

        //textView = (TextView)view.findViewById(R.id.textView4);

        //Bundle bundle = getArguments();
        //String message = Integer.toString(bundle.getInt("count"));
        //textView.setText(getArguments().getString("msg"));
        //textView.setText("holla");
       gridview = (GridView)view.findViewById(R.id.gridView);
        //gridview.setAdapter(new MyAdapter(view.getContext()));
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("BoardGames");
                query.whereEqualTo("boardID", "Dan Stemkoski");

                Toast.makeText(getActivity(), "Host" , Toast.LENGTH_SHORT).show();
            }
        });


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

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        private Integer[] mThumbs = {
                R.drawable.sample_2,
                R.drawable.sample_1,
                R.drawable.sample_0,
        };



        public Object getItem(int position)
        {
            return null;
        }
        public long getItemId(int position)
        {
            return 0;
        }
        public int getCount()
        {
            return mThumbs.length;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(320,320));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4,4,4,4);
            imageView.setImageResource(mThumbs[position]);
            return imageView;

        }
    }




}
