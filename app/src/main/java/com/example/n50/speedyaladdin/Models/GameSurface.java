package com.example.n50.speedyaladdin.Models;

/**
 * Created by 12124 on 4/30/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.n50.speedyaladdin.Constant;
import com.example.n50.speedyaladdin.MyApplication;
import com.example.n50.speedyaladdin.R;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {


    private static final int MAX_STREAMS = 100;
    private GameThread gameThread;
    private Bitmap mScaledBackground;
    private Aladdin aladdin;
    private Obstacle obstacleTower1;
    private Obstacle obstacleTower2;
    private Obstacle obstacleWand1;
    private Obstacle obstacleWand2;
    //    private int soundIdExplosion;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    private Paint mStrokePaint;
    private Paint mTextPaint;
    private Paint mScorePaint;


    public GameSurface(Context context) {
        super(context);

        mStrokePaint = new Paint();
        mStrokePaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setTextSize(70);
        mStrokePaint.setStrokeWidth(20);
        mStrokePaint.setColor(Color.WHITE);
        mTextPaint = new Paint();
        mTextPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(70);

        mScorePaint = new Paint();
        mScorePaint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        mScorePaint.setColor(Color.parseColor("#FAFAFC"));
        mScorePaint.setTextSize(50);


        // Đảm bảo Game Surface có thể focus để điều khiển các sự kiện.
        this.setFocusable(true);


        // Sét đặt các sự kiện liên quan tới Game.
        this.getHolder().addCallback(this);

        this.initSoundPool();
    }

    private void initSoundPool() {
        // Với phiên bản Android API >= 21
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // Với phiên bản Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }


        // Sự kiện SoundPool đã tải lên bộ nhớ thành công.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;

                // Phát nhạc nền
                playSoundBackground();
            }
        });

        // Tải file nhạc tiếng nổ (background.mp3) vào SoundPool.
        this.soundIdBackground = this.soundPool.load(this.getContext(), R.raw.background, 1);

        // Tải file nhạc tiếng nổ (explosion.wav) vào SoundPool.
//        this.soundIdExplosion = this.soundPool.load(this.getContext(), R.raw.explosion,1);


    }
//    public void playSoundExplosion()  {
//        if(this.soundPoolLoaded) {
//            float leftVolumn = 0.8f;
//            float rightVolumn =  0.8f;
//
//            // Phát âm thanh explosion.wav
//            int streamId = this.soundPool.play(this.soundIdExplosion,leftVolumn, rightVolumn, 1, 0, 1f);
//        }
//    }

    public void playSoundBackground() {
        if (this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn = 0.8f;

            // Phát âm thanh background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground, leftVolumn, rightVolumn, 1, -1, 1f);
        }
    }

    public void update() {
        this.aladdin.update();
        ((MyApplication) getContext().getApplicationContext()).mAladdinCurrentCoor = aladdin.mCoor;

        this.obstacleWand1.update();
        ((MyApplication) getContext().getApplicationContext()).mObstacle1Current = obstacleWand1;
        this.obstacleTower1.update();
        this.obstacleWand2.update();
        ((MyApplication) getContext().getApplicationContext()).mObstacle2Current = obstacleWand2;
        this.obstacleTower2.update();

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(mScaledBackground, 0, 0, null); // vẽ background

        MyApplication myApplication = (MyApplication) getContext().getApplicationContext();

        // vẽ Aladdin
        if (myApplication.mIsEndGame == false) {
            this.aladdin.draw(canvas);
        }

        // vẽ obstacle
        this.obstacleTower1.draw(canvas);
        this.obstacleWand1.draw(canvas);
        this.obstacleTower2.draw(canvas);
        this.obstacleWand2.draw(canvas);

//        // để test padding => không xoá
//        Paint paintText = new Paint();
//        paintText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
//        paintText.setColor(Color.GREEN);
//        canvas.drawRect(aladdin.mCoor.mX +Aladdin.mPaddingHor, //left
//            aladdin.mCoor.mY +Aladdin.mPaddingVer, //top
//            aladdin.mCoor.mX + aladdin.getWidth() -Aladdin.mPaddingHor, //right
//            aladdin.mCoor.mY + aladdin.getHeight() -Aladdin.mPaddingVer,//bottom
//            paintText);
//        // để test padding => không xoá
//        canvas.drawRect(obstacleTower1.mCoor.mX +Obstacle.mPaddingHor, //left
//                obstacleTower1.mCoor.mY +Obstacle.mPaddingVer, //top
//                obstacleTower1.mCoor.mX + obstacleTower1.getWidth() -Obstacle.mPaddingHor, //right
//                obstacleTower1.mCoor.mY + obstacleTower1.getHeight() -Obstacle.mPaddingVer,//bottom
//                paintText);
//        // để test padding => không xoá
//        canvas.drawRect(obstacleTower2.mCoor.mX +Obstacle.mPaddingHor, //left
//                obstacleTower2.mCoor.mY +Obstacle.mPaddingVer, //top
//                obstacleTower2.mCoor.mX + obstacleTower2.getWidth() -Obstacle.mPaddingHor, //right
//                obstacleTower2.mCoor.mY + obstacleTower2.getHeight() -Obstacle.mPaddingVer,//bottom
//                paintText);// để test padding => không xoá
//        canvas.drawRect(obstacleWand1.mCoor.mX +Obstacle.mPaddingHor, //left
//                obstacleWand1.mCoor.mY +Obstacle.mPaddingVer, //top
//                obstacleWand1.mCoor.mX + obstacleWand1.getWidth() -Obstacle.mPaddingHor, //right
//                obstacleWand1.mCoor.mY + obstacleWand1.getHeight() -Obstacle.mPaddingVer,//bottom
//                paintText);
//        // để test padding => không xoá
//        canvas.drawRect(obstacleWand2.mCoor.mX +Obstacle.mPaddingHor, //left
//                obstacleWand2.mCoor.mY +Obstacle.mPaddingVer, //top
//                obstacleWand2.mCoor.mX + obstacleWand2.getWidth() -Obstacle.mPaddingHor, //right
//                obstacleWand2.mCoor.mY + obstacleWand2.getHeight() -Obstacle.mPaddingVer,//bottom
//                paintText);



        // vẽ string "TAP TO START"
        if (myApplication.mIsPlaying == false) {
            drawText(mStrokePaint, canvas, "TAP TO START!!!", 200, getHeight() / 2 - 100);
            drawText(mTextPaint, canvas, "TAP TO START!!!", 200, getHeight() / 2 - 100);

        } else { // vẽ score
            drawText(mScorePaint, canvas, "Best score: " + myApplication.mBestScore, 50, 100);
            drawText(mScorePaint, canvas, "" + myApplication.mScore, 50, 200);
        }

        // vẽ string "GAME OVER"
        if (myApplication.mIsEndGame == true) {
            drawText(mStrokePaint, canvas, "GAME OVER", 200, getHeight() / 2 - 100);
            drawText(mTextPaint, canvas, "GAME OVER", 200, getHeight() / 2 - 100);
        }
    }

    // vẽ 1 string lên canvas
    public void drawText(Paint paint, Canvas canvas, String stringStart, int x, int y) {
        canvas.drawText(stringStart, x, y, paint);
    }

    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //background
        Bitmap backgroundRaw = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        float scale = (float) backgroundRaw.getHeight() / (float) getHeight();
        int newWidth = Math.round(backgroundRaw.getWidth() / scale);
        int newHeight = Math.round(backgroundRaw.getHeight() / scale);
        mScaledBackground = Bitmap.createScaledBitmap(backgroundRaw, newWidth, newHeight, true);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int x = displayMetrics.widthPixels / 6;
        int y = displayMetrics.heightPixels / 2;


        Bitmap aladdinBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.sprite_aladin_flying);
        this.aladdin = new Aladdin(getContext(), this, aladdinBitmap, x, y);

        Bitmap obstacleTowerBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.tower);
        Bitmap obstacleWandBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.wand);

        this.obstacleWand1 = new Obstacle(getContext(), this, Constant.ObstacleType.WAND, 1, obstacleWandBitmap, displayMetrics.widthPixels, 0);
        this.obstacleTower1 = new Obstacle(getContext(), this, Constant.ObstacleType.TOWER, 1, obstacleTowerBitmap, displayMetrics.widthPixels, 0);
        this.obstacleWand2 = new Obstacle(getContext(), this, Constant.ObstacleType.WAND, 2, obstacleWandBitmap, (int) (1.5 * displayMetrics.widthPixels), 0);
        this.obstacleTower2 = new Obstacle(getContext(), this, Constant.ObstacleType.TOWER, 2, obstacleTowerBitmap, (int) (1.5 * displayMetrics.widthPixels), 0);

        ((MyApplication) ((Activity) getContext()).getApplication()).mObstacle1Current = this.obstacleWand1;
        ((MyApplication) ((Activity) getContext()).getApplication()).mObstacle2Current = this.obstacleWand2;


        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    // Thi hành phương thức của interface SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);

                // Luồng cha, cần phải tạm dừng chờ GameThread kết thúc.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            int x=  (int)event.getX();
//            int y = (int)event.getY();
            if (((MyApplication) getContext().getApplicationContext()).mIsEndGame == true) {
                Intent intent = getContext().getPackageManager()
                        .getLaunchIntentForPackage(getContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent);
            } else {
                ((MyApplication) getContext().getApplicationContext()).mIsPlaying = true;
                this.aladdin.setMovingVectorForFlying(true);//fly up
            }


            return true;
        }
        return false;
    }
}
