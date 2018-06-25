package com.battlelancer.seriesguide.asv;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.battlelancer.seriesguide.settings.DisplaySettings;
import com.battlelancer.seriesguide.util.TextTools;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class EpisodeFormatTest {


    private static int EPISODE_NUMBER = 1;
    private static int SEASON_NUMBER = 1;

    private static String EXPECTED_DEFAULT_FORMAT = "1x01";
    private static String EXPECTED_ENGLISH_UPPER_FORMAT = "S01E01";
    private static String EXPECTED_ENGLISH_LOWER_FORMAT = "s01e01";

    @Test
    public void testEpisodeFormatDefault() {
        // Get instrumentation context.
        Context context = InstrumentationRegistry.getContext();

        // Set format.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DisplaySettings.KEY_NUMBERFORMAT, DisplaySettings.NUMBERFORMAT_DEFAULT)
                .apply();

        // Get the title
        String episodeTitle = TextTools.getEpisodeNumber(context, EPISODE_NUMBER, SEASON_NUMBER);

        // Assert if it matches the expected format.
        assertEquals(episodeTitle, EXPECTED_DEFAULT_FORMAT);
    }

    @Test
    public void testEpisodeFormatEnglishUpper() {
        Context context = InstrumentationRegistry.getContext();

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DisplaySettings.KEY_NUMBERFORMAT, DisplaySettings.NUMBERFORMAT_ENGLISHUPPER)
                .apply();

        String episodeTitle = TextTools.getEpisodeNumber(context, EPISODE_NUMBER, SEASON_NUMBER);

        assertEquals(episodeTitle, EXPECTED_ENGLISH_UPPER_FORMAT);
    }

    @Test
    public void testEpisodeFormatEnglishLower() {
        Context context = InstrumentationRegistry.getContext();

        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DisplaySettings.KEY_NUMBERFORMAT, DisplaySettings.NUMBERFORMAT_ENGLISHLOWER)
                .apply();

        String episodeTitle = TextTools.getEpisodeNumber(context, EPISODE_NUMBER, SEASON_NUMBER);

        assertEquals(episodeTitle, EXPECTED_ENGLISH_LOWER_FORMAT);
    }

}
