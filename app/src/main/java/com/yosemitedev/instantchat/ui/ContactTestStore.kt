package com.yosemitedev.instantchat.ui

import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType.*
import java.util.*

object ContactTestStore {

    val contacts = listOf(
        Contact("852", "123456", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("852", "123457", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("852", "123458", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("852", "123459", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("852", "123410", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("852", "123411", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("852", "123412", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("852", "123413", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123414", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("853", "123415", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123416", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("853", "123417", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123418", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("853", "123419", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123420", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("853", "123421", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123422", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE),
        Contact("853", "123423", Date(), null, AvatarStore.getRandomAvatar(), WORK),
        Contact("853", "123424", Date(), null, AvatarStore.getRandomAvatar(), PRIVATE)
    )
}