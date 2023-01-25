package com.lakshita.suman.advancecleaner.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lakshita.suman.advancecleaner.adapter.LanguageAdapter;
import com.lakshita.suman.advancecleaner.databinding.DialogChangeLanguageBinding;
import com.lakshita.suman.advancecleaner.model.ItemSelected;

import java.util.List;

public class DialogChangeLanguage extends BaseDialogFragment<DialogChangeLanguageBinding> {

    private LanguageAdapter languageAdapter;
    private CallBackDialog callBackDialog;
    private List<ItemSelected> itemSelecteds;
    private String selected;

    public DialogChangeLanguage(List<ItemSelected> itemSelecteds, String selected, CallBackDialog callBackDialog) {
        this.callBackDialog = callBackDialog;
        this.itemSelecteds = itemSelecteds;
        this.selected = selected;
    }

    @Override
    protected void initView() {
        languageAdapter = new LanguageAdapter(itemSelecteds, getContext(), selected);
        binding.rcvData.setAdapter(languageAdapter);
    }

    @Override
    protected void initData() {
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnDone.setOnClickListener(v -> {
            if (callBackDialog != null) {
                callBackDialog.onOK(languageAdapter.getSelected());
                dismiss();
            }
        });
    }

    @Override
    protected DialogChangeLanguageBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DialogChangeLanguageBinding.inflate(LayoutInflater.from(getContext()));
    }


    public interface CallBackDialog {
        void onOK(String selected);
    }

}
