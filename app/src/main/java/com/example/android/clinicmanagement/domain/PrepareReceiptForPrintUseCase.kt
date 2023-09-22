package com.example.android.clinicmanagement.domain

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import com.example.android.clinicmanagement.data.models.Receipt
import com.example.android.clinicmanagement.patientReceipt.PDFDocumentAdapter
import com.example.android.clinicmanagement.patientReceipt.ReceiptType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PrepareReceiptForPrintUseCase {

     operator fun invoke(context: Context) {

            val pdfFile =
                File(context.filesDir.path + "/PatientReceipt", "GeneratedReceipt.pdf")

            val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

            val builder = PrintAttributes.Builder()
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5)
            builder.setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print("Document", PDFDocumentAdapter(pdfFile), builder.build())


    }
}