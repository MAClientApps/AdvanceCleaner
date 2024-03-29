package com.lakshita.suman.advancecleaner.screen.result;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.AdmobHelp;
import com.airbnb.lottie.LottieAnimationView;
import com.lakshita.suman.advancecleaner.CleanMasterApp;
import com.lakshita.suman.advancecleaner.R;
import com.lakshita.suman.advancecleaner.adapter.FunctionAdapter;
import com.lakshita.suman.advancecleaner.listener.ObserverPartener.ObserverUtils;
import com.lakshita.suman.advancecleaner.listener.ObserverPartener.eventModel.EvbCheckLoadAds;
import com.lakshita.suman.advancecleaner.listener.ObserverPartener.eventModel.EvbOnResumeAct;
import com.lakshita.suman.advancecleaner.listener.ObserverPartener.eventModel.EvbOpenFunc;
import com.lakshita.suman.advancecleaner.screen.BaseActivity;
import com.lakshita.suman.advancecleaner.screen.ExitActivity;
import com.lakshita.suman.advancecleaner.screen.main.MainActivity;
import com.lakshita.suman.advancecleaner.utils.Config;
import com.lakshita.suman.advancecleaner.utils.PreferenceUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAcitvity extends BaseActivity {

    @BindView(R.id.rcv_funtion_suggest)
    RecyclerView rcvFunctionSuggest;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.imgDone)
    LottieAnimationView imgDone;
    @BindView(R.id.img_congratulations)
    LottieAnimationView imCongratulation;
    @BindView(R.id.ll_infor)
    NestedScrollView scvInfor;
    @BindView(R.id.ll_done)
    View llDone;
    @BindView(R.id.ll_main_result)
    View llMainResut;
    @BindView(R.id.ll_background)
    View llBackground;
    @BindView(R.id.layout_padding)
    View llToolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Config.FUNCTION mFunction;
    private FunctionAdapter mFunctionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_result);
        ButterKnife.bind(this);
        initView();
        initData();
        AdmobHelp.getInstance().loadNative(this);
    }

    private void initView() {
        llToolbar.setAlpha(0.0f);
        scvInfor.setAlpha(0.0f);
        if (getIntent() != null)
            mFunction = Config.getFunctionById(getIntent().getIntExtra(Config.DATA_OPEN_RESULT, 0));
        if (mFunction != null) {
            tvToolbar.setText(getString(mFunction.title));
            tvTitle.setText(getString(mFunction.titleResult));
            PreferenceUtils.setLastTimeUseFunction(mFunction);
        }
        imBack.setVisibility(View.VISIBLE);
        imgDone.playAnimation();
        imgDone.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AdmobHelp.getInstance().showInterstitialAd(ResultAcitvity.this, new AdmobHelp.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        imgDone.pauseAnimation();
                        llDone.setVisibility(View.GONE);
                        imCongratulation.setAnimation(mFunction != null ? mFunction.jsonResult : "restult_like.json");
                        if (mFunction != null)
                            imCongratulation.setColorFilter(getResources().getColor(mFunction.color));
                        imCongratulation.playAnimation();
                        YoYo.with(Techniques.SlideInUp).duration(1000).playOn(scvInfor);
                        YoYo.with(Techniques.FadeIn).duration(1000).playOn(llBackground);
                        YoYo.with(Techniques.FadeIn).duration(1000).playOn(llToolbar);
                    }
                });

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initData() {
        List<Config.FUNCTION> lstShow = new ArrayList<>();
        for (int i = 0; i < Config.LST_SUGGEST.length; i++) {
            if (PreferenceUtils.checkLastTimeUseFunction(Config.LST_SUGGEST[i]))
                if (mFunction != null) {
                    if (Config.LST_SUGGEST[i] != mFunction)
                        lstShow.add(Config.LST_SUGGEST[i]);
                } else {
                    lstShow.add(Config.LST_SUGGEST[i]);
                }
        }
        Config.FUNCTION[] lstAdapter = new Config.FUNCTION[lstShow.size()];
        for (int i = 0; i < lstShow.size(); i++) {
            lstAdapter[i] = lstShow.get(i);
        }
        mFunctionAdapter = new FunctionAdapter(lstAdapter, Config.TYPE_DISPLAY_ADAPTER.SUGGEST);
        mFunctionAdapter.setmClickItemListener(mFunction -> {
            if (CleanMasterApp.getInstance().getActivityList().size() == 1) {
                Intent intentAct = new Intent(this, MainActivity.class);
                intentAct.putExtra(Config.DATA_OPEN_FUNCTION, mFunction.id);
                startActivity(intentAct);
            } else {
                ObserverUtils.getInstance().notifyObservers(new EvbOpenFunc(mFunction));
            }
            finish();
        });
        rcvFunctionSuggest.setAdapter(mFunctionAdapter);
        ObserverUtils.getInstance().notifyObservers(new EvbOnResumeAct());
    }

    @Override
    public void onBackPressed() {
        try {
            if (mFunction == Config.FUNCTION.DEEP_CLEAN) {
                ObserverUtils.getInstance().notifyObservers(new EvbOpenFunc(mFunction));
                finish();
            } else if (mFunction == null) {
                ExitActivity.exitApplicationAndRemoveFromRecent(this);
            } else {
                ObserverUtils.getInstance().notifyObservers(new EvbCheckLoadAds());
                super.onBackPressed();
            }
        } catch (Exception e) {

        }
    }
}
