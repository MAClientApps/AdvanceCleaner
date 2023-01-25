package com.lakshita.suman.advancecleaner.lock.activities.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.utils.PreferenceUtils;
import com.lakshita.suman.advancecleaner.utils.Toolbox;
import com.lakshita.suman.advancecleaner.lock.activities.lock.GestureCreateLockActivity;
import com.lakshita.suman.advancecleaner.lock.activities.main.MainLockActivity;
import com.lakshita.suman.advancecleaner.lock.base.AppConstants;
import com.lakshita.suman.advancecleaner.lock.base.BaseLockActivity;
import com.lakshita.suman.advancecleaner.lock.services.BackgroundManager;
import com.lakshita.suman.advancecleaner.lock.services.LockService;
import com.lakshita.suman.advancecleaner.lock.utils.SpUtil;

import java.io.Serializable;
import java.util.Locale;

public class SecuritySettingActivity extends BaseLockActivity {

    private static final String DATA_TYPE_OPEN = "data type open";
    private ImageView imDone, imBack;
    private TextView tvQuestion;
    private EditText edtAnswer;
    private RelativeLayout llQuestion;
    private int idQuestion = R.id.password_question_01;
    private TYPE_OPEN typeOpen;

    public enum TYPE_OPEN implements Serializable {
        SET_PASS, FORGOT_PASS, FIRST_SETUP
    }

    public static void openSettingSecurytiScreen(Context mContext, TYPE_OPEN mTypeOpen) {
        if (mTypeOpen == TYPE_OPEN.FORGOT_PASS && PreferenceUtils.getAnswerSecutiryQuestion().isEmpty()) {
            Toast.makeText(mContext, mContext.getString(R.string.not_setup_answer_question), Toast.LENGTH_LONG).show();
            return;
        }
        Intent mIntent = new Intent(mContext, SecuritySettingActivity.class);
        mIntent.putExtra(DATA_TYPE_OPEN, mTypeOpen);
        mContext.startActivity(mIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock_security_settings;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        imDone = findViewById(R.id.im_done);
        tvQuestion = findViewById(R.id.tv_question);
        edtAnswer = findViewById(R.id.edt_answer);
        llQuestion = findViewById(R.id.ll_question);
        imBack = findViewById(R.id.btn_back_toolbar);
    }

    @Override
    protected void initData() {
        edtAnswer.setImeOptions(EditorInfo.IME_ACTION_DONE);
        typeOpen = (TYPE_OPEN) getIntent().getSerializableExtra(DATA_TYPE_OPEN);
    }

    @Override
    protected void initAction() {
        imDone.setOnClickListener(v -> {
            if (edtAnswer.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.answer_blank), Toast.LENGTH_LONG).show();
            } else {
                String answer = Toolbox.getStringByLocalPlus17(this, getIdString(idQuestion), String.valueOf(Locale.ENGLISH)) + edtAnswer.getText().toString();
                if (typeOpen == TYPE_OPEN.SET_PASS) {
                    PreferenceUtils.setAnswerSecutiryQuestion(Toolbox.endCode(answer));
                    Toast.makeText(this, getString(R.string.password_answer_set_toast), Toast.LENGTH_LONG).show();
                    finish();
                } else if (typeOpen == TYPE_OPEN.FORGOT_PASS) {
                    if (PreferenceUtils.getAnswerSecutiryQuestion().equals(Toolbox.endCode(answer))) {
                        Intent intent = new Intent(this, GestureCreateLockActivity.class);
                        startActivityForResult(intent, LockSettingLockActivity.REQUEST_CHANGE_PWD);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.answer_question_error), Toast.LENGTH_LONG).show();
                        edtAnswer.setText("");
                    }
                } else if (typeOpen == TYPE_OPEN.FIRST_SETUP) {
                    PreferenceUtils.setAnswerSecutiryQuestion(Toolbox.endCode(answer));
                    Toast.makeText(this, getString(R.string.password_answer_set_toast), Toast.LENGTH_LONG).show();
                    gotoLockMainActivity();
                }
            }
        });

        llQuestion.setOnClickListener(v -> {
            Toolbox.hideSoftKeyboard(this);
            PopupMenu popupMenu = new PopupMenu(this, llQuestion);
            popupMenu.getMenuInflater().inflate(R.menu.password_question_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                idQuestion = item.getItemId();
                tvQuestion.setText(item.getTitle().toString());
                return true;
            });
            popupMenu.show();
        });

        imBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        if (typeOpen == TYPE_OPEN.FIRST_SETUP) {
            Toolbox.hideSoftKeyboard(this);
            gotoLockMainActivity();
        } else {
            super.onBackPressed();
        }
    }

    private void gotoLockMainActivity() {
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
        BackgroundManager.getInstance().init(this).startService(LockService.class);
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_IS_FIRST_LOCK, false);
        startActivity(new Intent(this, MainLockActivity.class));
        finish();
    }

    public static int getIdString(int idMenu) {
        switch (idMenu) {
            case R.id.password_question_01:
                return R.string.password_question_01;
            case R.id.password_question_02:
                return R.string.password_question_02;
            case R.id.password_question_03:
                return R.string.password_question_03;
            case R.id.password_question_04:
                return R.string.password_question_04;
            case R.id.password_question_05:
                return R.string.password_question_05;
            case R.id.password_question_06:
                return R.string.password_question_06;
            case R.id.password_question_07:
                return R.string.password_question_07;
            case R.id.password_question_08:
                return R.string.password_question_08;
            case R.id.password_question_09:
                return R.string.password_question_09;
        }
        return 0;
    }
}
