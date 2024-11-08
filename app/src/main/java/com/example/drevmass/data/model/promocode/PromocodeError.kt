package kz.mobydev.drevmass.model.promocode


import com.google.gson.annotations.SerializedName

data class PromocodeError(
    @SerializedName("code")
    val code: String, // You already activated invite promocode
    @SerializedName("message")
    val message: String // You already activated invite promocode
)