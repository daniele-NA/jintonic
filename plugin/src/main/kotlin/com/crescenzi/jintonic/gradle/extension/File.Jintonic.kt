package com.crescenzi.jintonic.gradle.extension

import java.io.File

fun File?.isDirectoryEmpty() = ((this == null) || (list() == null) || list().isEmpty())