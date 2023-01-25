package com.lakshita.suman.advancecleaner.screen.main.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.AdmobHelp;
import com.ads.control.funtion.UtilsApp;
import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.dialog.DialogChangeLanguage;
import com.lakshita.suman.advancecleaner.screen.BaseActivity;
import com.lakshita.suman.advancecleaner.screen.BaseFragment;
import com.lakshita.suman.advancecleaner.screen.setting.SettingActivity;
import com.lakshita.suman.advancecleaner.service.ServiceManager;
import com.lakshita.suman.advancecleaner.utils.Config;
import com.lakshita.suman.advancecleaner.utils.PreferenceUtils;
import com.lakshita.suman.advancecleaner.utils.Toolbox;
import com.security.applock.utils.PreferencesHelper;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentPersional extends BaseFragment {

    @BindView(R.id.tv_header_persional)
    TextView tvPersionalHeader;

    public static FragmentPersional getInstance() {
        FragmentPersional mFragmentPersional = new FragmentPersional();
        Bundle mBundle = new Bundle();
        mFragmentPersional.setArguments(mBundle);
        return mFragmentPersional;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_persional_new, container, false);
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdmobHelp.getInstance().loadBannerFragment(getActivity(), view);
    }

    private void initData() {
        int day = (int) ((System.currentTimeMillis() - PreferenceUtils.getTimeInstallApp()) / (24 * 60 * 60 * 1000));
        tvPersionalHeader.setText(getString(R.string.has_protected_your_phone, getString(R.string.app_name), String.valueOf(day == 0 ? 1 : day)));
    }

    @OnClick({R.id.ll_settings, R.id.ll_feedback,R.id.ll_share, R.id.ll_upgrade, R.id.ll_language})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.ll_language:
                String selected = PreferencesHelper.getString(PreferencesHelper.KEY_LANGUAGE, Config.itemsLanguage[0].getValue());
                DialogChangeLanguage dialogLanguge = new DialogChangeLanguage(Arrays.asList(Config.itemsLanguage), selected, selected1 -> {
                    PreferencesHelper.putString(PreferencesHelper.KEY_LANGUAGE, selected1);
                    Toolbox.setLocale(getActivity(), selected1);
                    ((BaseActivity) getActivity()).setLanguage(selected1);
                    try {
                        ServiceManager.getInstance().updateNotification();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                dialogLanguge.show(getChildFragmentManager());
                break;
            case R.id.ll_settings:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.ll_feedback:
                UtilsApp.SendFeedBack(getActivity(), getString(R.string.email_feedback), getString(R.string.Title_email));
                break;
            case R.id.ll_share:
                UtilsApp.shareApp(getActivity());
                break;
            case R.id.ll_upgrade:
                UtilsApp.RateApp(getActivity());
                break;
        }
    }
}
