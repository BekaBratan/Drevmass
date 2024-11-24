package com.example.drevmass.presentation.course.lessons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.example.drevmass.data.model.courseModel.getAllBasket.GetAllBasketResponse
import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LessonViewModel(): ViewModel() {

    private var _responseListBasket = MutableLiveData<GetAllBasketResponse>()
    val responseListBasket: MutableLiveData<GetAllBasketResponse> = _responseListBasket

    private var _response1Basket = MutableLiveData<GetAllBasketResponse>()
    val response1Basket: MutableLiveData<GetAllBasketResponse> = _response1Basket

    private var _responseFamousProducts = MutableLiveData<List<getFamousProductsResponse>>()
    val responseFamousProducts: MutableLiveData<List<getFamousProductsResponse>> = _responseFamousProducts

    private var _responseLesson = MutableLiveData<LessonResponse>()
    val responseLesson: MutableLiveData<LessonResponse> = _responseLesson

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

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
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
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
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
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
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
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
                        val errorResponse = gson.fromJson(errorBody, AuthorizationResponse::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }

    fun getAllBasket1(token: String, isUsing: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getAllBasket(token, isUsing) }.fold(
                onSuccess = {
                    _response1Basket.postValue(it)
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

    fun getAllBasket(token: String, isUsing: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getAllBasket(token, isUsing) }.fold(
                onSuccess = {
                    _responseListBasket.postValue(it)
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