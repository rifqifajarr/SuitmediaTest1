package com.suitmediatest1.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.suitmediatest1.data.api.ApiConfig
import com.suitmediatest1.data.api.UserResponse
import com.suitmediatest1.data.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.suitmediatest1.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class ScreenViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<UserModel>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<UserModel>>> get() = _uiState

    private val _selectedUser = MutableStateFlow<String?>(null)

    val pager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSource(ApiConfig.getApiService()) }
    ).flow

    fun getUser() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            val client = ApiConfig.getApiService().getUserDate(1, 30)
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        _uiState.value = UiState.Success(responseBody.data)
                    } else {
                        Log.e("PagingSource", "onResponse")
                        _uiState.value = UiState.Error("No Data")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("PagingSource", "onFailure ${t.message}")
                    _uiState.value = UiState.Error(t.message.toString())
                }
            })
        }
    }

    fun setSelectedUser(name: String) {
        _selectedUser.value = name
    }

    fun getSelectedUser(): String {
        if (_selectedUser.value == null) {
            return "Selected User Name"
        } else {
            return _selectedUser.value.toString()
        }
    }
}

sealed class UiState<out T: Any?> {
    object Loading : UiState<Nothing>()

    data class Success<out T: Any>(val data: T) : UiState<T>()

    data class Error(val errorMessage: String) : UiState<Nothing>()
}