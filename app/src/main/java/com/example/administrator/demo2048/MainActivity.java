package com.example.administrator.demo2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView tv_score,tv_restartGame,tv_exit;
    private GameView gameView;
    private static MainActivity mainActivity;
    private int score;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.gameView);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_restartGame = (TextView) findViewById(R.id.tv_restartGame);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_restartGame.setOnClickListener(onClickListener);
        tv_exit.setOnClickListener(onClickListener);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tv_score.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }

    /**
     * 设置监听事件
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.tv_exit:
                    System.exit(0);
                    break;
                case R.id.tv_restartGame:
                    gameView.startGame();
                    break;
            }
        }
    };
}
