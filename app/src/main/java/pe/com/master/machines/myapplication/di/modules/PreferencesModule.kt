package pe.com.master.machines.myapplication.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import pe.com.master.machines.myapplication.helpers.PreferencesManager
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun providePreferences(context: Context): PreferencesManager = PreferencesManager(context)
}