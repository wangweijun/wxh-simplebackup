package com.Cissoid.simplebackup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ���view����������drawable���ϲ�Ϊǰһ��view��ͼƬ���²�Ϊ��һ��view��ͼƬ
 * ÿ�λ��������ϲ�ͼƬ��������ռ��Ļ�ı�������͸�������ﵽһ�ּ���Ч��
 * 
 * @author Administrator
 * @date 2013/1/29 17:52
 */
public class DrawableChangeView extends View
{

    private int mPosition = 0;
    private int mPrevPosition = 1;
    private float mDegree = 0;

    private Drawable[] mDrawables;
    private Drawable mBack;

    public DrawableChangeView( Context context )
    {
        super(context);
        setWillNotDraw(false);
    }

    public DrawableChangeView( Context context , AttributeSet attrs )
    {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public DrawableChangeView( Context context , AttributeSet attrs ,
            int defStyle )
    {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    public void setDrawables( Drawable[] drawables )
    {
        mDrawables = drawables;
        mBack = drawables[1];
    }

    public void setPosition( int position )
    {
        mPosition = position;
    }

    public void setDegree( float degree )
    {
        mDegree = degree;
    }

    @Override
    protected void onDraw( Canvas canvas )
    {
        
        if ( mDrawables == null )
        {
            return;
        }
        int alpha = 255 - (int) (mDegree * 255);
        Drawable fore = mDrawables[mPosition];
        fore.setBounds(0, 0, getWidth(), getHeight());
        mBack.setBounds(0, 0, getWidth(), getHeight());
        if ( mPrevPosition != mPosition )
        {
            if ( mPosition != mDrawables.length - 1 )
            {
                mBack = mDrawables[mPosition + 1];
            }
            else
            {
                mBack = mDrawables[mPosition];
            }
        }
        fore.setAlpha(alpha);
        mBack.setAlpha(255);
        mBack.draw(canvas);
        fore.draw(canvas);
        mPrevPosition = mPosition;
    }
}
