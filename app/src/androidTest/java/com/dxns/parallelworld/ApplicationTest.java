package com.dxns.parallelworld;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.dxns.parallelworld.core.ParallelwordApplacation;
import com.dxns.parallelworld.core.SubscriberWithWeakHost;
import com.dxns.parallelworld.data.api.Api;
import com.dxns.parallelworld.data.model.StatuData;
import com.dxns.parallelworld.data.service.UserServices;

import rx.android.schedulers.AndroidSchedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

    }



}