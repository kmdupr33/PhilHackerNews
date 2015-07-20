package com.philosophicalhacker.philhackernews.data;

import com.philosophicalhacker.philhackernews.model.Story;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by MattDupree on 7/20/15.
 */
public class SyncRequestingOnSubscribe implements Observable.OnSubscribe<List<Story>> {
    @Override
    public void call(Subscriber<? super List<Story>> subscriber) {

    }
}
