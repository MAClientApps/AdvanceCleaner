package com.lakshita.suman.advancecleaner;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.AdmobHelp;
import com.ads.control.AdsApplication;
import com.lakshita.suman.advancecleaner.screen.BaseActivity;
import com.lakshita.suman.advancecleaner.screen.main.MainActivity;
import com.lakshita.suman.advancecleaner.utils.LocaleManager;
import com.lakshita.suman.advancecleaner.utils.PreferenceUtils;
import com.lakshita.suman.advancecleaner.lock.activities.lock.GestureUnlockLockActivity;
import com.lakshita.suman.advancecleaner.lock.utils.SpUtil;
import com.sdk.perelander.Mob;
import com.sdk.perelander.MobConfig;
import com.security.applock.App;
import com.security.applock.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class CleanMasterApp extends AdsApplication {
    private static String mAuth = "436F70797269676874206279204C7562755465616D2E636F6D5F2B3834393731393737373937";

    private static List<BaseActivity> activityList;

    private static CleanMasterApp instance;

    public static CleanMasterApp getInstance() {
        return instance;
    }

    private synchronized static void setInstance(CleanMasterApp instance) {
        CleanMasterApp.instance = instance;
    }

    public MobConfig cleanMasterConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            setInstance(CleanMasterApp.this);
        App.getInstace().init(this);
        AdmobHelp.getInstance().init(this);
        PreferenceUtils.init(this);
        PreferencesHelper.init(this);
        if (PreferenceUtils.getTimeInstallApp() == 0)
            PreferenceUtils.setTimeInstallApp(System.currentTimeMillis());
        SpUtil.getInstance().init(instance);
        activityList = new ArrayList<>();
        initLanguage();

        cleanMasterConfig = new MobConfig(this, "hee3kwgpedj4");
        Mob.onCreate(cleanMasterConfig);
        registerActivityLifecycleCallbacks(new CleanMasterLifecycleCallbacks());
    }

    private void initLanguage() {
        if (TextUtils.isEmpty(PreferencesHelper.getString(PreferencesHelper.KEY_LANGUAGE))) {
            LocaleManager.getInstance(this).setPrefLanguage(LocaleManager.LANGUAGE_DEFAULT);
            String currentLanguage = getResources().getConfiguration().locale.getLanguage();
            for (int i = 0; i < LocaleManager.lstCodeLanguage.length; i++) {
                if (currentLanguage.equals(LocaleManager.lstCodeLanguage[i])) {
                    LocaleManager.getInstance(this).setPrefLanguage(currentLanguage);
                    LocaleManager.getInstance(this).setLocale();
                    return;
                }
            }
        }
    }

    private static final class CleanMasterLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Mob.onResume(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Mob.onPause();
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public BaseActivity getTopActivity() {
        if (activityList.isEmpty())
            return null;
        return activityList.get(activityList.size() - 1);
    }

    public void clearAllActivity() {
        for (BaseActivity activity : activityList) {
            if (activity != null && !(activity instanceof GestureUnlockLockActivity))
                activity.clear();
        }
        activityList.clear();
    }

    public List<BaseActivity> getActivityList() {
        return activityList;
    }

    public void clearAllActivityUnlessMain() {
        for (BaseActivity activity : activityList) {
            if (activity != null && !(activity instanceof MainActivity))
                activity.clear();
        }
        activityList.clear();
    }

}
