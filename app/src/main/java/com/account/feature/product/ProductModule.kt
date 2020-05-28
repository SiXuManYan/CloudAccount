package com.account.feature.product

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/25 0025 16:57.
 * </br>
 *
 */
@Module
class ProductModule {

    @Provides
    fun viewProvider(fragment: ProductFragment): ProductView {
        return fragment
    }

}