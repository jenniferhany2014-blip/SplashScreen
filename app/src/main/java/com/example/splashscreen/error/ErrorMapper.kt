package com.example.splashscreen.error

import android.database.SQLException
import retrofit2.HttpException
import java.io.IOException

/**
 * Converts technical exceptions into application-level errors.
 *
 * Responsibility:
 * - Receives exceptions from repositories/ViewModels.
 * - Converts technical exceptions into AppError.
 * - Prevents UI code from depending on Retrofit/Room exceptions.
 * - Provides user-friendly messages for AppError values.
 *
 * Libraries:
 * - Kotlin
 * - Retrofit
 * - Android SQLite
 */
object ErrorMapper {

    /**
     * Converts a Throwable into an application-level AppError.
     *
     * Error mapping:
     *
     * IOException
     *      -> Network
     *
     * HttpException 401/403
     *      -> Unauthorized
     *
     * HttpException 404
     *      -> NotFound
     *
     * Other HttpException
     *      -> Http
     *
     * SQLException
     *      -> Database
     *
     * IllegalStateException
     *      -> EmptyResponse
     *
     * Everything else
     *      -> Unknown
     *
     * @param throwable exception produced by a data operation.
     * @return corresponding AppError.
     */
    fun map(
        throwable: Throwable
    ): AppError {

        return when (throwable) {

            is HttpException -> {

                when (throwable.code()) {

                    401,
                    403 -> {
                        AppError.Unauthorized
                    }

                    404 -> {
                        AppError.NotFound
                    }

                    else -> {
                        AppError.Http(
                            code = throwable.code()
                        )
                    }
                }
            }

            is SQLException -> {
                AppError.Database
            }

            is IOException -> {
                AppError.Network
            }

            is IllegalStateException -> {
                AppError.EmptyResponse
            }

            else -> {
                AppError.Unknown
            }
        }
    }

    /**
     * Converts an AppError into a user-friendly message.
     *
     * The UI should display this message instead of displaying
     * technical exception messages.
     *
     * @param error application-level error.
     * @return message suitable for the user.
     */
    fun userMessage(
        error: AppError
    ): String {

        return when (error) {

            AppError.Network ->
                "Please check your internet connection and try again."

            AppError.Unauthorized ->
                "You are not authorized to perform this action."

            AppError.NotFound ->
                "The requested product was not found."

            AppError.EmptyResponse ->
                "No data was returned."

            AppError.Database ->
                "Unable to access saved data. Please try again."

            is AppError.Http ->
                "Server error (${error.code}). Please try again later."

            AppError.Unknown ->
                "Something went wrong. Please try again."
        }
    }
}