package com.example.drevmass.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.model.RegistrationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthorizationViewModel(): ViewModel() {

    private var _authorizaitonResponse: MutableLiveData<AuthorizationResponse> = MutableLiveData()
    val authorizationResponse: MutableLiveData<AuthorizationResponse> = _authorizaitonResponse

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    private var _registrationResponse: MutableLiveData<AuthorizationResponse> = MutableLiveData()
    val registrationResponse: MutableLiveData<AuthorizationResponse> = _registrationResponse

    private var _tryAgain: MutableLiveData<String?> = MutableLiveData()
    val tryAgain: LiveData<String?> = _tryAgain

    fun login(authorization: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.login(authorization) }.fold(
                onSuccess = {
                    _authorizaitonResponse.postValue(it)
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

    fun register(registration: RegistrationRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.registration(registration) }.fold(
                onSuccess = {
                    _registrationResponse.postValue(it)
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

    fun loginAfterRegister(authorization: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            runCatching { ServiceBuilder.api.login(authorization) }.fold(
                onSuccess = {
                    _authorizaitonResponse.postValue(it)
                },
                onFailure = {
                    _tryAgain.postValue(it.message)
                }
            )
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.forgotPassword(email) }.fold(
                onSuccess = {
                    _authorizaitonResponse.postValue(it)
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