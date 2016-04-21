package edu.wpi.zqinxhao.playerpooling.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.UserAreaActivity;
import edu.wpi.zqinxhao.playerpooling.model.User;

/**
 * Created by xhao on 4/21/16.
 */
public class DynamoDBManagerTask extends
        AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {
    private User user = null;
    LoginActivity activity;

    protected DynamoDBManagerTaskResult doInBackground(
            DynamoDBManagerType... types) {

        String tableStatus = DynamoDBManager.getUserTableStatus();

        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTableStatus(tableStatus);
        result.setTaskType(types[0]);

        if (types[0] == DynamoDBManagerType.INSERT_USER) {
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                DynamoDBManager.insertUser(user);
            }
        } else if (types[0] == DynamoDBManagerType.AUTHENTICATE_USER) {
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                boolean authenResult = DynamoDBManager.authenticateUser(LoginActivity.getUserEmail(),LoginActivity.getUserPwd());
                if(authenResult == false){
                    result.setTaskSuccess(false);
                }
            }
        }
        return result;
    }

    protected void onPostExecute(DynamoDBManagerTaskResult result) {
        if (result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.INSERT_USER) {
        }else if(result.getTableStatus().equalsIgnoreCase("ACTIVE")
                && result.getTaskType() == DynamoDBManagerType.AUTHENTICATE_USER){

            activity.setLoginSuccess(result.isTaskSuccess());
            if(activity.getLoginSuccess()) {
                Intent userAreaIntent = new Intent(activity, UserAreaActivity.class);
                activity.startActivity(userAreaIntent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }

        }


    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setActivity(LoginActivity activity) {
        this.activity = activity;
    }
}

