package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

// GameBoard with 3x3 grid of labels
// labels will be blank, X, or O
// Player = X
// Android = O
// winner is when player's symbol reaches three adjacent spaces in a row, column, or diagonal

public class PlayGameActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView[][] gameBoard;

    TextView r1c1, r1c2, r1c3,
             r2c1, r2c2, r2c3,
             r3c1, r3c2, r3c3,
             startInstruction;

    char[][] label;

    boolean gameWinner = false,
            p1Turn = true;

    int p1Winner, p2Winner, androidWinner,
        p1TotalGames, p2TotalGames, androidTotalGames,
        p1TotalMoves, p2TotalMoves, androidTotalMoves;

    String playerUsername,
           p2,
           p1RecentOpponent, p2RecentOpponent, androidRecentOpponent;

    Button button_home;
    Button button_quitGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        // don't show the top bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // retrieving data via shared preferences
        getData();

        // declaring variables

        // game board tiles textViews

        r1c1 = findViewById(R.id.r1c1);
        r1c2 = findViewById(R.id.r1c2);
        r1c3 = findViewById(R.id.r1c3);
        r2c1 = findViewById(R.id.r2c1);
        r2c2 = findViewById(R.id.r2c2);
        r2c3 = findViewById(R.id.r2c3);
        r3c1 = findViewById(R.id.r3c1);
        r3c2 = findViewById(R.id.r3c2);
        r3c3 = findViewById(R.id.r3c3);

        // click listening for each tile
        r1c1.setOnClickListener(this);
        r1c2.setOnClickListener(this);
        r1c3.setOnClickListener(this);
        r2c1.setOnClickListener(this);
        r2c2.setOnClickListener(this);
        r2c3.setOnClickListener(this);
        r3c1.setOnClickListener(this);
        r3c2.setOnClickListener(this);
        r3c3.setOnClickListener(this);

        //  game board tiles initially with blank (notext) labels
        label = new char[][]
        {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };

        // creating the 3x3 game board
        // r means row
        // c means column
        gameBoard = new TextView[][]
        {
                {r1c1, r1c2, r1c3},
                {r2c1, r2c2, r2c3},
                {r3c1, r3c2, r3c3}
        };

        // greeting players via username
        // instructions to begin game by tapping tile
        startInstruction = findViewById(R.id.textView_startInstruction);
        String output = "Welcome " + playerUsername + "!\nTap Tile To Start Match...";
        startInstruction.setText(output);




        // Change Opponents
        button_home = findViewById(R.id.button_diffOpponent);
        button_home.setOnClickListener(view ->
        {
            finish();
            Intent opponentIntent = new Intent(PlayGameActivity.this, PickOpponentActivity.class);
            startActivity(opponentIntent);
        });


        // exit game and return to Main Activity
        button_quitGame = findViewById(R.id.button_quitGame);
        button_quitGame.setOnClickListener(view ->
        {
            finish();
            Intent homeIntent = new Intent(PlayGameActivity.this, MainActivity.class);
            startActivity(homeIntent);
        });

    }

    // click each tile grid response
    // addTurn, count how many turns, until board is filled
    @Override
    public void onClick(View view) {
        switch(view.getId())
        // initially
        // row 1 = 0
        // column 1 = 0

        {
            // row 1 column 1
            case R.id.r1c1:
                addTurn(0, 0);
                break;

            // row 1 column 2
            case R.id.r1c2:
                addTurn(0, 1);
                break;

            // row 1 column 3
            case R.id.r1c3:
                addTurn(0, 2);
                break;

            // row 2 column 1
            case R.id.r2c1:
                addTurn(1, 0);
                break;

            // row 2 column 2
            case R.id.r2c2:
                addTurn(1, 1);
                break;

            // row 2 column 3
            case R.id.r2c3:
                addTurn(1, 2);
                break;

            // row 3 column 1
            case R.id.r3c1:
                addTurn(2, 0);
                break;

            // row 3 column 2
            case R.id.r3c2:
                addTurn(2, 1);
                break;

            // row 3 column 3
            case R.id.r3c3:
                addTurn(2, 2);
                break;

            // error?
            default:
                break;
        }
    }



    // Async task that performs the adding turn tasks
    // https://developer.android.com/reference/android/os/AsyncTask
    // https://stackoverflow.com/questions/61032158/setting-up-asynctask-to-call-other-methods
    // use Void, Void UPPERCASE
    // https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingWorker.html

    private class tttGame extends AsyncTask<Integer, Void, Void>
    {
        // which player's turn
        String turn;

        @Override
        protected Void doInBackground(Integer... integers) {

            int row = integers[0];
            int col = integers[1];

            // Add Move
            // Android Turn or Player X turn
            if( p2.equals("Android")|| p1Turn )
            {
                // X is added to board
                // player X moves are updated
                label[row][col] = 'X';
                gameBoard[row][col].setText(R.string.x);
                p1TotalMoves++;

                // has Player X won?
                if(checkP1Win()){
                    openDialog(playerUsername + " has won!");
                    // update winner
                    p1Winner++;
                    saveData();
            }

                // Android turn IF player X has not won
                if(p2.equals("Android") && !checkP1Win()){
                    // add Android Move
                    getAndroidMove();
                    turn = "Tap to play\nyour turn";
                    androidTotalMoves++;

                    // cannot have two winners scenario
                    // either or, not player X and yes to Android
                    if(!checkP1Win() && checkP2Win()){
                        openDialog(p2 + " has won!");
                        androidWinner++;
                        saveData();
                    }


                }
                else
                // start p2 turn if all above scenarios fail
                {
                    turn = p2 + "'s\nTurn";
                }


            }

            // getting p2 move
            else
            {
                // Adding p2 move onto board
                gameBoard[row][col].setText(R.string.o);
                label[row][col] = 'O';
                p2TotalMoves++;
                turn = playerUsername + "'s\nTurn";

                // p2 win?
                if(checkP2Win())
                {
                    // state player has won dialog!
                    openDialog(p2 + " wins!");
                    p2Winner++;
                    saveData();
                }
            }

            // is it a tie???
            // all spaces are filled and no 3 in a row scenario
            if(numberOfTilesLeft() == 0)
            {
                // state players have a draw for match
                openDialog("It's a draw!");
                saveData();
            }

            // game continue
            // turns should switch?
            if(!gameWinner)
            {
                p1Turn = !p1Turn;
            }
            else
            {
                gameWinner = false;
            }

            // don't need to return anything for this method
            return null;
        }

        @Override
        // https://developer.android.com/reference/android/os/AsyncTask
        protected void onPostExecute(Void v)
        {
            super.onPostExecute(v);
            startInstruction.setText(turn);
        }
    }


    // addTurn is Increment by 1, for every turn
    // via tttGame (AsyncTask)
    public void addTurn(int row, int column)
    {
        new tttGame().execute(row, column);

    }

    // p1 is X
    // space to ||

    // Check if player X has a win
    public boolean checkP1Win()
    {
        // initially assume not won
        boolean userWon = false;

        // HORIZONTAL WIN?
        if
                // ROW 1
                ((label[0][0] == label[0][1] && label[0][1] == label[0][2] && label[0][2] == 'X')
                ||
                // ROW 2
                (label[1][0] == label[1][1] && label[1][1] == label[1][2] && label[1][2] == 'X')
                ||
                // ROW 3
                (label[2][0] == label[2][1] && label[2][1] == label[2][2] && label[2][2] == 'X'))
                {
                    userWon = true;
                }
        // VERTICAL WIN?
        else if
                // COLUMN 1
                ((label[0][0] == label[1][0] && label[1][0] == label[2][0] && label[2][0] == 'X')
                ||
                // COLUMN 2
                (label[0][1] == label[1][1] && label[1][1] == label[2][1] && label[2][1] == 'X')
                ||
                // COLUMN 3
                (label[0][2] == label[1][2] && label[1][2] == label[2][2] && label[2][2] == 'X'))
                {
                    userWon = true;
                }
        // DIAGONAL WIN?
        else if
                // TOP LEFT TO BOTTOM RIGHT
                ((label[0][0] == label[1][1] && label[1][1] == label[2][2] && label[2][2] == 'X')
                ||
                // BOTTOM LEFT TO BOTTOM RIGHT
                (label[0][2] == label[1][1] && label[1][1] == label[2][0] && label[2][0] == 'X'))
                {
                    userWon = true;
                }
        // win combo?
        return userWon;
    }

    // Check if second player has won
    public boolean checkP2Win()
    {
        // initially assume not won
        boolean userWon = false;

        // HORIZONTAL WIN?
        if
                // ROW 1
                ((label[0][0] == label[0][1] && label[0][1] == label[0][2] && label[0][2] == 'O')
                ||
                // ROW 2
                (label[1][0] == label[1][1] && label[1][1] == label[1][2] && label[1][2] == 'O')
                ||
                // ROW 3
                (label[2][0] == label[2][1] && label[2][1] == label[2][2] && label[2][2] == 'O'))
                {
                    userWon = true;
                }

        // VERTICAL WIN?
        else if
                // COLUMN 1
                ((label[0][0] == label[1][0] && label[1][0] == label[2][0] && label[2][0] == 'O')
                ||
                // COLUMN 2
                (label[0][1] == label[1][1] && label[1][1] == label[2][1] && label[2][1] == 'O')
                ||
                // COLUMN 3
                (label[0][2] == label[1][2] && label[1][2] == label[2][2] && label[2][2] == 'O'))
                {
                    userWon = true;
                }
        // DIAGONAL WIN?
        else if
                // TOP LEFT TO BOTTOM RIGHT
                ((label[0][0] == label[1][1] && label[1][1] == label[2][2] && label[2][2] == 'O')
                ||
                // BOTTOM LEFT TO TOP RIGHT
                (label[0][2] == label[1][1] && label[1][1] == label[2][0] && label[2][0] == 'O'))
                {
                    userWon = true;
                }
        // win combo?
        return userWon;
    }

    // Android move
    public void getAndroidMove(){
       // generate a random location for tile to be put on (MAKE SURE EMPTY)
        Random rand = new Random();
        boolean valid = false;

        // make sure there is a tile space for android computer to choose from
        if(numberOfTilesLeft()>0)
        {
            do
            {
                // 3 x 3 grid!
                int compRow = rand.nextInt(3);
                int compCol = rand.nextInt(3);

                // if there is no X or O occupy a tile
                if (label[compRow][compCol] != 'X' && label[compRow][compCol] != 'O')
                {
                    label[compRow][compCol] = 'O';
                    gameBoard[compRow][compCol].setText("O");
                    valid = true;
                }
                // stays until open spot
            } while (!valid);
        }
    }

    // game board is reset and cleared
    public void resetBoard(){
        // scanning grid
        for (int i = 0 ; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // setting a blank space and replacing X, O, or already existing blank space
                label[i][j] = ' ';
                gameBoard[i][j].setText(" ");
            }
        }

        // once reset, p1 starts again
        String output = playerUsername + "\nstarts!";
        startInstruction.setText(output);
        p1Turn = true;
    }

    // checking to see how many tiles are left
    public int numberOfTilesLeft()
    {
        //empty initially
        int numberOfTiles=0;

        // go through grid and check if empty or not
        for (int i = 0 ; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (label[i][j] == ' ') {
                    numberOfTiles++;
                }
            }
        }

        return numberOfTiles;
    }








    // Saving data to Shared Preferences
    public void saveData(){
        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String time = getPlayTime();


        // Android saving data
        if (p2.equals("Android"))
        {
            p1RecentOpponent = "android";
            androidRecentOpponent = playerUsername.toLowerCase();
            androidTotalGames++;

            editor.putInt("android", androidWinner);
            editor.putInt("android_totalGames", androidTotalGames);
            editor.putString("android_recentOpponent", androidRecentOpponent);
            editor.putInt("android_totalMoves", androidTotalMoves);
            editor.putString("android_time", time);
        }

        // Another player saving data

        else
        {
            p1RecentOpponent = p2.toLowerCase();
            p2RecentOpponent = playerUsername.toLowerCase();
            p2TotalGames++;

            editor.putInt(p2.toLowerCase(), p2Winner);
            editor.putInt(p2.toLowerCase() + "_totalGames", p2TotalGames);
            editor.putString(p2.toLowerCase() + "_recentOpponent", p2RecentOpponent);
            editor.putInt(p2.toLowerCase() + "_totalMoves", p2TotalMoves);
            editor.putString(p2.toLowerCase() + "_time", time);
        }

        // Player 1 saving data
        p1TotalGames++;
        editor.putInt(playerUsername.toLowerCase(), p1Winner);
        editor.putInt(playerUsername.toLowerCase() + "_totalGames", p1TotalGames);
        editor.putString(playerUsername.toLowerCase() + "_recentOpponent", p1RecentOpponent);
        editor.putInt(playerUsername.toLowerCase() + "_totalMoves", p1TotalMoves);
        editor.putString(playerUsername.toLowerCase() + "_time", time);

        editor.apply();
    }


    // Retrieve data from shared preferences file
    public void getData(){
        SharedPreferences share = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        // UserName DATA
        playerUsername = share.getString("currentPlayerName", "Player 1");
        p2 = share.getString("secondPlayer", "Player 2");

        //>>>>


        // P1 DATA
        p1Winner = share.getInt(playerUsername.toLowerCase(), 0);
        p1TotalGames = share.getInt(playerUsername.toLowerCase() + "_totalGames", 0);
        p1TotalMoves = share.getInt(playerUsername.toLowerCase() + "_totalMoves", 0);

        // P2 DATA
        p2Winner = share.getInt(p2.toLowerCase(), 0);
        p2TotalGames = share.getInt(playerUsername.toLowerCase() + "_totalGames", 0);
        p2TotalMoves = share.getInt(playerUsername.toLowerCase() + "_totalMoves", 0);

        // ANDROID DATA
        androidWinner = share.getInt("android", 0);
        androidTotalGames = share.getInt("android_totalGames", 0);
        androidTotalMoves = share.getInt("android_totalMoves", 0);

    }





    // System time when game played
    // https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    public String getPlayTime()
    {
        // Time when game created
        long time = System.currentTimeMillis();

        // MONTH DATE YEAR
        // *** do I need PST or simplify to just Canada
        SimpleDateFormat formatter = new SimpleDateFormat(" MM/dd/yyyy hh:mm aa", Locale.CANADA);

        // convert to milli seconds via Calendar
        // https://stackoverflow.com/questions/5162607/android-milliseconds-as-of-a-time
        Calendar calendar = Calendar.getInstance();


        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }

    // pop up alert dialog window appears when someone wins!
    public void openDialog(String text){
        PopUp window = new PopUp(text);
        window.show(getSupportFragmentManager(), "dialog");
        gameWinner = true;
    }

    // turn phone save data
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        outState.putString("title", startInstruction.getText().toString());
        outState.putBoolean("gameWon", gameWinner);
        outState.putBoolean("player1Turn", p1Turn);

        // saving all values on Game Board
        outState.putString("r1c1", r1c1.getText().toString());
        outState.putString("r1c2", r1c2.getText().toString());
        outState.putString("r1c3", r1c3.getText().toString());
        outState.putString("r2c1", r2c1.getText().toString());
        outState.putString("r2c2", r2c2.getText().toString());
        outState.putString("r2c3", r2c3.getText().toString());
        outState.putString("r3c1", r3c1.getText().toString());
        outState.putString("r3c2", r3c2.getText().toString());
        outState.putString("r3c3", r3c3.getText().toString());


        outState.putCharArray("r1", label[0]);
        outState.putCharArray("r2", label[1]);
        outState.putCharArray("r3", label[2]);


        super.onSaveInstanceState(outState);
    }

    // // recall saved data after turning
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle saved) {


        startInstruction.setText(saved.getString("title"));
        gameWinner = saved.getBoolean("gameWon");
        p1Turn = saved.getBoolean("player1Turn");

        // Game Board Retrieval
        r1c1.setText(saved.getString("r1c1"));
        r1c2.setText(saved.getString("r1c2"));
        r1c3.setText(saved.getString("r1c3"));
        r2c1.setText(saved.getString("r2c1"));
        r2c2.setText(saved.getString("r2c2"));
        r2c3.setText(saved.getString("r2c3"));
        r3c1.setText(saved.getString("r3c1"));
        r3c2.setText(saved.getString("r3c2"));
        r3c3.setText(saved.getString("r3c3"));



        label = new char[][]
                {
                saved.getCharArray("r1"),
                saved.getCharArray("r2"),
                saved.getCharArray("r3")
                };

        super.onRestoreInstanceState(saved);
    }

}