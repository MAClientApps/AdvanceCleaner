package com.lakshita.suman.advancecleaner.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.screen.CallBackListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogQuestion extends DialogFragment {

    private CallBackListener<Boolean> callBackListener;

    public static DialogQuestion getInstance(CallBackListener<Boolean> callBackListener) {
        DialogQuestion dialogQuestion = new DialogQuestion();
        dialogQuestion.callBackListener = callBackListener;
        return dialogQuestion;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        String t = getClass().getSimpleName();
        if (manager.findFragmentByTag(t) == null) {
            super.show(manager, t);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.7f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
//            windowParams.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(windowParams);
//            dialog.setCanceledOnTouchOutside(false);
//            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_question, null);
        ButterKnife.bind(this, mView);
        dialogBuilder.setView(mView);
        return dialogBuilder.create();
    }

    @OnClick({R.id.tv_no, R.id.tv_yes})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.tv_cancel:
                if (callBackListener != null)
                    callBackListener.onResult(false);
                break;
            case R.id.tv_yes:
                if (callBackListener != null)
                    callBackListener.onResult(true);
                break;
        }
        dismiss();
    }
}
