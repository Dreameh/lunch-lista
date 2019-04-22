package moe.dreameh.assignment1.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import moe.dreameh.assignment1.Advice

@Database(entities = [Advice::class], version = 1)
abstract class AdviceDatabase : RoomDatabase() {
    abstract fun adviceDao(): AdviceDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}