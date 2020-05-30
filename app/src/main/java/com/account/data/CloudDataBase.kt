package com.account.data

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.account.R
import com.account.entity.news.NewsCategory
import com.account.entity.users.User


/**
 * 应用内数据库
 * 创建新表时 实体类 + 实体类Dao接口 后在此添加
 */
@Database(entities = [User::class, NewsCategory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CloudDataBase : RoomDatabase() {

    companion object {
        private const val DatabaseFileName = "CloudAccount.db"
        private var instance: CloudDataBase? = null

        @Synchronized
        fun get(context: Context): CloudDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, CloudDataBase::class.java, DatabaseFileName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
//                        .addMigrations(migration_1_2)
                    .build()
            }
            return instance!!
        }



        /**
         * version 1 -> 2 user表增加字段
         */
        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tb_user  ADD COLUMN wechatBindFlag INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE tb_user  ADD COLUMN wechatNickname TEXT DEFAULT '' ")
                database.execSQL("ALTER TABLE tb_user  ADD COLUMN passwordSetFlag INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE tb_user  ADD COLUMN photoSetFlag INTEGER NOT NULL DEFAULT 0")

                // 数据库BUG 会不识别code 字段
                database.execSQL("drop table tb_activity ")
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_activity` (`code` INTEGER NOT NULL, `name` TEXT, PRIMARY KEY(`code`))")
            }
        }

    }

    abstract fun userDao(): UserDao
    abstract fun newsCategoryDao(): NewsCategoryDao


}