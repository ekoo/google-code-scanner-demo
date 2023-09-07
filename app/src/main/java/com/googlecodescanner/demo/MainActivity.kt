package com.googlecodescanner.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.googlecodescanner.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanBarcodeButton.setOnClickListener {

            val scannerOptions = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)

            if (binding.allowManualInput.isChecked) {
                scannerOptions.allowManualInput()
            }

            if (binding.enableAutoZoom.isChecked) {
                scannerOptions.enableAutoZoom()
            }

            startScan(scannerOptions)
        }
    }

    private fun startScan(scannerOptions: GmsBarcodeScannerOptions.Builder) {
        GmsBarcodeScanning.getClient(this, scannerOptions.build()).startScan()
            .addOnSuccessListener { barcode ->
                val scanResult = "Format: ${barcodeFormat(barcode.format)}\n" +
                        "Type: ${barcodeType(barcode.valueType)}\n" +
                        "Value: ${barcode.rawValue}"

                MaterialAlertDialogBuilder(this)
                    .setTitle("Scan Result")
                    .setMessage(scanResult)
                    .setPositiveButton("Close", null)
                    .show()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                showToast("Error: ${e.message}")
            }
            .addOnCanceledListener {
                showToast("Canceled")
            }
    }

    private fun barcodeType(valueType: Int): String {
        return when (valueType) {
            Barcode.TYPE_URL -> "URL"
            Barcode.TYPE_WIFI -> "WiFi"
            Barcode.TYPE_EMAIL -> "Email"
            Barcode.TYPE_PHONE -> "Phone"
            Barcode.TYPE_SMS -> "SMS"
            Barcode.TYPE_CONTACT_INFO -> "Contact Info"
            Barcode.TYPE_CALENDAR_EVENT -> "Calendar Event"
            Barcode.TYPE_DRIVER_LICENSE -> "Driver License"
            Barcode.TYPE_GEO -> "Geo"
            Barcode.TYPE_ISBN -> "ISBN"
            Barcode.TYPE_PRODUCT -> "Product"
            Barcode.TYPE_TEXT -> "Text"
            else -> "Unknown"
        }
    }

    private fun barcodeFormat(format: Int): String {
        return when (format) {
            Barcode.FORMAT_QR_CODE -> "QR Code"
            Barcode.FORMAT_AZTEC -> "Aztec"
            else -> "Unknown"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}