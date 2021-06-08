package com.yosemitedev.instantchat.ui.avatar

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val avatarStore: AvatarStore
) : ViewModel() {

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