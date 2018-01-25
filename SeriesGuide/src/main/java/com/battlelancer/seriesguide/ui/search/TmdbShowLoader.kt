package com.battlelancer.seriesguide.ui.search

import android.content.Context
import android.support.annotation.StringRes
import com.battlelancer.seriesguide.R
import com.uwetrottmann.androidutils.AndroidUtils
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.TmdbDate
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.LinkedList

class TmdbShowLoader(val context: Context, val tmdb: Tmdb, val language: String) {

    fun getShowsWithNewEpisodes(): TvdbAddLoader.Result {
        val call = tmdb.discoverTv()
                .air_date_lte(dateNow)
                .air_date_gte(dateOneWeekAgo)
                .language(language)
                .build()

        val response = try {
            call.execute()
        } catch (e: IOException) {
            return buildResultFailure(R.string.tmdb)
        }

        val results = response.body()
        if (!response.isSuccessful || results == null) {
            return buildResultFailure(R.string.tmdb)
        }

        val tvService = tmdb.tvService()
        val searchResults = results.results.mapNotNull {
            val idResponse = try {
                tvService.externalIds(it.id, null).execute()
            } catch (e: IOException) {
                null
            }
            val externalIds = idResponse?.body()
            if (idResponse == null || !idResponse.isSuccessful ||
                    externalIds == null || externalIds.tvdb_id == null) {
                null // just ignore this show
            } else {
                val searchResult = SearchResult()
                searchResult.tvdbid = externalIds.tvdb_id
                searchResult.title = it.name
//                searchResult.overview = it.overview
                searchResult.overview = if (it.name != it.original_name) it.original_name else ""
                searchResult.language = language
                searchResult
            }
        }
        return TvdbAddLoader.Result(searchResults, context.getString(R.string.add_empty), true)
    }

    private val dateNow: TmdbDate
        get() = TmdbDate(Date())

    private val dateOneWeekAgo: TmdbDate
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -7)
            return TmdbDate(calendar.time)
        }

    private fun buildResultFailure(@StringRes serviceResId: Int): TvdbAddLoader.Result {
        // only check for network here to allow hitting the response cache
        val emptyText: String
        if (AndroidUtils.isNetworkConnected(context)) {
            emptyText = context.getString(R.string.api_error_generic,
                    context.getString(serviceResId))
        } else {
            emptyText = context.getString(R.string.offline)
        }
        return TvdbAddLoader.Result(LinkedList<SearchResult>(), emptyText, false)
    }

}