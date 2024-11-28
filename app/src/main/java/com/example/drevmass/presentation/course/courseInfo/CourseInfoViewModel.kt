package com.example.drevmass.presentation.course.courseInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drevmass.data.api.ServiceBuilder
import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.User
import com.example.drevmass.data.model.courseModel.CourseInfoResponse
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.example.drevmass.data.model.courseModel.MessageCourseProgressResponse
import com.example.drevmass.data.model.notifiation.NotificationRequest
import com.example.drevmass.data.model.notifiation.NotificationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CourseInfoViewModel(): ViewModel() {
    private var _responseCourseById = MutableLiveData<CourseInfoResponse>()
    val responseCourseById: MutableLiveData<CourseInfoResponse> = _responseCourseById

    private var _responseGetUser = MutableLiveData<User>()
    val responseGetUser: MutableLiveData<User> = _responseGetUser

    private var _responseCourseLessons = MutableLiveData<List<LessonResponse>>()
    val responseCourseLessons: MutableLiveData<List<LessonResponse>> = _responseCourseLessons

    private var _responseMessageCourseFragment = MutableLiveData<MessageCourseProgressResponse>()
    val responseMessageCourseFragment: MutableLiveData<MessageCourseProgressResponse> =_responseMessageCourseFragment

    private var _listDaysBottomSheetSelected = MutableLiveData<List<String>>()
    val listDaysBottomSheetSelected: MutableLiveData<List<String>> = _listDaysBottomSheetSelected

    private var _responseNotificationDaysInCourse = MutableLiveData<NotificationResponse>()
    val responseNotificationDaysInCourse: MutableLiveData<NotificationResponse> = _responseNotificationDaysInCourse

    private var _responseSuccessUpdateNotificationDaysInCourse = MutableLiveData<NotificationResponse>()
    val responseSuccessUpdateNotificationDaysInCourse: MutableLiveData<NotificationResponse> = _responseSuccessUpdateNotificationDaysInCourse

    private var _responseNotificationAllCourse = MutableLiveData<List<NotificationResponse>>()
    val responseNotificationAllCourse: MutableLiveData<List<NotificationResponse>> = _responseNotificationAllCourse

    private var _responseDeleteNotificationByDay = MutableLiveData<MessageResponse>()
    val responseDeleteNotificationByDay: MutableLiveData<MessageResponse> = _responseDeleteNotificationByDay

    private var _errorResponse: MutableLiveData<AuthorizationResponse?> = MutableLiveData()
    val errorResponse: MutableLiveData<AuthorizationResponse?> = _errorResponse

    private var _errorBody: MutableLiveData<String?> = MutableLiveData()
    val errorBody: LiveData<String?> = _errorBody

    fun getUserCourseId(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getCourseInfo(token, courseId) }.fold(
                onSuccess = {
                    _responseCourseById.postValue(it)
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

    fun getCourseLessons(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getCourseLessons(token, courseId) }.fold(
                onSuccess = {
                    _responseCourseLessons.postValue(it)
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

    fun startCourse(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.startCourse(token, courseId) }.fold(
                onSuccess = {
                    _responseMessageCourseFragment.postValue(it)
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

    fun getUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getUser(token) }.fold(
                onSuccess = {
                    _responseGetUser.postValue(it)
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

    fun addFavoriteLesson(token: String,courseId: Int,  lessonId: Int){
        Log.d("AAA","VM CLICK $lessonId")
        Log.d("AAA", "cInfoViewModel - addFavoriteLesson - C - ${courseId} - L - $lessonId")
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.addToFavorite(token, lessonId) }.fold(
                onSuccess = {
                    Log.d("AAA", "cInfoViewModel - WIN addFavoriteLesson - C - ${courseId} - L - $lessonId")
                    getCourseLessons(token,courseId)
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

    fun removeFavoriteLesson(token: String,courseId: Int, lessonId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.deleteFromFavorite(token, lessonId) }.fold(
                onSuccess = {
                    Log.d("AAA", "CinfoViewModel - removeFavoriteLesson - C - ${courseId} - L - $lessonId")
                    getCourseLessons(token,courseId)
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

    fun updateNotificationByCourse (token: String, notificationRequest: NotificationRequest){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.updateNotificationByCourse(token, notificationRequest) }.fold(
                onSuccess = {
                    _responseSuccessUpdateNotificationDaysInCourse.postValue(it)
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

    fun deleteNotificationByDayAPI(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.deleteNotificationByDay(token, courseId) }.fold(
                onSuccess = {
                    _responseDeleteNotificationByDay.postValue(it)
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

    fun getNotificationDaysInCourse(token: String, courseId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ServiceBuilder.api.getNotificationDaysInCourse(token, courseId) }.fold(
                onSuccess = {
                    _responseNotificationDaysInCourse.postValue(it)
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