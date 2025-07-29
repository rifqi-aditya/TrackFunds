package com.rifqi.trackfunds.feature.savings.ui.model

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.core.ui.utils.formatLocalDate
import com.rifqi.trackfunds.feature.savings.ui.add_edit_goal.AddEditSavingsGoalState
import java.math.BigDecimal
import java.util.UUID

fun SavingsGoal.toUiModel(): SavingsGoalUiModel {
    return SavingsGoalUiModel(
        id = this.id,
        name = this.name,
        progress = this.progress,
        savedAmountFormatted = formatCurrency(this.savedAmount),
        targetAmountFormatted = formatCurrency(this.targetAmount),
        remainingAmountFormatted = formatCurrency(this.remainingAmount),
        targetDateFormatted = this.targetDate?.let { formatLocalDate(it) },
        iconIdentifier = iconIdentifier
    )
}


/**
 * Mengubah state dari form UI menjadi objek SavingsGoal (domain model).
 * Mengembalikan Result karena proses ini bisa gagal (misal, format angka salah).
 */
fun AddEditSavingsGoalState.toDomainModel(id: String? = null): Result<SavingsGoal> {
    // Lakukan validasi akhir di sini jika perlu, meskipun lebih baik di UseCase validasi
    if (this.goalName.isBlank()) {
        return Result.failure(IllegalArgumentException("Goal name cannot be empty."))
    }
    if (this.iconIdentifier.isBlank()) {
        return Result.failure(IllegalArgumentException("An icon must be selected."))
    }

    return try {
        // Konversi String ke BigDecimal. Ini bisa memicu NumberFormatException.
        val targetDecimal = BigDecimal(this.targetAmount.replace(",", ""))

        if (targetDecimal <= BigDecimal.ZERO) {
            return Result.failure(IllegalArgumentException("Target amount must be positive."))
        }

        val domainModel = SavingsGoal(
            // Jika id null (mode tambah), buat UUID baru. Jika tidak (mode edit), gunakan id yang ada.
            id = id ?: UUID.randomUUID().toString(),
            name = this.goalName,
            targetAmount = targetDecimal,
            // Saat membuat baru, savedAmount adalah 0. Untuk edit, logika ini ada di ViewModel.
            savedAmount = if (this.isEditing) this.originalSavedAmount else BigDecimal.ZERO,
            targetDate = this.targetDate,
            iconIdentifier = this.iconIdentifier,
            isAchieved = false // Logika ini juga bisa lebih kompleks
        )
        Result.success(domainModel)

    } catch (e: NumberFormatException) {
        // Gagal jika 'targetAmount' bukan angka yang valid
        Result.failure(IllegalArgumentException("Invalid target amount format."))
    }
}