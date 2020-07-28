package com.fatcloud.account.feature.product.detail.input

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/28 0028 9:37.
 * </br>
 *
 */
@Module
class ProductInputModule {
    @Provides
    fun viewProvider(fragment: ProductInputFragment): ProductInputView {
        return fragment
    }


}