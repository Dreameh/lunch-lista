package moe.dreameh.assignment1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advices")
data class Advice(
        @ColumnInfo(name = "author")
        val author: String?,
        @ColumnInfo(name = "category")
        val category: String?,
        @ColumnInfo(name ="content")
        val content: String?
){
    @PrimaryKey(autoGenerate = true) var uid: Int? = null
}
