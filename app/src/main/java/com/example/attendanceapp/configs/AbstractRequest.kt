package com.example.attendanceapp.configs

import android.util.Log
import com.google.gson.Gson
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.math.BigInteger
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.ArrayList

abstract class AbstractRequest {
    var client = OkHttpClient.Builder()

    init {
        // Connection specs
        val connectionSpecs: ArrayList<ConnectionSpec> = ArrayList()
        Collections.addAll(connectionSpecs, ConnectionSpec.MODERN_TLS)
        Collections.addAll(connectionSpecs, ConnectionSpec.CLEARTEXT)
        Collections.addAll(connectionSpecs, ConnectionSpec.COMPATIBLE_TLS)
        Collections.addAll(connectionSpecs, ConnectionSpec.RESTRICTED_TLS)

        val protocols: ArrayList<Protocol> = ArrayList()
        Collections.addAll(protocols, Protocol.HTTP_1_1, Protocol.HTTP_2)

        // Create a trust manager that does not validate certificate chains
        val trustAllCertificates = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // Unused
                }

                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // Unused
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("TSL")
            sslContext.init(null, trustAllCertificates, SecureRandom())
        } catch (e: NoSuchAlgorithmException){
            Log.e("ssl error", "$e")
        } catch (e: KeyManagementException){
            Log.e("ssl error", "$e")
        }

        client = OkHttpClient.Builder()
            .connectionSpecs(connectionSpecs)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
    }

    open class BigIntegerAdapter {
        @FromJson
        fun fromJson(string: String) = BigInteger(string)

        @ToJson
        fun toJson(value: BigInteger) = value.toString(2)
    }

    open class DateAdapter {
        private lateinit var dateFormat: DateFormat

        init {
            try {
                dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.getDefault())
            } catch (exception: IllegalArgumentException) {
                Log.e("date error", "$exception")
            }
        }

        @ToJson
        fun toJson(date: Date): String {
            return dateFormat.format(date)
        }

        @FromJson
        @Synchronized
        @Throws(ParseException::class)
        open fun jsonToDate(s: String): Date? {
            return dateFormat.parse(s)
        }
    }

    class LinkedHashSetOfStringAdapter {
        @ToJson
        fun toJson(data: LinkedHashMap<String, Array<String>>): String? {
            return JSONObject(data as Map<*, *>).toString()
        }

        @Synchronized
        @FromJson
        @Throws(ParseException::class)
        fun fromJson(s: String): LinkedHashMap<*, *>? {
            return Gson().fromJson(s, LinkedHashMap::class.java)
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, Any?> {
            val delegate: Converter<ResponseBody, Any?> = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
        }

    }
}