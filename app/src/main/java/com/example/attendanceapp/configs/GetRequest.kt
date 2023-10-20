package com.example.attendanceapp.configs

import com.example.attendanceapp.Definitions
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

 class GetRequest : AbstractRequest() {

    class Builder {
        private val request: GetRequest = GetRequest()
        private val retrofit: Retrofit.Builder

        init {
            val moshi = Moshi.Builder()
                .add(DateAdapter())
                .add(LinkedHashSetOfStringAdapter())
                .build()
            retrofit = Retrofit.Builder()
                .baseUrl(Definitions.USER_PIN_WEBSERVICE)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        }

        fun setBaseUrl(url: String?): Builder {
            if (url != null) {
                retrofit.baseUrl(url)
            }
            return this
        }

        fun setTimeout(builder: OkHttpClient.Builder): Builder {
            retrofit.client(builder.build())
            return this
        }

        fun build(): Retrofit {
            return retrofit.build()
        }
    }
}