package com.example.diplomclient.arch.bottomsheets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public final class BottomSheetViewModel extends ViewModel {

    private final MutableLiveData<Integer> stateLiveData = new MutableLiveData<>();
    @Nullable
    private Input input;

    public void onStateChanged(final int state) {
        stateLiveData.setValue(state);
    }

    @NonNull
    public LiveData<Integer> getState() {
        return stateLiveData;
    }

    public void reset(@NonNull final Input input) {
        if (input.equals(this.input)) {
            return;
        }
        this.input = input;
        final int state;
        if (input.expandFull) {
            state = BottomSheetBehavior.STATE_EXPANDED;
        } else {
            state = BottomSheetBehavior.STATE_COLLAPSED;
        }
        stateLiveData.setValue(state);

    }

    public static final class Input {

        private final int requestCode;
        private final boolean expandFull;

        public Input(final int requestCode,
                     final boolean expandFull) {

            this.requestCode = requestCode;
            this.expandFull = expandFull;
        }

        @Override
        @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Input input = (Input) o;

            if (requestCode != input.requestCode) {
                return false;
            }
            if (expandFull != input.expandFull) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = requestCode;
            result = 31 * result + (expandFull ? 1 : 0);
            return result;
        }

        @Override
        @NonNull
        public String toString() {
            return "Input{"
                + "requestCode=" + requestCode
                + ", expandFull=" + expandFull
                + '}';
        }

    }

}