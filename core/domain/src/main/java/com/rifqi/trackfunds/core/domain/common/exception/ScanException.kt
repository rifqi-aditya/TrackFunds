package com.rifqi.trackfunds.core.domain.common.exception

sealed class ScanException(override val message: String) : Exception(message) {
    /**
     * Thrown when the image is blurry, dark, or contains no readable text.
     */
    class NoTextFound :
        ScanException("Couldn't read text. Please use a clearer, less blurry photo.")

    /**
     * Thrown when there's no internet connection or the server is unreachable.
     */
    class NetworkError :
        ScanException("Connection failed. Please check your internet connection.")

    /**
     * Thrown when the AI could not understand the structure of the receipt.
     */
    class ParsingFailed :
        ScanException("Couldn't understand the receipt. Try a different angle or a clearer photo.")

    /**
     * For all other unexpected errors.
     */
    data class UnknownError(val originalMessage: String?) :
        ScanException("An unexpected error occurred. Please try asdsa again in a moment.")

    class NotAReceipt : ScanException("The uploaded image does not appear to be a receipt.")

    /**
     * Thrown when the AI is not confident enough to process the receipt.
     */
    class LowConfidence(val reason: String = "The AI is not confident enough to process this receipt. Please try again with a clearer image.") :
        ScanException(reason)
}