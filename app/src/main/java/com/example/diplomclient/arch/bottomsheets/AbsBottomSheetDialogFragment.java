package com.example.diplomclient.arch.bottomsheets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.diplomclient.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.ref.WeakReference;

public abstract class AbsBottomSheetDialogFragment extends AbsLifecycleBottomSheetDialogFragment {

    public static final String USE_SHOW_AFTER_KEYBOARD_HIDES_MESSAGE = ""
        + "Method show() is not recommended for "
        + "AbsBottomSheetDialogFragment. Use showAfterKeyboardHides() instead";
    private boolean updateState = true;

    @LayoutRes
    private int contentLayoutId;

    public AbsBottomSheetDialogFragment() {}

    @ContentView
    public AbsBottomSheetDialogFragment(@LayoutRes final int contentLayoutId) {
        this();
        this.contentLayoutId = contentLayoutId;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.NoBgBottomSheetTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        if (contentLayoutId != 0) {
            return inflater.inflate(contentLayoutId, container, false);
        }
        return null;
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
            behavior.setPeekHeight(getPeekHeight());
            if (isExpandFull()) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            final LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
            viewModel.getState().observe(viewLifecycleOwner, (@Nullable final Integer state) -> {
                if (state == null) {
                    return;
                }
                if (state == BottomSheetBehavior.STATE_COLLAPSED
                    || state == BottomSheetBehavior.STATE_EXPANDED
                    || state == BottomSheetBehavior.STATE_HALF_EXPANDED
                    || behavior.isHideable() && state == BottomSheetBehavior.STATE_HIDDEN) {
                    updateState = false;
                    behavior.setState(state);
                    updateState = true;
                }
            });

            behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull final View view, final int state) {
                    if (updateState
                        && (state == BottomSheetBehavior.STATE_COLLAPSED
                                || state == BottomSheetBehavior.STATE_EXPANDED
                                || state == BottomSheetBehavior.STATE_HALF_EXPANDED
                                || behavior.isHideable() && state == BottomSheetBehavior.STATE_HIDDEN)) {
                        viewModel.onStateChanged(state);
                    }

                    if (state == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull final View view, final float v) {
                }
            });

        });

        final FragmentManager parentFragmentManager = getParentFragmentManager();
        view.setOnClickListener(v -> {
            dismiss();
        });
    }

    protected final void expandBottomSheet() {
        final Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
        final View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetInternal == null) {
            return;
        }
        final BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheetInternal);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    protected abstract int getRequestCode();

    /**
     * Set all content will be showed fullscreen or on auto PeekHeight when dialog will appear.
     */
    protected abstract boolean isExpandFull();

    public void showAfterKeyboardHides(@NonNull final FragmentManager fragmentManager,
                                       @Nullable final String tag,
                                       @Nullable final Activity activity) {

        if (activity == null) {
            super.show(fragmentManager, tag);
            return;
        }

        final View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView == null) {
            super.show(fragmentManager, tag);
            return;
        }

        final IBinder windowToken = currentFocusedView.getWindowToken();
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            super.show(fragmentManager, tag);
            return;
        }

        final boolean hide = inputMethodManager.hideSoftInputFromWindow(
            windowToken,
            0,
            new WeakResultReceiver(currentFocusedView.getHandler(), this, fragmentManager, tag)
        );
        if (!hide) {
            super.show(fragmentManager, tag);
        }
    }

    private void showInternal(@NonNull final FragmentManager fragmentManager, @Nullable final String tag) {
        super.show(fragmentManager, tag);
    }


    @Override
    public int show(@NonNull final FragmentTransaction transaction, @Nullable final String tag) {
        throw new UnsupportedOperationException(USE_SHOW_AFTER_KEYBOARD_HIDES_MESSAGE);
    }

    @Override
    public void showNow(@NonNull final FragmentManager manager, @Nullable final String tag) {
        throw new UnsupportedOperationException(USE_SHOW_AFTER_KEYBOARD_HIDES_MESSAGE);
    }

    /*
    There was a problem that the dialog remembered the initial height and did not change it,
    even when some fields were hidden (an empty space appears under the dialog) or shown (the dialog is cut off from the bottom).
    This flag fixes the problem, but for now we are testing only on the add vaccine dialog.
     */
    protected int getPeekHeight() {
        return BottomSheetBehavior.PEEK_HEIGHT_AUTO;
    }

    private static final class WeakResultReceiver extends ResultReceiver {

        @NonNull
        private final WeakReference<AbsBottomSheetDialogFragment> fragmentWeakReference;
        @NonNull
        private final WeakReference<FragmentManager> fragmentManagerWeakReference;
        @Nullable
        private final String tag;

        private WeakResultReceiver(@Nullable final Handler handler,
                                   @NonNull final AbsBottomSheetDialogFragment fragment,
                                   @NonNull final FragmentManager fragmentManager,
                                   @Nullable final String tag) {
            super(handler);
            this.fragmentWeakReference = new WeakReference<>(fragment);
            this.fragmentManagerWeakReference = new WeakReference<>(fragmentManager);
            this.tag = tag;
        }

        @Override
        protected void onReceiveResult(final int resultCode, final Bundle resultData) {
            final AbsBottomSheetDialogFragment fragment = fragmentWeakReference.get();
            if (fragment == null) {
                return;
            }
            final FragmentManager fragmentManager = fragmentManagerWeakReference.get();
            if (fragmentManager == null) {
                return;
            }
            fragment.showInternal(fragmentManager, tag);
        }

    }

}