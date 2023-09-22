package com.example.android.clinicmanagement.domain

import android.content.Context
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Receipt
import com.example.android.clinicmanagement.patientReceipt.ReceiptField
import com.example.android.clinicmanagement.patientReceipt.ReceiptType
import com.example.android.clinicmanagement.utilities.convertNumberToLetters
import com.example.android.clinicmanagement.utilities.getFormattedFullName
import com.example.android.clinicmanagement.utilities.getReceiptFormattedDate
import com.example.android.clinicmanagement.utilities.getReceiptNumberInYear
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

class GenerateReceiptUseCase {

    companion object {
        const val FIELD_PROPERTY_TEXT_FONT = "textfont"
        const val FIELD_PROPERTY_TEXT_SIZE = "textsize"
    }

    suspend operator fun invoke(context: Context, receipt: Receipt, receiptType: ReceiptType) {
        withContext(Dispatchers.IO) {
            val inputStream = context.assets.open("receiptTemplate.pdf")

            val pdfDirectory = File(context.filesDir.path + "/PatientReceipt")
            if (!pdfDirectory.exists()) {
                pdfDirectory.mkdir()
            }

            val fileOutput =
                File(context.filesDir.path + "/PatientReceipt", "GeneratedReceipt.pdf")
            if (!fileOutput.exists()) {
                fileOutput.createNewFile()
            }


            val reader = PdfReader(inputStream)
            val stamper = PdfStamper(reader, FileOutputStream(fileOutput))
            val form = stamper.acroFields


            for (receiptField in ReceiptField.values()) {
                form.setFieldProperty(
                    receiptField.fieldName,
                    FIELD_PROPERTY_TEXT_FONT,
                    receiptField.font,
                    null
                )
            }

            with(receipt) {
                form.setField(
                    ReceiptField.TITLE_RECEIPT_TYPE.fieldName,
                    context.getString(
                        when (receiptType) {
                            ReceiptType.QUOTATION -> R.string.quotation
                            ReceiptType.INVOICE -> R.string.invoice
                        }
                    ).uppercase(Locale.ROOT)
                )

                form.setField(
                    ReceiptField.TEXT_RECEIPT_NUMBER.fieldName,
                    getReceiptNumberInYear(receiptNumber, receiptDateInSeconds)
                )

                form.setField(
                    ReceiptField.TEXT_RECEIPT_DATE.fieldName,
                    getReceiptFormattedDate(receiptDateInSeconds)
                )
                form.setField(
                    ReceiptField.TEXT_DOCTOR.fieldName,
                    context.getString(R.string.doctor_prefix_with_text, doctorFullName)
                )
                form.setField(
                    ReceiptField.TEXT_PATIENT.fieldName,
                    getFormattedFullName(firstName, lastName)
                )
                form.setField(ReceiptField.TEXT_DIAGNOSIS.fieldName, diagnosis)
                form.setField(ReceiptField.TEXT_FREQUENCY.fieldName, frequency)
                form.setField(
                    ReceiptField.TEXT_SESSIONS_NUMBER.fieldName,
                    context.resources.getQuantityString(
                        R.plurals.done_sessions_field,
                        sessionCount,
                        sessionCount
                    )
                )
                form.setField(
                    ReceiptField.TEXT_UNIT_PRICE.fieldName, context.getString(
                        R.string.moroccan_currency_with_number,
                        sessionPrice
                    )
                )
                form.setField(
                    ReceiptField.TEXT_TOTAL.fieldName, context.getString(
                        R.string.moroccan_currency_with_number,
                        total
                    )
                )

                val totalInLetters = context.getString(R.string.moroccan_currency_with_letters, convertNumberToLetters(total))

                //Make sure this string fit in, if it doesn't change its text size.
                if (totalInLetters.count() > 90) {
                    form.setFieldProperty(ReceiptField.TEXT_TOTAL_IN_LETTERS.fieldName, FIELD_PROPERTY_TEXT_SIZE, 10f, null)
                }
                form.setField(ReceiptField.TEXT_TOTAL_IN_LETTERS.fieldName, totalInLetters)

                form.setField(
                    ReceiptField.TITLE_DOCTOR.fieldName,
                    "${context.getString(R.string.treating_doctor)} :"
                )
                form.setField(
                    ReceiptField.TITLE_PATIENT.fieldName,
                    "${context.getString(R.string.for_patient)} :"
                )
                form.setField(
                    ReceiptField.TITLE_DIAGNOSIS.fieldName,
                    context.getString(R.string.diagnosis)
                )
                form.setField(
                    ReceiptField.TITLE_FREQUENCY.fieldName,
                    context.getString(R.string.frequency)
                )
                form.setField(
                    ReceiptField.TITLE_SESSIONS_NUMBER.fieldName, context.getString(
                        when (receiptType) {
                            ReceiptType.QUOTATION -> R.string.planned_sessions
                            ReceiptType.INVOICE -> R.string.done_sessions
                        }
                    )
                )


                form.setField(
                    ReceiptField.TITLE_UNIT_PRICE.fieldName,
                    context.getString(R.string.unit_price)
                )
                form.setField(
                    ReceiptField.TITLE_TOTAL.fieldName,
                    context.getString(R.string.total).uppercase(Locale.ROOT)
                )
                form.setField(
                    ReceiptField.TITLE_TOTAL_IN_LETTERS.fieldName,
                    context.getString(R.string.total_in_letters)
                )
            }


            stamper.setFormFlattening(true)
            stamper.close()
            reader.close()


        }

    }
}