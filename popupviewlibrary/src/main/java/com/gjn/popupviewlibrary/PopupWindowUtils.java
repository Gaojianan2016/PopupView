package com.gjn.popupviewlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author gjn
 * @time 2018/8/6 10:48
 */

public abstract class PopupWindowUtils {
    private static final String TAG = "PopupWindowUtils";

    public static final int TYPE_CENTER =       0;
    public static final int TYPE_BOTTOM =       1;
    public static final int TYPE_TOP =          2;
    public static final int TYPE_LEFT =         3;
    public static final int TYPE_RIGHT =        4;
    public static final int TYPE_VIEW_BOTTOM =  5;
    public static final int TYPE_VIEW_TOP =     6;
    public static final int TYPE_VIEW_LEFT =    7;
    public static final int TYPE_VIEW_RIGHT =   8;

    @IntDef({TYPE_CENTER, TYPE_VIEW_BOTTOM, TYPE_VIEW_TOP, TYPE_VIEW_LEFT,
            TYPE_VIEW_RIGHT, TYPE_BOTTOM, TYPE_TOP, TYPE_LEFT, TYPE_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}

    private int width;
    private int height;
    private Activity activity;
    private View view;
    private View child;
    private PopupWindow popupWindow;
    private int type = TYPE_CENTER;
    private boolean canCancel = true;
    private List<PopupAnimationHelper> animationHelpers;

    public PopupWindowUtils(Activity activity, int layoutId) {
        this(activity, layoutId, true);
    }

    public PopupWindowUtils(Activity activity, int layoutId, boolean canCancel) {
        this(activity, layoutId, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, canCancel);
    }

    public PopupWindowUtils(Activity activity, int layoutId, int width, int height) {
        this(activity, layoutId, width, height, true);
    }

    public PopupWindowUtils(Activity activity, int layoutId, int width, int height, boolean canCancel) {
        this.activity = activity;
        this.width = width;
        this.height = height;
        this.canCancel = canCancel;
        this.view = LayoutInflater.from(activity).inflate(layoutId, null);

        initView();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        popupWindow = new PopupWindow(view, width, height, true);
        //测量窗口宽高
        popupWindow.getContentView().measure(0,0);
        //点击返回和点击popupwindow外弹出框是否消失
        if (canCancel) {
            popupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        //设置输入框弹出是向上移动
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        bindView(activity, view);

    }

    private void setType(@Type int type){
        if (type < TYPE_CENTER || type > TYPE_VIEW_RIGHT) {
            Log.w(TAG, "set type " + type + " is error.");
        }else {
            this.type = type;
        }
    }

    private void startAnimation(int res, int animation) {
        if (findViewById(res) != null && animation != 0) {
            findViewById(res).startAnimation(AnimationUtils.loadAnimation(activity, animation));
        }
    }

    protected <T extends View> T findViewById(@IdRes int id){
        return view.findViewById(id);
    }

    protected PopupWindowUtils setIdOnClickListener(@IdRes int id, View.OnClickListener l){
        if (findViewById(id) != null) {
            findViewById(id).setOnClickListener(l);
        }
        return this;
    }

    protected PopupWindowUtils setCanCancelId(@IdRes int... ids){
        for (int id : ids) {
            if (findViewById(id) != null) {
                findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        }
        return this;
    }

    protected PopupWindowUtils setTextViewText(@IdRes int id, CharSequence text){
        if (findViewById(id) != null) {
            ((TextView) findViewById(id)).setText(text);
        }
        return this;
    }

    protected ImageView getImageView(@IdRes int id){
        return findViewById(id);
    }

    public PopupWindowUtils setAnimationHelpers(List<PopupAnimationHelper> animationHelpers) {
        this.animationHelpers = animationHelpers;
        return this;
    }

    public int getType() {
        return type;
    }

    public PopupWindow getPopupWindow(){
        return popupWindow;
    }

    public void show(List<PopupAnimationHelper> animationHelpers){
        setAnimationHelpers(animationHelpers).show();
    }

    public void show(@Type int type){
        show(type, 0 , 0);
    }

    public void show(View view, @Type int type){
        show(view, type, 0 , 0);
    }

    public void show(@Type int type, int w, int h){
        show(null, type, w , h);
    }

    public void show(View view, @Type int type, int w, int h){
        child = view;
        setType(type);
        show(w, h);
    }

    public void show(){
        show(0, 0);
    }

    public void show(int w, int h){
        int gravity = Gravity.CENTER;

        if (animationHelpers != null && animationHelpers.size() > 0) {
            for (PopupAnimationHelper animationHelper : animationHelpers) {
                startAnimation(animationHelper.getRes(), animationHelper.getIn());
            }
        }

        if (child != null && type >= TYPE_VIEW_BOTTOM) {
            switch (type){
                case TYPE_VIEW_TOP:
                    h = -popupWindow.getContentView().getMeasuredHeight() - child.getHeight() + h;
                    break;
                case TYPE_VIEW_LEFT:
                    w = -popupWindow.getWidth() -popupWindow.getContentView().getMeasuredWidth() + w;
                    h = -child.getHeight() + h;
                    break;
                case TYPE_VIEW_RIGHT:
                    w = child.getWidth() + w;
                    h = -child.getHeight() + h;
                    break;
                case TYPE_VIEW_BOTTOM:
                default:
                    break;
            }
            popupWindow.showAsDropDown(child, w, h);
        }else {
            switch (type) {
                case TYPE_BOTTOM:
                    gravity = Gravity.BOTTOM;
                    break;
                case TYPE_LEFT:
                    gravity = Gravity.START;
                    break;
                case TYPE_RIGHT:
                    gravity = Gravity.END;
                    break;
                case TYPE_TOP:
                    gravity = Gravity.TOP;
                    break;
                case TYPE_CENTER:
                default:
                    break;
            }
            popupWindow.showAtLocation(activity.findViewById(android.R.id.content),
                    gravity, w, h);
        }
    }

    public void dismiss(){
        if (animationHelpers != null && animationHelpers.size() > 0) {
            int duration = 0;
            for (PopupAnimationHelper animationHelper : animationHelpers) {
                startAnimation(animationHelper.getRes(), animationHelper.getOut());
                duration = Math.max(duration, animationHelper.getDuration());
            }
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    popupWindow.dismiss();
                }
            }, duration);
        }else {
            popupWindow.dismiss();
        }
    }

    public abstract void bindView(Activity activity, View view);

    public static class PopupAnimationHelper{
        public static final int DEFAULT_DURATION = 300;
        private int res;
        private int in;
        private int out;
        private int duration = DEFAULT_DURATION;

        public PopupAnimationHelper() {
        }

        public PopupAnimationHelper(int res, int in) {
            this.res = res;
            this.in = in;
        }

        public PopupAnimationHelper(int res, int in, int out, int duration) {
            this.res = res;
            this.in = in;
            this.out = out;
            this.duration = duration;
        }

        public int getRes() {
            return res;
        }

        public void setRes(int res) {
            this.res = res;
        }

        public int getIn() {
            return in;
        }

        public void setIn(int in) {
            this.in = in;
        }

        public int getOut() {
            return out;
        }

        public void setOut(int out) {
            this.out = out;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
