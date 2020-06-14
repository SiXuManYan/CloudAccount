package com.fatcloud.account.feature.matisse;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.fatcloud.account.R;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.ui.AlbumPreviewActivity;
import com.zhihu.matisse.internal.ui.PreviewItemFragment;
import com.zhihu.matisse.internal.ui.adapter.PreviewPagerAdapter;
import com.zhihu.matisse.internal.ui.widget.CheckView;

public class AllPreviewActivity extends AlbumPreviewActivity {


    private int selectMediaType;
    private int mediaType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ViewGroup parent = ((ViewGroup) findViewById(R.id.bottom_toolbar).getParent());
//        TextView tip = new TextView(this);
//        tip.setTextColor(getResources().getColor(R.color.white));
//        tip.setBackgroundResource(R.color.preview_bottom_toolbar_bg);
//        tip.setPadding(SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f), SizeUtils.dp2px(10f));
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.ABOVE, R.id.bottom_toolbar);
//        parent.addView(tip, layoutParams);
        mediaType = getIntent().getIntExtra(Matisse.MEDIA_TYPE, 0);
        mCheckView.setOnClickListener(v -> {
            Item item = mAdapter.getMediaItem(mPager.getCurrentItem());
            if (mSelectedCollection.isSelected(item)) {
                mSelectedCollection.remove(item);
                if (mSpec.countable) {
                    mCheckView.setCheckedNum(CheckView.UNCHECKED);
                } else {
                    mCheckView.setChecked(false);
                }
            } else {
//                if ((selectMediaType == Matisse.GIF || selectMediaType == Matisse.VIDEO) && !mSelectedCollection.isEmpty()) {
//                    mSelectedCollection.remove(mSelectedCollection.asList().get(0));
//                }

                if (assertAddSelection(item)) {
                    mSelectedCollection.add(item);
                    if (mSpec.countable) {
                        mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item));
                    } else {
                        mCheckView.setChecked(true);
                    }
                }
            }
            updateApplyButton();

            if (mSpec.onSelectedListener != null) {
                mSpec.onSelectedListener.onSelected(
                        mSelectedCollection.asListOfUri(), mSelectedCollection.asListOfString());
            }
            if (item.isGif() || item.isVideo()) {
                findViewById(R.id.button_apply).performClick();
            }

        });
        updateSelectMediaType();
        Item item = getIntent().getParcelableExtra(EXTRA_ITEM);
        updateCheckButton(item);

    }

    private void updateApplyButton() {
        int selectedCount = mSelectedCollection.count();
        if (selectedCount == 0) {
            mButtonApply.setText(R.string.button_sure_default);
            mButtonApply.setEnabled(false);
            mButtonBack.setVisibility(View.VISIBLE);
            selectMediaType = mediaType;
        } else if (selectedCount == 1 && mSpec.singleSelectionModeEnabled()) {
            mButtonApply.setText(R.string.button_sure_default);
            mButtonApply.setEnabled(true);
        } else {
            mButtonApply.setEnabled(true);
            mButtonApply.setText(getString(R.string.button_sure, selectedCount));
        }
        if (selectedCount == 1) {
            updateSelectMediaType();
        }

    }

    private void updateCheckButton(Item item) {
        switch (selectMediaType) {
            case Matisse.VIDEO:
                mCheckView.setVisibility(item.isVideo() ? View.VISIBLE : View.GONE);
                break;
            case Matisse.IMG:
                mCheckView.setVisibility(item.isImage() && !item.isGif() ? View.VISIBLE : View.GONE);
                break;
            case Matisse.GIF:
                mCheckView.setVisibility(item.isGif() ? View.VISIBLE : View.GONE);
                break;
            default:
                mCheckView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateSelectMediaType() {
        if (mediaType != 0) {
            selectMediaType = mediaType;
        } else if (!mSelectedCollection.isEmpty()) {
            Item item = mSelectedCollection.asList().get(0);
            if (item.isVideo()) {
                selectMediaType = Matisse.VIDEO;
            } else if (item.isGif()) {
                selectMediaType = Matisse.GIF;
            } else {
                selectMediaType = Matisse.IMG;
            }
        }
    }

    private boolean assertAddSelection(Item item) {
        IncapableCause cause = mSelectedCollection.isAcceptable(item);
        IncapableCause.handleCause(this, cause);
        return cause == null;
    }

    @Override
    public void onPageSelected(int position) {
        PreviewPagerAdapter adapter = (PreviewPagerAdapter) mPager.getAdapter();
        if (mPreviousPos != -1 && mPreviousPos != position) {
            ((PreviewItemFragment) adapter.instantiateItem(mPager, mPreviousPos)).resetView();

            Item item = adapter.getMediaItem(position);

            if (mSpec.countable) {
                int checkedNum = mSelectedCollection.checkedNumOf(item);
                mCheckView.setCheckedNum(checkedNum);
                if (checkedNum > 0) {
                    mCheckView.setEnabled(true);
                } else {
                    mCheckView.setEnabled(!mSelectedCollection.maxSelectableReached());
                }
            } else {
                boolean checked = mSelectedCollection.isSelected(item);
                mCheckView.setChecked(checked);
                if (checked) {
                    mCheckView.setEnabled(true);
                } else {
                    mCheckView.setEnabled(!mSelectedCollection.maxSelectableReached());
                }
            }

            updateCheckButton(item);
            updateSize(item);
        }
        mPreviousPos = position;
    }

    @Override
    public void onBackPressed() {
        if ((selectMediaType == Matisse.GIF || selectMediaType == Matisse.VIDEO) && !mSelectedCollection.isEmpty()) {
            mSelectedCollection.remove(mSelectedCollection.asList().get(0));
        }

        sendBackResult(false);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            onBackPressed();
        } else if (v.getId() == R.id.button_apply) {

            if (selectMediaType == Matisse.VIDEO && !mSelectedCollection.isEmpty()) {
                if (mSelectedCollection.asList().get(0).size > 100 * 1024 * 1024) {
                    ToastUtils.showShort("视频大于100M，请重新选取");
                    return;
                }
            }

            sendBackResult(true);
            finish();
        }
    }
}
