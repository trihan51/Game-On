package com.example.ttpm.game_on;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by manbirrandhawa on 2/14/16.
 */
public class SessionPageAdapter extends ParseQueryAdapter<ParseObject>
{
    public SessionPageAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("GameOnSession");
                query.whereEqualTo("gameTitle", "chess");
                query.whereNotEqualTo("host", ParseUser.getCurrentUser());
                //query.include("gameTitle.boardName");
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.host_item_sessionpage, null);
        }

        super.getItemView(object, v, parent);

     /*   // Add and download the image
        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile imageFile = object.getParseFile("gameLogo");
        if (imageFile != null) {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground();
        }*/

        // Add Name of Game Title
        TextView titleTextView = (TextView) v.findViewById(R.id.GameNameSearchSessions);
        titleTextView.setText(object.getString("gameTitle"));

        //SetTimeStamp
        TextView timestampView = (TextView)v.findViewById(R.id.TimeRoomCreatedSearchSessions);

        //Format The date & time into 12 hour format
        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        String formattedDateTime = formatter.format(object.getCreatedAt());

        // Set the created at date/time
        timestampView.setText(formattedDateTime);

      /*  TextView participantsInRoomView = (TextView) v.findViewById(R.id.participantsInRoom);
        participantsInRoomView.setText(object.getJSONArray("participants").toString()); */

        return v;
    }
}
