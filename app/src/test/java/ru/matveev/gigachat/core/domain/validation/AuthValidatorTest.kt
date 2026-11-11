package ru.matveev.gigachat.core.domain.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun validEmail_isAccepted() {
        assertTrue(AuthValidator.isValidEmail("user@example.com"))
        assertTrue(AuthValidator.isValidEmail("  user.name+tag@sub.example.ru  "))
    }

    @Test
    fun invalidEmail_isRejected() {
        assertFalse(AuthValidator.isValidEmail(""))
        assertFalse(AuthValidator.isValidEmail("user"))
        assertFalse(AuthValidator.isValidEmail("user@"))
        assertFalse(AuthValidator.isValidEmail("user@example"))
        assertFalse(AuthValidator.isValidEmail("user @example.com"))
    }

    @Test
    fun passwordShorterThanMinimum_isRejected() {
        assertFalse(AuthValidator.isValidPassword("1234567"))
    }

    @Test
    fun passwordAtMinimumLength_isAccepted() {
        assertTrue(AuthValidator.isValidPassword("12345678"))
    }
}
