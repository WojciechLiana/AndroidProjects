package com.example.wojtas.guessinggame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;

/**
 * @version 1.0
 * @author Wojtas
 */
public class MainActivity extends AppCompatActivity {
    /**
     * field edits text where user enters his guess
     */
    private EditText txtGuess;
    /**
     * button for checking if guess is correct<br>
     * button is invisible when user: <br>
     * wins game<br>
     * chooses range
     */
    private Button btnGuess;
    /**
     * button visible after user won or lost game, it starts new game <br>
     * game can be started again before finish via menu
     */
    private Button btnPlayAgain;
    /**
     * button is visible only during manual choose of range, it accepts entered number as the range
     */
    private Button btnChoose;
    /**
     * field showing tips and instructions what to do
     */
    private TextView lblTip;
    /**
     * field showing info about range and remaining tries
     */
    private TextView lblInfo;
    /**
     * random number from 0 to range
     */
    private int theNumber;
    /**
     * counts number of guessing attempts
     */
    private int Tries=0;
    /**
     * minimal number of tries which allows win each game
     */
    private int youLost = 7;
    /**
     *  keeps highest guess but lower than theNumber (hint)
     */
    private int downHint = 0;
    /**
     *  keeps lowest guess but higher than theNumber (hint)
     */
    private int upHint = 1000;
    /**
     * range of numbers
     */
    private int range = 100;
    /**
     * actual users number
     */
    int guess;

    /**
     * method compering users number with theNumber (catching incorrect entries eg letters) <BR>
     * it also counts tries, shows tips, decides if game is won or lost
     */
    public void checkGuess(){
    String guessText = txtGuess.getText().toString();
    String message = "";
    try {
        guess = Integer.parseInt(guessText);
        youLost = youLost - 1;
        Tries= Tries+1;
        if (guess < theNumber) {
            if (downHint < guess) {
                downHint = guess;
                message = guess + " is too low. Try again. Tries left: " + youLost;
            } else
                message = "Number supposed to be higher than " + downHint + ".Tries left: " + youLost;
        }
        else if (guess > theNumber) {
            if (guess < upHint) {
                upHint = guess;
                message = guess + " is too high. Try again. Tries left: " + youLost;
            } else
                message = "Number supposed to be lower than " + upHint + " .Tries left: " + youLost;
        }
        else {
            message = guess + " is correct. Lets play again!";
            lblTip.setText(message);
            SharedPreferences preferences3 =
                    PreferenceManager.getDefaultSharedPreferences(this);
            int gamesWon = preferences3.getInt("gamesWon", 0) +1;
            SharedPreferences.Editor editor3 = preferences3.edit();
            editor3.putInt("gamesWon", gamesWon);
            editor3.apply();
            Toast.makeText(MainActivity.this, "Number of tries :" + Tries,
                    Toast.LENGTH_LONG).show();
            btnGuess.setVisibility(View.INVISIBLE);
            btnChoose.setVisibility(View.INVISIBLE);
            btnPlayAgain.setVisibility(View.VISIBLE);
        }
    }catch (Exception e) {
        message = "enter number between 1 and " + range + "!";
    }
    finally {
        if (youLost == 0){
            if (guess != theNumber) {
            message = "You lost. Play Again. Correct number was: " +theNumber;
            lblTip.setText(message);
            txtGuess.requestFocus();
            txtGuess.selectAll();
            btnGuess.setVisibility(View.INVISIBLE);
            btnChoose.setVisibility(View.INVISIBLE);
            btnPlayAgain.setVisibility(View.VISIBLE);
            SharedPreferences preferences2 =
                    PreferenceManager.getDefaultSharedPreferences(this);
            int gamesLost = preferences2.getInt("gamesLost", 0) +1;
            SharedPreferences.Editor editor2 = preferences2.edit();
            editor2.putInt("gamesLost", gamesLost);
            editor2.apply();}
        }
        else{
        lblTip.setText(message);
        txtGuess.requestFocus();
        txtGuess.selectAll();}
    }}

    /**
     * method {@link #onOptionsItemSelected(MenuItem item) Menu} and
     * button btnChoose (action listener) used in order to choose range
     *
     */
    public void chooseRange(){
        btnGuess.setVisibility(View.INVISIBLE);
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnChoose.setVisibility(View.VISIBLE);
        String message2 = "";
        message2 = "Choose own range";
        lblTip.setText(message2);
        txtGuess.requestFocus();
        txtGuess.selectAll();
    }

    /**
     * method resetting statistics
     */
    public void resetStats(){
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        int gamesWon = 0;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesWon", gamesWon);
        editor.apply();
            SharedPreferences preferences2 =
                    PreferenceManager.getDefaultSharedPreferences(this);
                int gamesLost = 0;
                SharedPreferences.Editor editor2 = preferences.edit();
                editor.putInt("gamesLost", gamesLost);
                editor.apply();
    }

    /**
     * method generating new random number and counting number of tries
     */
    public void newGame() {
        theNumber = (int) (Math.random() * range + 1);
        txtGuess.setText("" + range/2);
        Tries =0;
        upHint = range;
        downHint = 0;
        youLost= (int)(Math.log(range)/Math.log(2)+1);
        btnGuess.setVisibility(View.VISIBLE);
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnChoose.setVisibility(View.INVISIBLE);
        lblInfo.setText("Guess number between 1 and " + range + ". Tries: " +youLost);
    }

    @Override
    /**
     * connection between code and graphic elements <BR>
     * setting listeners
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        lblTip = (TextView) findViewById(R.id.lblTip);
        lblInfo = (TextView) findViewById(R.id.lblInfo);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        youLost= (int)(Math.log(range)/Math.log(2)+1);
        lblInfo.setText("Guess number between 1 and " + range + ". Tries: " +youLost);
        newGame();
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView v,int actionId, KeyEvent event){
            if (youLost==0)
            newGame();
            else
            checkGuess();
            return true;
        }
    });
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guessText = txtGuess.getText().toString();
                String message = "";
                range = Integer.parseInt(guessText);
                message = "New range is " + range + ". Play again!";
                lblTip.setText(message);
                btnGuess.setVisibility(View.INVISIBLE);
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnChoose.setVisibility(View.INVISIBLE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /**
     * game menu with options: new game, choose range, stats and about
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                final CharSequence[] items = {"1 - 10", "1 - 100", "1 - 1000","1 - 10000", "own"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                            case 3:
                                range = 10000;
                                storeRange(10000);
                                newGame();
                                break;
                            case 4:
                                chooseRange();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                int gamesLost   = preferences.getInt("gamesLost", 0);
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats:");
                statDialog.setMessage("Won games: " + gamesWon + ". Lost games: " + gamesLost
                            + ".");
                statDialog.setButton(AlertDialog.BUTTON2, "OK",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                        });
                SharedPreferences preferences2 =
                        PreferenceManager.getDefaultSharedPreferences(this);
                statDialog.setButton(AlertDialog.BUTTON1, "reset",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetStats();
                            }
                        });
                        statDialog.show();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("©2018 Mgr. Inż. Wojciech");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                aboutDialog.show();
                return true;
             default:
                 return super.onOptionsItemSelected(item);
        }
    }

    /**
     * method keeping range after game is closed using Shared Preferences
     */
    public void storeRange(int newRange){
    SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }
}