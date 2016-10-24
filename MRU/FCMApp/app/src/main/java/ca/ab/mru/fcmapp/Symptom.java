package ca.ab.mru.fcmapp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Symptom extends AppCompatActivity {

    private SeekBar feverSeekBar, nauseaSeekBar, diarrheaSeekBar;
    private TextView feverInterrogation, nauseaInterrogation, diarrheaInterrogation,
            feverIntensity, nauseaIntensity, diarrheaIntensity;
    private ImageView feverImage, nauseaImage, diarrheaImage;
    private static final String TAG = Symptom.class.getSimpleName();
    public int appStatus;

    public static Handler graphicUpdateHandler = new Handler();
    private static final int graphicUpdateTimeMillis = 100;

    public static GlobalVariables globalVariables = new GlobalVariables();

    public Button setButton, unsetButton;
    public boolean intensity;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        //initialize the app
        init();
        //update graphics
        graphicUpdateHandler.post(graphicUpdate);

    }

    @Override
    public void onBackPressed(){
        switch(appStatus){
            case Constants.APP_STATUS_MAIN:
                appStatus = Constants.APP_STATUS_EXIT;
                finish();
                break;
            case Constants.APP_STATUS_SETTING:
                //setContentView(R.layout.obd);
                appStatus = Constants.APP_STATUS_MAIN;

                break;
            default:
                finish();
                appStatus = Constants.APP_STATUS_EXIT;
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        appStatus = Constants.APP_STATUS_EXIT;
        //graphicUpdateHandler.removeCallbacks(graphicUpdate);
        finish();
    }

    public void init(){
        //fever
        feverImage = (ImageView) findViewById(R.id.feverImage);
        feverInterrogation = (TextView) findViewById(R.id.feverInterrogation);
        feverSeekBar = (SeekBar)findViewById(R.id.feverSeekBar);
        feverIntensity = (TextView) findViewById(R.id.feverIntensity);
        //nausea
        nauseaImage = (ImageView) findViewById(R.id.nauseaImage);
        nauseaInterrogation = (TextView) findViewById(R.id.nauseaInterrogation);
        nauseaSeekBar = (SeekBar)findViewById(R.id.nauseaSeekBar);
        nauseaIntensity = (TextView) findViewById(R.id.nauseaIntensity);
        //diarrhea
        diarrheaImage = (ImageView) findViewById(R.id.diarrheaImage);
        diarrheaInterrogation = (TextView) findViewById(R.id.diarrheaInterrogation);
        diarrheaSeekBar = (SeekBar)findViewById(R.id.diarrheaSeekBar);
        diarrheaIntensity = (TextView) findViewById(R.id.diarrheaIntensity);

        //set initial FCM Status to fever
        globalVariables.setFCMStaus(Constants.FEVER);

        //intensity set/unset button initialization
        setButton = (Button) findViewById(R.id.setButton);
        unsetButton = (Button) findViewById(R.id.unsetButton);
    }

    public Runnable graphicUpdate = new Runnable() {
        public void run() {
            //graphicUpdateHandler.postDelayed(graphicUpdate, graphicUpdateTimeMillis);
            graphicUpdateHandler.postDelayed(graphicUpdate, 100);
            switch (globalVariables.getFCMStatus()){
                case Constants.FEVER:
                    setVisibility(feverImage, feverInterrogation, feverIntensity, feverSeekBar, true);
                    updateIntensity(feverIntensity,feverSeekBar);
                    setIntensity(intensity);
                    unsetIntensity();
                    break;
                case Constants.NAUSEA:
                    intensity = false;
                    setIntensity(intensity);
                    setVisibility(nauseaImage, nauseaInterrogation, nauseaIntensity, nauseaSeekBar, true);
                    updateIntensity(nauseaIntensity,nauseaSeekBar);
                    unsetIntensity();
                    setIntensity(intensity);
                    fcmAlgorithm.fuzzyImplementation();
                    break;
                case Constants.DIARRHEA:
                    intensity = false;
                    setIntensity(intensity);
                    setVisibility(diarrheaImage, diarrheaInterrogation, diarrheaIntensity, diarrheaSeekBar, true);
                    updateIntensity(diarrheaIntensity,diarrheaSeekBar);
                    unsetIntensity();
                    setIntensity(intensity);
                    fcmAlgorithm.fuzzyImplementation();
                    break;
                default:
            }
        }
    };

    public void setVisibility(ImageView image, TextView interrogation, TextView intensity, SeekBar seekBar, boolean visible){
        if(visible) {
            image.setVisibility(View.VISIBLE);
            interrogation.setVisibility(View.VISIBLE);
            intensity.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
        }else{
            image.setVisibility(View.INVISIBLE);
            interrogation.setVisibility(View.INVISIBLE);
            intensity.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
        }
    }

    public void updateIntensity(final TextView symptomIntensity, SeekBar seekBar){
        //set intensity
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //updatePercentValue(seekBar.getProgress());
                symptomIntensity.setText(String.valueOf(progress)+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //updatePercentValue(seekBar.getProgress());
                symptomIntensity.setText(String.valueOf(seekBar.getProgress())+"%");
                intensity = true;
            }
        });
    }

    public void setIntensity(final boolean intensity){
        //option 1 - set/unset approach
        setButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                globalVariables.setIntensity(intensity);
                //TODO: fuzzy implementaiton - returns the next symptom
                fcmAlgorithm.fuzzyImplementation();
                //TODO: next symptom int equivalent obtained from Constants and update the FCMStatus
                //Only for testing
                if(globalVariables.getFCMStatus() == Constants.FEVER)
                    globalVariables.setFCMStaus(Constants.NAUSEA);
                else if(globalVariables.getFCMStatus() == Constants.NAUSEA)
                    globalVariables.setFCMStaus(Constants.DIARRHEA);
                else
                    globalVariables.setFCMStaus(Constants.DIARRHEA);

            }
        });
    }

    public void unsetIntensity(){
        unsetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(globalVariables.getFCMStatus() == Constants.FEVER)
                    updateIntensity(feverIntensity, feverSeekBar);
                else if(globalVariables.getFCMStatus() == Constants.NAUSEA) {
                    globalVariables.setFCMStaus(Constants.FEVER);
                    setVisibility(nauseaImage, nauseaInterrogation, nauseaIntensity, nauseaSeekBar, false);
                } else if(globalVariables.getFCMStatus() == Constants.DIARRHEA) {
                    globalVariables.setFCMStaus(Constants.NAUSEA);
                    setVisibility(diarrheaImage, diarrheaInterrogation, diarrheaIntensity, diarrheaSeekBar, false);
                }
            }
        });
    }


}
