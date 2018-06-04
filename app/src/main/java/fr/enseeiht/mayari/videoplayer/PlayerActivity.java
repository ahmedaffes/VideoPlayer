package fr.enseeiht.mayari.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

public class PlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback{


    private static final int SELECT_VIDEO = 100;

    Button selectVideo ;
    Button intentPlay ;
    TextView urlText ;
    SurfaceView surface ;
    Button play ;
    Button stop ;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    boolean stopResume = true ;
    SeekBar seekBar ;
    BarUpdaterTask barUpdaterTask = new BarUpdaterTask() ;

    @Override
    protected void onResume() {
        super.onResume();
        //barUpdaterTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //boolean cancel = barUpdaterTask.cancel(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        selectVideo = (Button) findViewById(R.id.selectVideoBtn) ;
        intentPlay = (Button) findViewById(R.id.intentPlayBtn) ;
        intentPlay.setEnabled(false);
        urlText = (TextView) findViewById(R.id.textViewUri) ;
        surface = (SurfaceView) findViewById(R.id.surfaceView) ;
        stop = (Button) findViewById(R.id.stopResume) ;
        play = (Button) findViewById(R.id.playFrom) ;
        seekBar = (SeekBar) findViewById(R.id.seekBar2) ;

// Get the surfaceHolder from it
        surfaceHolder = surface.getHolder();
// and assign to it the call back this class implements
        surfaceHolder.addCallback( this );
// this is a compatibility check, setType has been deprecated since HoneyComb,
// it guarantees back compatibility
        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB )
            surfaceHolder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );

        mediaPlayer = new MediaPlayer();

        selectVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                startActivityForResult(intent, SELECT_VIDEO);

            }
        });


        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(urlText.getText().toString()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stopResume == true)
                   mediaPlayer.pause();
                else
                    mediaPlayer.start();

                stopResume = !stopResume;
            }
        });


        intentPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

              /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlText.getText().toString())) ;
                startActivity(intent);*/
//this.getApplicationContext(),
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(urlText.getText().toString()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    intentPlay.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    mediaPlayer.pause();
                    mediaPlayer.seekTo(progress*1000);
                    mediaPlayer.start();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == SELECT_VIDEO) {
            super.onActivityResult(requestCode, resultCode, data);
            intentPlay.setEnabled(true);
            urlText.setText(data.getData().toString());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}


class BarUpdaterTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        while (!isCancelled()) {
            publishProgress() ;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
