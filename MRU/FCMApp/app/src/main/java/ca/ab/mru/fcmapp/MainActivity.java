package ca.ab.mru.fcmapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    public Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intensity set/unset button initialization
        submitButton = (Button) findViewById(R.id.submitProfile);
        //next activity
        submitButton.setOnClickListener(submitButtonListener);

    }

    View.OnClickListener submitButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: Save patient information to global variable

            //profile activity
            Intent intent = new Intent(context, PatientProfile.class);
            startActivity(intent);

        }
    };
}
