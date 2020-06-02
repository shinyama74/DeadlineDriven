package com.example.myoriginalapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


class RealmTaskApp :Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig:RealmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        //Realm.deleteRealm(realmConfiguration) ←Delete Realm between app restart
        Realm.setDefaultConfiguration((realmConfig))

    }
}