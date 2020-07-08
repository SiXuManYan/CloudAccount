package com.fatcloud.account.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fatcloud.account.entity.local.form.*
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
        EnterprisePackageDraft::class,
        BankPublicDraft::class
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
                        .addMigrations(migration_1_2)
                        .build()
            }
            return instance!!
        }


        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // 个体户营业执照草稿箱
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_license_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `mold` TEXT, `detail_address` TEXT, `area` TEXT, `id_number` TEXT, `final_money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `business_scope_id` TEXT, `business_scope_name` TEXT, `capital` TEXT, `employed_num` TEXT, `form_id` TEXT, `formName` TEXT, `gender` INTEGER, `identity_img` TEXT, `income` TEXT, `zero_name` TEXT, `first_name` TEXT, `second_name` TEXT, `nation` TEXT, `real_name` TEXT, `phone` TEXT, PRIMARY KEY(`id`))")

                // 个体户税务登记
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_tax_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `final_money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `taxpayer_number` TEXT, `legal_person_name` TEXT, `id_number` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `business_license_image_url` TEXT, `detail_address` TEXT, `area` TEXT, PRIMARY KEY(`id`))")


                // 个体户代理记账
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_bookkeeping_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `final_money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `taxpayer_number` TEXT, `legal_person_name` TEXT, `legal_person_phone` TEXT, `id_number` TEXT, `business_license_name` TEXT, `business_license_image_url` TEXT, `business_license_image_file_path` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `detail_address` TEXT, `area` TEXT, PRIMARY KEY(`id`))")

                // 企业套餐
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_enterprise_package_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `product_id` TEXT, `income_money` TEXT, `final_money` TEXT, `product_price_id` TEXT, `select_pid_list` TEXT NOT NULL, `select_pid_name_list` TEXT NOT NULL, `detail_address` TEXT, `area` TEXT, `zero_name` TEXT, `first_name` TEXT, `second_name` TEXT, `investment_year` TEXT, `invest_money` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `share_holders` TEXT, PRIMARY KEY(`id`))")

                // 银行对公账户草稿
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_bank_public_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `order_work_id` TEXT, `company_name` TEXT, `company_address` TEXT, `registered_capital` TEXT, `account_nature` TEXT, `reconciliation_name` TEXT, `reconciliation_phone` TEXT, `detail_address` TEXT, `area` TEXT, `business_license_url` TEXT, `electronic_seal_url` TEXT, `id_number` TEXT, `id_number_image_front_url` TEXT, `id_number_image_back_url` TEXT, `finance_name` TEXT, `finance_phone` TEXT, `finance_shares` TEXT, `legalPersonWarrantImgUrl` TEXT, PRIMARY KEY(`id`))")
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

    /**
     * 银行对公账户
     */
    abstract fun bankPublicDraftDao(): BankPublicDraftDao


}