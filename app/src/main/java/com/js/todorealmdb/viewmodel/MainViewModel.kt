package com.js.todorealmdb.viewmodel

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.js.todorealmdb.api.ApiInterface
import com.js.todorealmdb.model.ArticleModel
import com.js.todorealmdb.repository.Repository
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.Realm
import io.realm.kotlin.deleteFromRealm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var realm = Realm.getDefaultInstance()

    val allArticle by lazy {
        MutableLiveData<List<ArticleModel>>()
    }

    fun addArticle(title: String, completed: Boolean, activity: Activity) {

        realm.executeTransaction {
            val article = it.createObject(ArticleModel::class.java, UUID.randomUUID().toString())

            article.title = title
            article.completed = completed

            realm.insertOrUpdate(article)
        }


        viewModelScope.launch {
            val userId = 101
            repository.addItemInTodoList(title, completed.toString(), userId).apply {
                if(this.isSuccessful) {
                    DynamicToast.makeSuccess(activity, "TODO ADDED", Toast.LENGTH_SHORT).show()
                } else {
                    DynamicToast.makeError(activity, "API ISSUE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getAllArticles() {
        val article = realm.where(ArticleModel::class.java).findAll()
        allArticle.value = realm.copyFromRealm(article)
    }

    fun deleteArticle(id: String, activity: Activity) {

        val article = realm.where(ArticleModel::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            article?.deleteFromRealm()
        }


        viewModelScope.launch {


            repository.deleteItemInTodoList("1").apply {
                if(this.isSuccessful) {

                    DynamicToast.makeSuccess(activity, "TODO DELETED", Toast.LENGTH_SHORT).show()
                } else {
                    DynamicToast.makeError(activity, "API ISSUE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun updateArticle(id: String, title: String, completed: Boolean, activity: Activity) {

        val article = realm.where(ArticleModel::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            article?.title = title
            article?.completed = completed
            if (article != null) {
                realm.insertOrUpdate(article)
            }
        }

        viewModelScope.launch {
            repository.updateItemInTodoList(id, completed.toString()).apply {
                if(this.isSuccessful) {

                    DynamicToast.makeSuccess(activity, "TODO UPDATED", Toast.LENGTH_SHORT).show()
                } else {
                    DynamicToast.makeError(activity, "API ISSUE", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}