package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowReport
import kotlinx.coroutines.flow.Flow

interface GetCashFlowReportUseCase {
    /**
     * Menyiapkan data laporan arus kas berdasarkan pilihan periode.
     * @param periodOption Pilihan periode (misal: 6 bulan terakhir, tahun ini).
     * @return Flow yang berisi data CashFlowReport.
     */
    operator fun invoke(periodOption: CashFlowPeriodOption): Flow<CashFlowReport>
}