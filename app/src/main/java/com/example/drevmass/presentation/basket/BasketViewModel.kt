package com.example.drevmass.presentation.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.ErrorResponse
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.products.AddToCartRequest
import com.example.drevmass.data.model.products.BasketResponse
import com.example.drevmass.data.model.products.OrderRequest
import com.example.drevmass.data.model.products.ProductDetailResponse
import com.example.drevmass.data.model.products.ProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BasketViewModel(): ViewModel() {

    private var _basketResponse: MutableLiveData<BasketResponse> = MutableLiveData()
    val basketResponse: MutableLiveData<BasketResponse> = _basketResponse

    private var _messageResponse: MutableLiveData<MessageResponse> = MutableLiveData()
    val messageResponse: MutableLiveData<MessageResponse> = _messageResponse

    private var _errorResponse: MutableLiveData<ErrorResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<ErrorResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getBasket(token: String, isUsing: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getBasket(token, isUsing) }.fold(
                onSuccess = {
                    _basketResponse.postValue(it)
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

    fun deleteBasket(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.deleteBasket(token) }.fold(
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

    fun increaseCart(token: String, userId: Int, productId: Int, count: Int, isPromocode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.increaseCart(token, AddToCartRequest(count, productId, userId)) }.fold(
                onSuccess = {
                    _messageResponse.postValue(it)
                    getBasket(token, isPromocode)
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

    fun decreaseCart(token: String, userId: Int, productId: Int, count: Int, isPromocode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.decreaseCart(token, AddToCartRequest(count, productId, userId)) }.fold(
                onSuccess = {
                    _messageResponse.postValue(it)
                    getBasket(token, isPromocode)
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

    fun addToCart(token: String, userId: Int, productId: Int, count: Int, isPromocode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.addToCart(token, AddToCartRequest(count, productId, userId)) }.fold(
                onSuccess = {
                    _messageResponse.postValue(it)
                    getBasket(token, isPromocode)
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