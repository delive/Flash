package com.jwstudios.flash.game;

import java.text.DateFormat;
import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jwstudios.flash.R;
import com.jwstudios.flash.SibOne;
import com.jwstudios.flash.data.Data;
import com.jwstudios.flash.util.Fconstant;
import com.jwstudios.flash.util.PersistanceUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Android activity page that displays a shuffled word game
 *
 * @author johnwright
 */
public class PlayRandom
        extends Activity {
    private static Game myGame;
    private SibOne word;
    private TextView input1;
    private TextView input2;
    private TextView remaining;
    private TextView dateView;
    private TextView streakView;
    private TextView historyView;
    private int status = 0;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.playgame);

        initAds();

        //textfields that are updated on each button press
        input1 = (TextView) findViewById(R.id.game_input1);
        input2 = (TextView) findViewById(R.id.game_input2);
        remaining = (TextView) findViewById(R.id.remaining);
        dateView = (TextView) findViewById(R.id.date);
        streakView = (TextView) findViewById(R.id.streak);
        historyView = (TextView) findViewById(R.id.history);

        if (myGame == null) {
            //no game made yet, create one
            restartGame(GameType.NORMAL);
        }
        else {
            //just initialize the page with the current game
            restartGame(GameType.INIT);
        }

        super.onCreate(savedInstanceState);
    }

    private void initAds() {
        AdView mAdView = new AdView(this);
        mAdView.setAdUnitId(Data.ADMOBID.toString());
        mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdListener(new ToastAdListener(this));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ad);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mAdView, params);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                word = myGame.getNext();
                updateRound();
                break;

            case R.id.last:
                word = myGame.getLast();
                updateRound();
                break;
            case R.id.restart:
                //get the type of game to create!
                RadioGroup gameSelected = (RadioGroup) findViewById(R.id.gameTypeRadio);

                switch (gameSelected.getCheckedRadioButtonId()) {
                    case R.id.game_normal:
                        //restart random shuffle game
                        restartGame(GameType.NORMAL);
                        break;
                    case R.id.game_new50:
                        restartGame(GameType.NEW50);
                        break;
                    case R.id.game_verbs:
                        restartGame(GameType.ALLVERB);
                        break;
                }
                break;
            case R.id.showhide:
                if (status == 0) {
                    // invisible, show it now
                    input2.setVisibility(View.VISIBLE);
                    status = 1;
                }
                else {
                    input2.setVisibility(View.INVISIBLE);
                    status = 0;
                }


                break;

        }
        remaining.setText("Words Remaining: " + myGame.wordsLeft());
    }

    /**
     * Resets visibility and sets the game text
     */
    private void updateRound() {
        //deal with the language choice
        if (myGame.getLang() == Fconstant.SIBONE) {
            input1.setText(word.getName());
            input2.setText(word.getPair().getName());
        }
        else {
            input1.setText(word.getPair().getName());
            input2.setText(word.getName());
        }

        input2.setVisibility(View.INVISIBLE);
        status = 0;

        dateView.setText(DateFormat.getDateInstance().format(word.getDate()));
        historyView.setText("H: " + word.getCorrectCount() + "/" + word.getPlayedCount());
        streakView.setText("S: " + word.getDailyStreak());
    }

    /**
     * Restarts the game.  Multiple modes based on type for the style of game to be created.
     */
    private void restartGame(GameType type) {
        //set language
        int lang = ((RadioGroup) findViewById(R.id.langRadio)).getCheckedRadioButtonId();
        lang = (lang == R.id.lang_eng) ? Fconstant.SIBONE : Fconstant.SIBTWO;

        boolean verbs = ((CheckBox) findViewById(R.id.game_verbs_checkbox)).isChecked();

        if (type != GameType.INIT) {
            ArrayList<SibOne> myItems;
            myItems = PersistanceUtils.getSibOnesList(getApplicationContext());
            myGame = new Game(myItems, type, lang, verbs, getApplicationContext());
        }

        //initialize the textfields
        word = myGame.getNext();
        updateRound();
        remaining.setText("Words Remaining: " + myGame.wordsLeft());

    }

}
