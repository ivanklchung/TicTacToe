package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Type;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// display simple form
// have a save button for saving the player username
// click back on android device to return to home menu

public class EnterNamesActivity extends AppCompatActivity {

    TextView instructions;
    TextView username;
    EditText editText_enterUsername;
    Button button_save;

    String playerSelection;
    
    ArrayList<String> listUsernames = new ArrayList<>();
    
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names);

        // don't show top bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        
        // create variables 
        instructions = findViewById(R.id.textView_usernameDescription);
        username = findViewById(R.id.textView_playerUsername);
        editText_enterUsername = findViewById(R.id.editText_enterUsername);
        button_save = findViewById(R.id.button_save);


        // retrieving intent data
        Bundle bundle = getIntent().getExtras();
        
        // What player has been chosen 
        playerSelection = bundle.getString("playerSelection");
        
        // First Run of app?
        firstRun = bundle.getBoolean("firstRun");

        // If two player mode chosen
        if (playerSelection.equals("Player 2"))
        {
            username.setText(R.string.enterP2username);
        }

        // save button clicked
        button_save.setOnClickListener(view ->
        {
            // saving username data
            saveData();

            // two player mode selected
            // make sure INTENT (straight to game mode)
            if(playerSelection.equals("Player 2"))
            {
                Intent intent = new Intent(EnterNamesActivity.this, PlayGameActivity.class);
                startActivity(intent);
            }
            // first time opening game, need to choose opponent
            else if(firstRun)
            {
                Intent intent1 = new Intent(EnterNamesActivity.this, PickOpponentActivity.class);
                startActivity(intent1);
            }
        });
    }

    // saving all current data
    public void saveData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // username chosen
        String enteredUsername = editText_enterUsername.getText().toString();

        // this method is to gain access to all usernames
        // https://www.youtube.com/watch?v=8H6trQzcEw4
        // https://stackoverflow.com/questions/5571092/convert-object-to-json-in-android
        Gson g = new Gson();
        String j = sharedPreferences.getString("listUsernames", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listUsernames = g.fromJson(j, type);

        // if no usernames are created,
        // need to create a new one
        if(listUsernames == null){
            listUsernames = new ArrayList<>();
            listUsernames.add("android");

            // framework for android computer stats
            // opponent
            editor.putString("android_recentOpponent", "name");
            // time game played
            editor.putString("android_time", "N/A");
            // wins
            editor.putInt("android", 0);
            // games played in total
            editor.putInt("android_totalGames", 0);
            // moves made in total
            editor.putInt("android_totalMoves", 0);

        }

        // scanning if username is in current list
        // absent>>> make a new username "blank log" for specific username and then add that to the list
        // switch to lowercase for easy comparison
        int storedUsername = sharedPreferences.getInt(enteredUsername.toLowerCase(), 1000);
        if(storedUsername == 1000)
        {
            // switch lowercase!
            String lowercaseUsername = enteredUsername.toLowerCase();

            // create new blank log for new username
            // opponent
            editor.putString(lowercaseUsername + "_recentOpponent", "name");
            // time game played at
            editor.putString(lowercaseUsername + "_time", "N/A");
            // wins
            editor.putInt(lowercaseUsername, 0);
            // games played in total
            editor.putInt(lowercaseUsername + "_totalGames", 0);
            // moves made in total
            editor.putInt(lowercaseUsername + "_totalMoves", 0);

            // add username to full list of usernames players have entered (make sure lowercase)
            listUsernames.add(enteredUsername.toLowerCase());
        }

        // all usernames saved to be used in Play Game Activity

        // p1 selection
        if(playerSelection.equals("Player 1"))
        {
            editor.putString("currentPlayerName", enteredUsername);
        }
        // p2 selection
        else if (playerSelection.equals("Player 2"))

        {
            editor.putString("secondPlayer", enteredUsername);
        }

        // json conversion + save
        Gson gson = new Gson();
        String json = gson.toJson(listUsernames);
        editor.putString("listUsernames", json);

        // must apply + commit data to sharedPreferences
        editor.apply();
    }

    // turn phone save data
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {

        outState.putString("currentUsername", editText_enterUsername.getText().toString());
        super.onSaveInstanceState(outState);
    }

    // recall saved data after turning
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        editText_enterUsername.setText(savedInstanceState.getString("currentUsername"));
        super.onRestoreInstanceState(savedInstanceState);
    }

}