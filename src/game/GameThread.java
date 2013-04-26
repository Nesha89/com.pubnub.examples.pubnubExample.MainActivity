package game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


public class GameThread extends Thread{
	
	private SurfaceHolder surfaceHolder; //Surface holder that can access the physical surface
    private GamePanel gamePanel; //The actual view that handles inputs and draws to the surface
    private Canvas canvas;
    private long sleepTime, beforeTime; //sleeptime: time required to sleep to keep the game running smoothly
    private long delayTime = 70; //time (in milliseconds) between each call to update
    public int state = 1; //state of the game (Running or Paused).
    public final static int RUNNING = 1;
    public final static int PAUSED = 2;

	public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
		super();
		Log.w(GameThread.class.getName(), "gameThread cons");
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@Override
	public void run() {
		Log.w(GameThread.class.getName(), "run");
		while(state == RUNNING){
	        beforeTime = System.nanoTime();
	        canvas = null;
	        // try locking the canvas for exclusive pixel editing in the surface
	        try {
	        	//lock canvas so nothing else can use it
	            canvas = this.surfaceHolder.lockCanvas();
	            synchronized (surfaceHolder) {
	                this.gamePanel.render(canvas); //update game state
	            }
	        } 
	        finally {
	        	// in case of an exception the surface is not left in an inconsistent state
	            if (canvas != null) {
	            	surfaceHolder.unlockCanvasAndPost(canvas);
	            }
	        }
	        //code for sleep
	        sleepTime = delayTime-((System.nanoTime()-beforeTime)/1000000L);
	        try {
                if(sleepTime > 0){
                	Thread.sleep(sleepTime);//this.sleep(sleepTime);
                }
            }
	        catch (InterruptedException e) {
	        	Log.w(GameThread.class.getName(), e);
            }
		}
	}
}
