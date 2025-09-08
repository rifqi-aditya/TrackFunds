package com.rifqi.trackfunds.core.domain.category.model

enum class StandardCategoryKey(
    val key: String,
    val iconIdentifier: String,
    val type: TransactionType
) {
    // Expense Categories
    FOOD_DRINK("category_food_drink", "food_and_drink", TransactionType.EXPENSE),
    GROCERIES("category_groceries", "groceries", TransactionType.EXPENSE),
    TRANSPORTATION("category_transportation", "transportation", TransactionType.EXPENSE),
    FUEL("category_fuel", "fuel", TransactionType.EXPENSE),
    SHOPPING("category_shopping", "shopping", TransactionType.EXPENSE),
    UTILITIES("category_utilities", "utilities", TransactionType.EXPENSE),
    HOUSING("category_housing", "housing", TransactionType.EXPENSE),
    HEALTH("category_health", "health", TransactionType.EXPENSE),
    INSURANCE("category_insurance", "insurance", TransactionType.EXPENSE),
    PERSONAL_CARE("category_personal_care", "personal_care", TransactionType.EXPENSE),
    ENTERTAINMENT("category_entertainment", "entertainment", TransactionType.EXPENSE),
    EDUCATION("category_education", "education", TransactionType.EXPENSE),
    TRAVEL("category_travel", "travel", TransactionType.EXPENSE),
    SPORTS_FITNESS("category_sports_fitness", "sports_fitness", TransactionType.EXPENSE),
    PETS("category_pets", "pets", TransactionType.EXPENSE),
    GIFTS_DONATIONS("category_gifts_donations", "gifts_donations", TransactionType.EXPENSE),
    FEES_CHARGES("category_fees_charges", "fees_charges", TransactionType.EXPENSE),
    MISCELLANEOUS("category_miscellaneous", "miscellaneous", TransactionType.EXPENSE),

    // Income Categories
    SALARY("category_salary", "salary", TransactionType.INCOME),
    BONUS("category_bonus", "bonus", TransactionType.INCOME),
    INVESTMENT_INCOME("category_investment_income", "investment_income", TransactionType.INCOME),
    GIFTS_RECEIVED("category_gifts_received", "gifts_received", TransactionType.INCOME),
    FREELANCE("category_freelance", "freelance", TransactionType.INCOME),
    OTHER_INCOME("category_other_income", "other_income", TransactionType.INCOME);
}