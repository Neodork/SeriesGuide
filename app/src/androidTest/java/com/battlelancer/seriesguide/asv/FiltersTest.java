package com.battlelancer.seriesguide.asv;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.battlelancer.seriesguide.ui.shows.ShowsDistillationSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

@RunWith(AndroidJUnit4.class)
public class FiltersTest {

    Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();

        // Set filter default to false.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_FAVORITES, false)
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UNWATCHED, false)
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UPCOMING, false)
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_HIDDEN, false)
                .apply();
    }

    @Test
    public void testFilterFavoritesEnable() {
        // Enable the favorites filter.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_FAVORITES, true)
                .apply();

        // Test if filter is on using the "ShowsDistillationSetting" class.
        assertTrue(ShowsDistillationSettings.isFilteringFavorites(context));
    }

    @Test
    public void testFilterUnwatchedEnable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UNWATCHED, true)
                .apply();

        assertTrue(ShowsDistillationSettings.isFilteringUnwatched(context));
    }

    @Test
    public void testFilterUpcomingEnable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UPCOMING, true)
                .apply();

        assertTrue(ShowsDistillationSettings.isFilteringUpcoming(context));
    }

    @Test
    public void testFilterHiddenEnable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_HIDDEN, true)
                .apply();

        assertTrue(ShowsDistillationSettings.isFilteringHidden(context));
    }

    @Test
    public void testFilterFavoritesDisable() {
        // Enable the favorites filter.
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_FAVORITES, false)
                .apply();

        // Test if filter is on using the "ShowsDistillationSetting" class.
        assertFalse(ShowsDistillationSettings.isFilteringFavorites(context));
    }

    @Test
    public void testFilterUnwatchedDisable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UNWATCHED, false)
                .apply();

        assertFalse(ShowsDistillationSettings.isFilteringUnwatched(context));
    }

    @Test
    public void testFilterUpcomingDisable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_UPCOMING, false)
                .apply();

        assertFalse(ShowsDistillationSettings.isFilteringUpcoming(context));
    }

    @Test
    public void testFilterHiddenDisable() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_HIDDEN, false)
                .apply();

        assertFalse(ShowsDistillationSettings.isFilteringHidden(context));
    }
}
