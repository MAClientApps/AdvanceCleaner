package com.lakshita.suman.advancecleaner.listener.ObserverPartener.eventModel;

import com.lakshita.suman.advancecleaner.model.NotifiModel;

import java.util.List;

public class EvbAddListNoti extends ObserverAction {
    public List<NotifiModel> lst;

    public EvbAddListNoti(List<NotifiModel> lst) {
        this.lst = lst;
    }

}
