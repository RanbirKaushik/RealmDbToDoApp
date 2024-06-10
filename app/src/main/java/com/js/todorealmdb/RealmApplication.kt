package com.js.todorealmdb

import android.app.Application
import com.pluto.Pluto
import com.pluto.plugins.network.PlutoNetworkPlugin
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
//import io.realm.kotlin.Realm
//import io.realm.kotlin.RealmConfiguration

//import io.realm.kotlin.Realm
//import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp

class RealmApplication : Application() {

    private var config : RealmConfiguration? = null


    override fun onCreate() {
        super.onCreate()

        Pluto.Installer(this)
            .addPlugin(PlutoNetworkPlugin("1"))
        .install()

//        Realm.init(this)

        Realm.init(this)

        config = RealmConfiguration.Builder()
            .name("article.db")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .build()


        config.let {
            Realm.setDefaultConfiguration(it)
        }
    }

}