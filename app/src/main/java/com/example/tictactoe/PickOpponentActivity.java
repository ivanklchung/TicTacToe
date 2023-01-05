package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import java.util.Objects;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PickOpponentActivity extends AppCompatActivity
{
    String player;
    ListView opponentList;
    String[] opponentSelection;
    Button button_homePage;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_opponent);


        // don't show top bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Return Main Activity
        button_homePage = findViewById(R.id.button_homePage);
        // HOME button click response
        button_homePage.setOnClickListener(view -> finish());

        // stating the resources here
        // getting from strings.xml to select who opponents are
        Resources res = getResources();
        opponentSelection = res.getStringArray(R.array.opponentSelection);

        // creating a listview adapter
        opponentList = findViewById(R.id.listView_opponents);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.storevalues, android.R.id.text1, opponentSelection);
        opponentList.setAdapter(adapter);

        // Click to select opponent (two choices)
        opponentList.setOnItemClickListener((parent, view, position, id) -> {
            switch (position)

            {
                //  Android chosen as opponent
                case 0:
                    player = "Android";
                    saveData();

                    // start game vs Android
                    Intent intent = new Intent(PickOpponentActivity.this, PlayGameActivity.class);
                    startActivity(intent);
                    break;

                // 2 player mode chosen as opponent
                case 1:
                    player = "Player 2";

                    // Enter Player 2 username
                    Intent intentp2 = new Intent(PickOpponentActivity.this, EnterNamesActivity.class);
                    intentp2.putExtra("playerSelection", "Player 2");
                    // Start game vs Player 2 (whatever username has been entered)
                    startActivity(intentp2);
                    break;



                    // error?
                default:
                    break;

            }
        });
    }

    // saving all data to Shared Preferences
    // User vs Android mode has been selected -- SAVE
    public void saveData()
    {
        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if(player.equals("Android"))
        {
            // chosen opponent is Android!
            editor.putString("secondPlayer", "Android");
        }



        
        editor.apply();
    }
}