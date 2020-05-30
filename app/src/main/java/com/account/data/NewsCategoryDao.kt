package com.account.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.account.entity.news.NewsCategory

/**
 * 资讯tab 分类
 * @see NewsCategory
 *
 */
@Dao
interface NewsCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllServiceSubTypes(list: List<NewsCategory>)

    /**
     * 获取 tab分类
     */
    @Query("SELECT * FROM news_category ")
    fun findTopServiceSubTypes(): List<NewsCategory>


    @Query("DELETE FROM news_category")
    fun clear()
}