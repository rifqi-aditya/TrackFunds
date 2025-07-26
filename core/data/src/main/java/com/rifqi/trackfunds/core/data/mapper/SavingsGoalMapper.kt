package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.SavingsGoalEntity
import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel

fun SavingsGoalEntity.toDomain(): SavingsGoalModel {
    return SavingsGoalModel(
        id = this.id,
        name = this.name,
        targetAmount = this.targetAmount,
        currentAmount = this.currentAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}

fun SavingsGoalModel.toEntity(userUid: String): SavingsGoalEntity {
    return SavingsGoalEntity(
        id = this.id,
        userUid = userUid,
        name = this.name,
        targetAmount = this.targetAmount,
        currentAmount = this.currentAmount,
        targetDate = this.targetDate,
        iconIdentifier = this.iconIdentifier,
        isAchieved = this.isAchieved
    )
}