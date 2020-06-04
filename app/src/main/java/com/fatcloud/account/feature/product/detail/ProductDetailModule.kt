package com.fatcloud.account.feature.product.detail

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/5/28 0028 17:55.
 * </br>
 *
 */
@Module
class ProductDetailModule {

    @Provides
    fun viewProvider(activity: ProductDetailActivity): ProductDetailView {
        return activity
    }
    
}