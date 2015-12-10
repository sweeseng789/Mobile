package com.ss.mobileframework.Text;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

import com.ss.mobileframework.R;
import com.ss.mobileframework.Utility.CColor;
import com.ss.mobileframework.Utility.Vector3;

/**
 * Created by sweeseng789 on 26/11/2015.
 */
public class CText
{
    private float scale;
    private String text;
    private Vector3 pos;
    private CColor color;
    private Paint paint;

    public CText()
    {
        text = "";
        pos = new Vector3();
        color = new CColor();
        paint = new Paint();
    }

    public CText(Context context, float scale, Vector3 pos, CColor color)
    {
        setFont(context);
        setScale(scale);
        setColor(color.Alpha, color.Red, color.Green, color.Blue);
    }

    public void setScale(float scale)
    {
        this.scale = scale;
        paint.setTextSize(this.scale);
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setColor(int Alpha, int Red, int Green, int Blue)
    {
        color.Alpha = Alpha;
        color.Red = Red;
        color.Green = Green;
        color.Blue = Blue;
        paint.setARGB(color.Alpha, color.Red, color.Green, color.Blue);
    }

    public void setFont(Context context)
    {
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "Supernatural_Knight.ttf");
        paint.setTypeface(myTypeface);
    }

    public Vector3 getPos()
    {
        return pos;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public String getText()
    {
        return text;
    }

    public float getScale()
    {
        return scale;
    }

    public void renderText(Canvas canvas)
    {
        canvas.drawText(getText(), getPos().x, getPos().y, getPaint());
    }
}
