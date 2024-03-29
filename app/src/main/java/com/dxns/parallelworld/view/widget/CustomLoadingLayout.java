package com.dxns.parallelworld.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.dxns.parallelworld.R;
import com.lsjwzh.loadingeverywhere.LoadingLayout;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * 自定义的Loading Layout;用于替换掉LoadingMask
 *
 * @author Green
 * @since: 15/2/9 上午1:19
 */
public class CustomLoadingLayout extends LoadingLayout {
    CircleProgressBar mProgressBar;


    public CustomLoadingLayout(Context context) {
        super(context);
    }

    public CustomLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * create a LoadingLayout to wrap and replace the targetView.
     * Note: if you attachTo targetView on 'onCreate' method,targetView may be not layout complete.
     *
     * @param targetView
     * @return
     */
    public static CustomLoadingLayout wrap(final View targetView) {
        if (targetView == null) {
            throw new IllegalArgumentException();
        }

        final CustomLoadingLayout loadingLayout = new CustomLoadingLayout(targetView.getContext());
        loadingLayout.attachTo(targetView);
        return loadingLayout;
    }

    @Override
    protected View createOverlayView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.simple_loading_dialog, null, false);
        mProgressBar = (CircleProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_blue_bright);
        return view;
    }

    public int getMax() {
        if (mProgressBar == null) {
            return -1;
        }
        return mProgressBar.getMax();
    }

    public void setMax(int max) {
        if (mProgressBar == null) {
            return;
        }
        mProgressBar.setMax(max);
    }

    public int getProgress() {
        if (mProgressBar == null) {
            return -1;
        }
        return mProgressBar.getProgress();
    }

    public void setProgress(int progress) {
        if (mProgressBar == null) {
            return;
        }
        if (getMax() > 0) {
            mProgressBar.setProgress(progress);
        }
    }

}
