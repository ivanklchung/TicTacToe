package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import java.util.Objects;
import java.text.DecimalFormat;



public class AdvancedStatsActivity extends AppCompatActivity {
    TextView advancedStatsTitle;
    ListView advancedStats;

    String player,
           lastOpponent,
           percentageFormat, averageMovesFormat,
           time;

    int wins,
        totalGames,
        totalMoves,
    // how many digits in a row
    maxNumDigits =11;

    double percentage,
         averageMoves;

    ArrayList<String> advancedStatsArrayList = new ArrayList<>();


    Button button_advancedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_stats);


        // don't show top bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        advancedStatsTitle = findViewById(R.id.textview_advancedStatsTitle);
        advancedStats = findViewById(R.id.listView_advancedStats);
        button_advancedBack = findViewById(R.id.button_back);

        // getting the player's username via intent
        Bundle bundle = getIntent().getExtras();
        player = bundle.getString("playerName");

        getData();


        // Advanced stats + Player name
        String advancedOutput = "Advanced Stats \n" + player.toUpperCase() + ":";
        advancedStatsTitle.setText(advancedOutput);



        // adding ADVANCED STATS to array list
        // Time Stamp
        advancedStatsArrayList.add("Last played " + lastOpponent.toUpperCase() +  " on " + time.substring(0,11) + " @\n" + time.substring(11));
        // # Games Won
        advancedStatsArrayList.add(getStringFormat("Games Won:", String.valueOf(wins)));
        // # Game Played
        advancedStatsArrayList.add(getStringFormat("Total Games:", String.valueOf(totalGames)));
        // % Win Rate
        advancedStatsArrayList.add(getStringFormat("Win Rate(%):", percentageFormat + "%"));
        // Total Moves Used
        advancedStatsArrayList.add(getStringFormat("Total Moves Used:", String.valueOf(totalMoves)));
        // Average Moves Used
        advancedStatsArrayList.add(getStringFormat("Average Moves Used:", averageMovesFormat));


        // Make listview Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.storevalues, android.R.id.text1, advancedStatsArrayList);
        advancedStats.setAdapter(adapter);

        // when clicked, return back to previous page (Show Standings Activity)
        button_advancedBack.setOnClickListener(view -> finish());

    }

    // getting data from Shared Preferences
    public void getData(){
        SharedPreferences sh = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        time = sh.getString(player + "_time", "N/A");
        wins = sh.getInt(player, 0);
        totalGames = sh.getInt(player + "_totalGames", 1);
        lastOpponent = sh.getString(player + "_recentOpponent", "none");
        totalMoves = sh.getInt(player + "_totalMoves", 0);

        // Percentage for win rate
        percentage = (((double) wins)/totalGames) * 100;
        // Average moves calculation
        averageMoves = (((double)totalMoves)/totalGames);
        // Format decimal placement (up to two digits after decimal place)
        DecimalFormat format = new DecimalFormat("###.##");
        percentageFormat = format.format(percentage);
        averageMovesFormat = format.format(averageMoves);
    }

    // creating a string with format
    // adjust number of ">" needed to fill the space
    public String getStringFormat(String category, String value){
        int numDigitsFirst = category.length();
        int numDigitsLast = value.length();

        // start empty, will take on symbol for everything in between
        String output = "";

        // formula for determining how many symbols to fill in between
        int filler = maxNumDigits - numDigitsFirst - numDigitsLast;

        // empty + category name
        output = output + category;
        // add a symbol until filler amount
        for (int i = 0; i < filler; i= i+ 1){
            output += " ";
        }
        // finish off by adding the value
        output += (" " + value);

        return output;
    }
}