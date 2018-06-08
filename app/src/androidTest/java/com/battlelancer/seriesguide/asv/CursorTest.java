package com.battlelancer.seriesguide.asv;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.provider.ProviderTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.battlelancer.seriesguide.Constants;
import com.battlelancer.seriesguide.SgApp;
import com.battlelancer.seriesguide.dataliberation.model.Season;
import com.battlelancer.seriesguide.dataliberation.model.Show;
import com.battlelancer.seriesguide.provider.SeriesGuideContract;
import com.battlelancer.seriesguide.provider.SeriesGuideProvider;
import com.battlelancer.seriesguide.thetvdbapi.TvdbEpisodeTools;
import com.battlelancer.seriesguide.util.DBUtils;
import com.uwetrottmann.thetvdb.entities.Episode;
import com.uwetrottmann.thetvdb.entities.Series;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CursorTest {

    private static final Show SHOW;
    private static final Season SEASON;
    private static final Episode EPISODE;

    static {
        SHOW = new Show();
        SHOW.tvdb_id = 12;

        SEASON = new Season();
        SEASON.tvdbId = 1234;
        SEASON.season = 42;

        EPISODE = new Episode();
        EPISODE.id = 123456;
    }

    @Rule
    public ProviderTestRule providerRule = new ProviderTestRule.Builder(SeriesGuideProvider.class,
            SgApp.CONTENT_AUTHORITY).build();

    @Test
    public void testShowAddToCursor() {
        Context context = InstrumentationRegistry.getTargetContext();

        // Create a new show insert operation.
        ContentValues values = SHOW.toContentValues(context, true);
        ContentProviderOperation op = ContentProviderOperation.newInsert(SeriesGuideContract.Shows.CONTENT_URI)
                .withValues(values).build();

        // Retrieve current shows in Cursor.
        Cursor query = providerRule.getResolver().query(SeriesGuideContract.Shows.CONTENT_URI, null,
                null, null, null);
        try{
            query.moveToFirst();
        }catch(NullPointerException e){
            // If there is no first entry then fail the test.
            fail();
        }

        // Assert that the ID equals that of the tvdb_id we set on instantiating the test class.
        assertEquals(SHOW.tvdb_id, query.getInt(query.getColumnIndex("_id")));
    }

    @Test
    public void testSeasonAddToCursor() {
        // Create the season with a relation to the show.
        ContentProviderOperation op = DBUtils
                .buildSeasonOp(SHOW.tvdb_id, SEASON.tvdbId, SEASON.season, true);

        // Retrieve current Seasons in Cursor.
        Cursor query = providerRule.getResolver().query(SeriesGuideContract.Seasons.CONTENT_URI, null,
                null, null, null);
        try{
            query.moveToFirst();
        }catch(NullPointerException e){
            // If there is no first entry then fail the test.
            fail();
        }

        // Assert that the season values equal that of the values we set on instantiating the test class.
        assertEquals(SHOW.tvdb_id, query.getInt(query.getColumnIndex(SeriesGuideContract.Shows.REF_SHOW_ID)));
        assertEquals(SEASON.tvdbId, query.getInt(query.getColumnIndex(SeriesGuideContract.Seasons._ID)));
    }

    @Test
    public void testEpisodeAddToCursor() {
        ContentValues values = new ContentValues();
        TvdbEpisodeTools.toContentValues(EPISODE, values, EPISODE.id, SEASON.tvdbId, SHOW.tvdb_id, 0, Constants.EPISODE_UNKNOWN_RELEASE);

        Cursor query = providerRule.getResolver().query(SeriesGuideContract.Episodes.CONTENT_URI, null,
                null, null, null);
        try{
            query.moveToFirst();
        }catch(NullPointerException e){
            // If there is no first entry then fail the test.
            fail();
        }

        // Assert that the season values equal that of the values we set on instantiating the test class.
        assertEquals(SHOW.tvdb_id, query.getInt(query.getColumnIndex(SeriesGuideContract.Shows.REF_SHOW_ID)));
        assertEquals(SEASON.tvdbId, query.getInt(query.getColumnIndex(SeriesGuideContract.Seasons.REF_SEASON_ID)));
        assertEquals((Object)EPISODE.id, query.getInt(query.getColumnIndex(SeriesGuideContract.Episodes._ID)));
    }
}
