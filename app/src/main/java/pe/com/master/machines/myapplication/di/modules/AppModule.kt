package pe.com.master.machines.myapplication.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    private val TAG = AppModule::class.java.simpleName

    @Provides
    @Singleton
    fun provideContext(): Context = context
}