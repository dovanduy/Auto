package com.stardust.scriptdroid.ui.main.drawer;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stardust.scriptdroid.R;
import com.stardust.scriptdroid.ui.widget.BindableViewHolder;
import com.stardust.scriptdroid.ui.widget.PrefSwitch;
import com.stardust.scriptdroid.ui.widget.SwitchCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Stardust on 2017/12/10.
 */

public class DrawerMenuItemViewHolder extends BindableViewHolder<DrawerMenuItem> {

    private static final long CLICK_TIMEOUT = 1000;
    @BindView(R.id.sw)
    PrefSwitch mSwitchCompat;

    @BindView(R.id.progress_bar)
    MaterialProgressBar mProgressBar;

    @BindView(R.id.icon)
    ImageView mIcon;

    @BindView(R.id.title)
    TextView mTitle;
    private boolean mAntiShake;
    private long mLastClickMillis;
    private DrawerMenuItem mDrawerMenuItem;

    public DrawerMenuItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> onClick());
        itemView.setOnClickListener(v -> {
            if (mSwitchCompat.getVisibility() == VISIBLE) {
                mSwitchCompat.toggle();
            } else {
                onClick();
            }
        });
    }

    @Override
    public void bind(DrawerMenuItem item, int position) {
        mDrawerMenuItem = item;
        mIcon.setImageResource(item.getIcon());
        mTitle.setText(item.getTitle());
        mAntiShake = item.antiShake();
        setSwitch(item);
        setProgress(item.isProgress());
    }

    private void setSwitch(DrawerMenuItem item) {
        if (!item.isSwitchEnabled()) {
            mSwitchCompat.setVisibility(GONE);
            return;
        }
        mSwitchCompat.setVisibility(VISIBLE);
        int prefKey = item.getPrefKey();
        if (prefKey != 0) {
            mSwitchCompat.setPrefKey(itemView.getResources().getString(prefKey));
        }
        mSwitchCompat.setChecked(item.isChecked());
    }

    private void onClick() {
        if (mAntiShake && (System.currentTimeMillis() - mLastClickMillis < CLICK_TIMEOUT)) {
            Toast.makeText(itemView.getContext(), R.string.text_click_too_frequently, Toast.LENGTH_SHORT).show();
            mSwitchCompat.setChecked(!mSwitchCompat.isChecked(), false);
            return;
        }
        mLastClickMillis = System.currentTimeMillis();
        if (mDrawerMenuItem != null) {
            mDrawerMenuItem.performAction(this);
        }
    }

    private void setProgress(boolean onProgress) {
        mProgressBar.setVisibility(onProgress ? VISIBLE : GONE);
        mIcon.setVisibility(onProgress ? GONE : VISIBLE);
        mSwitchCompat.setEnabled(!onProgress);
        itemView.setEnabled(!onProgress);
    }

    public SwitchCompat getSwitchCompat() {
        return mSwitchCompat;
    }

}
