package com.sasha.authservice.delegates

import com.sasha.authservice.extensions.Logging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>)
            = getLogger(getClassForLogging(thisRef.javaClass))
}

inline fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}


fun getLogger(forClass: Class<*>): Logger =
    LoggerFactory.getLogger(forClass)
