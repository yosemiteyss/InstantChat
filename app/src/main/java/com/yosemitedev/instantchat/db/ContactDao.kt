package com.yosemitedev.instantchat.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import java.util.*

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact WHERE type = :contactType ORDER BY accessDate DESC")
    fun getContacts(contactType: ContactType): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Query("UPDATE Contact SET accessDate = :date WHERE country_code = :countryCode AND phone_num = :phoneNum")
    suspend fun updateAccessDate(countryCode: String, phoneNum: String, date: Date)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM Contact")
    suspend fun deleteAllContacts()

}