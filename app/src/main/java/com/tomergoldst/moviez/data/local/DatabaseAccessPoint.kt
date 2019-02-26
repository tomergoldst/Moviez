package com.tomergoldst.moviez.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomergoldst.moviez.model.Movie

@Database(entities = [Movie::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class DatabaseAccessPoint : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {

        private const val DATABASE_NAME = "com.tomergoldst.moviez.db"

        @Volatile
        private var sInstance: DatabaseAccessPoint? = null

        fun getDatabase(context: Context): DatabaseAccessPoint {
            if (sInstance == null) {
                synchronized(DatabaseAccessPoint::class.java) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseAccessPoint::class.java,
                            DATABASE_NAME
                        )
                            .build()
                    }
                }
            }
            return sInstance!!
        }
    }

}
