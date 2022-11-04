package com.xtremepixel.instaclone

open class Event<out T>(private val content: T) {

    var hasBeenHandle = false
    private set

    fun getContentOrNull(): T? {
        return if (hasBeenHandle){
            null
        }else {
            hasBeenHandle = true
            content
        }
    }
}