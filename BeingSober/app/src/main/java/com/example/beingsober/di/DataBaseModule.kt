package com.example.beingsober.di

import android.content.Context
import androidx.room.Room
import com.example.beingsober.data.local.BeingSoberDatabase
import com.example.beingsober.data.local.dao.IncidentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BeingSoberDatabase {

        return Room.databaseBuilder(
            context,
            BeingSoberDatabase::class.java,
            "being_sober_database"
        ).build()
    }

    @Provides
    fun provideIncidentDao(
        database: BeingSoberDatabase
    ): IncidentDao {

        return database.incidentDao()
    }
}