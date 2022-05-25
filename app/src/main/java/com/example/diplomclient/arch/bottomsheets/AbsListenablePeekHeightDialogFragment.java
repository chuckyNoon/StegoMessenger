package com.example.diplomclient.arch.bottomsheets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.diplomclient.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class AbsListenablePeekHeightDialogFragment extends AbsArchBottomSheetDialogFragment {

    @Nullable
    private OnPeekHeightChangedListener onPeekHeightChangedListener;
    private boolean updateState = true;

    public AbsListenablePeekHeightDialogFragment() {
        super();
    }

    @ContentView
    public AbsListenablePeekHeightDialogFragment(@LayoutRes final int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.NoBgBottomSheetTheme);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Dialog dialog = requireDialog();
        final BottomSheetViewModel viewModel = new ViewModelProvider(this).get(BottomSheetViewModel.class);
        viewModel.reset(new BottomSheetViewModel.Input(getRequestCode(), isExpandFull()));
        dialog.setOnShowListener((final DialogInterface dialogInterface) -> {
            final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
            final View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheetInternal == null) {
                return;
            }
            final BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheetInternal);
            final float peekHeightPercent = getPeekHeightPercent();
            if (peekHeightPercent == BottomSheetBehavior.PEEK_HEIGHT_AUTO) {
                behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
                return;
            }

            final float peekHeightPercentConstrained = Math.max(0f, Math.min(peekHeightPercent, 1f));
            final int bottomSheetInternalHeight = bottomSheetInternal.getHeight();
            final int peekHeightCollapsed = (int) (bottomSheetInternalHeight * peekHeightPercentConstrained);
            final int abovePeekHeight = bottomSheetInternalHeight - peekHeightCollapsed;
            behavior.setPeekHeight(peekHeightCollapsed);
            behavior.setSkipCollapsed(true);
            if (onPeekHeightChangedListener != null) {
                onPeekHeightChangedListener.onPeekHeightChanged(bottomSheetInternalHeight, peekHeightCollapsed);
            }

            final LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
            viewModel.getState().observe(viewLifecycleOwner, (@Nullable final Integer state) -> {
                if (state == null) {
                    return;
                }
                updateState = false;
                behavior.setState(state);
                if (onPeekHeightChangedListener != null) {
                    if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                        onPeekHeightChangedListener.onPeekHeightChanged(bottomSheetInternalHeight, peekHeightCollapsed);
                    } else if (state == BottomSheetBehavior.STATE_EXPANDED) {
                        onPeekHeightChangedListener.onPeekHeightChanged(bottomSheetInternalHeight, bottomSheetInternalHeight);
                    }
                }
                updateState = true;
            });

            behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull final View view, final int state) {
                    if (updateState) {
                        viewModel.onStateChanged(state);
                    }
                    if (state == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull final View view, final float slideOffset) {
                    final int peekHeight;
                    if (slideOffset >= 0) {
                        peekHeight = (int) (slideOffset * abovePeekHeight + peekHeightCollapsed);
                    } else {
                        peekHeight = (int) ((1 + slideOffset) * peekHeightCollapsed);
                    }
                    if (onPeekHeightChangedListener != null) {
                        onPeekHeightChangedListener.onPeekHeightChanged(bottomSheetInternalHeight, peekHeight);
                    }
                }
            });

        });
        view.setOnClickListener(v -> dismiss());
    }

    /**
     * Can be overriden to return value in range {0.0f, 1.0f}.
     * So that percent of all content will be showed by default
     */
    protected abstract float getPeekHeightPercent();

    /**
     * Set all content will be showed fullscreen or on predefined PeekHeight when dialog will appear.
     */
    protected abstract boolean isExpandFull();

    public void setOnPeekHeightChangedListener(@Nullable final OnPeekHeightChangedListener onPeekHeightChangedListener) {
        this.onPeekHeightChangedListener = onPeekHeightChangedListener;
    }

    @Nullable
    protected abstract int getRequestCode();

    public interface OnPeekHeightChangedListener {

        void onPeekHeightChanged(int bottomSheetInternalHeight, int peekHeight);

    }

}