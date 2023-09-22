package com.example.android.clinicmanagement.patientReceipt

import com.example.android.clinicmanagement.utilities.getBaseFont
import com.itextpdf.text.pdf.BaseFont


const val NUNITO_REGULAR_PATH = "res/font/nunito.ttf"
const val NUNITO_BOLD_PATH = "res/font/nunito_bold.ttf"
const val DOSIS_MEDIUM_PATH = "res/font/dosis_medium.ttf"
const val DOSIS_BOLD_PATH = "res/font/dosis_bold.ttf"
const val DOSIS_LIGHT_PATH = "res/font/dosis_light.ttf"

enum class ReceiptField(val fieldName: String, val font: BaseFont) {

    TITLE_RECEIPT_TYPE("title_receipt_type", getBaseFont(NUNITO_BOLD_PATH)),
    TEXT_RECEIPT_NUMBER("text_receipt_number", getBaseFont(DOSIS_LIGHT_PATH)),
    TEXT_RECEIPT_DATE("text_receipt_date", getBaseFont(DOSIS_LIGHT_PATH)),
    TEXT_DOCTOR("text_doctor", getBaseFont(NUNITO_REGULAR_PATH)),
    TEXT_PATIENT("text_patient", getBaseFont(NUNITO_BOLD_PATH)),
    TEXT_DIAGNOSIS("text_diagnosis", getBaseFont(DOSIS_MEDIUM_PATH)),
    TEXT_FREQUENCY("text_frequency", getBaseFont(DOSIS_MEDIUM_PATH)),
    TEXT_SESSIONS_NUMBER("text_sessions_number", getBaseFont(DOSIS_MEDIUM_PATH)),
    TEXT_UNIT_PRICE("text_unit_price", getBaseFont(DOSIS_BOLD_PATH)),
    TEXT_TOTAL("text_total", getBaseFont(DOSIS_BOLD_PATH)),
    TEXT_TOTAL_IN_LETTERS("text_total_in_letters", getBaseFont(DOSIS_MEDIUM_PATH)),
    TITLE_DOCTOR("title_doctor", getBaseFont(DOSIS_LIGHT_PATH)),
    TITLE_PATIENT("title_patient", getBaseFont(DOSIS_LIGHT_PATH)),
    TITLE_DIAGNOSIS("title_diagnosis", getBaseFont(NUNITO_BOLD_PATH)),
    TITLE_FREQUENCY("title_frequency", getBaseFont(NUNITO_BOLD_PATH)),
    TITLE_SESSIONS_NUMBER("title_sessions_number", getBaseFont(NUNITO_BOLD_PATH)),
    TITLE_UNIT_PRICE("title_unit_price", getBaseFont(NUNITO_BOLD_PATH)),
    TITLE_TOTAL("title_total", getBaseFont(NUNITO_BOLD_PATH)),
    TITLE_TOTAL_IN_LETTERS("title_total_in_letters", getBaseFont(NUNITO_BOLD_PATH))
    ;

}