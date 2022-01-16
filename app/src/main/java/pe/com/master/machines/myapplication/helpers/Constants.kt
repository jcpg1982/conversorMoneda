package pe.com.master.machines.myapplication.helpers

import pe.com.master.machines.myapplication.BuildConfig

class Constants {

    companion object {

        const val PATH_NAME_APP = BuildConfig.APP_NAME
        const val NAME_DATA_BASE: String = PATH_NAME_APP + ".db"
    }

    class Intents {
        companion object {
            const val INTENT_EXTRA_MONEY =
                BuildConfig.APPLICATION_ID + ".INTENT_EXTRA_MONEY"
        }
    }
}