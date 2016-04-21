package edu.wpi.zqinxhao.playerpooling;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.AmazonServiceException;

import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerTask;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerTaskResult;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerType;
import edu.wpi.zqinxhao.playerpooling.model.EncriptionUtils;
import edu.wpi.zqinxhao.playerpooling.model.User;

public class LoginActivity extends AppCompatActivity {
    private static AmazonClientManager AmzClientManager=null;
    private static boolean loginSuccess=false;
    private static String userEmail;
    private static String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AmzClientManager=new AmazonClientManager(getApplicationContext());
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etEmail.getText().toString();
                userPwd = EncriptionUtils.computeSHAHash(etPassword.getText().toString());
                boolean success = false;
                try {
                    //DynamoDBManager.insertUser(user);
                    DynamoDBManagerTask authenTask=new DynamoDBManagerTask();
                    authenTask.setActivity(LoginActivity.this);
                    //insertUserTask.setUser(user);
                    authenTask.execute(DynamoDBManagerType.AUTHENTICATE_USER);
                    success = isLoginSuccess();
                }catch(AmazonServiceException e) {
                    success = false;
                }



            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }
    public static AmazonClientManager getAmzClientManager(){
        return AmzClientManager;
    }
    public static void setLoginSuccess(boolean loginSuccess) {
        LoginActivity.loginSuccess = loginSuccess;
    }
    public static boolean getLoginSuccess() {
        return LoginActivity.loginSuccess;
    }
    public static boolean isLoginSuccess() {
        return loginSuccess;
    }
    public static String getUserEmail() {
        return userEmail;
    }
    public static String getUserPwd() {
        return userPwd;
    }
}
