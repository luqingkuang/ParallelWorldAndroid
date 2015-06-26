package com.dxns.parallelworld.view.activity.commom;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dxns.parallelworld.R;
import com.dxns.parallelworld.core.rx.Action0WithWeakHost;
import com.dxns.parallelworld.util.DisplayAdapter;
import com.dxns.parallelworld.util.LoadingDialogUtils;
import com.dxns.parallelworld.view.fragment.LoadingDialogFragment;
import com.dxns.parallelworld.view.widget.CustomLoadingLayout;

import com.umeng.analytics.MobclickAgent;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;


/**
 * 注意：使用loadingMask时，必须确保activity的根为id==root的RelativeLayout
 * Created by Administrator on 13-10-1.
 */
public class ActionBarBaseActivity extends ActionBarActivity {
    //    private Handler mTextColorHandler = new Handler();
    protected Toolbar mToolbar;
    protected View mContentView;
    private Hashtable<View, CustomLoadingLayout> mLoadingLayoutPool = new Hashtable<>();
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayAdapter.hideStatusbar(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        toolBarReplaceActionBar();
    }

    public final
    @Nullable
    LoadingDialogFragment getLoadingDailog() {
        if (mLoadingDialogFragment == null) {
            LoadingDialogFragment ldf = LoadingDialogUtils.findByTag(getSupportFragmentManager(), getClass().getSimpleName());
            mLoadingDialogFragment = ldf;
        }
        return mLoadingDialogFragment;
    }

    public final
    @NonNull
    LoadingDialogFragment showLoadingDialog() {
        return showLoadingDialog("");
    }

    public final
    @NonNull
    LoadingDialogFragment showLoadingDialog(String title) {
        LoadingDialogFragment ldf = getLoadingDailog();
        if (ldf == null) {
            mLoadingDialogFragment = ldf = new LoadingDialogFragment();
        }
        if (LoadingDialogUtils.findByTag(getSupportFragmentManager(), getClass().getSimpleName()) == null) {
            LoadingDialogUtils.show(getSupportFragmentManager(), getClass().getSimpleName(), ldf);
        }
        ldf.getArguments().putString(LoadingDialogFragment.KEY_TITLE, title);
        ldf.getArguments().putBoolean(LoadingDialogFragment.KEY_SHOW_PROGRESS_VALUE, false);
        ldf.refresh();
        return ldf;
    }

    public final
    @Nullable
    LoadingDialogFragment setLoadingProgress(int progress) {
        if (getLoadingDailog() == null) {
            return null;
        } else {
            LoadingDialogFragment ldf = getLoadingDailog();
            ldf.setProgress(progress);
            return ldf;
        }
    }

    public final
    @Nullable
    LoadingDialogFragment setLoadingMax(int max) {
        if (getLoadingDailog() == null) {
            return null;
        } else {
            LoadingDialogFragment ldf = getLoadingDailog();
            ldf.setMax(max);
            return ldf;
        }
    }

    public final void hideLoadingDialog() {
        if (mLoadingDialogFragment == null) {
            return;
        }

        //如果Fragment还没有附加上，则延迟500ms处理
        if (!mLoadingDialogFragment.isAdded()) {
            AndroidSchedulers.mainThread()
                    .createWorker()
                    .schedule(new HideAction(this), 100, TimeUnit.MILLISECONDS);
        } else {
            mLoadingDialogFragment = null;
            LoadingDialogUtils.hide(getSupportFragmentManager(), getClass().getSimpleName());
        }
    }

    /**
     * 在指定View上显示Loading蒙版层
     *
     * @param view
     */
    public final synchronized CustomLoadingLayout showLoadingMaskOn(View view, boolean hideTargetView) {
        boolean hideFlag = hideTargetView;
        if (mLoadingLayoutPool.get(view) != null) {
            mLoadingLayoutPool.get(view).setIsHideTargetViewWhenLoading(hideFlag);
            mLoadingLayoutPool.get(view).showLoading();
        } else {
            CustomLoadingLayout loadingLayout = CustomLoadingLayout.wrap(view);
            mLoadingLayoutPool.put(view, loadingLayout);
            mLoadingLayoutPool.get(view).setIsHideTargetViewWhenLoading(hideFlag);
            loadingLayout.showLoading();
        }
        return mLoadingLayoutPool.get(view);
    }

    /**
     * 获取在指定View上的Loading蒙版层；如果没有蒙版层，则为null
     *
     * @param view
     */
    public final synchronized CustomLoadingLayout getLoadingMaskOn(View view) {
        if (mLoadingLayoutPool.get(view) != null) {
            return mLoadingLayoutPool.get(view);
        }
        return null;
    }

    /**
     * 关闭在指定View上的Loading蒙版层；如果没有蒙版层，则不处理
     *
     * @param view
     */
    public final synchronized void hideLoadingMaskOn(View view) {
        if (mLoadingLayoutPool.get(view) != null) {
            mLoadingLayoutPool.get(view).hideLoading();
        }
    }

    /**
     * 关闭本窗口View上的所有Loading蒙版层
     */
    public final synchronized void hideAllLoadingMask() {
        View[] views = new View[mLoadingLayoutPool.size()];
        mLoadingLayoutPool.keySet().toArray(views);
        for (View view : views) {
            mLoadingLayoutPool.get(view).hideLoading();
        }
    }

    private void toolBarReplaceActionBar() {
        mContentView = findViewById(android.R.id.content);
        ViewGroup contentViewParent = (ViewGroup) mContentView.getParent();
        if (mContentView instanceof ViewGroup) {
            if (getWindow().hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY)) {
                if (mContentView instanceof FrameLayout) {
                    mToolbar = (Toolbar) getLayoutInflater().inflate(R.layout.common_toolbar, (ViewGroup) mContentView, false);
                    ((FrameLayout) mContentView).addView(mToolbar);
                } else {
                    View oldContentView = findViewById(android.R.id.content);
                    contentViewParent.removeView(mContentView);

                    mContentView = new FrameLayout(this);
                    mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    mToolbar = (Toolbar) getLayoutInflater().inflate(R.layout.common_toolbar, (ViewGroup) mContentView, false);
                    contentViewParent.addView(mContentView);
                    ((FrameLayout) mContentView).addView(oldContentView);
                }
            } else {
                View oldContentView = findViewById(android.R.id.content);
                contentViewParent.removeView(mContentView);
                mContentView = new LinearLayout(this);
                ((LinearLayout) mContentView).setOrientation(LinearLayout.VERTICAL);
                mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                contentViewParent.addView(mContentView);
                mToolbar = (Toolbar) getLayoutInflater().inflate(R.layout.common_toolbar, (ViewGroup) mContentView, false);
                addPlaceHolderView((LinearLayout) mContentView);
                ((LinearLayout) mContentView).addView(mToolbar);
                ((LinearLayout) mContentView).addView(oldContentView);
            }

        }
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.btn_return_white_selected);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /** 隐藏菜单栏 add by reason*/
        if (null != menu) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        setContentView(R.layout.empty_layout);
        //关闭受托管的LoadingDialog
        hideLoadingDialog();
        hideAllLoadingMask();

        super.onDestroy();
    }

    private static class HideAction extends Action0WithWeakHost<ActionBarBaseActivity> {

        public HideAction(ActionBarBaseActivity host) {
            super(host);
        }

        @Override
        public void call() {
            if (isHostExist()) {
                getHost().mLoadingDialogFragment = null;
                LoadingDialogUtils.hide(getHost().getSupportFragmentManager(), getClass().getSimpleName());
                getHost().hideLoadingDialog();
            }
        }
    }


    View statusBarView;

    public void addPlaceHolderView(ViewGroup background) {
        //4.4版本及以上可用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, DisplayAdapter.getStatusBarHeight(this));
            statusBarView = new View(this);
            statusBarView.setBackgroundResource(R.color.bar_color);
            statusBarView.setLayoutParams(lParams);
            background.addView(statusBarView);
        }
    }

    public void removePlaceHolderView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mContentView instanceof LinearLayout) {
                if (statusBarView.getParent() == mContentView) {
                    ((LinearLayout) mContentView).removeView(statusBarView);
                }
            }
        }
    }

    public void addPlaceHolderView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mContentView instanceof LinearLayout) {
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, DisplayAdapter.getStatusBarHeight(this));
                statusBarView = new View(this);
                statusBarView.setBackgroundResource(R.color.bar_color);
                statusBarView.setLayoutParams(lParams);
                ((LinearLayout) mContentView).addView(statusBarView, 0);
            }
        }
    }
    public void setStatusBarColor(int statusBarColor) {
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(statusBarColor);
        }
    }

    public View getStatusBarView() {
        return statusBarView;
    }
}
