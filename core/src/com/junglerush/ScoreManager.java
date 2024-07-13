package com.junglerush;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.Binary;

import java.nio.file.Files;


public class ScoreManager {
    private final String CONNECTION_STRING = "mongodb+srv://ashrafulislamdarkeye:1NfaFxQb1kRYcROD@cluster0.72phzuy.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;


    private final String fileName = "highestScores.txt";
    private final Array<PlayerData> playerData;

    public ScoreManager() {
        connect();
        this.playerData = new Array<>();
        loadData();
    }



    public void connect() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase("myDatabase");
        collection = database.getCollection("fileContents");
        System.out.println("Connected to the database successfully");
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public void saveFileContent(String fileContent) {
        Document document = new Document("fileName", fileName)
                .append("fileContent", fileContent);

        collection.updateOne(
                Filters.eq("fileIdentifier", "uniqueFileIdentifier"),
                new Document("$set", document),
                new UpdateOptions().upsert(true)
        );

        System.out.println("File stored successfully in MongoDB: " + fileName);
    }

    public String getFileContent() {
        Document result = collection.find(Filters.eq("fileIdentifier", "uniqueFileIdentifier")).first();

        if(result != null) {
            return result.getString("fileContent");
        }
        else
        {
            System.out.println("Error! Failed To Load File");
            String defaultValue = "darkEye:524288\n" +
                    "Ashraf:131072\n" +
                    "darkEye:32768\n" +
                    "void:32768\n" +
                    "darkEye:8192\n" +
                    "darkEye:8192\n" +
                    "Ashraf:2048\n" +
                    "Ashraf:2048\n" +
                    "void:2048\n" +
                    "void:2048\n" +
                    "Ashraful:1024\n" +
                    "Ashraful Islam:512\n" +
                    "Ashraful:256\n" +
                    "Ashraful:128";
            return defaultValue;
        }
    }





    public Array<PlayerData> getPlayersData()
    {
        return playerData;
    }

    public void loadData()
    {
        String fileContent = getFileContent();

//        String fileContent = Gdx.files.internal(fileName).readString();
        for (String line : fileContent.split("\n")) {

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


        StringBuilder stringBuilder = new StringBuilder();
        for(PlayerData player:playerData)
        {
            stringBuilder.append(player.getPlayerName()).append(":").append(player.getScore().toString()).append("\n");
        }

        saveFileContent(stringBuilder.toString());
    }

}
