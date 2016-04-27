package edu.wpi.zqinxhao.playerpooling.DB;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.wpi.zqinxhao.playerpooling.BrowseGamesActivity;
import edu.wpi.zqinxhao.playerpooling.CreateGameActivity;
import edu.wpi.zqinxhao.playerpooling.HostGameActivity;
import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.RegisterActivity;
import edu.wpi.zqinxhao.playerpooling.UserAreaActivity;
import edu.wpi.zqinxhao.playerpooling.exceptions.DuplicateEmailException;
import edu.wpi.zqinxhao.playerpooling.model.Game;
import edu.wpi.zqinxhao.playerpooling.model.User;

/**
 * Created by xhao on 4/21/16.
 */
public class DynamoDBManagerTask extends
        AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {
    private User user = null;
    private Game game=null;


    private ArrayList<Game> gamesSearch = new ArrayList<Game>();

    private LatLng location =null;
    LoginActivity login_activity;
    RegisterActivity register_activity;

    public void setCreate_game_activity(CreateGameActivity create_game_activity) {
        this.create_game_activity = create_game_activity;
    }

    CreateGameActivity create_game_activity;


    UserAreaActivity userAreaActivity;

    protected DynamoDBManagerTaskResult doInBackground(
            DynamoDBManagerType... types) {


        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        if (types[0] == DynamoDBManagerType.INSERT_USER) {
            String tableStatus = DynamoDBManager.getUserTableStatus();


            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                boolean insertResult = false;
                try {
                    insertResult = DynamoDBManager.insertUser(user);
                } catch (DuplicateEmailException e) {
                    result.setException(e);
                    result.setTaskSuccess(false);
                    return result;
                }
                if (!insertResult) {
                    result.setTaskSuccess(false);
                }
            }
        } else if (types[0] == DynamoDBManagerType.AUTHENTICATE_USER) {
            String tableStatus = DynamoDBManager.getUserTableStatus();


            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                boolean authenResult = DynamoDBManager.authenticateUser(LoginActivity.getUserEmail(),LoginActivity.getUserPwd());
                if(!authenResult) {
                    result.setTaskSuccess(false);
                }
            }
        } else if(types[0] == DynamoDBManagerType.INSEERT_GAME){
            String tableStatus = DynamoDBManager.getGameTableStatus();

            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);
            if(tableStatus.equalsIgnoreCase("ACTIVE")){
                boolean insertResult=DynamoDBManager.insertGame(CreateGameActivity.getGame());
                if(!insertResult){
                    result.setTaskSuccess(false);
                }
            }
        } else if(types[0] == DynamoDBManagerType.QUERY_GAME){
            String tableStatus = DynamoDBManager.getGameTableStatus();
            result.setTableStatus(tableStatus);
            result.setTaskType(types[0]);
            if(tableStatus.equalsIgnoreCase("ACTIVE")){
                this.gamesSearch = DynamoDBManager.searchGames(location);

            }
        }
        return result;
    }

    protected void onPostExecute(DynamoDBManagerTaskResult result) {
        if (result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.INSERT_USER) {

            register_activity.setRegisterSuccess(result.isTaskSuccess());

            if(register_activity.getRegisterSuccess()) {
                Intent loginIntent = new Intent(register_activity, LoginActivity.class);
                register_activity.startActivity(loginIntent);

            } else {
                if (result.getException() instanceof DuplicateEmailException) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register_activity);
                    builder.setMessage("User with this email has already existed")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
            }

        }else if(result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.AUTHENTICATE_USER){

            login_activity.setLoginSuccess(result.isTaskSuccess());

            if(login_activity.getLoginSuccess()) {

                Intent userAreaIntent = new Intent(login_activity, UserAreaActivity.class);
                login_activity.startActivity(userAreaIntent);


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(login_activity);
                builder.setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }

        }else if(result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.INSEERT_GAME){
            Log.d("TEST INSERT GAME", "Insert");

            create_game_activity.setCreateGameSuccess(result.isTaskSuccess());

            if (create_game_activity.isCreateGameSuccess()) {
                Intent hostGameIntent = new Intent(create_game_activity, HostGameActivity.class);
                create_game_activity.startActivity(hostGameIntent);

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(create_game_activity);
                builder.setMessage("Create Game Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }


        }else if(result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.QUERY_GAME){
            try {
                Intent browseActivity = new Intent(userAreaActivity,BrowseGamesActivity.class);
                browseActivity.putParcelableArrayListExtra("gameList", getGamesSearch());
                userAreaActivity.startActivity(browseActivity);

            }catch(AmazonServiceException e) {
                //TO DO
            }

        }


    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActivity(LoginActivity activity) {
        this.login_activity = activity;
    }

    public void setRegisterActivity(RegisterActivity registerActivity) {
        this.register_activity = registerActivity;
    }
    public void setGame(Game game){
        this.game=game;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
    public UserAreaActivity getUserAreaActivity() {
        return userAreaActivity;
    }

    public void setUserAreaActivity(UserAreaActivity userAreaActivity) {
        this.userAreaActivity = userAreaActivity;
    }

    public ArrayList<Game> getGamesSearch() {
        return gamesSearch;
    }

}

