package com.js.todorealmdb.repository

import com.js.todorealmdb.api.ApiInterface
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getTodoList(limit: String = "0"): Response<ResponseBody> {
        return apiInterface.getTodoList(limit)
    }

    suspend fun addItemInTodoList(todo: String, completed: String, userId: Int): Response<ResponseBody> {
        return apiInterface.addItemInTodoList(todo, completed, userId)
    }

    suspend fun updateItemInTodoList(id: String, completed: String): Response<ResponseBody> {
        return apiInterface.updateItemInTodoList("1", completed)
    }

    suspend fun deleteItemInTodoList(id: String): Response<ResponseBody> {
        return apiInterface.deleteItemInTodoList("1")
    }
}