package com.coooldoggy.imagesearchcompose.di

import android.content.Context
import androidx.room.Room
import com.coooldoggy.imagesearchcompose.KAKAO_BASE_URL
import com.coooldoggy.imagesearchcompose.NAVER_BASE_URL
import com.coooldoggy.imagesearchcompose.data.database.FavoriteDB
import com.coooldoggy.imagesearchcompose.data.database.FavoriteDao
import com.coooldoggy.imagesearchcompose.data.network.KakaoImageService
import com.coooldoggy.imagesearchcompose.data.network.NaverImageService
import com.coooldoggy.weverseimagesearch.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Naver

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KaKao

    @Singleton
    @Provides
    @Naver
    fun provideNaverRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NAVER_BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    @Naver
    fun provideNaverApiService(@Naver retrofit: Retrofit): NaverImageService {
        return retrofit.create(NaverImageService::class.java)
    }

    @Singleton
    @Provides
    @KaKao
    fun provideKaKaoRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(KAKAO_BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    @KaKao
    fun provideKakaoApiService(@KaKao retrofit: Retrofit): KakaoImageService {
        return retrofit.create(KakaoImageService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(favoriteDB: FavoriteDB): FavoriteDao = favoriteDB.favoriteDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): FavoriteDB {
        return Room.databaseBuilder(
            context = context,
            klass = FavoriteDB::class.java,
            name = "favorite_db",
        ).fallbackToDestructiveMigration().build()
    }
}
