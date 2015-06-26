package com.dxns.parallelworld.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dxns.parallelworld.R;
import com.dxns.parallelworld.core.rx.SubscriberWithWeakHost;
import com.dxns.parallelworld.data.api.Api;
import com.dxns.parallelworld.data.model.StatuData;
import com.dxns.parallelworld.data.service.UserServices;
import com.dxns.parallelworld.util.MD5Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;


public class MainActivity extends Activity {
    @InjectView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        fechData();

    }


    private void fechData() {

        Api.createApi(this, UserServices.class).Login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new sus(this));

    }


    public static class sus extends
            SubscriberWithWeakHost<StatuData, MainActivity> {

        public sus(MainActivity host) {
            super(host);
        }

        @Override
        public void doOnCompleted() {
            super.doOnCompleted();

        }

        @Override
        public void doOnError(Throwable e) {
            super.doOnError(e);

        }

        @Override
        public void doOnNext(StatuData statuData) {

            if (statuData != null) {
                getHost().text.setText(statuData.message+":"+ MD5Utils.verify("sss", "492d8954e89968f703292937511991c76f25678e96504774"));

            }
        }
    }
}
