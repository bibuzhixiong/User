package com.ude.one.step.city.user.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.utils.RescourseUtils;



/**
 * Created by lan on 2016/6/28.
 */
public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener,TextWatcher{
    /**
     * 删除图标
     */
    private Drawable mClearDrawable;

    /**
     * 是否获得焦点
     */
    private boolean isFocus;

    public ClearEditText(Context context) {
        this(context,null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs,android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获得EditText的右边图标，没有，则设置我们的图标
        mClearDrawable=getCompoundDrawables()[2];
        if(mClearDrawable==null){
            mClearDrawable= RescourseUtils.getDrawble(R.mipmap.icon_delete);
        }
        //不设置这句话，图片不会显示
        mClearDrawable.setBounds(0,0,mClearDrawable.getIntrinsicWidth(),mClearDrawable.getIntrinsicHeight());
        //默认删除图标是隐藏的
        setClearIconVisible(false);
        //设置监听
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(mClearDrawable!=null){
                boolean touchable=(event.getX()>(getWidth()-getTotalPaddingRight()))&&
                        (event.getX()<(getWidth()-getPaddingRight()));
                if(touchable){
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    //是否显示删除图标
    private void setClearIconVisible(boolean b) {
        Drawable right=b?mClearDrawable:null;
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1]
                ,right,getCompoundDrawables()[3]);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.isFocus=hasFocus;
        if(isFocus){
            setClearIconVisible(getText().length()>0);
        }else{
            setClearIconVisible(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if(isFocus) {
            setClearIconVisible(getText().length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
