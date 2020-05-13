package com.yosemitedev.instantchat.ui.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yosemitedev.instantchat.viewmodel.ContactViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ContactViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ContactViewModelKey(ContactViewModel::class)
    abstract fun bindContactViewModel(contactViewModel: ContactViewModel): ViewModel

    @Binds
    @IntoMap
    @ContactViewModelKey(ContactEditViewModel::class)
    abstract fun bindContactEditViewModel(contactEditViewModel: ContactEditViewModel): ViewModel
}