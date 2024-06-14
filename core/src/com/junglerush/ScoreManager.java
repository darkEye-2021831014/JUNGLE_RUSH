package com.junglerush;


import com.badlogic.gdx.utils.Array;
import java.io.*;
import java.math.BigInteger;
import java.util.*;


public class ScoreManager {
    private String fileName = "Files/highestScores.txt";
    private Array<PlayerData> playerData;

    public ScoreManager() {

        this.playerData = new Array<>();
        loadData();
    }

    public Array<PlayerData> getPlayersData()
    {
        return playerData;
    }

    public void loadData()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringBuilder playerScore = new StringBuilder(),
                        playerName = new StringBuilder();
                int ok = 0;
                for (int i = 0; i < line.length(); i++) {
                    if(ok==1) playerScore.append(line.charAt(i));
                    if(line.charAt(i)==':') ok=1;
                    if(ok == 0)playerName.append(line.charAt(i));
                }
                playerData.add(new PlayerData(playerName.toString(),new BigInteger(playerScore.toString())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void addScore(String playerName, BigInteger score,int limit) {
        playerData.add(new PlayerData(playerName,score));
        //sort in descending order
        playerData.sort(new Comparator<PlayerData>() {
            @Override
            public int compare(PlayerData player1, PlayerData player2) {
                return player2.getScore().compareTo(player1.getScore());
            }
        });

        while (playerData.size > limit)
        {
            playerData.removeIndex(playerData.size-1);
        }

        int cnt = 0;
        for(PlayerData player:playerData)
        {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, cnt != 0))) {
                bw.write(player.getPlayerName() + ":" + player.getScore().toString() + "\n"); // Add a newline character to separate scores
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to write score to file: " + e.getMessage());
            }
            cnt++;
        }
    }


}
