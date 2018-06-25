package com.battlelancer.seriesguide.asv;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.battlelancer.seriesguide.settings.DisplaySettings;
import com.battlelancer.seriesguide.util.TextTools;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class EpisodeFormatContraintTest {


    private static int EPISODE_NUMBER = 1;
    private static int EPISODE_NUMBER_DECIMAL = 10;
    private static int SEASON_NUMBER = 1;

    private static String EXPECTED_FORMAT = "s01";
    private static String EXPECTED_DECIMAL_FORMAT = "s10";

    @Test
    public void testEpisodeFormatInteger() {
        // Get instrumentation context.
        Context context = InstrumentationRegistry.getContext();

        // Set format.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DisplaySettings.KEY_NUMBERFORMAT, DisplaySettings.NUMBERFORMAT_ENGLISHLOWER)
                .apply();

        // Get the title
        String episodeTitle = TextTools.getEpisodeNumber(context, EPISODE_NUMBER, SEASON_NUMBER);

        // Assert if it matches the expected format.
        assertThat(episodeTitle, CoreMatchers.containsString(EXPECTED_FORMAT));
    }

    @Test
    public void testEpisodeFormatDecimalInteger() {
        // Get instrumentation context.
        Context context = InstrumentationRegistry.getContext();

        // Set format.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DisplaySettings.KEY_NUMBERFORMAT, DisplaySettings.NUMBERFORMAT_ENGLISHLOWER)
                .apply();

        // Get the title
        String episodeTitle = TextTools.getEpisodeNumber(context, EPISODE_NUMBER_DECIMAL, SEASON_NUMBER);

        // Assert if it matches the expected format.
        assertThat(episodeTitle, CoreMatchers.containsString(EXPECTED_DECIMAL_FORMAT));
    }



}
