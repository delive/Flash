package com.android.flash.dailies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.flash.R;
import com.android.flash.SibOne;
import com.android.flash.data.Data;
import com.android.flash.game.Game;
import com.android.flash.game.GameType;
import com.android.flash.util.Fconstant;
import com.android.flash.util.PersistanceUtils;
import com.android.flash.util.Serializer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 1/15/13
 * Time: 10:59 PM
 */
public class Dailies extends Activity {
    private static Game myGame;
    private static final DailyCoordinator COORDINATOR = DailyCoordinator.get();
    private SibOne word;
    private TextView input1;
    private TextView input2;
    private TextView remaining;
    private TextView dateView;
    private TextView streakView;
    private TextView historyView;
    private int status = 0;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dailies);

        initAds();

        //textfields that are updated on each button press
        input1 = (TextView) findViewById(R.id.input1);
        input2 = (TextView) findViewById(R.id.input2);
        remaining = (TextView) findViewById(R.id.remaining);
        dateView = (TextView) findViewById(R.id.date);
        streakView = (TextView) findViewById(R.id.streak);
        historyView = (TextView) findViewById(R.id.history);

        if (COORDINATOR.isFinished()) {
            super.finish();
        } else {

            if (myGame == null) {
                //no game made yet, create one
                restartGame(GameType.DAILIES);
            } else {
                //just initialize the page with the current game
                restartGame(GameType.INIT);
            }
        }

        super.onCreate(savedInstanceState);
    }

    private void initAds() {
        //todo make this actually work
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
            case R.id.correct:
                COORDINATOR.completeWord(word, true);
                if (myGame.wordsLeft() == 0) {
                    //switch activities to summary view
                    finish();
                }
                word = myGame.getNext();
                updateRound();
                break;

            case R.id.incorrect:
                COORDINATOR.completeWord(word, false);
                if (myGame.wordsLeft() == 0) {
                    //switch activities to summary view
                    finish();
                }
                word = myGame.getNext();
                updateRound();
                break;

            case R.id.showhide:
                if (status == 0) {
                    // invisible, show it now
                    input2.setVisibility(View.VISIBLE);
                    status = 1;
                } else {
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
        } else {
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
     * @param type
     */
    private void restartGame(GameType type) {
        //set language
        int lang = Fconstant.SIBONE;

        if (type != GameType.INIT) {
            ArrayList<SibOne> myItems = null;
            myItems = PersistanceUtils.getSibOnesList();
            myGame = new Game(myItems, type, lang, false);
        }

        //initialize the textfields
        word = myGame.getCurrent();

        updateRound();
        remaining.setText("Words Remaining: " + myGame.wordsLeft());
    }
}