package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.math.BigDecimal;
import java.util.Objects;

// display welcome message
// listView with 3 items
// - EnterNamesActivity, PlayGameActivity,ShowStandingsActivity

// 3 features added
// 1. create a welcome pop up page (only occurs in beginning)
// 2. implement two user mode (TWO PLAYERS)
// 3. state who is playing and turn time
// 4. advanced stats for ANDROID COMPUTER + all users entered and played.
// 5. add click sound effects

public class MainActivity extends AppCompatActivity
{
    // declare variables
    TextView welcomeMessage;
    TextView ttt;

    ListView listview;
    String[] mainMenuSelection;

    // for checking case scenarios
    int checkpoint;

    // welcome page button
    WelcomePage welcome_page;
    boolean entered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // don't show the top bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // call method to see if shared preferences exists
        checkData();

        // Welcome Page Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        welcome_page = new WelcomePage();
        fragmentTransaction.add(R.id.frame_layout, welcome_page);
        fragmentTransaction.commit();


        Resources res = getResources();
        // get main menu selection from strings.xml
        mainMenuSelection = res.getStringArray(R.array.mainMenu);
        welcomeMessage = findViewById(R.id.textView_welcomeMessage);
        ttt = findViewById(R.id.textView_TTT);

        // populate the listView by setting up Adapter
        listview = findViewById(R.id.listView_selectionMP);
        // use custom array adapter retrieve content from mainMenuSelection
        // ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.storevalues, android.R.id.text1, mainMenuSelection);
        listview.setAdapter(adapter);




        // case 0: Launch Enter Names Activity
        // case 1: Launch Enter Names Activity OR Launch Pick Opponent Activity
        // case 2: Launch Show Standings Activity
        // create intent 1,2,3 for each of the selections
        listview.setOnItemClickListener((parent, view, position, id) ->
        {
            switch (position)
            {
                // will state which player is being chosen
                case 0:
                    Intent intent = new Intent(MainActivity.this, EnterNamesActivity.class);
                    intent.putExtra("playerSelection", "Player 1");
                    startActivity(intent);
                    break;

                case 1:
                    // if equal 10101010, shared preference does NOT exist
                    if(checkpoint == 10101010)
                    {
                        Intent intent1 = new Intent(MainActivity.this, EnterNamesActivity.class);
                        //  will state which player is being chosen
                        intent1.putExtra("playerSelection", "Player 1");
                        intent1.putExtra("firstRun", true);
                        startActivity(intent1);
                    }
                    else
                    {
                        Intent intent2 = new Intent(MainActivity.this, PickOpponentActivity.class);
                        startActivity(intent2);
                    }
                    break;

                case 2:
                    Intent intent3 = new Intent(MainActivity.this, ShowStandingsActivity.class);
                    startActivity(intent3);
                    break;

                // error scenario, since only three selection options
                default:
                    break;

            }
        });
    }

    // Welcome page check if opened?
    public void fragmentClicked(boolean isClicked){
        entered = isClicked;
    }

    // when called will check if shared preference exist in history
    // if it equates to random big number, shared preferences does not exist yet
    public void checkData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        checkpoint = sharedPreferences.getInt("android", 10101010);
    }


    // turn phone save data
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        outState.putString("welcome", welcomeMessage.getText().toString());
        outState.putString("ttt", ttt.getText().toString());

        // outState.putParcelable(mainMenuSelection, listview.onSaveInstanceState());




        super.onSaveInstanceState(outState);
    }

    // recall saved data after turning
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {
        super.onRestoreInstanceState(saved);

        entered = saved.getBoolean("entered");

        // check if welcome screen has passed or not
        if(entered){
            welcome_page.button_enter.performClick();
        }

        welcomeMessage.setText(saved.getString("welcome"));
        ttt.setText(saved.getString("ttt"));

        // mainMenuSelection.toString(saved.getString("mainMenuSelection"));


    }


}