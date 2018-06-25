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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class SeasonDisplayTest {
    @Mock
    Context mockContext;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        // Mock the getSharedPreferences() method for the PreferenceManager#getDefaultSharedPreferences()
        // method
        when(mockContext.getString(eq(R.string.specialseason))).thenReturn("Special Episode");
        when(mockContext.getString(eq(R.string.season_number), anyInt())).thenReturn("Season");
    }

    @Test
    public void isSeasonDisplayTest() {
        String seasonString = SeasonTools.getSeasonString(mockContext, 2);
        assertEquals(seasonString, "Season");
    }

    @Test
    public void isSpecialEpisodeDisplayTest(){
        String seasonString = SeasonTools.getSeasonString(mockContext, 0);
        assertEquals("Special Episode", seasonString);
    }
}
