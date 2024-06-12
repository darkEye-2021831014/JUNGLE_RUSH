package com.junglerush;

import java.math.BigInteger;

public class PlayerData{
    private String playerName;
    private BigInteger score;
    public PlayerData(String playerName, BigInteger score){
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public BigInteger getScore() {
        return score;
    }
}
