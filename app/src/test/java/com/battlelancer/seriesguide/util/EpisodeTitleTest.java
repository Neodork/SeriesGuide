package com.battlelancer.seriesguide.util;

import android.content.Context;

import com.battlelancer.seriesguide.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class EpisodeTitleTest {

    private static int EPISODE_NUMBER = 1;
    private static String EPISODE_TITLE = "Episode title";
    private static String EPISODE_NUMBER_TITLE = "Episode number";

    @Mock
    Context mockContext;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        // Mock the android get string call due to no android sandbox available.
        when(mockContext.getString(anyInt(), anyInt())).thenReturn(EPISODE_NUMBER_TITLE);
    }

    @Test
    public void episodeHasTitleTest() {
        // Get the title for an episode with an title.
        String title = TextTools.getEpisodeTitle(mockContext, EPISODE_TITLE, EPISODE_NUMBER);

        // Assert that the title equals EPISODE_TITLE.
        assertEquals(EPISODE_TITLE, title);
    }

    @Test
    public void episodeHasNoTitleTest(){
        // Get the title for an episode without an title.
        String title = TextTools.getEpisodeTitle(mockContext, null, EPISODE_NUMBER);

        // Assert that the title equals EPISODE_TITLE.
        assertEquals(EPISODE_NUMBER_TITLE, title);
    }
}
