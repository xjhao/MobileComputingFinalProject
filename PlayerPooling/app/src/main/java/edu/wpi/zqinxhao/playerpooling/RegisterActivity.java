package edu.wpi.zqinxhao.playerpooling;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManager;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerTask;
import edu.wpi.zqinxhao.playerpooling.DB.DynamoDBManagerType;
import edu.wpi.zqinxhao.playerpooling.gcm.GCMRegistrationIntentService;
import edu.wpi.zqinxhao.playerpooling.model.EncriptionUtils;
import edu.wpi.zqinxhao.playerpooling.model.User;

public class RegisterActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private User user;
    AmazonClientManager AmzClientManager;
    private static boolean registerSuccess = false;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String gcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AmzClientManager=LoginActivity.getAmzClientManager();
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.etPasswordConfirm);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Registration success
                    gcmToken = intent.getStringExtra("token");
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //To be defined
                }
            }
        };

        //Check status of Google paly service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not installed/enabled in this device!", Toast.LENGTH_LONG).show();
                // So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This divice does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String hashPassword = EncriptionUtils.computeSHAHash(password);
                final String confirmPassword = etConfirmPassword.getText().toString();
                final int age = Integer.parseInt(etAge.getText().toString());

                boolean success = false;

                user = createUser(name, email, hashPassword, age, gcmToken);
                try {
                    if (!isEmailValid(email)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Invalid Email")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                        return;
                    }

                    if (!isPasswordValid(password)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Password cannot less than 4 characters")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                        return;
                    }

                    if (!isPasswordConsistent(password, confirmPassword)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Passwords don't match")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                        return;
                    }

                    DynamoDBManagerTask insertUserTask = new DynamoDBManagerTask();
                    insertUserTask.setUser(user);
                    insertUserTask.setRegisterActivity(RegisterActivity.this);
                    insertUserTask.execute(DynamoDBManagerType.INSERT_USER);
                }catch(AmazonServiceException e) {
                    success = false;
                }



            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.wpi.zqinxhao.playerpooling/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.wpi.zqinxhao.playerpooling/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public User createUser(String name, String email, String hashPassword, int age, String gcmToken) {
        User u=new User();
        u.setName(name);
        //this.name = name;
        u.setEmail(email);
        u.setHashPassword(hashPassword);
        u.setAge(age);
        u.setGcmToken(gcmToken);
        return u;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private boolean isPasswordConsistent(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isRegisterSuccess() {
        return registerSuccess;
    }

    public static void setRegisterSuccess(boolean registerSuccess) {
        RegisterActivity.registerSuccess = registerSuccess;
    }

    public static boolean getRegisterSuccess() {
        return RegisterActivity.registerSuccess;
    }
}
