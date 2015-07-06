package com.example.audio;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ui.ViewProxy;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private static final String TAG = "MainActivity";
	private static final int INVALID_POINTER_ID = -1;
	private TextView recordTimeText;
	private ImageView audioSendButton;
	private View recordPanel;
	private View slideText;
	private float startedDraggingX = -1;
	private float distCanMove = dp(100);
	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	private Timer timer;
	RecordAudio recordAudio;
	private float _xDelta;
	private int _yDelta;
	RelativeLayout relContainer;
	float mLastTouchX,mLastTouchY,mPosX,mPosY , viewX,viewY;
	boolean interceptTouchEvents = true,recordingStopped = true;

	float dX, dY;

	private int mActivePointerId = INVALID_POINTER_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recordPanel = findViewById(R.id.record_panel);
		recordTimeText = (TextView) findViewById(R.id.recording_time_text);
		slideText = findViewById(R.id.slideText);
		ViewProxy.setAlpha(slideText, 0);
		relContainer = (RelativeLayout) findViewById(R.id.relContainer);
		audioSendButton = (ImageView) findViewById(R.id.chat_audio_send_button);
		TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
		textView.setText("SlideToCancel");
		audioSendButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				final int action = motionEvent.getActionMasked();
				/*switch (action){
					case MotionEvent.ACTION_DOWN: {
						final int pointerIndex = motionEvent.getActionIndex();
						final float x = motionEvent.getX(pointerIndex);
						final float y = motionEvent.getY(pointerIndex);

						// Remember where we started (for dragging)
						mLastTouchX = x;
						mLastTouchY = y;
						// Save the ID of this pointer (for dragging)
						mActivePointerId = motionEvent.getPointerId( 0);
						break;
					}

					case MotionEvent.ACTION_MOVE: {
						// Find the index of the active pointer and fetch its position
						final int pointerIndex =
								motionEvent.findPointerIndex(mActivePointerId);

						final float x = motionEvent.getX(pointerIndex);
						final float y = motionEvent.getY( pointerIndex);

						// Calculate the distance moved
						final float dx = x - mLastTouchX;
						final float dy = y - mLastTouchY;

						mPosX += dx;
						mPosY += dy;

						view.setX(mPosX);
						//view.setY(mPosY);
						view.invalidate();

						// Remember this touch position for the next move event
						mLastTouchX = x;
						mLastTouchY = y;

						break;
					}
					case MotionEvent.ACTION_UP: {
						mActivePointerId = INVALID_POINTER_ID;
						break;
					}

					case MotionEvent.ACTION_CANCEL: {
						mActivePointerId = INVALID_POINTER_ID;
						break;
					}

				}*/



				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
							.getLayoutParams();
					params.leftMargin = dp(30);
					slideText.setLayoutParams(params);
					ViewProxy.setAlpha(slideText, 1);
					startedDraggingX = -1;
					// startRecording();
					startrecord();
					audioSendButton.getParent()
							.requestDisallowInterceptTouchEvent(true);
					recordPanel.setVisibility(View.VISIBLE);
					scaleAnimation();

					mPosX = motionEvent.getRawX();
					if(viewX == 0){
						viewX = view.getX();
						viewY = view.getY();
					}


					dX = view.getX() - motionEvent.getRawX();
					dY = view.getY() - motionEvent.getRawY();

					//Log.d(TAG,"mPosX = "+mPosX +" viewX = "+viewX +" viewY = "+viewY);

					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
					//layoutParams.rightMargin = 0;

					//view.setLayoutParams(layoutParams);
					//Log.d(TAG,"Right margin when finger Down = "+layoutParams.rightMargin);

				} else if (motionEvent.getAction() == MotionEvent.ACTION_UP
						|| motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
					startedDraggingX = -1;
					if(!recordingStopped){
						stoprecord(false);
					}
					scaleAnimationReverse();
					ViewProxy.setAlpha(slideText, 0);
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
					layoutParams.rightMargin = 0;

					view.setLayoutParams(layoutParams);

					//view.setX(viewX);
					//view.setY(viewY);
					mPosX = 0;
					_xDelta = 0;
					interceptTouchEvents = true;


					//Log.d(TAG,"Right margin when finger lift = "+layoutParams.rightMargin);


					// stopRecording(true);
				} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
					if(interceptTouchEvents){
						_xDelta = mPosX - motionEvent.getRawX();
						//Log.d(TAG, "Moving X position " + motionEvent.getRawX() + " xDelta = " + _xDelta);
						float diff = view.getX() - _xDelta;


						//view.setX(diff);

						RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
						layoutParams.rightMargin = dp(_xDelta);
						view.setLayoutParams(layoutParams);



						float x = motionEvent.getX();
						if (x < -distCanMove) {
							//stoprecord(true);
							// stopRecording(false);
						}
						x = x + ViewProxy.getX(audioSendButton);
						FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
								.getLayoutParams();
						if (startedDraggingX != -1) {
							//Log.d(TAG,"startedDraggingX != -1 "+startedDraggingX);
							float dist = (x - startedDraggingX);
							params.leftMargin = dp(30) + (int) dist;
							slideText.setLayoutParams(params);


							float alpha = 1.0f + dist / distCanMove;
							if (alpha > 1) {
								alpha = 1;

							} else if (alpha < 0) {
								alpha = 0;
								stoprecord(true);
								interceptTouchEvents = false;
								layoutParams.rightMargin = 0;
								vibrate();
								view.setLayoutParams(layoutParams);

							}
							ViewProxy.setAlpha(slideText, alpha);
						}
						if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
								+ dp(30)) {
							if (startedDraggingX == -1) {
								startedDraggingX = x;
								distCanMove = (recordPanel.getMeasuredWidth()
										- slideText.getMeasuredWidth() - dp(48)) / 2.0f;
								if (distCanMove <= 0) {
									distCanMove = dp(80);
								} else if (distCanMove > dp(80)) {
									distCanMove = dp(80);
								}
							}
						}
						if (params.leftMargin > dp(30)) {
							params.leftMargin = dp(30);
							slideText.setLayoutParams(params);
							ViewProxy.setAlpha(slideText, 1);
							startedDraggingX = -1;
						}

					}

				}
				view.onTouchEvent(motionEvent);
				relContainer.invalidate();
				return true;
			}
		});

	}

	private void scaleAnimation(){
		/*ScaleAnimation scale = new ScaleAnimation((float)1.0, (float)1.5, (float)1.0, (float)1.5);
		scale.setFillAfter(true);
		scale.setDuration(500);
		audioSendButton.startAnimation(scale);*/
		ScaleAnimation fade_in =  new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		fade_in.setDuration(100);     // animation duration in milliseconds
		fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
		audioSendButton.startAnimation(fade_in);

		/*Animation scale = AnimationUtils.loadAnimation(this,R.anim.scale);
		audioSendButton.startAnimation(scale);*/
	}

	private void scaleAnimationReverse(){
		ScaleAnimation scale = new ScaleAnimation((float)1.5, (float)1.0, (float)1.5, (float)1.0);
		scale.setFillAfter(true);
		scale.setDuration(500);
		audioSendButton.startAnimation(scale);

		ScaleAnimation fade_in =  new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		fade_in.setDuration(100);     // animation duration in milliseconds
		fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
		audioSendButton.startAnimation(fade_in);
	}

	private void startrecord() {
		// TODO Auto-generated method stub
		if(recordAudio == null){
			recordAudio = new RecordAudio();
		}
		recordingStopped = false;
		Log.d(TAG,"Started to record Audio ");
		recordAudio.startRecording();
		startTime = SystemClock.uptimeMillis();
		timer = new Timer();
		MyTimerTask myTimerTask = new MyTimerTask();
		timer.schedule(myTimerTask, 1000, 1000);
		vibrate();
	}

	private void stoprecord(boolean deleteAudio) {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}


		Log.d(TAG,"Stop audio recording booleane value = "+deleteAudio);
		if (recordAudio != null) {
			recordingStopped = true;
			if(deleteAudio){
				Toast.makeText(this,"Audio Deleted !",Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this,"Audio Saved at location !"+recordAudio.getmFileName(),Toast.LENGTH_LONG).show();
			}
			recordAudio.stopRecording(deleteAudio);
		}
		if (recordTimeText.getText().toString().equals("00:00")) {
			return;
		}
		recordTimeText.setText("00:00");
		vibrate();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (recordAudio != null) {
			recordAudio.onPause();
		}
	}

	private void vibrate() {
		// TODO Auto-generated method stub
		try {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int dp(float value) {
		return (int) Math.ceil(1 * value);
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			final String hms = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(updatedTime)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(updatedTime)),
					TimeUnit.MILLISECONDS.toSeconds(updatedTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(updatedTime)));
			long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
					- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
							.toMinutes(updatedTime));
			System.out.println(lastsec + " hms " + hms);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						if (recordTimeText != null)
							recordTimeText.setText(hms);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
