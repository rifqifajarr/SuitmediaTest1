package com.suitmediatest1.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.suitmediatest1.data.api.ApiConfig
import com.suitmediatest1.data.api.ApiService
import com.suitmediatest1.data.api.UserResponse
import com.suitmediatest1.data.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagingSource(private val apiService: ApiService): PagingSource<Int, UserModel>() {
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, UserModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserModel> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            var list: List<UserModel> = emptyList()
            val client = apiService.getUserDate(page, params.loadSize)
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        list = responseBody.data
                    } else {
                        Log.e("PagingSource", "onResponse")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("PagingSource", "onFailure ${t.message}")
                }
            })

            LoadResult.Page(
                data = list,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error load", e)
            return LoadResult.Error(e)
        }
    }
}