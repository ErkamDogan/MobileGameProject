package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class Map extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private final Joystick joystick;
    private final Fire fire;
    private Thread thread;
    private SurfaceHolder holder;
    private boolean canDraw = true;
    private Paint paint;
    private Bitmap player;
    private Bitmap ghostBitmap;
    private int totalFrame = 4;
    private int currentPlayerFrame = 0;
    private int currentJoystickFrame = 0;
    private long frameTicker;
    private int xPosPlayer;
    private int yPosPlayer;
    private int xPosGhost;
    private int yPosGhost;
    int xDistance;
    int yDistance;
    private int direction = 4;
    private int viewDirection = 2;
    private int nextDirection = 4;
    private int ghostDirection;
    private int screenWidth;
    private int blockSize;
    private int currentScore = 0;
    private int lives=1;
    private int bullet=5;
    private boolean playerdie = false;

    public Map(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        frameTicker = 1000/totalFrame;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        blockSize = screenWidth/17;
        blockSize = (blockSize / 5) * 5;
        xPosGhost = 8 * blockSize;
        yPosGhost = 4 * blockSize;
        xPosPlayer = 8 * blockSize;
        yPosPlayer = 13 * blockSize;
        ghostDirection = 4;
        joystick = new Joystick(6*blockSize,23*blockSize,150,75);
        fire = new Fire(context,13*blockSize,23*blockSize,150);
        loadBitmapImages();
        Log.i("info", "Constructor");
    }

    @Override
    public void run() {
        Log.i("info", "Run");
        while (canDraw) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = holder.lockCanvas();
            // Set background color to Transparent
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                drawMap(canvas);
                updateFrame(System.currentTimeMillis());
                drawJoystick(canvas);
                drawFireButton(canvas);
                moveGhost(canvas);
                movePlayer(canvas);
                drawPellets(canvas);
                updateScores(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
    private void loadBitmapImages() {
        int spriteSize = screenWidth / 17;
        spriteSize = (spriteSize / 5) * 5;
        player = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.player3), spriteSize, spriteSize, false);

        ghostBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ghost), spriteSize, spriteSize, false);

    }

    public void updateScores(Canvas canvas) {
        paint.setTextSize(blockSize);

        Globals g = Globals.getInstance();
        int highScore = g.getHighScore();
        if (currentScore > highScore) {
            g.setHighScore(currentScore);
        }

        if(currentScore == 100) {
            lives++;
            currentScore = 0;
        }

        String formattedLives = String.format("%02d", lives);
        String lives = "Lives : " + formattedLives;
        canvas.drawText(lives, 0, 1*blockSize - 10, paint);

        String formattedScore = String.format("%03d", currentScore);
        String score = "Score : " + formattedScore;
        canvas.drawText(score, 7 * blockSize, 1 * blockSize - 10, paint);

        String formattedBullet = String.format("%02d", bullet);
        String bullet = "Bullet : " + formattedBullet;
        canvas.drawText(bullet, 4*blockSize, 2*blockSize - 10, paint);
    }
    public void drawJoystick(Canvas canvas){
        joystick.draw(canvas);
    }
    public void moveGhost(Canvas canvas) {
        short ch;

        xDistance = xPosPlayer - xPosGhost;
        yDistance = yPosPlayer - yPosGhost;

        if ((xPosGhost % blockSize == 0) && (yPosGhost % blockSize == 0)) {
            ch = leveldata1[yPosGhost / blockSize][xPosGhost / blockSize];

            if (xPosGhost >= blockSize * 18) {
                xPosGhost = 0;
            }
            if (xPosGhost < 0) {
                xPosGhost = blockSize * 18;
            }


            if (xDistance >= 0 && yDistance >= 0) {
                if ((ch & 4) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else
                    ghostDirection = 3;
            }
            if (xDistance >= 0 && yDistance <= 0) {
                if ((ch & 4) == 0 && (ch & 2) == 0 ) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 1;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 4) == 0) {
                    ghostDirection = 1;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }
            if (xDistance <= 0 && yDistance >= 0) {
                if ((ch & 1) == 0 && (ch & 8) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 2;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 8) == 0) {
                    ghostDirection = 2;
                }
                else ghostDirection = 1;
            }
            if (xDistance <= 0 && yDistance <= 0) {
                if ((ch & 1) == 0 && (ch & 2) == 0) {
                    if (Math.abs(xDistance) > Math.abs(yDistance)) {
                        ghostDirection = 3;
                    } else {
                        ghostDirection = 0;
                    }
                }
                else if ((ch & 1) == 0) {
                    ghostDirection = 3;
                }
                else if ((ch & 2) == 0) {
                    ghostDirection = 0;
                }
                else ghostDirection = 2;
            }
            // Handles wall collisions
            if ( (ghostDirection == 3 && (ch & 1) != 0) ||
                    (ghostDirection == 1 && (ch & 4) != 0) ||
                    (ghostDirection == 0 && (ch & 2) != 0) ||
                    (ghostDirection == 2 && (ch & 8) != 0) ) {
                ghostDirection = 4;
            }
        }

        if (ghostDirection == 0) {
            yPosGhost += -blockSize / 20;
        } else if (ghostDirection == 1) {
            xPosGhost += blockSize / 20;
        } else if (ghostDirection == 2) {
            yPosGhost += blockSize / 20;
        } else if (ghostDirection == 3) {
            xPosGhost += -blockSize / 20;
        }

        // Checks for ghost and player collisions
        // If lives of player is zero, we goes to finish screen
        // Otherwise lives of player decreases and return the starting point
        if(xPosGhost== xPosPlayer && yPosGhost== yPosPlayer){
            if(lives==1) { // Last collision
                playerdie = true;
                Intent finish = new Intent(getContext(), FinishScreen.class);
                getContext().startActivity(finish);
            }
            else
                lives--;
            xPosPlayer = 8 * blockSize;
            yPosPlayer = 13 * blockSize;
        }

        canvas.drawBitmap(ghostBitmap, xPosGhost, yPosGhost, paint);
    }
    public void drawPellets(Canvas canvas) {
        float x;
        float y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                x = j * blockSize;
                y = i * blockSize;
                // Draws pellet in the middle of a block
                if ((leveldata1[i][j] & 16) != 0)
                    canvas.drawCircle(x + blockSize / 2, y + blockSize / 2, blockSize / 10, paint);
            }
        }
    }
    public void drawMap(Canvas canvas) {
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2.5f);
        int x;
        int y;
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                x = j * blockSize;
                y = i * blockSize;
                // "bitwise and" binary control
                if ((leveldata1[i][j] & 1) != 0) // draws left
                    canvas.drawLine(x, y, x, y + blockSize - 1, paint);

                if ((leveldata1[i][j] & 2) != 0) // draws top
                    canvas.drawLine(x, y, x + blockSize - 1, y, paint);

                if ((leveldata1[i][j] & 4) != 0) // draws right
                    canvas.drawLine(
                            x + blockSize, y, x + blockSize, y + blockSize - 1, paint);
                if ((leveldata1[i][j] & 8) != 0) // draws bottom
                    canvas.drawLine(
                            x, y + blockSize, x + blockSize - 1, y + blockSize , paint);
            }
        }
        paint.setColor(Color.WHITE);
    }
    public void drawFireButton(Canvas canvas){ fire.draw(canvas);}
    public void movePlayer(Canvas canvas) {
        short ch;

        // Check if xPos and yPos of player is both a multiple of block size
        if ( (xPosPlayer% blockSize == 0) && (yPosPlayer  % blockSize == 0) ) {

            // When player goes through tunnel on
            // the right reappear at left tunnel
            if (xPosPlayer >= blockSize * 18) {
                xPosPlayer= 0;
            }

            // Is used to find the number in the level array in order to
            // check wall placement, pellet placement, and candy placement
            ch = leveldata1[yPosPlayer / blockSize][xPosPlayer / blockSize];

            // If there is a pellet, eat it
            if ((ch & 16) != 0) {
                // Toggle pellet so it won't be drawn anymore
                leveldata1[yPosPlayer / blockSize][xPosPlayer/ blockSize] = (short) (ch ^ 16);
                currentScore += 10;
            }

            // Checks for direction buffering
            if (!((nextDirection == 3 && (ch & 1) != 0) ||
                    (nextDirection == 1 && (ch & 4) != 0) ||
                    (nextDirection == 0 && (ch & 2) != 0) ||
                    (nextDirection == 2 && (ch & 8) != 0))) {
                viewDirection = direction = nextDirection;
            }

            // Checks for wall collisions
            if ((direction == 3 && (ch & 1) != 0) ||
                    (direction == 1 && (ch & 4) != 0) ||
                    (direction == 0 && (ch & 2) != 0) ||
                    (direction == 2 && (ch & 8) != 0)) {
                direction = 4;
            }
        }

        // When Player goes through tunnel on
        // the left reappear at right tunnel
        if (xPosPlayer< 0) {
            xPosPlayer = blockSize * 18;
        }

        canvas.drawBitmap(player, xPosPlayer, yPosPlayer, paint);

        // Depending on the direction move the position of Player
        if (direction == 0) {
            yPosPlayer += -blockSize/15;
        } else if (direction == 1) {
            xPosPlayer += blockSize/15;
        } else if (direction == 2) {
            yPosPlayer += blockSize/15;
        } else if (direction == 3) {
            xPosPlayer += -blockSize/15;
        }

    }

    Runnable longPressed = new Runnable() {
        public void run() {
            Log.i("info", "LongPress");
            Intent pauseIntent = new Intent(getContext(), Pause.class);
            getContext().startActivity(pauseIntent);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
               if(joystick.isPressed(event.getX(),event.getY())){
                   joystick.setIsPressed(true);
               }
               return true;
            case (MotionEvent.ACTION_MOVE):
                if(joystick.getIsPressed()){
                    joystick.setActuator(event.getX(),event.getY());
                    calculateSwipeDirection(joystick);
                }
                return true;
            case (MotionEvent.ACTION_UP):
                joystick.setIsPressed(false);
                joystick.resetActuator();
                nextDirection=4;
                return true;
        }
        return super.onTouchEvent(event);
    }
    private void calculateSwipeDirection(Joystick joystick) {

        // Directions
        // 0 means going down
        // 1 means going right
        // 2 means going up
        // 3 means going left
        // 4 means stop moving, look at move function

        double x = joystick.getActuatorX();
        double y = joystick.getActuatorY();
        if((x==0 && y==0) || playerdie){
            nextDirection=4;
        }
        else if(y > 0){
            if(x<0){
                if(Math.abs(x)>Math.abs(y)){
                    nextDirection = 3;
                }
                else{
                    nextDirection = 2;
                }
            }
            else{
                if(Math.abs(x)>Math.abs(y)){
                    nextDirection = 1;
                }
                else{
                    nextDirection = 2;
                }
            }
        }
        else{
            if(x<0){
                if(Math.abs(x)>Math.abs(y)){
                    nextDirection = 3;
                }
                else{
                    nextDirection = 0;
                }
            }
            else{
                if(Math.abs(x)>Math.abs(y)){
                    nextDirection = 1;
                }
                else{
                    nextDirection = 0;
                }
            }
        }
    }
    private void updateFrame(long gameTime) {

        // If enough time has passed go to next frame
        if (gameTime > frameTicker + (totalFrame * 30)) {
            frameTicker = gameTime;

            // Increment the frame
            currentPlayerFrame++;
            if (currentPlayerFrame >= totalFrame) {
                currentPlayerFrame = 0;
            }
        }
        joystick.update();

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("info", "Surface Created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("info", "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("info", "Surface Destroyed");
    }
    public void pause() {
        Log.i("info", "pause");
        canDraw = false;
        thread = null;
    }

    public void resume() {
        Log.i("info", "resume");
        if (thread != null) {
            thread.start();
        }
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
            Log.i("info", "resume thread");
        }
        canDraw = true;
    }
    final short leveldata1[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {19, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 26, 24, 26, 22, 0, 21, 0, 0, 0, 0},
            {26, 26, 26, 16, 26, 20, 0, 0, 0, 0, 0, 17, 26, 16, 26, 26, 26, 26},
            {0, 0, 0, 21, 0, 17, 26, 26, 26, 26, 26, 20, 0, 21, 0, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 19, 28},
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 21, 0},
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26, 26, 26, 28},

    };


}
