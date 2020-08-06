package com.yosemitedev.instantchat.ui.avatar

import androidx.annotation.DrawableRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.repository.ContactRepository
import kotlinx.coroutines.launch

class AvatarViewModel @ViewModelInject constructor(
    private val contactRepository: ContactRepository,
    private val avatarStore: AvatarStore
) : ViewModel() {

    // Avatar List
    val avatars: LiveData<List<Int>> = liveData {
        emit(avatarStore.avatars)
    }

    fun updateContactAvatar(contact: Contact, @DrawableRes avatarRes: Int) {
        viewModelScope.launch {
            contactRepository.updateOrInsertContact(
                contact.copy(avatarRes = avatarRes)
            )
        }
    }
}