package com.rifqi.trackfunds.core.ui.util


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.rifqi.trackfunds.core.ui.R

/**
 * Memetakan String identifier ke @DrawableRes Int.
 * Identifier HARUS cocok dengan nama file drawable setelah "ic_" dan sebelum ".xml".
 */

@DrawableRes
fun mapIconIdentifierToDrawableRes(identifier: String?): Int {
    return when (identifier?.trim()?.lowercase()) {
        "music" -> R.drawable.ic_music
        "electricity" -> R.drawable.ic_light_on
        "parking" -> R.drawable.ic_parcking_ticket
        "gift" -> R.drawable.ic_christmas_gift
        "parking_meter" -> R.drawable.ic_parking_meter
        "community_grants" -> R.drawable.ic_community_grants
        "water" -> R.drawable.ic_water
        "gas" -> R.drawable.ic_gas_station
        "shopping" -> R.drawable.ic_shopping_cart
        "insurance" -> R.drawable.ic_protect
        "event_accepted" -> R.drawable.ic_event_accepted
        "internet" -> R.drawable.ic_wi_fi
        "doctor" -> R.drawable.ic_medical_doctor
        "restaurant" -> R.drawable.ic_restaurant
        "road" -> R.drawable.ic_road
        "documentary" -> R.drawable.ic_documentary
        "shuttle_bus" -> R.drawable.ic_shuttle_bus
        "scooter" -> R.drawable.ic_scooter
        "shopping_bag" -> R.drawable.ic_shopping_bag
        "dumbbell" -> R.drawable.ic_dumbbell
        "family_clothes" -> R.drawable.ic_family_clothes
        "e_learning" -> R.drawable.ic_e_learning
        "iphone_14" -> R.drawable.ic_iphone_14
        "hangouts" -> R.drawable.ic_hangouts
        "game_controller" -> R.drawable.ic_game_controller
        "taxi" -> R.drawable.ic_taxi
        "multiple_devices" -> R.drawable.ic_multiple_devices
        "pills" -> R.drawable.ic_pills
        "ingredients" -> R.drawable.ic_ingredients
        "theme_park" -> R.drawable.ic_theme_park
        "hospital" -> R.drawable.ic_hospital
        "train" -> R.drawable.ic_train
        "money_transfer" -> R.drawable.ic_money_transfer
        "micro" -> R.drawable.ic_micro
        "netflix" -> R.drawable.ic_netflix
        "car" -> R.drawable.ic_car
        "cafe" -> R.drawable.ic_cafe
        "stack_of_money" -> R.drawable.ic_stack_of_money
        "growing_money" -> R.drawable.ic_growing_money
        "car_service" -> R.drawable.ic_car_service
        "online_store" -> R.drawable.ic_online_store
        "workspace" -> R.drawable.ic_workspace
        "fiat_500" -> R.drawable.ic_fiat_500
        "books" -> R.drawable.ic_books
        "home" -> R.drawable.ic_home
        "wallet_account" -> R.drawable.ic_wallet
        "person" -> R.drawable.ic_person
        "earbud_case" -> R.drawable.ic_earbud_case
        "diversity" -> R.drawable.ic_diversity
        "cash" -> R.drawable.ic_cash
        "sorting" -> R.drawable.ic_sorting
        "calendar" -> R.drawable.ic_calendar
        "arrow_back_ios_new" -> R.drawable.ic_arrow_back_ios_new
        "close" -> R.drawable.ic_close
        "date_range" -> R.drawable.ic_date_range
        "payments" -> R.drawable.ic_payments
        "manual_transaction" -> R.drawable.ic_manual_transaction
        "scan_receipt" -> R.drawable.ic_scan_receipt
        "budgets" -> R.drawable.ic_budgets
        else -> R.drawable.ic_diversity
    }
}

/**
 * Composable helper untuk menampilkan ikon berdasarkan identifier string dari drawable resource.
 */
@Composable
fun DisplayIconFromResource(
    identifier: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val resourceId = mapIconIdentifierToDrawableRes(identifier)

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = modifier
    )
}