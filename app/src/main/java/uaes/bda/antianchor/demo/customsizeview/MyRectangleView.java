package uaes.bda.antianchor.demo.customsizeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LENOVO on 2017/4/14.
 */

public class MyRectangleView extends View {
    //构造器
    public MyRectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制黑色背景
        canvas.drawColor(Color.BLACK);
        //创建画笔
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置画笔的颜色
        paint.setColor(Color.BLACK);
        //绘制矩形
        canvas.drawRect(20,10,10,20,paint);


       /* //字符串 以字符串下面为基准
        canvas.drawText("这是字符串", 10, 130, paint);

        //定义一个矩形
        RectF rf1 = new RectF(10, 130, 110, 230);
        //画弧顺时针
        canvas.drawArc(rf1, 0, 45, true, paint);
        //画线
        canvas.drawLine(150, 10, 250, 110, paint);

        //定义一个矩形
        RectF rf2 = new RectF(150, 130, 250, 230);
        //画圆
        canvas.drawOval(rf2, paint);*/

    }
}
