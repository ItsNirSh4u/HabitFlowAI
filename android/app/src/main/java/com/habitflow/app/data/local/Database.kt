package com.habitflow.app.data.local

import androidx.room.*
import com.habitflow.app.data.model.PersonaType

/**
 * Room Database Entities
 */

@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val motivationMsg: String,
    val completionMsg: String,
    val color: String,
    val emoji: String
)

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val completed: Boolean = false,
    val date: String // ISO date string
)

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val selectedPersonaId: String?,
    val onboardingCompleted: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)

/**
 * DAOs (Data Access Objects)
 */

@Dao
interface PersonaDao {
    @Query("SELECT * FROM personas")
    suspend fun getAllPersonas(): List<PersonaEntity>

    @Query("SELECT * FROM personas WHERE id = :personaId")
    suspend fun getPersonaById(personaId: String): PersonaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersona(persona: PersonaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personas: List<PersonaEntity>)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE date = :date ORDER BY id ASC")
    suspend fun getHabitsForDate(date: String): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE date < :cutoffDate")
    suspend fun deleteOldHabits(cutoffDate: String)
}

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getSettings(): UserSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: UserSettingsEntity)
}

/**
 * Room Database
 */
@Database(
    entities = [PersonaEntity::class, HabitEntity::class, UserSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HabitFlowDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDao
    abstract fun habitDao(): HabitDao
    abstract fun userSettingsDao(): UserSettingsDao
}

