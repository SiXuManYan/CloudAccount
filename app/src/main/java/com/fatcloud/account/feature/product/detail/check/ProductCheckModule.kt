package com.fatcloud.account.feature.product.detail.check

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/10 0010 13:26.
 * </br>
 *
 */
@Module
class ProductCheckModule {

    @Provides
    fun viewProvider(fragment: ProductCheckFragment): ProductCheckView {
        return fragment
    }

}