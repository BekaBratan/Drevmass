package com.example.drevmass.presentation.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.drevmass.data.model.courseModel.getAllBasket.GetAllBasketResponse
import com.example.drevmass.data.model.courseModel.BonusBannerResponse
import com.example.drevmass.data.model.courseModel.CourseItemResponse
import retrofit2.HttpException

class CourseViewModel(): ViewModel() {
    private var _courseList = MutableLiveData<List<CourseItemResponse>>()
    val courseList: MutableLiveData<List<CourseItemResponse>> = _courseList

    private var _responseListBasket = MutableLiveData<GetAllBasketResponse>()
    val responseListBasket: MutableLiveData<GetAllBasketResponse> = _responseListBasket

    private var _courseBannerBonus = MutableLiveData<BonusBannerResponse>()
    val courseBannerBonus: MutableLiveData<BonusBannerResponse> = _courseBannerBonus

    private var _errorResponse: MutableLiveData<HttpException> = MutableLiveData()
    val errorResponse: MutableLiveData<HttpException> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getBonusBanner(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getBonusCourse(token) }.fold(
                onSuccess = {
                    _courseBannerBonus.postValue(it)
                },
                onFailure = { throwable ->
                    if (throwable is HttpException) {
                        val gson = com.google.gson.Gson()
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val errorResponse = gson.fromJson(errorBody, HttpException::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }

    fun getCourseList(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getCourse(token) }.fold(
                onSuccess = {
                    _courseList.postValue(it)
                },
                onFailure = { throwable ->
                    if (throwable is HttpException) {
                        val gson = com.google.gson.Gson()
                        val errorBody = throwable.response()?.errorBody()?.string()
                        val errorResponse = gson.fromJson(errorBody, HttpException::class.java)
                        _errorResponse.postValue(errorResponse)
                    } else {
                        _errorBody.postValue(throwable.message)
                    }
                }
            )
        }
    }

    /*fun getAllBasket(token: String, isUsing: Boolean) {
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
    }*/
}