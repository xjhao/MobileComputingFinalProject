package edu.wpi.zqinxhao.playerpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.zqinxhao.playerpooling.model.Game;

/**
 * Created by zishanqin on 4/25/16.
 */
public class BrowseGamesActivity extends AppCompatActivity  {

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    private List<Game> gameList = new ArrayList<Game>();
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent i = getIntent();
        gameList =  i.getParcelableArrayListExtra("gameList");
        setContentView(R.layout.game_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        gameAdapter = new GameAdapter(gameList);
        RecyclerView.LayoutManager recyclerViewManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gameAdapter);
    }


}

class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>  {

    private List<Game> gameList;

    public GameAdapter(List<Game> games){
        this.gameList = games;
    }
    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameAdapter.MyViewHolder holder, int position) {
        Game game=gameList.get(position);
        holder.gameTitle.setText(game.getGameName());
        holder.gameLocation.setText(game.getGameAddress());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }



    class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView gameTitle;
        TextView gameLocation;

        public MyViewHolder(View itemView) {

            super(itemView);
            gameTitle=(TextView) itemView.findViewById(R.id.gameTitle);
            gameLocation = (TextView) itemView.findViewById(R.id.gameLocation);


        }
    }
}