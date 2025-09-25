@file:Suppress("ClassName")

package com.crescenzi.jintonic.domain.exceptions

import kotlin.jvm.Throws

enum class JINTONIC_CODE {
    NO_INTERNET, GENERIC, SYSTEM,ROOTED_DEVICE,NON_ROOTED_DEVICE,VPN_REQUIRED
}

@Suppress("unused")
class JintonicException(
    val code: JINTONIC_CODE,
    override val message: String
) : Exception(message)

@Throws(JintonicException::class) // JAVA INTEROP
internal fun drunk(message: String, code: JINTONIC_CODE): Nothing {
    throw JintonicException(code = code, message = message)
}
