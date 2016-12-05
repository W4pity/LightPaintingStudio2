package com.w4pity.lightpaintingstudio2;

import java.io.IOException;


import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class OptionActivity extends Activity implements OnClickListener {

    EditText c, s, f,n, cam;
    Button valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_option);
        c = (EditText) findViewById(R.id.color);
        s = (EditText) findViewById(R.id.sensibility);
        f = (EditText) findViewById(R.id.speedFrame);
        n = (EditText) findViewById(R.id.frame);
        cam = (EditText) findViewById(R.id.camera);
        valid = (Button) findViewById(R.id.valid);
        valid.setOnClickListener(this);
        cam.setText(Options.camera+"");
        c.setText(Options.couleur + "");
        s.setText(Options.sensibility+"");
        f.setText(Options.invervalPhoto+"");
        n.setText(Options.nbFrame+"");
    }

    @Override
    public void onResume() {
        super.onResume();
//        cameraPreview.resumePreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        // cameraPreview.releaseCamera();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.valid) {
            try {
                Options.couleur = Integer.parseInt(c.getText().toString());
                Options.sensibility = Integer.parseInt(s.getText().toString());
                Options.invervalPhoto = Integer.parseInt(f.getText().toString());
                Options.nbFrame = Integer.parseInt((n.getText().toString()));
                Options.camera = Integer.parseInt((cam.getText().toString()));
                CameraPreview.tabBit= new Bitmap[Integer.parseInt((n.getText().toString()))];
            }
            catch (Exception e)
            {

            }
            finish();
        }


    }




}