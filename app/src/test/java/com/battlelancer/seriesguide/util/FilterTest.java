package com.battlelancer.seriesguide.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.battlelancer.seriesguide.ui.episodes.EpisodeFlags;
import com.battlelancer.seriesguide.ui.episodes.EpisodeTools;
import com.battlelancer.seriesguide.ui.shows.ShowsDistillationSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilterTest {

    @Mock
    Context mockContext;

    @Before
    public void setUp(){
        // Mock the getSharedPreferences() method for the PreferenceManager#getDefaultSharedPreferences()
        // method
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(new MockSharedPreference());
    }


    @Test
    public void isWatched() {
        // Enable the favorites filter.
        PreferenceManager.getDefaultSharedPreferences(mockContext).edit()
                .putBoolean(ShowsDistillationSettings.KEY_FILTER_FAVORITES, true)
                .apply();

        // Test if filter is on using the "ShowsDistillationSetting" class.
        assertTrue(ShowsDistillationSettings.isFilteringFavorites(mockContext));
    }

    public class MockSharedPreference implements SharedPreferences {

        private final HashMap<String, Object> preferenceMap;
        private final MockSharedPreferenceEditor preferenceEditor;

        MockSharedPreference() {
            preferenceMap = new HashMap<>();
            preferenceEditor = new MockSharedPreferenceEditor(preferenceMap);
        }

        @Override
        public Map<String, ?> getAll() {
            return preferenceMap;
        }

        @Nullable
        @Override
        public String getString(final String s, @Nullable final String s1) {
            return (String) preferenceMap.get(s);
        }

        @Nullable
        @Override
        public Set<String> getStringSet(final String s, @Nullable final Set<String> set) {
            return (Set<String>) preferenceMap.get(s);
        }

        @Override
        public int getInt(final String s, final int i) {
            return (int) preferenceMap.get(s);
        }

        @Override
        public long getLong(final String s, final long l) {
            return (long) preferenceMap.get(s);
        }

        @Override
        public float getFloat(final String s, final float v) {
            return (float) preferenceMap.get(s);
        }

        @Override
        public boolean getBoolean(final String s, final boolean b) {
            return (boolean) preferenceMap.get(s);
        }

        @Override
        public boolean contains(final String s) {
            return preferenceMap.containsKey(s);
        }

        @Override
        public Editor edit() {
            return preferenceEditor;
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            // No listeners for memory mock
        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            // No listeners for memory mock
        }

        class MockSharedPreferenceEditor implements Editor {

            private final HashMap<String, Object> preferenceMap;

            MockSharedPreferenceEditor(final HashMap<String, Object> preferenceMap) {
                this.preferenceMap = preferenceMap;
            }

            @Override
            public Editor putString(final String s, @Nullable final String s1) {
                preferenceMap.put(s, s1);
                return this;
            }

            @Override
            public Editor putStringSet(final String s, @Nullable final Set<String> set) {
                preferenceMap.put(s, set);
                return this;
            }

            @Override
            public Editor putInt(final String s, final int i) {
                preferenceMap.put(s, i);
                return this;
            }

            @Override
            public Editor putLong(final String s, final long l) {
                preferenceMap.put(s, l);
                return this;
            }

            @Override
            public Editor putFloat(final String s, final float v) {
                preferenceMap.put(s, v);
                return this;
            }

            @Override
            public Editor putBoolean(final String s, final boolean b) {
                preferenceMap.put(s, b);
                return this;
            }

            @Override
            public Editor remove(final String s) {
                preferenceMap.remove(s);
                return this;
            }

            @Override
            public Editor clear() {
                preferenceMap.clear();
                return this;
            }

            @Override
            public boolean commit() {
                return true;
            }

            @Override
            public void apply() {
                // Nothing to do, everything is saved in memory.
            }
        }
    }
}

