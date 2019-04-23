package moe.dreameh.assignment1.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.Category

@Database(entities = arrayOf(Advice::class, Category::class), version = 1)
abstract class AdviceDatabase : RoomDatabase() {
    abstract fun adviceDao(): AdviceDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AdviceDatabase? = null

        fun getDatabase(context: Context): AdviceDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AdviceDatabase::class.java,
                        "Advice_database"
                ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}