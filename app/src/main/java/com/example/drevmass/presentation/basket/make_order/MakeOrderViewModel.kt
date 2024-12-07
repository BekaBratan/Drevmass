package com.example.drevmass.presentation.basket.make_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.ErrorResponse
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.products.BasketResponse
import com.example.drevmass.data.model.products.OrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MakeOrderViewModel (): ViewModel() {

    private var _messageResponse: MutableLiveData<MessageResponse> = MutableLiveData()
    val messageResponse: MutableLiveData<MessageResponse> = _messageResponse

    private var _errorResponse: MutableLiveData<ErrorResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<ErrorResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun makeOrder(token: String, orderRequest: OrderRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.makeOrder(token, orderRequest) }.fold(
                onSuccess = {
                    _messageResponse.postValue(it)
                },
                onFailure = { throwable ->
                    if (throwable is HttpException) {
                        val gson = com.google.gson.Gson()
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }
}