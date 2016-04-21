package edu.wpi.zqinxhao.playerpooling.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.RegisterActivity;
import edu.wpi.zqinxhao.playerpooling.UserAreaActivity;
import edu.wpi.zqinxhao.playerpooling.exceptions.DuplicateEmailException;
import edu.wpi.zqinxhao.playerpooling.model.User;

/**
 * Created by xhao on 4/21/16.
 */
public class DynamoDBManagerTask extends
        AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {
    private User user = null;
    LoginActivity login_activity;
    RegisterActivity register_activity;

    protected DynamoDBManagerTaskResult doInBackground(
            DynamoDBManagerType... types) {

        String tableStatus = DynamoDBManager.getUserTableStatus();

        DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();
        result.setTableStatus(tableStatus);
        result.setTaskType(types[0]);

        if (types[0] == DynamoDBManagerType.INSERT_USER) {
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
            if (tableStatus.equalsIgnoreCase("ACTIVE")) {
                boolean authenResult = DynamoDBManager.authenticateUser(LoginActivity.getUserEmail(),LoginActivity.getUserPwd());
                if(!authenResult) {
                    result.setTaskSuccess(false);
                }
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
}

