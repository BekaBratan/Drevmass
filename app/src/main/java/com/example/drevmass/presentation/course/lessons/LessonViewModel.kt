package com.example.drevmass.presentation.course.lessons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.ErrorResponse
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import com.example.drevmass.data.model.products.AddToCartRequest
import com.example.drevmass.data.model.products.Product
import com.example.drevmass.data.model.products.ProductDetailResponse
import com.example.drevmass.data.model.products.ProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LessonViewModel(): ViewModel() {
    private var _responseFamousProducts = MutableLiveData<ProductsResponse>()
    val responseFamousProducts: MutableLiveData<ProductsResponse> = _responseFamousProducts

    private var _responseLesson = MutableLiveData<LessonResponse>()
    val responseLesson: MutableLiveData<LessonResponse> = _responseLesson

    private var _productResponse: MutableLiveData<ProductDetailResponse> = MutableLiveData()
    val productResponse: MutableLiveData<ProductDetailResponse> = _productResponse

    private var _messageResponse: MutableLiveData<MessageResponse> = MutableLiveData()
    val messageResponse: MutableLiveData<MessageResponse> = _messageResponse

    private var _errorResponse: MutableLiveData<ErrorResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<ErrorResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getLesson(token: String, courseId: Int, lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getLesson(token, courseId, lessonId) }.fold(
                onSuccess = {
                    _responseLesson.postValue(it)
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

    fun favoriteSelectLesson(token: String, courseId: Int, lessonId: Int){
        if (_responseLesson.value?.isFavorite == false){
            addFavoriteLesson(token, courseId, lessonId)
        }else{
            removeFavoriteLesson(token, courseId, lessonId)
        }
    }

    fun addFavoriteLesson(token: String,  courseId: Int, lessonId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.addToFavorite(token, lessonId) }.fold(
                onSuccess = {
                    getLesson(token, courseId, lessonId)
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

    fun removeFavoriteLesson(token: String,  courseId: Int, lessonId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.deleteFromFavorite(token, lessonId) }.fold(
                onSuccess = {
                    Log.d("AAA", "LessonViewModel - removeFavoriteLesson - ${it}")
                    getLesson(token, courseId, lessonId)
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

    fun getFamousProducts(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getFamousProducts(token) }.fold(
                onSuccess = {
                    _responseFamousProducts.postValue(it)
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

    fun addToCart(token: String, userId: Int, productId: Int, count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.addToCart(token, AddToCartRequest(count, productId, userId)) }.fold(
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

    fun getProduct(token: String, productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getProduct(token, productId) }.fold(
                onSuccess = {
                    _productResponse.postValue(it)
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