package com.battlelancer.seriesguide.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.battlelancer.seriesguide.dataliberation.model.Episode;
import com.battlelancer.seriesguide.dataliberation.model.Season;
import com.battlelancer.seriesguide.dataliberation.model.Show;
import com.battlelancer.seriesguide.thetvdbapi.TvdbLinks;
import com.battlelancer.seriesguide.ui.shows.ShowsDistillationSettings;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TvdbLinkTest {

    private static final Show SHOW;
    private static final Season SEASON;
    private static final Episode EPISODE;

    // Tvdb Contant values
    private static final Integer ENGLISH_LID = 7;
    private static final Integer SPANISH_LID = 16;
    private static final String ENGLISH_LANGCODE = "en";
    private static final String SPANISH_LANGCODE = "es";
    private static final String FAROE_ISLANDS_LANGCODE = "fo"; // Faroe Islands

    static {
        SHOW = new Show();
        SHOW.tvdb_id = 259972;

        SEASON = new Season();
        SEASON.tvdbId = 1;
        SEASON.season = 1;

        EPISODE = new Episode();
        EPISODE.tvdbId = 123456;
    }

    @Test
    public void tvdbLinkShowDefaultLanguageTest() {
        // Get the link from the TvdbLinks utility class.
        String link = TvdbLinks.show(SHOW.tvdb_id, FAROE_ISLANDS_LANGCODE);
        // Check if link actually points towards the TVDB domain name.
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        // Assert that the link contains the correct ID.
        assertThat(link, CoreMatchers.containsString("id="+SHOW.tvdb_id));
        // Assert that the link contains the correct language ID.
        assertThat(link, CoreMatchers.containsString("lid="+ENGLISH_LID));
    }

    @Test
    public void tvdbLinkShowEnglishTest() {
        String link = TvdbLinks.show(SHOW.tvdb_id, ENGLISH_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        assertThat(link, CoreMatchers.containsString("id="+SHOW.tvdb_id));
        assertThat(link, CoreMatchers.containsString("lid="+ENGLISH_LID));
    }

    @Test
    public void tvdbLinkShowSpanishTest() {
        String link = TvdbLinks.show(SHOW.tvdb_id, SPANISH_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        assertThat(link, CoreMatchers.containsString("id="+SHOW.tvdb_id));
        assertThat(link, CoreMatchers.containsString("lid="+SPANISH_LID));
    }

    @Test
    public void tvdbLinkShowFailLanguageTest() {
        String link = TvdbLinks.show(SHOW.tvdb_id, ENGLISH_LANGCODE);
        // Even though this case has to fail, the domain stays the same.
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        // Even though this case has to fail, the ID should still be present.
        assertThat(link, CoreMatchers.containsString("id="+SHOW.tvdb_id));
        // Assert that the language code is incorrect, should be ENGLISH_LID.
        assertFalse(link, link.contains("lid="+SPANISH_LID));
    }

    @Test
    public void tvdbLinkEpisodeDefaultLanguageTest() {
        String link = TvdbLinks.episode(SHOW.tvdb_id, SEASON.tvdbId, EPISODE.tvdbId, FAROE_ISLANDS_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        // Assert that the series ID matches.
        assertThat(link, CoreMatchers.containsString("seriesid="+SHOW.tvdb_id));
        // Assert that the seasonId matches.
        assertThat(link, CoreMatchers.containsString("seasonid="+SEASON.tvdbId));
        // Assert that the episode ID matches.
        assertThat(link, CoreMatchers.containsString("id="+EPISODE.tvdbId));
        // Assert that the Language matches the default ENGLISH_LID.
        assertThat(link, CoreMatchers.containsString("lid="+ENGLISH_LID));
    }

    @Test
    public void tvdbLinkEpisodeEnglishLanguageTest() {
        String link = TvdbLinks.episode(SHOW.tvdb_id, SEASON.tvdbId, EPISODE.tvdbId, ENGLISH_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        // Assert that the series ID matches.
        assertThat(link, CoreMatchers.containsString("seriesid="+SHOW.tvdb_id));
        // Assert that the seasonId matches.
        assertThat(link, CoreMatchers.containsString("seasonid="+SEASON.tvdbId));
        // Assert that the episode ID matches.
        assertThat(link, CoreMatchers.containsString("id="+EPISODE.tvdbId));
        // Assert that the Language matches the default ENGLISH_LID.
        assertThat(link, CoreMatchers.containsString("lid="+ENGLISH_LID));
    }

    @Test
    public void tvdbLinkEpisodeSpanishLanguageTest() {
        String link = TvdbLinks.episode(SHOW.tvdb_id, SEASON.tvdbId, EPISODE.tvdbId, SPANISH_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        assertThat(link, CoreMatchers.containsString("seriesid="+SHOW.tvdb_id));
        assertThat(link, CoreMatchers.containsString("seasonid="+SEASON.tvdbId));
        assertThat(link, CoreMatchers.containsString("id="+EPISODE.tvdbId));
        // Assert that the Language matches the default SPANISH_LID.
        assertThat(link, CoreMatchers.containsString("lid="+SPANISH_LID));
    }

    @Test
    public void tvdbLinkEpisodeFailLanguageTest() {
        String link = TvdbLinks.episode(SHOW.tvdb_id, SEASON.tvdbId, EPISODE.tvdbId, ENGLISH_LANGCODE);
        assertThat(link, CoreMatchers.containsString("thetvdb.com"));
        assertThat(link, CoreMatchers.containsString("seriesid="+SHOW.tvdb_id));
        assertThat(link, CoreMatchers.containsString("seasonid="+SEASON.tvdbId));
        assertThat(link, CoreMatchers.containsString("id="+EPISODE.tvdbId));
        // Assert that the Language matches the default SPANISH_LID.
        assertFalse(link, link.contains("lid="+SPANISH_LID));
    }
}

