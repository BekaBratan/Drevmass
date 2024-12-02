package com.example.drevmass.data.api

import com.example.drevmass.data.model.AuthorizationResponse
import com.example.drevmass.data.model.LoginRequest
import com.example.drevmass.data.model.MessageResponse
import com.example.drevmass.data.model.RegistrationRequest
import com.example.drevmass.data.model.ResetPasswordRequest
import com.example.drevmass.data.model.User
import com.example.drevmass.data.model.bonus.BonusResponse
import com.example.drevmass.data.model.products.AddToCartRequest
import com.example.drevmass.data.model.products.BasketResponse
import com.example.drevmass.data.model.products.ProductDetailResponse
import com.example.drevmass.data.model.products.ProductsResponse
import com.example.drevmass.data.model.userInfo.UserInfoRequest
import com.example.drevmass.data.model.userInfo.UserInfoResponse
import com.example.drevmass.data.model.userPromocode.UserPromocodeResponse
import com.example.drevmass.data.model.courseModel.getAllBasket.GetAllBasketResponse
import com.example.drevmass.data.model.courseModel.BonusBannerResponse
import com.example.drevmass.data.model.courseModel.CourseInfoResponse
import com.example.drevmass.data.model.courseModel.CourseItemResponse
import com.example.drevmass.data.model.courseModel.FavoriteCourseListResponseItem
import com.example.drevmass.data.model.courseModel.LessonResponse
import com.example.drevmass.data.model.courseModel.MessageCompletedModel
import com.example.drevmass.data.model.courseModel.MessageCourseProgressResponse
import com.example.drevmass.data.model.courseModel.getFamousProductsBasket.getFamousProductsResponse
import com.example.drevmass.data.model.notifiation.NotificationRequest
import com.example.drevmass.data.model.notifiation.NotificationResponse
import kz.mobydev.drevmass.model.promocode.PromocodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body authorization: LoginRequest
    ): AuthorizationResponse

    @POST("signup")
    suspend fun registration(
        @Body registration: RegistrationRequest
    ): AuthorizationResponse

    @FormUrlEncoded
    @POST("forgot_password")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): AuthorizationResponse

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): ProductsResponse

    @GET("products/famous")
    suspend fun getProductsFamous(
        @Header("Authorization") token: String
    ): ProductsResponse

    @GET("products/pricedown")
    suspend fun getProductsPriceDown(
        @Header("Authorization") token: String
    ): ProductsResponse

    @GET("products/priceup")
    suspend fun getProductsPriceUp(
        @Header("Authorization") token: String
    ): ProductsResponse

    @GET("products/{id}")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): ProductDetailResponse

    @POST("basket")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): MessageResponse

    @POST("decrease")
    suspend fun decreaseCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): MessageResponse

    @POST("increase")
    suspend fun increaseCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): MessageResponse

    @GET("basket")
    suspend fun getBasket(
        @Header("Authorization") token: String,
        @Query("is_using") isUsing: String
    ): BasketResponse

    @DELETE("basket")
    suspend fun deleteBasket(
        @Header("Authorization") token: String
    ): MessageResponse

    @GET("bonus")
    suspend fun getBonus(@Header("Authorization") token: String): BonusResponse

    @GET("user/information")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserInfoResponse

    @POST("user/information")
    suspend fun postUserInfo(
        @Header("Authorization") token: String,
        @Body request: UserInfoRequest
    ): UserInfoResponse

    @GET("user/promocode")
    suspend fun getUserPromocode(@Header("Authorization") token: String): UserPromocodeResponse

    @POST("activate")
    @FormUrlEncoded
    suspend fun activatePromocode(
        @Header("Authorization") token: String,
        @Field("promocode") promocode: String
    ): Response<PromocodeResponse>

    @POST("reset_password")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body request: ResetPasswordRequest
    ): MessageResponse

    @GET("course/bonus")
    suspend fun getBonusCourse(@Header("Authorization") token: String): BonusBannerResponse

    @GET("course")
    suspend fun getCourse(@Header("Authorization") token: String): List<CourseItemResponse>

    @GET("course/{course_id}")
    suspend fun getCourseInfo(
        @Header("Authorization") token: String,
        @Path("course_id") course_id: Int
    ): CourseInfoResponse

    @GET("course/{course_id}/lessons")
    suspend fun getCourseLessons(
        @Header("Authorization") token: String,
        @Path("course_id") course_id: Int
    ): List<LessonResponse>

    @POST("course/start/{id}")
    suspend fun startCourse(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): MessageCourseProgressResponse

    @GET("user")
    suspend fun getUser(@Header("Authorization") token: String): User

    @POST("favorites")
    @Multipart
    suspend fun addToFavorite(
        @Header("Authorization") token: String,
        @Part("lesson_id") lessonId: Int
    ): MessageResponse

    @DELETE("favorites/{lessonid}")
    suspend fun deleteFromFavorite(
        @Header("Authorization") token: String,
        @Path("lessonid") lessonId: Int
    ): MessageResponse

    @GET("days/{course_id}")
    suspend fun getNotificationDaysInCourse(
        @Header("Authorization") token: String,
        @Path("course_id") course_id: Int
    ): NotificationResponse

    @POST("days")
    suspend fun updateNotificationByCourse(
        @Header("Authorization") token: String,
        @Body request: NotificationRequest
    ): NotificationResponse

    @POST("days/{course_id}")
    suspend fun deleteNotificationByDay(
        @Header("Authorization") token: String,
        @Path("course_id") course_id: Int):MessageResponse

    @GET("course/{course_id}/lessons/{id}")
    suspend fun getLesson(
        @Header("Authorization") token: String,
        @Path("course_id") course_id: Int,
        @Path("id") id: Int
    ): LessonResponse

    @POST("lessons/complete")
    @FormUrlEncoded
    suspend fun completeLesson(
        @Header("Authorization") token: String,
        @Field("lesson_id") lesson_id: Int,
        @Field("course_id") course_id: Int
    ): MessageCompletedModel


    @GET("products/famous")
    suspend fun getFamousProducts(@Header("Authorization") token: String): List<getFamousProductsResponse>

    @GET("favorites")
    suspend fun getFavoriteCourse(@Header("Authorization") token: String): List<FavoriteCourseListResponseItem>

    @GET("basket")
    suspend fun getAllBasket(@Header("Authorization") token: String, @Query("is_using") isUsing: Boolean): GetAllBasketResponse
}
