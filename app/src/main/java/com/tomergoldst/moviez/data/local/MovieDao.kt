package com.tomergoldst.moviez.data.local

import androidx.room.*
import com.tomergoldst.moviez.model.Movie
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): Observable<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    fun getAllSync(offset: Int, limit: Int): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun get(id: Long): Observable<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getSync(id: Long): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<Movie>): Completable

    @Update
    fun update(movie: Movie): Completable

    @Delete
    fun delete(movie: Movie): Completable

    @Query("DELETE FROM movie")
    fun deleteAll(): Completable

}
