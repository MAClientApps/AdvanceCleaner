package com.lakshita.suman.advancecleaner.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;


public abstract class BaseDialogFragment<B extends ViewBinding> extends DialogFragment {
    protected B binding;

    protected abstract void initView();

    protected abstract void initData();

    protected abstract B getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getDialog().getWindow();
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout(((getWidth(getContext()) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
        windowParams.dimAmount = 0.7f;
        window.setAttributes(windowParams);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        initData();
    }

    public int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    @Override
    public void onDestroy() {
        hideSoftKeyboard(requireActivity());
        super.onDestroy();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, this.getClass().getName());
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }

    }

}
