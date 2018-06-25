package com.battlelancer.seriesguide.asv;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.rule.provider.ProviderTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.battlelancer.seriesguide.Constants;
import com.battlelancer.seriesguide.SgApp;
import com.battlelancer.seriesguide.dataliberation.model.Season;
import com.battlelancer.seriesguide.dataliberation.model.Show;
import com.battlelancer.seriesguide.provider.SeriesGuideContract;
import com.battlelancer.seriesguide.provider.SeriesGuideProvider;
import com.battlelancer.seriesguide.thetvdbapi.TvdbEpisodeTools;
import com.uwetrottmann.thetvdb.entities.Episode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class IsWatchedCursorTest {

    private static final Show SHOW;
    private static final Season SEASON;
    private static final Episode EPISODE_WATCHED;
    private static final Episode EPISODE_UNWATCHED;

    static {
        SHOW = new Show();
        SHOW.tvdb_id = 12;

        SEASON = new Season();
        SEASON.tvdbId = 1234;
        SEASON.season = 421;

        EPISODE_WATCHED = new Episode();
        EPISODE_WATCHED.id = 34;

        EPISODE_UNWATCHED = new Episode();
        EPISODE_UNWATCHED.id = 23;
    }

    @Rule
    public ProviderTestRule providerRule = new ProviderTestRule.Builder(SeriesGuideProvider.class,
            SgApp.CONTENT_AUTHORITY).build();

    @Test
    public void testWatchedTrueEpisodeAddToCursor() {
        ContentValues values = new ContentValues();
        TvdbEpisodeTools.toContentValues(EPISODE_WATCHED, values, EPISODE_WATCHED.id, SEASON.tvdbId, SHOW.tvdb_id, 0, Constants.EPISODE_UNKNOWN_RELEASE);

        // Set the watched option within the storage to true (watched).
        values.put(SeriesGuideContract.Episodes.WATCHED, 1);

        Cursor query = providerRule.getResolver().query(SeriesGuideContract.Episodes.CONTENT_URI, null,
                null, null, null);
        try {
            assertTrue(query.moveToFirst());
        } catch (NullPointerException e) {
            // If there is no first entry then fail the test.
            fail();
        }

        // Assert that the season values equal that of the values we set on instantiating the test class.
        assertEquals(SHOW.tvdb_id, query.getInt(query.getColumnIndex(SeriesGuideContract.Shows.REF_SHOW_ID)));
        assertEquals(SEASON.tvdbId, query.getInt(query.getColumnIndex(SeriesGuideContract.Seasons.REF_SEASON_ID)));
        assertEquals((Object) EPISODE_WATCHED.id, query.getInt(query.getColumnIndex(SeriesGuideContract.Episodes._ID)));
        assertEquals(1, query.getInt(query.getColumnIndex(SeriesGuideContract.Episodes.WATCHED)));
    }

    @Test
    public void testWatchedFalseEpisodeAddToCursor() {
        ContentValues values = new ContentValues();
        TvdbEpisodeTools.toContentValues(EPISODE_UNWATCHED, values, EPISODE_UNWATCHED.id, SEASON.tvdbId, SHOW.tvdb_id, 0, Constants.EPISODE_UNKNOWN_RELEASE);

        // Set the watched option within the storage to false (not watched).
        values.put(SeriesGuideContract.Episodes.WATCHED, 0);

        Cursor query = providerRule.getResolver().query(SeriesGuideContract.Episodes.CONTENT_URI, null,
                null, null, null);
        try {
            assertTrue(query.moveToFirst());
        } catch (NullPointerException e) {
            // If there is no first entry then fail the test.
            fail();
        }

        // Assert that the season values equal that of the values we set on instantiating the test class.
        assertEquals(SHOW.tvdb_id, query.getInt(query.getColumnIndex(SeriesGuideContract.Shows.REF_SHOW_ID)));
        assertEquals(SEASON.tvdbId, query.getInt(query.getColumnIndex(SeriesGuideContract.Seasons.REF_SEASON_ID)));
        assertEquals((Object) EPISODE_UNWATCHED.id, query.getInt(query.getColumnIndex(SeriesGuideContract.Episodes._ID)));
        assertEquals(0, query.getInt(query.getColumnIndex(SeriesGuideContract.Episodes.WATCHED)));
    }
}
