package com.yassir.test.states

import com.yassir.test.models.details.DetailsModel

sealed class UiStates {
    object  EMPTY : UiStates()
    object LOADING : UiStates()
    data class SUCCESS(var data : DetailsModel) : UiStates()
    data class ERROR(var error : String) : UiStates()

}