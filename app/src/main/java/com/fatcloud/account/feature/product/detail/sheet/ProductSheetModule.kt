package com.fatcloud.account.feature.product.detail.sheet

import dagger.Module
import dagger.Provides


@Module
class ProductSheetModule  {

    @Provides
    fun viewProvider(fragment: ProductSheetFragment): ProductSheetView {
        return fragment
    }
}