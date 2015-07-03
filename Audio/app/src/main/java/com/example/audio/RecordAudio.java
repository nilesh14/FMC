package com.example.audio;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Nilesh on 30/06/15.
 */
public class RecordAudio {
    private static final String LOG_TAG = "RecordAudio";
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;
    String mfileName;

    public String getmFileName() {
        return mFileName;
    }

    public void AudioRecordTest() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    public void startRecording() {
        mRecorder = new MediaRecorder();
        AudioRecordTest();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    public void onPause(){
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    public void stopRecording(boolean deleteAudio) {
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            if(deleteAudio){
                File file = new File(mFileName);
                if(file.exists()){
                    file.delete();
                }
            }
        }

    }
}
