package com.philosophicalhacker.philhackernews;

import android.net.ConnectivityManager;

import com.philosophicalhacker.philhackernews.data.ConnectivityAwareStoryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by MattDupree on 7/17/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectivityAwareStoryRepositoryTests {

    @Mock
    ConnectivityManager mConnectivityManager;

    @Mock
    ConnectableObservable<List<Integer>> mStoriesObservable;

    @Mock
    ConnectableObservable<List<Integer>> mCachedStoriesObservable;

    @Test
    public void attemptsToLoadDataFromCacheWhenDeviceIsNotConnected() {
        //Arrange
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(null);
        ConnectivityAwareStoryRepository connectivityAwareStoryRepository = new ConnectivityAwareStoryRepository(mStoriesObservable, mCachedStoriesObservable, mConnectivityManager);

        //Act
        connectivityAwareStoryRepository.loadTopStories();

        //Assert
        verify(mCachedStoriesObservable).connect(any(Action1.class));
    }
}
