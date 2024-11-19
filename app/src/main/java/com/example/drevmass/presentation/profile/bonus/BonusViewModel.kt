package com.example.drevmass.presentation.profile.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.bonus.BonusResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BonusViewModel(): ViewModel() {
    private var _responseBonus = MutableLiveData<BonusResponse>()
    val responseBonus: MutableLiveData<BonusResponse> = _responseBonus

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getBonus(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getBonus(token) }.fold(
                onSuccess = {
                    _responseBonus.postValue(it)
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