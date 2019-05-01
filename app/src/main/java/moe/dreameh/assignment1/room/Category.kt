package moe.dreameh.assignment1.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(@PrimaryKey
                    @ColumnInfo(name = "id")
                    val id: Int?,
                    @ColumnInfo(name = "name")
                    val name: String?)