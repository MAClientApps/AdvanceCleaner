package com.lakshita.suman.advancecleaner.lock.mvp.contract;

import android.content.Context;

import com.lakshita.suman.advancecleaner.lock.base.BasePresenter;
import com.lakshita.suman.advancecleaner.lock.base.BaseView;
import com.lakshita.suman.advancecleaner.lock.model.CommLockInfo;
import com.lakshita.suman.advancecleaner.lock.mvp.p.LockMainPresenter;

import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public interface LockMainContract {
    interface View extends BaseView<Presenter> {

        void loadAppInfoSuccess(List<CommLockInfo> list);

        void showProgressbar(boolean isShow);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void searchAppInfo(String search, LockMainPresenter.ISearchResultListener listener);

        void onDestroy();
    }
}
