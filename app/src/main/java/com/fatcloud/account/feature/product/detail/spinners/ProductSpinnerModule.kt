package com.fatcloud.account.feature.product.detail.spinners

import dagger.Module
import dagger.Provides


@Module
class ProductSpinnerModule  {

    @Provides
    fun viewProvider(fragment: ProductSpinnerFragment): ProductSpinnerView {
        return fragment
    }
}