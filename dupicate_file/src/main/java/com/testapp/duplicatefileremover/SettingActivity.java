package com.testapp.duplicatefileremover;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.testapp.duplicatefileremover.utilts.SharePreferenceUtils;
import com.testapp.duplicatefileremover.utilts.Utils;

import java.util.Locale;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RelativeLayout rlMoreApp, rlRate, rlShare, rlPolicy, rlLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocale(this);
        setContentView(R.layout.activity_dupicate_setting);
        intView();
        intData();
        intEvent();
    }

    public void intView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.setting));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rlMoreApp = (RelativeLayout) findViewById(R.id.rlMoreApp);
        rlRate = (RelativeLayout) findViewById(R.id.rlRate);
        rlShare = (RelativeLayout) findViewById(R.id.rlShare);
        rlPolicy = (RelativeLayout) findViewById(R.id.rlPolicy);
        rlLanguage = (RelativeLayout) findViewById(R.id.rlLanguage);
    }

    public void intData() {
    }

    public void intEvent() {
        rlMoreApp.setOnClickListener(this);
        rlRate.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlPolicy.setOnClickListener(this);
        rlLanguage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.rlLanguage) {
        } else if (id == R.id.rlRate) {
        } else if (id == R.id.rlShare) {
        } else if (id == R.id.rlPolicy) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
