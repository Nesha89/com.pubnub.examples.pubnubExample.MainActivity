package game;

import com.pubnub.examples.pubnubExample.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
	
	GameThread gameThread;
	private int screenWidth, screenHeight;
	public static Bitmap board;

	public GamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		gameThread = new GameThread(getHolder(), this);
		setFocusable(true);
	}
	
	void setUpBitmaps(){
		board = BitmapFactory.decodeResource(getResources(), R.drawable.board_200px);
		board = Bitmap.createScaledBitmap(board, screenWidth, screenHeight - screenHeight/7, false);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//Log.w(GamePanel.class.getName(), "surfaceChanged");
		screenWidth = width;
		screenHeight = height;
		setUpBitmaps();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(GamePanel.class.getName(), "surfaceChanged");
		if(gameThread.state==GameThread.PAUSED){
			//When game is opened again in the Android OS
            gameThread = new GameThread(getHolder(), this);
		}
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.w(GamePanel.class.getName(), "surfaceDestroyed");
		boolean retry = true;
        //code to end gameloop
        gameThread.state = GameThread.PAUSED;
        while (retry) {
        	try {
                //code to kill gameThread
        		gameThread.join();
                retry = false;
            } 
        	catch (InterruptedException e) {
        		Log.w(GamePanel.class.getName(), e);
            }
        }
	}
	
	private OnTouchListener clickListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			
			return false;
		}
	};
	
	public void render(Canvas canvas) {
		//Log.w(GamePanel.class.getName(), "inside render");
		canvas.drawColor(Color.CYAN);
		canvas.drawBitmap(board, 0, 0, null);//
		
	}
}
