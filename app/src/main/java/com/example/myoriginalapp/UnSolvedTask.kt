package com.example.myoriginalapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

//RealmDatabaseのテーブル設定

open class UnSolvedTask (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    //↑一意なidの生成
    open var taskName: String = "",
    open var taskDeadLine: String= "",
    open var taskCostTime: Int = 0,
    open var isChosen: Boolean = false,
    open var taskRegisterDay: Date = Date(System.currentTimeMillis())
): RealmObject()