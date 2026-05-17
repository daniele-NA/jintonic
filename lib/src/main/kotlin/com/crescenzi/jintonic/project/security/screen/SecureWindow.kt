package com.crescenzi.jintonic.project.security.screen

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
/**
 NO EXTRA PERMISSION REQUIRED

 It Allows to block Screenshot & Screen Recording

 @SecureWindow
 class MyActivity:Activity{}

 */
annotation class SecureWindow()
