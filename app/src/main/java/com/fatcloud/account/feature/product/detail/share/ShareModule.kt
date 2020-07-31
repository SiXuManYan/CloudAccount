package com.fatcloud.account.feature.product.detail.share

import dagger.Module
import dagger.Provides

/**
 * Created by Wangsw on 2020/7/31 0031 16:08.
 * </br>
 *
 */
@Module
class ShareModule {

    @Provides
    fun viewProvider(fragment: ShareFragment): ShareView {
        return fragment
    }

}