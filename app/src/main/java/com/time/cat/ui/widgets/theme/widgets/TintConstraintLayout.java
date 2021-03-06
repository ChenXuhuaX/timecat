package com.time.cat.ui.widgets.theme.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.time.cat.ui.widgets.theme.utils.TintManager;

/**
 * @author dlink
 * @date 2018/1/25
 * @discription
 */
public class TintConstraintLayout extends ConstraintLayout implements Tintable, AppCompatBackgroundHelper.BackgroundExtensible, AppCompatForegroundHelper.ForegroundExtensible {

    private AppCompatBackgroundHelper mBackgroundHelper;
    private AppCompatForegroundHelper mForegroundHelper;

    public TintConstraintLayout(Context context) {
        this(context, null);
    }

    public TintConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TintConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        TintManager tintManager = TintManager.get(context);

        mBackgroundHelper = new AppCompatBackgroundHelper(this, tintManager);
        mBackgroundHelper.loadFromAttribute(attrs, defStyleAttr);

        mForegroundHelper = new AppCompatForegroundHelper(this, tintManager);
        mForegroundHelper.loadFromAttribute(attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (getBackground() != null) {
            invalidateDrawable(getBackground());
        }
    }

    @Override
    public void setForeground(Drawable foreground) {
        super.setForeground(foreground);
        if (mForegroundHelper != null) {
            mForegroundHelper.setForegroundDrawableExternal(foreground);
        }
    }

    public void setForegroundResource(int resId) {
        if (mForegroundHelper != null) {
            mForegroundHelper.setForegroundResId(resId);
        }
    }

    @Override
    public void setForegroundTintList(int resId) {
        if (mForegroundHelper != null) {
            mForegroundHelper.setForegroundTintList(resId, null);
        }
    }

    @Override
    public void setForegroundTintList(int resId, PorterDuff.Mode mode) {
        if (mForegroundHelper != null) {
            mForegroundHelper.setForegroundTintList(resId, mode);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundDrawableExternal(background);
        }
    }

    @Override
    public void setBackgroundResource(int resId) {
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundResId(resId);
        } else {
            super.setBackgroundResource(resId);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundColor(color);
        }
    }

    @Override
    public void setBackgroundTintList(int resId) {
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundTintList(resId, null);
        }
    }

    @Override
    public void setBackgroundTintList(int resId, PorterDuff.Mode mode) {
        if (mBackgroundHelper != null) {
            mBackgroundHelper.setBackgroundTintList(resId, mode);
        }
    }

    @Override
    public void tint() {
        if (mBackgroundHelper != null) {
            mBackgroundHelper.tint();
        }
        if (mForegroundHelper != null) {
            mForegroundHelper.tint();
        }
    }
}