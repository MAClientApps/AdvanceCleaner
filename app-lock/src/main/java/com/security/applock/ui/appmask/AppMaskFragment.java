package com.security.applock.ui.appmask;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.security.applock.R;
import com.security.applock.databinding.FragmentAppMaskBinding;
import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;
import com.security.applock.ui.BaseFragment;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.SystemUtil;

import java.util.Arrays;

public class AppMaskFragment extends BaseFragment<FragmentAppMaskBinding> {
    private AppMaskAdapter appMaskAdapter;

    private int idIconCurrent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView() {
        idIconCurrent = PreferencesHelper.getInt(PreferencesHelper.SETTING_APP_MASK, 0);
        appMaskAdapter = new AppMaskAdapter(getContext(),
                Arrays.asList(Config.lstIconApp), idIconCurrent);
        binding.rcvAppMask.setAdapter(appMaskAdapter);
    }

    @Override
    protected void initControl() {
        binding.tvSave.setOnClickListener(v -> applyMask());
    }

    private void applyMask() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.changed_icon)
                .setMessage(R.string.changed_icon_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    PreferencesHelper.putInt(PreferencesHelper.SETTING_APP_MASK, appMaskAdapter.getItemSelected());
                    if (BackgroundManager.getInstance(getActivity()).isServiceRunning(AntiTheftService.class)) {
                        Intent intent = new Intent(getActivity(), AntiTheftService.class);
                        intent.setAction(Config.ActionIntent.ACTION_UPDATE_APP_MASK);
                        getActivity().startService(intent);
                    }
                    SystemUtil.replaceIconHome(getActivity(), Config.lstIconApp[idIconCurrent].getClassName()
                            , Config.lstIconApp[appMaskAdapter.getItemSelected()].getClassName());
                }).show();
    }

    @Override
    protected FragmentAppMaskBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAppMaskBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.app_mask;
    }
}

