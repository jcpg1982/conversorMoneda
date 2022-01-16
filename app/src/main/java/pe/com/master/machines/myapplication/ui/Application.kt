package pe.com.master.machines.myapplication.ui

import androidx.multidex.MultiDexApplication
import pe.com.master.machines.myapplication.di.dependencies.AppComponent
import pe.com.master.machines.myapplication.di.dependencies.DaggerAppComponent
import pe.com.master.machines.myapplication.di.modules.AppModule

class Application : MultiDexApplication() {

    companion object {

        private var INSTANCE: Application? = null

        private var APP_COMPONENT: AppComponent? = null

        fun getInstanceApp(): Application {
            return INSTANCE ?: synchronized(this) {
                val instance by lazy {
                    Application()
                }
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun getAppComponent(): AppComponent {
        return APP_COMPONENT ?: synchronized(this) {
            val appComponent by lazy {
                DaggerAppComponent
                    .builder()
                    .appModule(AppModule(getInstanceApp()))
                    .build()
            }
            APP_COMPONENT = appComponent
            appComponent
        }
    }
}