package edu.wpi.zqinxhao.playerpooling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.wpi.zqinxhao.playerpooling.model.Game;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameDisplayActivityFragment extends Fragment {
    private Game gameRequestJoin;
    TextView gameNameTextView;
    TextView gameDescriptionTextView;
    TextView gameLocationTextView;
    TextView gameHostTextView;
    TextView gameHostNumberTextView;

    Button btnRequest;
    public GameDisplayActivityFragment() {
    }

    public void setGame(Game g){
        this.gameRequestJoin=g;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_game_display, container, false);
        gameNameTextView= (TextView) v.findViewById(R.id.gameNameInFragment);
        gameDescriptionTextView =(TextView) v.findViewById(R.id.gameDescriptionInFragment);
        gameLocationTextView =(TextView)v.findViewById(R.id.gameLocationInFragment);
        gameHostNumberTextView = (TextView) v.findViewById(R.id.gameNumberInFragment);
        gameHostTextView =(TextView) v.findViewById(R.id.gameHostInFragment);
        btnRequest=(Button) v.findViewById(R.id.joinBtn);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To Do......
            }
        });



        return v;
    }
}
