package com.vaultx.features.ai.ocr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class OCRProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    data class OCRResult(
        val extractedText: String,
        val confidence: Float,
        val language: String?,
        val blocks: List<TextBlock>
    )
    
    data class TextBlock(
        val text: String,
        val boundingBox: android.graphics.Rect?,
        val confidence: Float
    )
    
    suspend fun processImage(imagePath: String): OCRResult {
        return try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap == null) {
                return OCRResult("", 0f, null, emptyList())
            }
            
            val image = InputImage.fromBitmap(bitmap, 0)
            val visionText = recognizeText(image)
            
            val blocks = visionText.textBlocks.map { block ->
                TextBlock(
                    text = block.text,
                    boundingBox = block.boundingBox,
                    confidence = block.confidence ?: 0f
                )
            }
            
            val extractedText = visionText.text
            val confidence = blocks.maxOfOrNull { it.confidence } ?: 0f
            val language = detectLanguage(extractedText)
            
            OCRResult(extractedText, confidence, language, blocks)
        } catch (e: Exception) {
            OCRResult("", 0f, null, emptyList())
        }
    }
    
    private suspend fun recognizeText(image: InputImage): com.google.mlkit.vision.text.Text {
        return suspendCancellableCoroutine { continuation ->
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(visionText)
                }
                .addOnFailureListener { exception ->
                    continuation.resume(com.google.mlkit.vision.text.Text("", emptyList()))
                }
        }
    }
    
    private fun detectLanguage(text: String): String? {
        return when {
            text.matches(Regex(".*[\\u0600-\\u06FF].*")) -> "ar" // Arabic
            text.matches(Regex(".*[a-zA-Z].*")) -> "en" // English
            text.matches(Regex(".*[\\u4e00-\\u9fff].*")) -> "zh" // Chinese
            text.matches(Regex(".*[\\u3040-\\u309f\\u30a0-\\u30ff].*")) -> "ja" // Japanese
            text.matches(Regex(".*[\\uac00-\\ud7af].*")) -> "ko" // Korean
            else -> null
        }
    }
    
    suspend fun saveOCRResult(ocrResult: OCRResult, originalImagePath: String, vaultId: String): String? {
        return try {
            val ocrDirectory = File(context.filesDir, "ocr_results/$vaultId")
            if (!ocrDirectory.exists()) {
                ocrDirectory.mkdirs()
            }
            
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val originalFileName = File(originalImagePath).nameWithoutExtension
            val ocrFileName = "${originalFileName}_ocr_$timestamp.txt"
            val ocrFile = File(ocrDirectory, ocrFileName)
            
            val content = buildString {
                appendLine("=== نتيجة استخراج النص ===")
                appendLine("الملف الأصلي: ${File(originalImagePath).name}")
                appendLine("التاريخ: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}")
                appendLine("الثقة: ${String.format("%.2f", ocrResult.confidence * 100)}%")
                if (ocrResult.language != null) {
                    appendLine("اللغة المكتشفة: ${getLanguageName(ocrResult.language)}")
                }
                appendLine("عدد الكتل النصية: ${ocrResult.blocks.size}")
                appendLine()
                appendLine("=== النص المستخرج ===")
                appendLine(ocrResult.extractedText)
                appendLine()
                appendLine("=== تفاصيل الكتل النصية ===")
                ocrResult.blocks.forEachIndexed { index, block ->
                    appendLine("كتلة ${index + 1}:")
                    appendLine("النص: ${block.text}")
                    appendLine("الثقة: ${String.format("%.2f", block.confidence * 100)}%")
                    if (block.boundingBox != null) {
                        appendLine("الموقع: (${block.boundingBox.left}, ${block.boundingBox.top}) - (${block.boundingBox.right}, ${block.boundingBox.bottom})")
                    }
                    appendLine()
                }
            }
            
            FileOutputStream(ocrFile).use { output ->
                output.write(content.toByteArray())
            }
            
            ocrFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getLanguageName(languageCode: String): String {
        return when (languageCode) {
            "ar" -> "العربية"
            "en" -> "الإنجليزية"
            "zh" -> "الصينية"
            "ja" -> "اليابانية"
            "ko" -> "الكورية"
            "fr" -> "الفرنسية"
            "de" -> "الألمانية"
            "es" -> "الإسبانية"
            "it" -> "الإيطالية"
            "ru" -> "الروسية"
            else -> "غير معروف"
        }
    }
    
    fun extractKeywords(text: String): List<String> {
        val keywords = mutableListOf<String>()
        
        // Common Arabic keywords
        val arabicPatterns = listOf(
            "رقم" to Regex("رقم\\s*:?\\s*(\\d+)"),
            "تاريخ" to Regex("تاريخ\\s*:?\\s*([\\d/\\-]+)"),
            "اسم" to Regex("اسم\\s*:?\\s*([\\u0600-\\u06FF\\s]+)"),
            "هاتف" to Regex("هاتف\\s*:?\\s*([\\d\\+\\-\\s]+)"),
            "عنوان" to Regex("عنوان\\s*:?\\s*([\\u0600-\\u06FF\\d\\s,]+)")
        )
        
        // Common English keywords
        val englishPatterns = listOf(
            "email" to Regex("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})"),
            "phone" to Regex("(\\+?\\d[\\d\\-\\s\\(\\)]{7,})"),
            "date" to Regex("(\\d{1,2}[/\\-]\\d{1,2}[/\\-]\\d{2,4})"),
            "number" to Regex("(\\d+)"),
            "url" to Regex("(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]+)")
        )
        
        // Extract Arabic patterns
        arabicPatterns.forEach { (type, pattern) ->
            pattern.findAll(text).forEach { match ->
                if (match.groupValues.size > 1) {
                    keywords.add("$type: ${match.groupValues[1].trim()}")
                }
            }
        }
        
        // Extract English patterns
        englishPatterns.forEach { (type, pattern) ->
            pattern.findAll(text).forEach { match ->
                keywords.add("$type: ${match.value.trim()}")
            }
        }
        
        return keywords.distinct()
    }
    
    fun categorizeDocument(text: String): String {
        val lowerText = text.lowercase()
        
        return when {
            lowerText.contains("فاتورة") || lowerText.contains("invoice") || 
            lowerText.contains("receipt") || lowerText.contains("وصل") -> "فاتورة"
            
            lowerText.contains("عقد") || lowerText.contains("contract") || 
            lowerText.contains("اتفاقية") || lowerText.contains("agreement") -> "عقد"
            
            lowerText.contains("هوية") || lowerText.contains("id") || 
            lowerText.contains("جواز") || lowerText.contains("passport") -> "وثيقة شخصية"
            
            lowerText.contains("شهادة") || lowerText.contains("certificate") || 
            lowerText.contains("diploma") -> "شهادة"
            
            lowerText.contains("تقرير") || lowerText.contains("report") -> "تقرير"
            
            lowerText.contains("رسالة") || lowerText.contains("letter") || 
            lowerText.contains("email") || lowerText.contains("بريد") -> "رسالة"
            
            lowerText.contains("قائمة") || lowerText.contains("list") || 
            lowerText.contains("جدول") || lowerText.contains("table") -> "قائمة"
            
            else -> "مستند عام"
        }
    }
    
    fun isTextDocument(imagePath: String, threshold: Float = 0.7f): Boolean {
        // This would need to be implemented as a suspend function in real usage
        // For now, return a simple heuristic based on file name
        val fileName = File(imagePath).name.lowercase()
        return fileName.contains("document") || 
               fileName.contains("text") || 
               fileName.contains("scan") ||
               fileName.contains("pdf")
    }
}

