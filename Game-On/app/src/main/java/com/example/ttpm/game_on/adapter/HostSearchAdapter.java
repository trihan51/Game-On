package com.example.ttpm.game_on.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ttpm.game_on.adapter.viewholder.HostSearchViewHolder;
import com.example.ttpm.game_on.R;
import com.example.ttpm.game_on.adapter.models.BoardGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Nguyen on 2/7/2016.
 */
public class HostSearchAdapter extends RecyclerView.Adapter<HostSearchViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<BoardGame> mBoardGames;

    public HostSearchAdapter(Context context, List<BoardGame> boardGames) {
//            mBoardGames = boardGames;
        mLayoutInflater = LayoutInflater.from(context);
        mBoardGames = new ArrayList<>(boardGames);
    }

    @Override
    public HostSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_host_games, parent, false);
        return new HostSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HostSearchViewHolder holder, int position) {
        BoardGame boardGame = mBoardGames.get(position);
        holder.bindGame(boardGame);
    }

    @Override
    public int getItemCount() {
        return mBoardGames.size();
    }

    public void setBoardGames(List<BoardGame> boardGames) {
//            mBoardGames = boardGames;
        mBoardGames = new ArrayList<>(boardGames);
    }

    public BoardGame removeGame(int position) {
        BoardGame model = mBoardGames.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addGame(int position, BoardGame boardGame) {
        mBoardGames.add(position, boardGame);
        notifyItemInserted(position);
    }

    public void moveGame(int fromPosition, int toPosition) {
        BoardGame model = mBoardGames.remove(fromPosition);
        mBoardGames.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<BoardGame> boardGames) {
        // remove all items that do not exist in filtered List anymore
        applyAndAnimateRemovals(boardGames);
        // add all items that did not exist in og list but do in filtered list
        applyAndAnimateAdditions(boardGames);
        // move all items which exist in both lists
        applyAndAnimatedMovedItems(boardGames);
    }

    // iterate backwards thru internal list of Adapter
    // check if ea item is contained in new filtered list
    // if not, call removeGame
    private void applyAndAnimateRemovals(List<BoardGame> newBoardGames) {
        for (int i = mBoardGames.size() - 1; i >= 0; i--) {
            BoardGame boardGame = mBoardGames.get(i);
            if(!newBoardGames.contains(boardGame)) {
                removeGame(i);
            }
        }
    }

    // iterate thru filtered list
    // check if ea item exists in internal list of adapter
    // if not, call addGame
    private void applyAndAnimateAdditions(List<BoardGame> newBoardGames) {
        for (int i = 0, count = newBoardGames.size(); i < count; i++) {
            BoardGame boardGame = newBoardGames.get(i);
            if(mBoardGames.contains(boardGame)) {
                addGame(i, boardGame);
            }
        }
    }

    // after removal and addition, both lists contain the same elements but in different order
    // iterate backwards thru filtered list
    // look up index of ea item in internal list
    // if index different between the two, call moveItem to sort internal list
    private void applyAndAnimatedMovedItems(List<BoardGame> newBoardGames) {
        for (int toPosition = newBoardGames.size() - 1; toPosition >= 0; toPosition--) {
            BoardGame boardGame = newBoardGames.get(toPosition);
            int fromPosition = mBoardGames.indexOf(boardGame);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveGame(fromPosition, toPosition);
            }
        }
    }
}
