package com.example.naturespring

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/** Turns a Firebase exception into a short message the user can understand. */
fun friendlyAuthError(e: Exception?): String = when (e) {
    is FirebaseAuthWeakPasswordException -> "Password is too weak. Use at least 6 characters."
    is FirebaseAuthUserCollisionException -> "This email is already registered. Please log in."
    is FirebaseAuthInvalidUserException,
    is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
    is FirebaseNetworkException -> "No internet connection. Please try again."
    else -> e?.localizedMessage ?: "Something went wrong. Please try again."
}
