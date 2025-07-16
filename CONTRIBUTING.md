# دليل المساهمة في Vault X – Ultra File Locker

نرحب بمساهماتكم في تطوير Vault X! هذا الدليل سيساعدكم على فهم كيفية المساهمة بفعالية في المشروع.

## جدول المحتويات

- [مدونة السلوك](#مدونة-السلوك)
- [كيفية المساهمة](#كيفية-المساهمة)
- [الإبلاغ عن الأخطاء](#الإبلاغ-عن-الأخطاء)
- [اقتراح ميزات جديدة](#اقتراح-ميزات-جديدة)
- [إرشادات التطوير](#إرشادات-التطوير)
- [معايير الكود](#معايير-الكود)
- [عملية المراجعة](#عملية-المراجعة)
- [الاختبارات](#الاختبارات)
- [الوثائق](#الوثائق)

## مدونة السلوك

بالمشاركة في هذا المشروع، أنت توافق على الالتزام بـ [مدونة السلوك](CODE_OF_CONDUCT.md). يرجى قراءتها لفهم السلوك المتوقع في مجتمعنا.

## كيفية المساهمة

### 1. إعداد بيئة التطوير

```bash
# استنساخ المستودع
git clone https://github.com/vaultx/ultra-file-locker.git
cd ultra-file-locker

# إنشاء فرع جديد
git checkout -b feature/your-feature-name

# إعداد البيئة
echo "sdk.dir=/path/to/android/sdk" > local.properties

# بناء المشروع
./gradlew build
```

### 2. أنواع المساهمات

#### إصلاح الأخطاء
- ابحث في [Issues](https://github.com/vaultx/ultra-file-locker/issues) عن الأخطاء المعروفة
- تأكد من أن الخطأ لم يتم إصلاحه بالفعل
- أنشئ فرعاً جديداً: `bugfix/issue-number-description`

#### ميزات جديدة
- تحقق من [خارطة الطريق](README.md#خارطة-الطريق) للميزات المخططة
- ناقش الميزة أولاً في [Discussions](https://github.com/vaultx/ultra-file-locker/discussions)
- أنشئ فرعاً جديداً: `feature/feature-name`

#### تحسين الوثائق
- تحسين README أو الوثائق الأخرى
- إضافة أمثلة أو توضيحات
- أنشئ فرعاً جديداً: `docs/improvement-description`

#### تحسين الأداء
- تحسينات في السرعة أو استهلاك الذاكرة
- قياس الأداء قبل وبعد التحسين
- أنشئ فرعاً جديداً: `perf/improvement-description`

## الإبلاغ عن الأخطاء

### قبل الإبلاغ
1. تأكد من أنك تستخدم أحدث إصدار
2. ابحث في Issues الموجودة للتأكد من عدم الإبلاغ عن نفس الخطأ
3. جرب إعادة إنتاج الخطأ على أجهزة مختلفة

### قالب تقرير الخطأ

```markdown
**وصف الخطأ**
وصف واضح ومختصر للخطأ.

**خطوات إعادة الإنتاج**
1. اذهب إلى '...'
2. اضغط على '...'
3. انتقل إلى '...'
4. شاهد الخطأ

**السلوك المتوقع**
وصف واضح لما كان متوقعاً أن يحدث.

**السلوك الفعلي**
وصف لما حدث بالفعل.

**لقطات الشاشة**
إذا كان ذلك مناسباً، أضف لقطات شاشة.

**معلومات البيئة:**
- نظام التشغيل: [مثل Android 12]
- إصدار التطبيق: [مثل 1.0.0]
- نوع الجهاز: [مثل Samsung Galaxy S21]
- إصدار Android: [مثل API 31]

**معلومات إضافية**
أي معلومات أخرى قد تساعد في حل المشكلة.

**سجلات الأخطاء**
```
أضف سجلات الأخطاء هنا إذا كانت متوفرة
```
```

## اقتراح ميزات جديدة

### قبل الاقتراح
1. تحقق من [خارطة الطريق](README.md#خارطة-الطريق)
2. ابحث في Issues والDiscussions الموجودة
3. فكر في كيفية تناسب الميزة مع رؤية المشروع

### قالب اقتراح الميزة

```markdown
**هل اقتراحك مرتبط بمشكلة؟**
وصف واضح لما هي المشكلة. مثال: أشعر بالإحباط عندما [...]

**وصف الحل المقترح**
وصف واضح ومختصر لما تريده أن يحدث.

**وصف البدائل المدروسة**
وصف واضح ومختصر لأي حلول أو ميزات بديلة فكرت فيها.

**معلومات إضافية**
أضف أي سياق أو لقطات شاشة أخرى حول طلب الميزة هنا.

**تأثير الميزة**
- من سيستفيد من هذه الميزة؟
- كم مرة ستُستخدم؟
- هل هي ضرورية أم تحسين؟

**اعتبارات التنفيذ**
- هل تتطلب تغييرات في واجهة المستخدم؟
- هل تؤثر على الأمان؟
- هل تتطلب أذونات جديدة؟
```

## إرشادات التطوير

### بنية المشروع

```
app/                    # الوحدة الرئيسية
├── src/main/
│   ├── java/           # كود Kotlin
│   └── res/            # الموارد
├── src/test/           # اختبارات الوحدة
└── src/androidTest/    # اختبارات واجهة المستخدم

core/                   # الوحدة الأساسية
├── security/           # نظام الأمان
├── ui/                 # مكونات واجهة المستخدم
└── utils/              # أدوات مساعدة

data/                   # طبقة البيانات
├── database/           # قاعدة البيانات
├── repository/         # مستودعات البيانات
└── preferences/        # التفضيلات

features/               # الميزات
├── vault/              # إدارة الخزائن
├── gallery/            # معرض الصور
├── player/             # مشغل الوسائط
└── settings/           # الإعدادات
```

### تقنيات مستخدمة

- **Kotlin**: لغة البرمجة الأساسية
- **Jetpack Compose**: واجهة المستخدم
- **Hilt**: حقن التبعيات
- **Room**: قاعدة البيانات
- **Coroutines**: البرمجة غير المتزامنة
- **ML Kit**: الذكاء الاصطناعي

## معايير الكود

### Kotlin Style Guide

اتبع [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// أسماء الفئات: PascalCase
class VaultManager

// أسماء الوظائف والمتغيرات: camelCase
fun encryptFile(filePath: String): Boolean

// الثوابت: SCREAMING_SNAKE_CASE
const val MAX_FILE_SIZE = 1024 * 1024 * 100 // 100MB

// أسماء الحزم: lowercase
package com.vaultx.core.security
```

### تنسيق الكود

```kotlin
// استخدم 4 مسافات للمسافة البادئة
class ExampleClass {
    private val property = "value"
    
    fun exampleFunction(
        parameter1: String,
        parameter2: Int
    ): String {
        return when (parameter2) {
            1 -> "one"
            2 -> "two"
            else -> "other"
        }
    }
}
```

### التعليقات والوثائق

```kotlin
/**
 * يشفر ملف باستخدام خوارزمية AES-256-GCM
 * 
 * @param inputPath مسار الملف المراد تشفيره
 * @param outputPath مسار الملف المشفر
 * @param key مفتاح التشفير (32 بايت)
 * @return true إذا نجح التشفير، false إذا فشل
 * @throws SecurityException إذا كان المفتاح غير صالح
 */
suspend fun encryptFile(
    inputPath: String,
    outputPath: String,
    key: ByteArray
): Boolean {
    require(key.size == 32) { "Key must be 32 bytes" }
    
    // تنفيذ التشفير...
    return try {
        // كود التشفير
        true
    } catch (e: Exception) {
        Log.e(TAG, "Encryption failed", e)
        false
    }
}
```

### معالجة الأخطاء

```kotlin
// استخدم Result للعمليات التي قد تفشل
suspend fun loadVault(vaultId: String): Result<Vault> {
    return try {
        val vault = vaultRepository.getVault(vaultId)
        Result.success(vault)
    } catch (e: Exception) {
        Log.e(TAG, "Failed to load vault", e)
        Result.failure(e)
    }
}

// استخدم sealed classes للحالات المختلفة
sealed class VaultState {
    object Loading : VaultState()
    data class Success(val vault: Vault) : VaultState()
    data class Error(val message: String) : VaultState()
}
```

## عملية المراجعة

### قبل إرسال Pull Request

1. **تأكد من نجاح جميع الاختبارات**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

2. **تشغيل فحص الكود**
```bash
./gradlew lint
./gradlew detekt
```

3. **تحديث الوثائق**
- أضف أو حدث التعليقات
- حدث README إذا لزم الأمر
- أضف إدخال في CHANGELOG.md

4. **اختبار يدوي**
- اختبر الميزة على أجهزة مختلفة
- تأكد من عدم كسر الميزات الموجودة

### وصف Pull Request

```markdown
## نوع التغيير
- [ ] إصلاح خطأ
- [ ] ميزة جديدة
- [ ] تحسين أداء
- [ ] تحديث وثائق
- [ ] تغيير كسر

## الوصف
وصف موجز للتغييرات المقترحة.

## الدافع والسياق
لماذا هذا التغيير ضروري؟ ما المشكلة التي يحلها؟

## كيف تم اختباره؟
وصف الاختبارات التي أجريتها للتحقق من تغييراتك.

## لقطات الشاشة (إذا كان مناسباً)
أضف لقطات شاشة لتوضيح التغييرات في واجهة المستخدم.

## قائمة التحقق
- [ ] الكود يتبع معايير المشروع
- [ ] تم إجراء اختبار ذاتي للتغييرات
- [ ] تم تحديث الوثائق
- [ ] تم إضافة اختبارات جديدة
- [ ] جميع الاختبارات تنجح
- [ ] تم تحديث CHANGELOG.md
```

### عملية المراجعة

1. **المراجعة التلقائية**: CI/CD يتحقق من الاختبارات والجودة
2. **مراجعة الكود**: مطور أساسي يراجع التغييرات
3. **اختبار إضافي**: اختبار على أجهزة مختلفة إذا لزم الأمر
4. **الموافقة**: موافقة من مطور أساسي واحد على الأقل
5. **الدمج**: دمج في الفرع الرئيسي

## الاختبارات

### أنواع الاختبارات

#### اختبارات الوحدة
```kotlin
@Test
fun `encryptFile should return true for valid input`() {
    // Given
    val cryptoManager = CryptoManager(context)
    val key = cryptoManager.generateKey()
    val inputFile = createTestFile("test content")
    
    // When
    val result = cryptoManager.encryptFile(
        inputFile.absolutePath,
        "encrypted.enc",
        key
    )
    
    // Then
    assertTrue(result)
}
```

#### اختبارات واجهة المستخدم
```kotlin
@Test
fun `clicking create vault button should open dialog`() {
    composeTestRule.setContent {
        VaultListScreen(
            onNavigateToVault = { },
            onCreateVault = { },
            onNavigateToSettings = { }
        )
    }
    
    composeTestRule.onNodeWithText("إنشاء خزنة جديدة").performClick()
    composeTestRule.onNodeWithText("اسم الخزنة").assertIsDisplayed()
}
```

#### اختبارات التكامل
```kotlin
@Test
fun `full encryption workflow should work correctly`() {
    // اختبار العملية الكاملة من إنشاء خزنة إلى تشفير الملف
}
```

### تشغيل الاختبارات

```bash
# جميع الاختبارات
./gradlew test

# اختبارات وحدة محددة
./gradlew :app:testDebugUnitTest

# اختبارات واجهة المستخدم
./gradlew connectedAndroidTest

# اختبار مع تقرير التغطية
./gradlew jacocoTestReport
```

## الوثائق

### أنواع الوثائق

1. **وثائق الكود**: KDoc للفئات والوظائف المهمة
2. **وثائق المستخدم**: README وأدلة الاستخدام
3. **وثائق المطور**: أدلة التطوير والمساهمة
4. **وثائق API**: وصف واجهات البرمجة

### كتابة الوثائق

```kotlin
/**
 * مدير التشفير الرئيسي للتطبيق
 * 
 * يوفر وظائف تشفير وفك تشفير الملفات باستخدام AES-256-GCM.
 * جميع العمليات آمنة ومحمية ضد الهجمات الشائعة.
 * 
 * @property context سياق التطبيق المطلوب للوصول إلى الملفات
 * @constructor ينشئ مثيل جديد من مدير التشفير
 * 
 * @sample
 * ```kotlin
 * val cryptoManager = CryptoManager(context)
 * val key = cryptoManager.generateKey()
 * val success = cryptoManager.encryptFile("input.txt", "output.enc", key)
 * ```
 */
class CryptoManager(private val context: Context) {
    // تنفيذ الفئة...
}
```

## الحصول على المساعدة

إذا كنت بحاجة إلى مساعدة:

1. **اقرأ الوثائق**: ابدأ بـ README والوثائق الموجودة
2. **ابحث في Issues**: قد تجد إجابة لسؤالك
3. **اسأل في Discussions**: للأسئلة العامة والنقاشات
4. **أنشئ Issue**: للمشاكل المحددة أو طلبات الميزات
5. **تواصل مباشر**: support@vaultx.com للمساعدة الفورية

## الشكر

شكراً لك على اهتمامك بالمساهمة في Vault X! مساهماتك تساعد في جعل التطبيق أفضل للجميع.

---

**ملاحظة**: هذا الدليل قابل للتطوير. إذا كان لديك اقتراحات لتحسينه، يرجى إنشاء Issue أو Pull Request.

