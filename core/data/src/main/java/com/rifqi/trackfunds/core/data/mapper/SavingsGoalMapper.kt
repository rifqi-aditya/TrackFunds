package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.SavingsGoalEntity
import com.rifqi.trackfunds.core.domain.model.SavingsGoal

fun SavingsGoalEntity.toDomain(): SavingsGoal {
    return SavingsGoal(
        id = this.id,
        name = this.name,
        targetAmount = this.targetAmount,
        currentAmount = this.currentAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}

fun SavingsGoal.toEntity(): SavingsGoalEntity {
    return SavingsGoalEntity(
        id = this.id,
        name = this.name,
        targetAmount = this.targetAmount,
        currentAmount = this.currentAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}