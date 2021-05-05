package com.android.motive.utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED

}

class State(val status: Status, val msg: String) {

    companion object {

        val END :State = State(Status.FAILED, "Masalah endlist")
        val LOADED: State =State(Status.SUCCESS, "Status data sukses")
        val LOADING: State = State(Status.RUNNING, "Status data running")
        val ERROR: State = State(Status.FAILED, "Sesuatu yang salah :(")

    }
}