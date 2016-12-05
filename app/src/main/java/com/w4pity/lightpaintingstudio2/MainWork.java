package com.w4pity.lightpaintingstudio2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainWork extends Activity implements OnClickListener {
   private CameraPreview cameraPreview;
    private Peephole peephole;
    private Button buttonGo, buttonStop, buttonOption, save;
    private Button buttonClear;
    ImageView iv;
    long time;
    ProgressBar pb;
    Context cont;
    //static int nbFrame = 50;
    private boolean again = true;
    private Handler myHandler;
    Bitmap bitmapWork;
    int timeFrameTaken = 0;
    MediaPlayer md;
    private Handler handlerProgressBar = new Handler();

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // Code à éxécuter de façon périodique
           // Bitmap bitTemp = null;
            if(again)
            if(timeFrameTaken == 0) {
                md.start();
                if(cameraPreview.getBitmap(0)) {
                    // bitmapWork = bitTemp;
                    bitmapWork = CameraPreview.tabBit[0].copy(Bitmap.Config.ARGB_8888, true);
                    timeFrameTaken++;
                }
            }
            else
            {
               if(cameraPreview.getBitmap(timeFrameTaken-1)) {
                   if (timeFrameTaken - 1 == Options.nbFrame - 1)
                       again = false;

                   timeFrameTaken++;
               }

            }

            if(again)
                 myHandler.postDelayed(this, 0);
            else {

                md.start();
                //iv.setImageBitmap(bitmapWork);
                Log.i("timeexec", "run "+ (System.currentTimeMillis()-time));
                new Thread(new Runnable() {
                    public void run() {
                                workOnTab(CameraPreview.tabBit);
                        runOnUiThread(new Runnable() {
                           public void run() {
                                iv.setImageBitmap(bitmapWork);
                            }
                        });

                    }
                }).start();



            }
        }
    };



    public void workOnTab(Bitmap[] tab)
    {
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(tab.length);

        pb.setProgress(0);
        int sensibility = Options.sensibility;
        int color = Options.couleur;
//        Toast.makeText(cont,  " frames finish: working", Toast.LENGTH_SHORT).show();
        for(int a = 0; a<tab.length; a++)
        {
        int coef = 1;
            int pixel;
            int r, g, b;
            int rb, gb, bb;
            int pixelBase;
            int tempColor;
         //   Log.d("bugdebound", "workOnTab " + tab[0].getWidth());
            int largeur = tab[0].getWidth(), hauteur = tab[0].getHeight();
        for(int i = 0; i<hauteur; i+=coef)
            for(int j = 0; j<largeur; j+=coef) {

                pixel = tab[a].getPixel(j, i);
                r = Color.red(pixel);
                g = Color.green(pixel);
                b = Color.blue(pixel);

                pixel = bitmapWork.getPixel(j, i);
                rb = Color.red(pixel);
                gb = Color.green(pixel);
                bb = Color.blue(pixel);
                if(color == 1) {
                    if (rb + sensibility < r && r>g && r>b) {
                        tempColor = Color.rgb(r, g, b);
                        bitmapWork.setPixel(j, i, tempColor);
                    }
                }
                else if(color == 2 && g > r && g>b) {
                    if (gb + sensibility < g) {
                        tempColor = Color.rgb(r, g, b);
                        bitmapWork.setPixel(j, i, tempColor);
                    }
                }
                else if(color == 3 && b>r && b>g) {
                    if (bb + sensibility < b) {
                        tempColor = Color.rgb(r, g, b);
                        bitmapWork.setPixel(j, i, tempColor);
                    }
                }
                else {
                    if (rb + sensibility < r || gb + sensibility < g || bb + sensibility < b) {
                        tempColor = Color.rgb(r, g, b);
                        bitmapWork.setPixel(j, i, tempColor);
                    }
                }

            }
            pb.setProgress(pb.getProgress()+1);
        }
        if(Options.camera == 1) {
            bitmapWork = rotate90(bitmapWork);
        }
        else
            bitmapWork = rotateN90(bitmapWork);
    }


    public Bitmap rotateN90(Bitmap b)
    {
        Bitmap tempBitmap = Bitmap.createBitmap(b.getHeight(), b.getWidth(), Bitmap.Config.ARGB_8888);
        tempBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
        for(int i = 0; i<b.getHeight()  ; i++)
            for(int j=0; j<b.getWidth(); j++)
            {
                tempBitmap.setPixel(i, j, b.getPixel(j, b.getHeight()-1-i));
            }
        return tempBitmap;
    }
















    public Bitmap rotate90(Bitmap b)
    {
        Bitmap tempBitmap = Bitmap.createBitmap(b.getHeight(), b.getWidth(), Bitmap.Config.ARGB_8888);
        tempBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
        for(int i = 0; i<b.getHeight()  ; i++)
            for(int j=0; j<b.getWidth(); j++)
            {
                tempBitmap.setPixel(b.getHeight()-1-i, b.getWidth()-1-j, b.getPixel(j, i));
            }
        return tempBitmap;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_work);
        cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview1);
        buttonGo = (Button) findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(this);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        cont = this;
        iv = (ImageView) findViewById(R.id.iv);
        //cameraPreview.resumePreview();
        myHandler = new Handler();
        md= MediaPlayer.create(cont, R.raw.pomme);
        buttonOption = (Button) findViewById(R.id.buttonOption);
        buttonOption.setOnClickListener(this);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        pb.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onResume() {
         super.onResume();
        cameraPreview.resumePreview();
    }

    @Override
    public void onPause() {
      super.onPause();
        cameraPreview.releaseCamera();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.buttonGo) {
            timeFrameTaken = 0;
            time = System.currentTimeMillis();
            myHandler.postDelayed(myRunnable, 1000);
            again = true;
        }
        if(v.getId() == R.id.buttonStop) {
            again = false;
            Toast.makeText(this, timeFrameTaken + " frames taken", Toast.LENGTH_LONG).show();
        }
        if(v.getId() == R.id.buttonOption) {
            Intent option = new Intent(cont, OptionActivity.class);
            cont.startActivity(option);
        }
        if(v.getId() == R.id.save)
        {
            Date d= new Date();
            File fichier = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"LightPaintingStudio2" +System.nanoTime()   +".jpg");
            try {
                bitmapWork.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(fichier));
                Log.d("Erreursave", "onClick " + "ok " + fichier.getPath());
                Toast.makeText(cont, "saved", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fichier)));

              //  MediaStore.Images.Media.insertImage(getContentResolver(), bitmapWork, "LightPaintingStudio2"+d.toString()+".jpg" , " ");
                Toast.makeText(cont, "saved", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Log.d("Erreursave", "onClick "+e.toString());
                Toast.makeText(cont, "failed saved", Toast.LENGTH_SHORT).show();
            }
        }

    }




}