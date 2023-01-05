package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ShowStandingsActivity extends AppCompatActivity
{
    ListView standings;

    ArrayList<String> leaderboard = new ArrayList<>();
    ArrayList<String> listUsernames = new ArrayList<>();
    HashMap<String, Integer> board = new HashMap<>();

    Button button_home,
           button_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_standings);

        // don't show top bar
        Objects.requireNonNull(getSupportActionBar()).hide();


        getData();

        // declare all initial variables
        standings = findViewById(R.id.listview_LEADERBOARD);
        button_home = findViewById(R.id.button_home);
        button_reset = findViewById(R.id.button_resetLeaderboard);

        // listview connect with string ARRAY via adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.storevalues, android.R.id.text1, leaderboard);
        standings.setAdapter(adapter);

        // getting the leaderboard values
        Object[] sortedScores = board.values().toArray();
        Object[] sortedUsernames = board.keySet().toArray();

        // adding stats to leaderboard
        for(int i = sortedUsernames.length; i > 0; i--)
        {
            String username = String.valueOf(sortedUsernames[i-1]);
            int score = Integer.parseInt(String.valueOf(sortedScores[i-1]));
            leaderboard.add(username + " . Wins: " + score);
        }

        // HOME button click response
        button_home.setOnClickListener(view -> finish());

        // RESET button click response
        // all stats will be cleared
        // return to Main Activity Page
        // state username currently
        button_reset.setOnClickListener(view ->
        {
            getSharedPreferences("prefs", 0).edit().clear().apply();
            Intent in = new Intent(ShowStandingsActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
        );

        // Click standings >>>> Advanced Stats
        // click specific location on the listView
        // get the name and convert to text
        standings.setOnItemClickListener((parent, view, position, id) ->
        {

            String selectedFromList = (String) (standings.getItemAtPosition(position));
            selectedFromList = selectedFromList.substring(0, selectedFromList.indexOf('.')-1).toLowerCase();

            // Advanced Stats Page opens
            // state which name has been clicked from the list
            // show the advanced stats
            Intent intent = new Intent(ShowStandingsActivity.this, AdvancedStatsActivity.class);
            intent.putExtra("playerName", selectedFromList);
            startActivity(intent);
        }
        );
    }

    // Getting data from the Shared Preferences
    // https://stackoverflow.com/questions/20773850/gson-typetoken-with-dynamic-arraylist-item-type
    public void getData(){
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        // retrieve from file all the string array
        Gson gson = new Gson();
        String json = sharedPreferences.getString("listUsernames", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listUsernames = gson.fromJson(json, type);

        // if there are usernames present , add from shared preferences
        if(listUsernames != null){
            for(int i = 0; i < listUsernames.size(); i++)
            {
                // getting from Shared Preferences
                String username = listUsernames.get(i);
                int wins = sharedPreferences.getInt(username, 0);

                // hashmap add these values
                board.put(username.toUpperCase(Locale.ROOT), wins);
            }
        }





        // Use HASHMAP!
        // https://www.youtube.com/watch?v=70qy6_gw1Hc
        // This is hashmap organization
        // List<Map.Entry<String, Integer> > list = new LinkedList<>(board.entrySet());

        List<Map.Entry<String, Integer> > list = new LinkedList<>(board.entrySet());
        // https://stackoverflow.com/questions/42252481/please-how-do-i-sort-map-elements-based-on-values-in-ascending-order-in-java
        Collections.sort(list, (o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));
        HashMap<String, Integer> hashTemp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list)
        {
            // mapping the list values
            hashTemp.put(aa.getKey(), aa.getValue());
        }
        board = hashTemp;

    }

}