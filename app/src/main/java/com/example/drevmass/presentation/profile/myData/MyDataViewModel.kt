package com.example.drevmass.presentation.profile.myData

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.userInfo.UserInfoRequest
import com.example.drevmass.data.model.userInfo.UserInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MyDataViewModel(): ViewModel() {
    private var _responseUserInfo = MutableLiveData<UserInfoResponse>()
    val responseUserInfo: MutableLiveData<UserInfoResponse> = _responseUserInfo

    private var _responsePostUserInfo = MutableLiveData<UserInfoResponse>()
    val responsePostUserInfo: MutableLiveData<UserInfoResponse> = _responsePostUserInfo

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getUserInfo(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getUserInfo(token) }.fold(
                onSuccess = {
                    _responseUserInfo.postValue(it)
                },
                onFailure = { throwable ->
                    if (throwable is HttpException) {
                        val gson = com.google.gson.Gson()
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }

    fun updateUserInfo(token: String, requestBody: UserInfoRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.postUserInfo(token, requestBody) }.fold(
                onSuccess = {
                    _responseUserInfo.postValue(it)
                },
                onFailure = { throwable ->
                    if (throwable is HttpException) {
                        val gson = com.google.gson.Gson()
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }
}