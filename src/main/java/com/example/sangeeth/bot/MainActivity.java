package com.example.sangeeth.bot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;


import com.bhargavms.dotloader.DotLoader;
import com.skyfishjy.library.RippleBackground;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    DotLoader dotLoader;
    AVLoadingIndicatorView avi;
    RippleBackground rippleBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img=(ImageView)findViewById(R.id.centerImage);
        rippleBackground=(RippleBackground)findViewById(R.id.content);
        requestAudioPermissions();
        CircleImageView info=(CircleImageView)findViewById(R.id.info);

        info.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Toast.makeText(getApplicationContext(),"Click on the image to record your line",Toast.LENGTH_LONG).show();
                                                 }});


                img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {                     // inital stage for speech

                rippleBackground.startRippleAnimation();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

                mySpeechRecognizer.startListening(intent);
                }


                });

        intializeTextTospeech();
        intializeSpeechRecognizer();

        }

        private void intializeTextTospeech() {

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (myTTS.getEngines().size() == 0) {
                    Toast.makeText(getApplicationContext(), "There is no TTS engine on your device", Toast.LENGTH_SHORT).show();
                    finish();
                }

                else
                    {
                    myTTS.setLanguage(Locale.US);
                    speak("Hello there ! I am mia");
                }

            }


        });

    }

    private void intializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(results.get(0));
                    rippleBackground.stopRippleAnimation();
                }
                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String command) {

        command = command.toLowerCase();

        //questions about her

        if (command.indexOf("what") != -1) {
            if (command.indexOf("your name") != -1) {
                speak("my name is mia");
            }
            if (command.indexOf("your gender") != -1) {
                speak("female");
            }

            if (command.indexOf("who") != -1) {
                if (command.indexOf("created you") != -1) {
                    speak("samgeeth");
                }
            }
        }
        if (command.contains("hey") ||command.contains("hi") || command.contains("hello") ) {
            speak("hey there");
        }


              if (command.contains("thank you") || command.contains("thanks") || command.contains("thank") ) {
              speak("my pleasure");
               }
    }


    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }

    private void requestAudioPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            //message why no granted?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(getApplicationContext(), "please grant permissions to record audio", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            } else {
                //show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
        case MY_PERMISSIONS_RECORD_AUDIO: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //permission was granted
                intializeTextTospeech();
            } else{

                Toast.makeText(getApplicationContext(), "permission not granted", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}

}


