package com.example.translatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText text;
    EditText from;
    EditText to;
    Button btn;
    TextView tv;
    FloatingActionButton floatingbtn;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText) findViewById(R.id.text);
        from = (EditText) findViewById(R.id.text2);
        to = (EditText) findViewById(R.id.text3);
        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);
        floatingbtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = t1.setLanguage(Locale.ENGLISH);
                    if(lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(MainActivity.this, "language not supported", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Language supported", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("script");
//        PyObject voiceobj = py.getModule("voice");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PyObject obj = pyobj.callAttr("main", text.getText().toString(), from.getText().toString(), to.getText().toString());
                tv.setText(obj.toString());
            }
        });


        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PyObject voice = voiceobj.callAttr("to_voice", tv.getText().toString(), to.getText().toString());

                t1.speak(text.getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (t1 != null){
            t1.stop();
            t1.shutdown();
        }
    }
}