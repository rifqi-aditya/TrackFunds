package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.SavingsGoalEntity
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal

fun SavingsGoalEntity.toDomain(): SavingsGoal {
    return SavingsGoal(
        id = this.id,
        name = this.name,
        targetAmount = this.targetAmount,
        savedAmount = this.savedAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}

fun SavingsGoal.toEntity(userUid: String): SavingsGoalEntity {
    return SavingsGoalEntity(
        id = this.id,
        userUid = userUid,
        name = this.name,
        targetAmount = this.targetAmount,
        savedAmount = this.savedAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}