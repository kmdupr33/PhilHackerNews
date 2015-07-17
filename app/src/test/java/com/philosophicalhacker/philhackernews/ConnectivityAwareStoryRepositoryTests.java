package com.philosophicalhacker.philhackernews;

import android.net.ConnectivityManager;

import com.philosophicalhacker.philhackernews.data.ConnectivityAwareStoryRepository;
import com.philosophicalhacker.philhackernews.data.HackerNewsCache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.observables.ConnectableObservable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    HackerNewsCache mHackerNewsCache;

    @Test
    public void attemptsToLoadDataFromCacheWhenDeviceIsNotConnected() {
        //Arrange
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(null);
        ConnectivityAwareStoryRepository connectivityAwareStoryRepository = new ConnectivityAwareStoryRepository(mStoriesObservable, mConnectivityManager);

        //Act
        connectivityAwareStoryRepository.loadTopStories();

        //Assert
        verify(mHackerNewsCache).loadTopStories();
    }
}
