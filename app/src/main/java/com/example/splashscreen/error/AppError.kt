package com.example.splashscreen.error

/**
 * Represents application-level errors.
 *
 * Responsibility:
 * - Provides one consistent error model for the application.
 * - Prevents UI/ViewModels from depending directly on
 *   Retrofit, Room, or other technical exception types.
 * - Allows the UI to handle errors consistently.
 *
 * Libraries:
 * - Kotlin
 */
sealed class AppError {

    /**
     * Indicates that the application could not communicate
     * with the remote server.
     *
     * Usually caused by:
     * - No internet connection
     * - Connection timeout
     * - DNS failure
     * - Network interruption
     */
    data object Network : AppError()

    /**
     * Indicates an HTTP error returned by the server.
     *
     * @param code HTTP status code returned by the server.
     */
    data class Http(
        val code: Int
    ) : AppError()

    /**
     * Indicates that authentication or authorization failed.
     *
     * Common HTTP codes:
     * - 401 Unauthorized
     * - 403 Forbidden
     */
    data object Unauthorized : AppError()

    /**
     * Indicates that the requested resource does not exist.
     *
     * Common HTTP code:
     * - 404 Not Found
     */
    data object NotFound : AppError()

    /**
     * Indicates that an operation expected data but
     * received an empty result.
     */
    data object EmptyResponse : AppError()

    /**
     * Indicates that a local database operation failed.
     *
     * This normally represents a Room/database problem.
     */
    data object Database : AppError()

    /**
     * Indicates that an unexpected error occurred.
     *
     * This is the fallback error when the application
     * cannot classify the original exception.
     */
    data object Unknown : AppError()
}