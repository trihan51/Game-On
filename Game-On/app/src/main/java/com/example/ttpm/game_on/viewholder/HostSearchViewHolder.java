package com.example.ttpm.game_on.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.models.BoardGame;

/**
 * Created by Tony Nguyen on 2/7/2016.
 */
public class HostSearchViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitleTextView;
    private TextView mSessionsTextView;
    private TextView mJoinButton;

    private BoardGame mBoardGame;

    public HostSearchViewHolder(View itemView) {
        super(itemView);

        mTitleTextView =
                (TextView) itemView.findViewById(R.id.list_item_host_games_game_pic);
        mSessionsTextView =
                (TextView) itemView.findViewById(R.id.list_item_host_games_game_open);
        mJoinButton =
                (TextView) itemView.findViewById(R.id.list_item_host_games_button);
        mJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "Wow, you QUICK JOIN this game!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void bindGame(BoardGame boardGame) {
        mBoardGame = boardGame;
        mTitleTextView.setText(mBoardGame.getBoardName());
        mSessionsTextView.setText(Integer.toString(mBoardGame.getOpenSessions()));
    }
}
