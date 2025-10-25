package com.mobilerpgpack.phone.translator.models

import android.content.Context
import com.mobilerpgpack.ctranslate2proxy.NLLB200Translator
import com.mobilerpgpack.ctranslate2proxy.Translator
import kotlinx.coroutines.async
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.java.KoinJavaComponent.get

class NLLB200TranslationModel(
    private val context: Context,
    private val modelFile: String,
    private val spmFile: String,
    private val allowDownloadingOverMobile: Boolean = false
) : BaseM2M100TranslationModel(context, modelFile, spmFile, allowDownloadingOverMobile),KoinComponent {

    private val locales = hashMapOf(
        "aa" to "aar_Latn",
        "ab" to "abk_Cyrl",
        "af" to "afr_Latn",
        "ak" to "aka_Latn",
        "am" to "amh_Ethi",
        "ar" to "arb_Arab",
        "as" to "asm_Beng",
        "ay" to "ayr_Latn",
        "az" to "azj_Latn",
        "ba" to "bak_Cyrl",
        "be" to "bel_Cyrl",
        "bg" to "bul_Cyrl",
        "bm" to "bam_Latn",
        "bn" to "ben_Beng",
        "bo" to "bod_Tibt",
        "bs" to "bos_Latn",
        "ca" to "cat_Latn",
        "cs" to "ces_Latn",
        "cy" to "cym_Latn",
        "da" to "dan_Latn",
        "de" to "deu_Latn",
        "dv" to "div_Thaa",
        "dz" to "dzo_Tibt",
        "ee" to "ewe_Latn",
        "el" to "ell_Grek",
        "en" to "eng_Latn",
        "es" to "spa_Latn",
        "et" to "est_Latn",
        "eu" to "eus_Latn",
        "fa" to "pes_Arab",
        "ff" to "fuv_Latn",
        "fi" to "fin_Latn",
        "fo" to "fao_Latn",
        "fr" to "fra_Latn",
        "fy" to "fry_Latn",
        "ga" to "gle_Latn",
        "gd" to "gla_Latn",
        "gl" to "glg_Latn",
        "gn" to "grn_Latn",
        "gu" to "guj_Gujr",
        "ha" to "hau_Latn",
        "he" to "heb_Hebr",
        "hi" to "hin_Deva",
        "hr" to "hrv_Latn",
        "hu" to "hun_Latn",
        "hy" to "hye_Armn",
        "id" to "ind_Latn",
        "ig" to "ibo_Latn",
        "is" to "isl_Latn",
        "it" to "ita_Latn",
        "ja" to "jpn_Jpan",
        "jv" to "jav_Latn",
        "ka" to "kat_Geor",
        "kk" to "kaz_Cyrl",
        "km" to "khm_Khmr",
        "kn" to "kan_Knda",
        "ko" to "kor_Hang",
        "ku" to "kmr_Latn",
        "ky" to "kir_Cyrl",
        "la" to "lat_Latn",
        "lb" to "ltz_Latn",
        "lg" to "lug_Latn",
        "ln" to "lin_Latn",
        "lo" to "lao_Laoo",
        "lt" to "lit_Latn",
        "lv" to "lvs_Latn",
        "mg" to "plt_Latn",
        "mi" to "mri_Latn",
        "mk" to "mkd_Cyrl",
        "ml" to "mal_Mlym",
        "mn" to "khk_Cyrl",
        "mr" to "mar_Deva",
        "ms" to "zsm_Latn",
        "mt" to "mlt_Latn",
        "my" to "mya_Mymr",
        "nb" to "nob_Latn",
        "ne" to "npi_Deva",
        "nl" to "nld_Latn",
        "nn" to "nno_Latn",
        "no" to "nor_Latn",
        "ny" to "nya_Latn",
        "om" to "gaz_Latn",
        "or" to "ory_Orya",
        "pa" to "pan_Guru",
        "pl" to "pol_Latn",
        "ps" to "pbt_Arab",
        "pt" to "por_Latn",
        "qu" to "quy_Latn",
        "ro" to "ron_Latn",
        "ru" to "rus_Cyrl",
        "rw" to "kin_Latn",
        "sd" to "snd_Arab",
        "si" to "sin_Sinh",
        "sk" to "slk_Latn",
        "sl" to "slv_Latn",
        "sn" to "sna_Latn",
        "so" to "som_Latn",
        "sq" to "als_Latn",
        "sr" to "srp_Cyrl",
        "sv" to "swe_Latn",
        "sw" to "swh_Latn",
        "ta" to "tam_Taml",
        "te" to "tel_Telu",
        "tg" to "tgk_Cyrl",
        "th" to "tha_Thai",
        "ti" to "tir_Ethi",
        "tk" to "tuk_Latn",
        "tl" to "tgl_Latn",
        "tr" to "tur_Latn",
        "tt" to "tat_Cyrl",
        "ug" to "uig_Arab",
        "uk" to "ukr_Cyrl",
        "ur" to "urd_Arab",
        "uz" to "uzn_Latn",
        "vi" to "vie_Latn",
        "wo" to "wol_Latn",
        "xh" to "xho_Latn",
        "yi" to "ydd_Hebr",
        "yo" to "yor_Latn",
        "zh" to "zho_Hans",
        "zu" to "zul_Latn"
    )

    override val supportedLocales: Collection<String> get() = locales.keys

    override val zipFileId: String = "1fTEEnpbiJ3zJ9vzrMwGVKFbQ6k6DTFPx"

    override val zipFileSha256: String =
        "112f9f615eb89b0ad093d4e17e58f10d5298a031fac4ed96b0ad71a22633f125"

    override val translator: Translator = get<NLLB200Translator>()

    override val translationType: TranslationType = TranslationType.NLLB200

    override suspend fun translate(
        text: String,
        sourceLocale: String,
        targetLocale: String
    ): TranslationResult {
        if ( !(locales.containsKey(sourceLocale) && locales.containsKey(targetLocale)) ){
            return TranslationResult(text, false)
        }

        super.translate(text, locales[sourceLocale]!!, locales[targetLocale]!!)
        if (!isLocaleSupported(targetLocale) || !isModelDownloaded){
            return TranslationResult(text, false)
        }

        val sourceLocaleNLLB200Code = locales[sourceLocale]!!
        val targetLocaleNLLB200Code = locales[targetLocale]!!

        val deferred = scope.async {
            initialize(sourceLocaleNLLB200Code, targetLocaleNLLB200Code)
            translator.translate(text, sourceLocaleNLLB200Code, targetLocaleNLLB200Code)
        }
        return TranslationResult(deferred.await(),true)
    }
}