package pe.com.master.machines.myapplication.di.dependencies

import dagger.Component
import pe.com.master.machines.myapplication.di.modules.AppModule
import pe.com.master.machines.myapplication.di.modules.PreferencesModule
import pe.com.master.machines.myapplication.di.modules.SqliteModule
import pe.com.master.machines.myapplication.ui.activities.activityListMoney.ActivityListMoney
import pe.com.master.machines.myapplication.ui.activities.loadingActivity.LoadingActivity
import pe.com.master.machines.myapplication.ui.fragments.conversorFragment.ConversorFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        PreferencesModule::class,
        SqliteModule::class]
)
interface AppComponent {

    fun inject(activity: LoadingActivity)

    fun inject(activity: ActivityListMoney)


    fun inject(fragment: ConversorFragment)
}