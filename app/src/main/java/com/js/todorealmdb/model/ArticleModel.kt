package com.js.todorealmdb.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class ArticleModel : RealmModel{

    @PrimaryKey
    var id : String ? = null

    @Required
    var title : String? = null

    @Required
    var userId : Int? = null

    @Required
    var completed: Boolean? = null
}