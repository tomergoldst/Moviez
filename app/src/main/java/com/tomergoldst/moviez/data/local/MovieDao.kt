package com.tomergoldst.moviez.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tomergoldst.moviez.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): LiveData<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    fun getAllSync(offset: Int, limit: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun get(id: Long): LiveData<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getSync(id: Long): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>)

    @Update
    fun update(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM movie")
    fun deleteAll()

}
