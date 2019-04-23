package moe.dreameh.assignment1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(@PrimaryKey
                    @ColumnInfo(name = "id")
                    val id: Int?,
                    @ColumnInfo(name = "name")
                    val name: String?)