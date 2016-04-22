package edu.wpi.zqinxhao.playerpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.wpi.zqinxhao.playerpooling.model.User;

public class UserAreaActivity extends AppCompatActivity {
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = LoginActivity.getLoginUser();
        setContentView(R.layout.activity_user_area);

        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        welcomeMessage.setText("Welcome, " + user.getName() + "!");

        final Button bHost = (Button) findViewById(R.id.bHost);
        final Button bJoin = (Button) findViewById(R.id.bJoin);

        bHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createActivityIntent = new Intent(UserAreaActivity.this, CreateGameActivity.class);
                UserAreaActivity.this.startActivity(createActivityIntent);
            }
        });
    }
}
