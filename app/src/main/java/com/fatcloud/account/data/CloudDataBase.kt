package com.fatcloud.account.data

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import android.content.Context
import com.fatcloud.account.entity.local.form.EnterprisePackageDraft
import com.fatcloud.account.entity.local.form.PersonalBookkeepingDraft
import com.fatcloud.account.entity.local.form.PersonalLicenseDraft
import com.fatcloud.account.entity.local.form.PersonalTaxDraft
import com.fatcloud.account.entity.news.NewsCategory
import com.fatcloud.account.entity.users.User


/**
 * 应用内数据库
 * 创建新表时 实体类 + 实体类Dao接口 后在此添加
 */
@Database(
    entities = [
        User::class,
        NewsCategory::class,
        PersonalLicenseDraft::class,
        PersonalTaxDraft::class,
        PersonalBookkeepingDraft::class,
        EnterprisePackageDraft::class
    ],
    version = 2
//    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CloudDataBase : RoomDatabase() {

    companion object {
        private const val DatabaseFileName = "CloudAccount.db"
        private var instance: CloudDataBase? = null

        @Synchronized
        fun get(context: Context): CloudDataBase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, CloudDataBase::class.java, DatabaseFileName)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
//                        .addMigrations(migration_1_2)
                        .build()
            }
            return instance!!
        }


        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // 个体户营业执照草稿箱
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_license_draft` (`id` INTEGER NOT NULL, `mold` TEXT, `login_user_phone` TEXT, `address` TEXT, `area` TEXT, `id_number` TEXT, `money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `business_scope_id` TEXT, `capital` TEXT, `employed_num` TEXT, `form` INTEGER, `gender` TEXT, `imgs` TEXT, `income` TEXT, `name0` TEXT, `name1` TEXT, `name2` TEXT, `nation` TEXT, `real_name` TEXT, `tel` TEXT, PRIMARY KEY(`id`))")

                // todo 个体户税务登记草稿箱

                // todo 个体户代理记账草稿箱

            }
        }

    }

    abstract fun userDao(): UserDao
    abstract fun newsCategoryDao(): NewsCategoryDao

    /**
     * 个体户营业执照草稿箱 db
     */
    abstract fun personalLicenseDraftDao(): PersonalLicenseDraftDao

    /**
     * 个体户税务登记 草稿箱 db
     */
    abstract fun personalTaxDraftDao(): PersonalTaxDraftDao

    /**
     * 个体户代理记账 草稿箱 db
     */
    abstract fun personalBookkeepingDraftDao(): PersonalBookkeepingDraftDao


    /**
     * 企业套餐
     */
    abstract fun enterprisePackageDraftDao(): EnterprisePackageDraftDao


}