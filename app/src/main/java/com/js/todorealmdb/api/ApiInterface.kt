package com.js.todorealmdb.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET
    suspend fun getTodoList(
        @Query("limit") limit: String,
    ): Response<ResponseBody>

    @Multipart
    @POST("add")
    suspend fun addItemInTodoList(
        @Part("todo") todo: String,
        @Part("completed") completed: String,
        @Part("userId") userId: Int,
    ): Response<ResponseBody>


    @Multipart
    @PUT("{id}")
    suspend fun updateItemInTodoList(
        @Path("id") id: String,
        @Part("completed") completed: String,
    ): Response<ResponseBody>

    @DELETE("{id}")
    suspend fun deleteItemInTodoList(
        @Path("id") id: String
    ): Response<ResponseBody>
}