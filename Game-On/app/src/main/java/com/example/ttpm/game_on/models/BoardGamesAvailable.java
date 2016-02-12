package com.example.ttpm.game_on.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by manbirrandhawa on 2/11/16.
 */
@ParseClassName("BoardGames")
public class BoardGamesAvailable extends ParseObject
{
    public String getboardName()
    {
        return getString("boardName");
    }
}
