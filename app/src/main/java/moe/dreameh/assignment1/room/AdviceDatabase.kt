package moe.dreameh.assignment1.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.Category

@Database(entities = [Advice::class, Category::class], version = 2)
abstract class AdviceDatabase : RoomDatabase() {
    abstract fun adviceDao(): AdviceDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AdviceDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AdviceDatabase {
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
                        .addCallback(AdviceDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }

        private class AdviceDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateAdvices(database.adviceDao())
                        populateCategories(database.categoryDao())
                    }
                }
            }
        }
        // For debugging purposes.
        fun populateAdvices(adviceDao: AdviceDao) {
            adviceDao.deleteAll()
            adviceDao.insert(Advice(
                    "Han Kolo",
                    "Lifestyle",
                    "I still get a funny feeling about that old man and the kid. I'm not sure what it is about them, but they're trouble"))

            adviceDao.insert( Advice(
                    "Obi-wan Henobi",
                    "Technology",
                    "These aren't the droids you're looking for."
            ))

            adviceDao.insert(Advice(
                    "Darth Vaber",
                    "Miscellaneous",
                    "Impressive. Most impressive. Obi-Wan has taught you well. You have controlled your fear. Now, release your anger. Only your hatred can destroy me."
            ))
        }

        // For debugging purposes.
        fun populateCategories(categoryDao: CategoryDao) {
            categoryDao.deleteAll()

            categoryDao.insert(Category(1, "Lifestyle"))
            categoryDao.insert(Category(2, "Technology"))
            categoryDao.insert(Category(3, "Miscellaneous"))
        }
    }
}