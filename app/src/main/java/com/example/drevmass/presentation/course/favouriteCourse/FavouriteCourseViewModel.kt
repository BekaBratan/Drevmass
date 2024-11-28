package com.example.drevmass.presentation.course.favouriteCourse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.courseModel.FavoriteCourseListResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavouriteCourseViewModel(): ViewModel() {

    private var _responseFavoriteList = MutableLiveData<List<FavoriteCourseListResponseItem>>()
    val responseFavoriteList: MutableLiveData<List<FavoriteCourseListResponseItem>> = _responseFavoriteList

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getFavoriteCoursesAndLessons(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getFavoriteCourse(token) }.fold(
                onSuccess = {
                    _responseFavoriteList.postValue(it)
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

    fun addFavoriteLesson(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.addToFavorite(token, courseId) }.fold(
                onSuccess = {
                    getFavoriteCoursesAndLessons(token)
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

    fun removeFavoriteLesson(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.deleteFromFavorite(token, courseId) }.fold(
                onSuccess = {
                    getFavoriteCoursesAndLessons(token)
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