package moe.dreameh.lunchlista.persistence

data class Results(val status: Int,
                 val message: String,
                 val cache: Cache)


data class Cache(val day: String?,
                  val week_number: Int?,
                  val date: String?,
                  val timestamp: Int,
                  val restaurants: MutableList<Restaurant>)