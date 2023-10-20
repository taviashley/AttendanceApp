package com.example.attendanceapp.configs

import com.example.attendanceapp.service.RequestCallBackService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiGateway {
    @JvmStatic
    fun callMeBackCampaign(): RequestCallBackService {
        val retrofit = GetRequest.Builder()
            .setTimeout(setConnectionTimeout())
            .build()
        return retrofit.create(RequestCallBackService::class.java)
    }

    private fun setConnectionTimeout(): OkHttpClient.Builder {
        return OkHttpClient.Builder().readTimeout(40000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(false)
            .connectTimeout(40000, TimeUnit.MILLISECONDS).writeTimeout(40000, TimeUnit.MILLISECONDS)
    }
}