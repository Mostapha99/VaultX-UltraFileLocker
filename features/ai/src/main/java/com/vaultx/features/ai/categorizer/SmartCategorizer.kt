package com.vaultx.features.ai.categorizer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.vaultx.data.database.entities.FileType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class SmartCategorizer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    
    data class CategoryResult(
        val category: String,
        val confidence: Float,
        val tags: List<String>
    )
    
    suspend fun categorizeFile(filePath: String, mimeType: String): CategoryResult {
        return when {
            mimeType.startsWith("image/") -> categorizeImage(filePath)
            mimeType.startsWith("video/") -> categorizeVideo(filePath)
            mimeType.startsWith("audio/") -> categorizeAudio(filePath)
            mimeType.startsWith("application/") -> categorizeDocument(filePath, mimeType)
            else -> CategoryResult("متنوع", 0.5f, listOf("ملف غير معروف"))
        }
    }
    
    private suspend fun categorizeImage(imagePath: String): CategoryResult {
        return try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap == null) {
                return CategoryResult("صور", 0.3f, listOf("صورة تالفة"))
            }
            
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = analyzeImageWithMLKit(image)
            
            val category = determineImageCategory(labels)
            val tags = labels.map { it.text }
            val confidence = labels.maxOfOrNull { it.confidence } ?: 0.5f
            
            CategoryResult(category, confidence, tags)
        } catch (e: Exception) {
            CategoryResult("صور", 0.3f, listOf("خطأ في التحليل"))
        }
    }
    
    private suspend fun analyzeImageWithMLKit(image: InputImage): List<com.google.mlkit.vision.label.ImageLabel> {
        return suspendCancellableCoroutine { continuation ->
            imageLabeler.process(image)
                .addOnSuccessListener { labels ->
                    continuation.resume(labels)
                }
                .addOnFailureListener { exception ->
                    continuation.resume(emptyList())
                }
        }
    }
    
    private fun determineImageCategory(labels: List<com.google.mlkit.vision.label.ImageLabel>): String {
        val labelTexts = labels.map { it.text.lowercase() }
        
        return when {
            labelTexts.any { it.contains("person") || it.contains("face") || it.contains("human") } -> "صور شخصية"
            labelTexts.any { it.contains("screenshot") || it.contains("text") || it.contains("document") } -> "لقطات الشاشة"
            labelTexts.any { it.contains("food") || it.contains("meal") || it.contains("restaurant") } -> "صور الطعام"
            labelTexts.any { it.contains("nature") || it.contains("landscape") || it.contains("outdoor") } -> "صور الطبيعة"
            labelTexts.any { it.contains("car") || it.contains("vehicle") || it.contains("transport") } -> "صور المركبات"
            labelTexts.any { it.contains("animal") || it.contains("pet") || it.contains("cat") || it.contains("dog") } -> "صور الحيوانات"
            labelTexts.any { it.contains("building") || it.contains("architecture") || it.contains("house") } -> "صور المباني"
            else -> "صور عامة"
        }
    }
    
    private fun categorizeVideo(videoPath: String): CategoryResult {
        val fileName = File(videoPath).name.lowercase()
        
        return when {
            fileName.contains("camera") || fileName.contains("vid_") -> CategoryResult("مقاطع شخصية", 0.8f, listOf("فيديو مسجل"))
            fileName.contains("download") -> CategoryResult("مقاطع محملة", 0.9f, listOf("فيديو محمل"))
            fileName.contains("whatsapp") || fileName.contains("telegram") -> CategoryResult("مقاطع الرسائل", 0.9f, listOf("فيديو من تطبيق"))
            fileName.contains("screen") || fileName.contains("record") -> CategoryResult("تسجيل الشاشة", 0.9f, listOf("تسجيل شاشة"))
            else -> CategoryResult("مقاطع فيديو", 0.6f, listOf("فيديو عام"))
        }
    }
    
    private fun categorizeAudio(audioPath: String): CategoryResult {
        val fileName = File(audioPath).name.lowercase()
        
        return when {
            fileName.contains("music") || fileName.contains("song") -> CategoryResult("موسيقى", 0.9f, listOf("ملف موسيقي"))
            fileName.contains("voice") || fileName.contains("record") -> CategoryResult("تسجيلات صوتية", 0.9f, listOf("تسجيل صوتي"))
            fileName.contains("whatsapp") || fileName.contains("telegram") -> CategoryResult("رسائل صوتية", 0.9f, listOf("رسالة صوتية"))
            fileName.contains("podcast") -> CategoryResult("بودكاست", 0.9f, listOf("بودكاست"))
            fileName.contains("call") -> CategoryResult("مكالمات", 0.9f, listOf("تسجيل مكالمة"))
            else -> CategoryResult("ملفات صوتية", 0.6f, listOf("صوت عام"))
        }
    }
    
    private fun categorizeDocument(documentPath: String, mimeType: String): CategoryResult {
        val fileName = File(documentPath).name.lowercase()
        
        return when {
            mimeType.contains("pdf") -> CategoryResult("مستندات PDF", 0.9f, listOf("ملف PDF"))
            mimeType.contains("word") || mimeType.contains("document") -> CategoryResult("مستندات Word", 0.9f, listOf("مستند Word"))
            mimeType.contains("excel") || mimeType.contains("spreadsheet") -> CategoryResult("جداول البيانات", 0.9f, listOf("جدول بيانات"))
            mimeType.contains("powerpoint") || mimeType.contains("presentation") -> CategoryResult("عروض تقديمية", 0.9f, listOf("عرض تقديمي"))
            mimeType.contains("zip") || mimeType.contains("rar") || mimeType.contains("archive") -> CategoryResult("ملفات مضغوطة", 0.9f, listOf("أرشيف"))
            mimeType.contains("apk") -> CategoryResult("تطبيقات Android", 0.9f, listOf("تطبيق أندرويد"))
            fileName.contains("invoice") || fileName.contains("receipt") -> CategoryResult("فواتير ووصولات", 0.8f, listOf("فاتورة"))
            fileName.contains("contract") || fileName.contains("agreement") -> CategoryResult("عقود واتفاقيات", 0.8f, listOf("عقد"))
            fileName.contains("id") || fileName.contains("passport") || fileName.contains("license") -> CategoryResult("وثائق شخصية", 0.8f, listOf("وثيقة شخصية"))
            else -> CategoryResult("مستندات عامة", 0.6f, listOf("مستند"))
        }
    }
    
    fun getSmartTags(filePath: String, mimeType: String): List<String> {
        val fileName = File(filePath).name.lowercase()
        val tags = mutableListOf<String>()
        
        // Date-based tags
        val file = File(filePath)
        val lastModified = file.lastModified()
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = lastModified
        
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        
        tags.add("سنة $year")
        tags.add("شهر $month")
        
        // Size-based tags
        val sizeInMB = file.length() / (1024 * 1024)
        when {
            sizeInMB < 1 -> tags.add("ملف صغير")
            sizeInMB < 10 -> tags.add("ملف متوسط")
            sizeInMB < 100 -> tags.add("ملف كبير")
            else -> tags.add("ملف ضخم")
        }
        
        // Source-based tags
        when {
            fileName.contains("whatsapp") -> tags.add("واتساب")
            fileName.contains("telegram") -> tags.add("تيليجرام")
            fileName.contains("instagram") -> tags.add("إنستجرام")
            fileName.contains("facebook") -> tags.add("فيسبوك")
            fileName.contains("download") -> tags.add("محمل")
            fileName.contains("camera") -> tags.add("كاميرا")
            fileName.contains("screenshot") -> tags.add("لقطة شاشة")
        }
        
        return tags.distinct()
    }
    
    fun suggestVaultName(files: List<String>): String {
        val categories = files.map { file ->
            val mimeType = getMimeType(file)
            categorizeFile(file, mimeType)
        }
        
        // This would need to be implemented as a suspend function in real usage
        // For now, return a simple suggestion based on file types
        val fileTypes = files.map { getFileType(it) }
        
        return when {
            fileTypes.all { it == FileType.IMAGE } -> "معرض الصور"
            fileTypes.all { it == FileType.VIDEO } -> "مقاطع الفيديو"
            fileTypes.all { it == FileType.AUDIO } -> "الملفات الصوتية"
            fileTypes.all { it == FileType.DOCUMENT } -> "المستندات"
            fileTypes.all { it == FileType.APK } -> "التطبيقات"
            else -> "ملفات متنوعة"
        }
    }
    
    private fun getMimeType(filePath: String): String {
        val extension = File(filePath).extension.lowercase()
        return when (extension) {
            "jpg", "jpeg", "png", "gif", "bmp", "webp" -> "image/$extension"
            "mp4", "avi", "mkv", "mov", "wmv", "flv" -> "video/$extension"
            "mp3", "wav", "flac", "aac", "ogg", "m4a" -> "audio/$extension"
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "ppt", "pptx" -> "application/vnd.ms-powerpoint"
            "zip", "rar", "7z" -> "application/zip"
            "apk" -> "application/vnd.android.package-archive"
            else -> "application/octet-stream"
        }
    }
    
    private fun getFileType(filePath: String): FileType {
        val mimeType = getMimeType(filePath)
        return when {
            mimeType.startsWith("image/") -> FileType.IMAGE
            mimeType.startsWith("video/") -> FileType.VIDEO
            mimeType.startsWith("audio/") -> FileType.AUDIO
            mimeType.contains("pdf") || mimeType.contains("document") || 
            mimeType.contains("word") || mimeType.contains("excel") || 
            mimeType.contains("powerpoint") -> FileType.DOCUMENT
            mimeType.contains("apk") -> FileType.APK
            mimeType.contains("zip") || mimeType.contains("archive") -> FileType.ARCHIVE
            else -> FileType.OTHER
        }
    }
}

