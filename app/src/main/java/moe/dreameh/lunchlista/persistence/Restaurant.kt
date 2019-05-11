package moe.dreameh.lunchlista.persistence

data class Restaurant(
    val id: Int?,
    val image: String?,
    val name: String?,
    val info: String?,
    val address: String?,
    val phone: String?,
    val menu: String?
)