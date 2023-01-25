package com.lakshita.suman.advancecleaner.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.lakshita.suman.advancecleaner.CleanMasterApp;
import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.dialog.DialogAskPermission;
import com.lakshita.suman.advancecleaner.dialog.DialogQuestion;
import com.lakshita.suman.advancecleaner.screen.antivirus.AntivirusActivity;
import com.lakshita.suman.advancecleaner.screen.appManager.AppManagerActivity;
import com.lakshita.suman.advancecleaner.screen.cleanNotification.NotificationCleanActivity;
import com.lakshita.suman.advancecleaner.screen.cleanNotification.NotificationCleanGuildActivity;
import com.lakshita.suman.advancecleaner.screen.cleanNotification.NotificationCleanSettingActivity;
import com.lakshita.suman.advancecleaner.screen.gameboost.GameBoostActivity;
import com.lakshita.suman.advancecleaner.screen.listAppSelect.AppSelectActivity;
import com.lakshita.suman.advancecleaner.screen.junkfile.JunkFileActivity;
import com.lakshita.suman.advancecleaner.screen.phoneboost.PhoneBoostActivity;
import com.lakshita.suman.advancecleaner.screen.result.ResultAcitvity;
import com.lakshita.suman.advancecleaner.screen.guildPermission.GuildPermissionActivity;
import com.lakshita.suman.advancecleaner.screen.smartCharger.SmartChargerActivity;
import com.lakshita.suman.advancecleaner.service.NotificationListener;
import com.lakshita.suman.advancecleaner.utils.Config;
import com.lakshita.suman.advancecleaner.utils.PermissionUtils;
import com.lakshita.suman.advancecleaner.utils.PreferenceUtils;
import com.lakshita.suman.advancecleaner.utils.SystemUtil;
import com.lakshita.suman.advancecleaner.utils.Toolbox;
import com.security.applock.ui.splash.SplashActivity;
import com.testapp.duplicatefileremover.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BaseActivity extends LocalizationActivity {

    private ImageView imBackToolbar;
    private View loPanel;
    private List<Callable<Void>> callables = new ArrayList<>();
    public BetterActivityResult<Intent, ActivityResult> activityLauncher =
            BetterActivityResult.registerActivityForResult(this);

    private CallBackListener<Boolean> listenerPermissionStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CleanMasterApp.getInstance().doForCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanMasterApp.getInstance().doForFinish(this);
    }

    public final void clear() {
        super.finish();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        imBackToolbar = findViewById(R.id.im_back_toolbar);
        loPanel = findViewById(R.id.layout_padding);
        if (loPanel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Toolbox.getHeightStatusBar(this) > 0) {
                loPanel.setPadding(0, Toolbox.getHeightStatusBar(this), 0, 0);
            }
            Toolbox.setStatusBarHomeTransparent(this);
        }
        initControl();
    }

    public void exitDialog(CallBackListener<Boolean> callBackListener) {
        DialogQuestion.getInstance(callBackListener).show(getSupportFragmentManager(), "");
    }

    private void initControl() {
        if (imBackToolbar != null)
            imBackToolbar.setOnClickListener(v -> onBackPressed());
    }

    public void openScreenFunction(Config.FUNCTION mFunction) {
        switch (mFunction) {
            case JUNK_FILES:
                try {
                    askPermissionUsageSetting(() -> {
                        requestPermissionStorage(data -> {
                            if (data) {
                                JunkFileActivity.startActivityWithData(BaseActivity.this);
                            }
                        });
                        return null;
                    });
                } catch (Exception e) {

                }
                break;
            case CPU_COOLER:
            case PHONE_BOOST:
            case POWER_SAVING:
                try {
                    askPermissionUsageSetting(() -> {
                        if (PreferenceUtils.checkLastTimeUseFunction(mFunction))
                            PhoneBoostActivity.startActivityWithData(BaseActivity.this, mFunction);
                        else
                            openScreenResult(mFunction);
                        return null;
                    });
                } catch (Exception e) {

                }
                break;
            case ANTIVIRUS:
                try {
                    requestPermissionStorage(data -> {
                        if (data) {
                            startActivity(new Intent(this, AntivirusActivity.class));
                        }
                    });
                } catch (Exception e) {

                }
                break;


            case APP_LOCK:
                try {
                    checkdrawPermission(() -> {
                        askPermissionUsageSetting(() -> {
                            startActivity(new Intent(BaseActivity.this, SplashActivity.class));
                            return null;
                        });
                        return null;
                    });
                } catch (Exception e) {

                }

                break;
            case SMART_CHARGE:
                if (!SystemUtil.checkCanWriteSettings(this)) {
                    try {
                        askPermissionUsageSetting(() -> {
                            askPermissionWriteSetting(() -> {
                                startActivity(new Intent(BaseActivity.this, SmartChargerActivity.class));
                                return null;
                            });
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    startActivity(new Intent(this, SmartChargerActivity.class));
                }
                break;
//            case MESSAGE_SECURITY:
//                break;
//            case SMART_CLEANUP:
//                break;
            case DEEP_CLEAN:
                requestPermissionStorage(data -> {
                    if (data) {
                        startActivity(new Intent(this, MainActivity.class));
                    }
                });
                break;
            case APP_UNINSTALL:
                startActivity(new Intent(this, AppManagerActivity.class));
                break;
            case GAME_BOOSTER_MAIN:
            case GAME_BOOSTER:
                startActivity(new Intent(this, GameBoostActivity.class));
                break;
            case NOTIFICATION_MANAGER:
                if (PreferenceUtils.isFirstUsedFunction(mFunction)) {
                    startActivity(new Intent(this, NotificationCleanGuildActivity.class));
                } else {
                    try {
                        askPermissionNotificaitonSetting(() -> {
                            if (NotificationListener.getInstance() != null) {
                                if (NotificationListener.getInstance().getLstSave().isEmpty()) {
                                    startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                                } else {
                                    startActivity(new Intent(BaseActivity.this, NotificationCleanActivity.class));
                                }
                            } else {
                                startActivity(new Intent(BaseActivity.this, NotificationCleanSettingActivity.class));
                            }
                            return null;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
//            case HARASSMENT_FILLTER:
//                break;
//            case SPEEND_PROTECTOR:
//                break;
//            case PAYMENT_SAFE:
//                break;
//            case FILE_MOVE:
//                break;
        }
    }

    public void openIgnoreScreen() {
        AppSelectActivity.openSelectAppScreen(this, AppSelectActivity.TYPE_SCREEN.IGNORE);
    }

    public void openWhileListVirusSceen() {
        AppSelectActivity.openSelectAppScreen(this, AppSelectActivity.TYPE_SCREEN.WHILE_LIST_VIRUS);
    }

    public void openScreenResult(Config.FUNCTION mFunction) {
        Intent mIntent = new Intent(BaseActivity.this, ResultAcitvity.class);
        if (mFunction != null)
            mIntent.putExtra(Config.DATA_OPEN_RESULT, mFunction.id);
        startActivity(mIntent);


    }

    public void askPermissionStorage(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                DialogAskPermission.getInstance(Manifest.permission.READ_EXTERNAL_STORAGE, () -> {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.MY_PERMISSIONS_REQUEST_STORAGE);
                }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
            } else {
                callable.call();
            }
        } else {
            callable.call();
        }
    }

    protected void requestPermissionStorage(CallBackListener<Boolean> listener) {
        this.listenerPermissionStorage = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent;
                try {
                    intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                    activityLauncher.launch(intent, result -> {
                        if (Environment.isExternalStorageManager()) {
                            listener.onResult(true);
                        } else {
                            listener.onResult(false);
                        }
                    });
                } catch (Exception ex) {
                    intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    activityLauncher.launch(intent, result -> {
                            if (Environment.isExternalStorageManager()) {
                            listener.onResult(true);
                        } else {
                            listener.onResult(false);
                        }
                    });
                }
            } else {
                listener.onResult(true);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !PermissionUtils.checkPermissonAccept(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PermissionUtils.requestRuntimePermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                listener.onResult(true);
            }
        }
    }

    public void askPermissionUsageSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && !SystemUtil.isUsageAccessAllowed(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_USAGE_ACCESS_SETTINGS, () -> {
                try {
                    startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), Config.PERMISSIONS_USAGE);
                    GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_USAGE_ACCESS_SETTINGS);
                } catch (Exception e) {

                }
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void askPermissionWriteSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_MANAGE_WRITE_SETTINGS, () -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, Config.PERMISSIONS_WRITE_SETTINGS);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_MANAGE_WRITE_SETTINGS);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void askPermissionNotificaitonSetting(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) && !SystemUtil.isNotificationListenerEnabled(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS, () -> {
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), Config.PERMISSIONS_NOTIFICATION_LISTENER);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void checkdrawPermission(Callable<Void> callable) throws Exception {
        this.callables.clear();
        this.callables.add(callable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            DialogAskPermission.getInstance(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, () -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, Config.PERMISSIONS_DRAW_APPICATION);
                GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
        } else {
            callable.call();
        }
    }

    public void requestDetectHome() {
        DialogAskPermission.getInstance(Settings.ACTION_ACCESSIBILITY_SETTINGS, () -> {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            GuildPermissionActivity.openActivityGuildPermission(this, Settings.ACTION_ACCESSIBILITY_SETTINGS);
        }).show(getSupportFragmentManager(), DialogAskPermission.class.getName());
    }

    public void openSettingApplication(String packageName) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.PERMISSIONS_DRAW_APPICATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this))
                    callListener();
                break;
            case Config.PERMISSIONS_USAGE:
                if (SystemUtil.isUsageAccessAllowed(this)) {
                    callListener();
                }
                break;
            case Config.PERMISSIONS_NOTIFICATION_LISTENER:
                if (SystemUtil.isNotificationListenerEnabled(this))
                    callListener();
                break;
            case Config.PERMISSIONS_WRITE_SETTINGS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this))
                    callListener();
                break;
        }
    }

    public void callListener() {
        for (Callable callable : callables) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Config.MY_PERMISSIONS_REQUEST_CLEAN_CACHE:
            case Config.MY_PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (Callable callable : callables) {
                        try {
                            callable.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    callables.clear();
                }
                break;
            case PermissionUtils.MY_PERMISSIONS_REQUEST:
                if (PermissionUtils.checkPermissonAccept(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                ) {
                    listenerPermissionStorage.onResult(true);
                } else {
                    listenerPermissionStorage.onResult(false);
                }
                break;
        }
    }

    public void addFragmentWithTag(Fragment targetFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left, R.anim.pull_in_left, R.anim.push_out_right);
        if (targetFragment != null) {
            ft.addToBackStack(null);
            ft.add(R.id.layout_fragment, targetFragment, tag).commitAllowingStateLoss();
        }
    }

}
