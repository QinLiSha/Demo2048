package com.example.administrator.demo2048;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    private void initGameView() {

        setColumnCount(4);
        setBackgroundColor(0x88E4B45C);
/**
 * 监听
 */
        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;//开始,,,,偏移

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                                Log.i(TAG, "onTouch: ===========left===========");
                            } else if (offsetX > 5) {
                                swipeRight();
                                Log.i(TAG, "onTouch: ===========right===========");
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                                Log.i(TAG, "onTouch: ===========up===========");
                            } else if (offsetY > 5) {
                                swipeDown();
                                Log.i(TAG, "onTouch: ===========down===========");
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, widthSpec);
    }

    /**
     * 只有刚刚打开时执行这个方法
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h) - 10) / 4;//宽高求手机宽高最小值
        addCards(cardWidth, cardWidth);
        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardWidth);
                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame() {
        MainActivity.getMainActivity().clearScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
        //Math.random()是0__1之间的数
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);//生成2和4之间的概率是9:1
    }

    private void swipeLeft() {
        boolean merge = false;//merge  vt.融入; （使） 混合; 相融; 渐渐消失在某物中;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置上的值是空的
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;

                        }
                        break;

                    }
                }
            }

        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置上的值是空的
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeUp() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置上的值是空的
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }

        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置上的值是空的
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete() {
        boolean complete = true;
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0
                        || (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
                        || (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))
                        || (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
                        || (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }
        }
        if (complete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle("@^w^@")
                    .setMessage("当你看到这个对话框时,就说明Game Over了,不过不要灰心哈,据说只有1%的人才能通关,继续加油啊!!!!")
                    .setPositiveButton("愈挫愈勇", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    })
                    .setNeutralButton("愤然离去", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           System.exit(0);
                        }
                    });
            builder.setCancelable(false);
            builder.show();
        }
    }

    /**
     * 设置标签颜色
     * @param i , j ,str
     */
    public void setColor(int i, int j, String str){
        switch(str){
            case "2":
                cardsMap[i][j].setBackgroundColor(50886677);
                break;
            case "4":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "8":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "16":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "32":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "64":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "128":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "256":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "512":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "1024":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "2048":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            case "":
            case "4096":
                cardsMap[i][j].setBackgroundColor(50886677);

                break;
            default:
                break;
        }
    }
}

