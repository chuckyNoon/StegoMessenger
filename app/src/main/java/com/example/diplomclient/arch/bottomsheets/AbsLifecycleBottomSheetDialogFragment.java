package com.example.diplomclient.arch.bottomsheets;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.aita.base.bottomsheets.AbsBackAwareBottomSheetDialogFragment;

public abstract class AbsLifecycleBottomSheetDialogFragment extends AbsBackAwareBottomSheetDialogFragment {

    private LifecycleOwner viewLifecycleOwner;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewLifecycleOwner = this::ensureRegistry;
    }

    @Override
    public void onStart() {
        super.onStart();
        final LifecycleRegistry registry = ensureRegistry();
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        ensureRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        ensureRegistry().handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        final LifecycleRegistry registry = ensureRegistry();
        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public final LifecycleOwner getViewLifecycleOwner() {
        if (viewLifecycleOwner == null) {
            throw new IllegalStateException("Can't access the DialogFragment View's LifecycleOwner when "
                                                + "getView() is null i.e., before onCreateView() or after onDestroyView()");
        }
        return viewLifecycleOwner;
    }

    @NonNull
    private LifecycleRegistry ensureRegistry() {
        if (lifecycleRegistry == null) {
            lifecycleRegistry = new LifecycleRegistry(viewLifecycleOwner);
        }
        return lifecycleRegistry;
    }
}