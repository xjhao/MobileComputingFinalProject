package edu.wpi.zqinxhao.playerpooling;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.wpi.zqinxhao.playerpooling.model.Game;


public class RequestJoinActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Game gameRequestJoin;
    TextView gameNameTextView;
    TextView gameDescriptionTextView;
    TextView gameLocationTextView;
    TextView gameHostTextView;
    TextView gameHostNumberTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setContentView(R.layout.activity_request_join);
        this.gameNameTextView = (TextView)findViewById(R.id.gameNameInFragment);
        this.gameDescriptionTextView = (TextView) findViewById(R.id.gameDescriptionInFragment);
        this.gameHostNumberTextView = (TextView) findViewById(R.id.gameNumberInFragment);
        this.gameHostTextView =(TextView) findViewById(R.id.gameHostInFragment);
        this.gameLocationTextView =(TextView) findViewById(R.id.gameLocationInFragment);

        this.gameNameTextView.setText(gameRequestJoin.getGameName());
        this.gameHostTextView.setText(gameRequestJoin.getHost());
        this.gameLocationTextView.setText(gameRequestJoin.getGameAddress());
        this.gameHostNumberTextView.setText(gameRequestJoin.getHostNumber());
        this.gameDescriptionTextView.setText(gameRequestJoin.getDescription());

        return null;




    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
