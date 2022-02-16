package com.example.android.politicalpreparedness.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.android.politicalpreparedness.data.source.remote.api.ApiConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.android.politicalpreparedness.data.source.remote.api.interceptors.NetworkStatusInterceptor
import com.example.android.politicalpreparedness.data.source.remote.api.services.CivicsApiService
import com.example.android.politicalpreparedness.data.source.remote.api.jsonadapter.ElectionJsonAdapter
import com.example.android.politicalpreparedness.data.source.remote.api.jsonadapter.LocalDateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(30, TimeUnit.SECONDS)
        return builder
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        app: Application,
        builder: OkHttpClient.Builder,
        networkStatusInterceptor: NetworkStatusInterceptor,
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        builder.addInterceptor { chain ->
            val original = chain.request()
            val url = original
                .url
                .newBuilder()
                .addQueryParameter("key", ApiConstants.API_KEY)
                .build()
            val request = original
                .newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }
        builder.addInterceptor(
            ChuckerInterceptor.Builder(app)
                .collector(ChuckerCollector(app))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        builder.addInterceptor(networkStatusInterceptor)
        builder.addInterceptor(logging)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(LocalDateJsonAdapter())
            .add(ElectionJsonAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {
    @Provides
    fun provideCivicsApiService(retrofit: Retrofit): CivicsApiService =
        retrofit.create(CivicsApiService::class.java)
}
