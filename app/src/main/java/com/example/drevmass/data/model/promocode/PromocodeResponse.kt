package kz.mobydev.drevmass.model.promocode

import com.google.gson.annotations.SerializedName

data class PromocodeResponse(
    @SerializedName("message")
    val message: String
)
