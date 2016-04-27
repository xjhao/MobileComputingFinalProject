package edu.wpi.zqinxhao.playerpooling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import edu.wpi.zqinxhao.playerpooling.model.Game;

public class GameDisplayActivity extends AppCompatActivity {
    Game gameDisplayed=null;
    GameDisplayActivityFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_display);
        gameDisplayed = getIntent().getParcelableExtra("GAME_SELECTED");
        fragment= (GameDisplayActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment.setGame(gameDisplayed);
        fragment.gameNameTextView.setText(gameDisplayed.getGameName());
        fragment.gameHostTextView.setText(gameDisplayed.getHost());
        fragment.gameLocationTextView.setText(gameDisplayed.getGameAddress());
        fragment.gameHostNumberTextView.setText(gameDisplayed.getHostNumber());
        fragment.gameDescriptionTextView.setText(gameDisplayed.getDescription());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
