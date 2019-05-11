package moe.dreameh.lunchlista.persistence

import java.security.Timestamp

data class Cache(val day: String?,
                 val week_number: Int?,
                 val date: String?,
                 val timestamp: Timestamp,
                 val restaurants: MutableList<Restaurant>)