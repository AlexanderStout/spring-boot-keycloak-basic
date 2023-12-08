package com.sasha.authservice.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

interface Logging

inline fun <reified T : Logging> T.logger(): Lazy<Logger> {
  return lazy {
    LoggerFactory.getLogger(T::class.java.enclosingClass?.takeIf {
      it.kotlin.companionObject?.java == T::class.java
    } ?: T::class.java)
  }
}