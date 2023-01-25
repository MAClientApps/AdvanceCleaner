package com.lakshita.suman.advancecleaner.listener.ObserverPartener.eventModel;

import com.lakshita.suman.advancecleaner.utils.Config;

public class EvbOpenFunc extends ObserverAction {
    public Config.FUNCTION mFunction;

    public EvbOpenFunc(Config.FUNCTION mFunction) {
        this.mFunction = mFunction;
    }
}
