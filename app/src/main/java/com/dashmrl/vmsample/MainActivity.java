package com.dashmrl.vmsample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dashmrl.vm.VMFactory;


public class MainActivity extends AppCompatActivity implements MainContract.MainView {
    protected MainContract.BaseMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        presenter = VMFactory.viewModel(this, MainPresenter.class, "this is msg");
        presenter.takeView(this);
        fab.setOnClickListener(view -> presenter.loadMsg());
    }

    @Override
    public void onLoad(String msg) {
        Snackbar.make(findViewById(R.id.fab), msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
