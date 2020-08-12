package com.fatcloud.account.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fatcloud.account.entity.form.p9p10.NativeFormPersonalPackageP9P10Draft
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
        BankPublicDraft::class,
        BankPersonalDraft::class,
        NativeFormPersonalPackageP9P10Draft::class
    ],
    version = 2,
    exportSchema = false
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
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_tax_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `final_money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `taxpayer_number` TEXT, `legal_person_name` TEXT, `id_number` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `detail_address` TEXT, `area` TEXT, `business_license_image_url` TEXT, `business_license_image_file_path` TEXT, PRIMARY KEY(`id`))")

                // 个体户代理记账
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_personal_bookkeeping_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `final_money` TEXT, `product_id` TEXT, `product_price_id` TEXT, `taxpayer_number` TEXT, `legal_person_name` TEXT, `legal_person_phone` TEXT, `id_number` TEXT, `business_license_name` TEXT, `business_license_image_url` TEXT, `business_license_image_file_path` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `detail_address` TEXT, `area` TEXT, PRIMARY KEY(`id`))")

                // 企业套餐
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_enterprise_package_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `product_id` TEXT, `income_money` TEXT, `final_money` TEXT, `product_price_id` TEXT, `select_pid_list` TEXT NOT NULL, `select_pid_name_list` TEXT NOT NULL, `detail_address` TEXT, `area` TEXT, `area_id` TEXT, `zero_name` TEXT, `first_name` TEXT, `second_name` TEXT, `investment_year` TEXT, `invest_money` TEXT, `bank_number` TEXT, `bank_phone` TEXT, `share_holders` TEXT, PRIMARY KEY(`id`))")

                // 银行对公账户草稿
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_bank_public_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `order_work_id` TEXT, `company_name` TEXT, `company_address` TEXT, `registered_capital` TEXT, `account_nature` TEXT, `reconciliation_name` TEXT, `reconciliation_phone` TEXT, `detail_address` TEXT, `area` TEXT, `area_id` TEXT, `share_holders` TEXT, `business_license_url` TEXT, `business_license_path` TEXT, `electronic_seal_url` TEXT, `electronic_seal_path` TEXT, `legal_person_warrant_image_url` TEXT, `legal_person_warrant_image_path` TEXT, `postcode` TEXT, PRIMARY KEY(`id`))")

                // 个人对公账户
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_bank_personal_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `product_id` TEXT, `product_price_id` TEXT, `final_money` TEXT, `bank_name` TEXT, `depositor_name` TEXT, `enterprise_code` TEXT, `address_registered` TEXT, `currency` TEXT, `account_type` TEXT, `account_type_name` TEXT, `address_post` TEXT, `address_detailed` TEXT, `identity_img` TEXT, `license_img` TEXT, `deposit_img` TEXT, `person_legal` TEXT, `person_finance` TEXT, `person_verification_first` TEXT, `person_verification_second` TEXT, `person_reconciliation` TEXT, `order_work_id` TEXT, `mold` TEXT, PRIMARY KEY(`id`))")

                // p9 个体户套餐 p10 个人独资套餐
                database.execSQL("CREATE TABLE IF NOT EXISTS `tb_p9_p10_personal_package_draft` (`id` INTEGER NOT NULL, `login_phone` TEXT, `product_id` TEXT, `product_price_id` TEXT, `final_money` TEXT, `address` TEXT NOT NULL, `area` TEXT NOT NULL, `bank_number` TEXT NOT NULL, `bank_phone` TEXT NOT NULL, `business_scope_id` TEXT, `business_scope_name` TEXT, `capital` TEXT NOT NULL, `employed_number` TEXT NOT NULL, `form_id` TEXT NOT NULL, `form_name` TEXT NOT NULL, `gender` INTEGER NOT NULL, `id_number` TEXT NOT NULL, `id_images_list` TEXT, `name0` TEXT NOT NULL, `name1` TEXT NOT NULL, `name2` TEXT NOT NULL, `nation` TEXT NOT NULL, `real_name` TEXT NOT NULL, `telephone` TEXT NOT NULL, `product_mold` TEXT NOT NULL, PRIMARY KEY(`id`))")
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
     * 企业银行对公账户
     */
    abstract fun bankPublicDraftDao(): BankPublicDraftDao

    /**
     * 个体户银行对公账户
     */
    abstract fun bankPersonalDraftDao(): BankPersonalDraftDao

    /**
     * 个体户套餐，个人独资套餐
     */
    abstract fun p9p10PersonalPackageDao(): P9P10PersonalPackageDao


}