package com.account.app

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
* App Dagger2注解组件
*/
@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, ActivityBindModule::class])
interface AppComponent : AndroidInjector<CloudAccountApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: CloudAccountApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(application: CloudAccountApplication)
}