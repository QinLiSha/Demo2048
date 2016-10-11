package com.example.administrator.demo2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class Card extends FrameLayout {
    private int num;
    private TextView label;

    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setBackgroundColor(0x33ffffff);
        label.setTextSize(33);
        label.setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(13, 13, 0, 0);
        addView(label, layoutParams);
        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(num + "");//不能直接写num
        }
    }

    /**
     * 将equals方法写错了,就没有被调用,要重写,但因为名字写错了,就没有没调用
     * @param o
     * @return
     */
    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }


}
