/* options.h.in
 *
 * Copyright (C) 2006-2026 wolfSSL Inc.
 *
 * This file is part of wolfSSL.
 *
 * wolfSSL is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * wolfSSL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1335, USA
 */


/* cmake template for options.h */

#ifdef WOLFSSL_NO_OPTIONS_H
/* options.h inhibited by configuration */
#elif !defined(WOLFSSL_OPTIONS_H)
#define WOLFSSL_OPTIONS_H


#ifdef __cplusplus
extern "C" {
#endif

#ifndef WOLFSSL_OPTIONS_IGNORE_SYS
#undef _GNU_SOURCE
/* #undef _GNU_SOURCE */
#undef _POSIX_THREADS
/* #undef _POSIX_THREADS */
#endif
#undef ASIO_USE_WOLFSSL
/* #undef ASIO_USE_WOLFSSL */
#undef BOOST_ASIO_USE_WOLFSSL
/* #undef BOOST_ASIO_USE_WOLFSSL */
#undef CURVE25519_SMALL
/* #undef CURVE25519_SMALL */
#undef CURVE448_SMALL
/* #undef CURVE448_SMALL */
#undef DEBUG
/* #undef DEBUG */
#undef DEBUG_WOLFSSL
/* #undef DEBUG_WOLFSSL */
#undef ECC_SHAMIR
#define ECC_SHAMIR
#undef ECC_TIMING_RESISTANT
#define ECC_TIMING_RESISTANT
#undef ED25519_SMALL
/* #undef ED25519_SMALL */
#undef ED448_SMALL
/* #undef ED448_SMALL */
#undef GCM_SMALL
/* #undef GCM_SMALL */
#undef GCM_TABLE
/* #undef GCM_TABLE */
#undef GCM_TABLE_4BIT
#define GCM_TABLE_4BIT
#undef GCM_WORD32
/* #undef GCM_WORD32 */
#undef HAVE___UINT128_T
/* #undef HAVE___UINT128_T */
#undef HAVE_AES_KEYWRAP
/* #undef HAVE_AES_KEYWRAP */
#undef HAVE_AESCCM
/* #undef HAVE_AESCCM */
#undef HAVE_AESGCM
#define HAVE_AESGCM
#undef HAVE_ALPN
#define HAVE_ALPN
#undef HAVE_ARIA
/* #undef HAVE_ARIA */
#undef HAVE_CERTIFICATE_STATUS_REQUEST
#define HAVE_CERTIFICATE_STATUS_REQUEST
#undef HAVE_CERTIFICATE_STATUS_REQUEST_V2
#define HAVE_CERTIFICATE_STATUS_REQUEST_V2
#undef HAVE_CHACHA
#define HAVE_CHACHA
#undef HAVE_CRL
#define HAVE_CRL
#undef HAVE_CRL_IO
/* #undef HAVE_CRL_IO */
#undef WOLFSSL_CUSTOM_CURVES
/* #undef WOLFSSL_CUSTOM_CURVES */
#undef HAVE_CURVE25519
#define HAVE_CURVE25519
#undef HAVE_CURVE448
/* #undef HAVE_CURVE448 */
#undef HAVE_DH_DEFAULT_PARAMS
#define HAVE_DH_DEFAULT_PARAMS
#undef HAVE_DILITHIUM
/* #undef HAVE_DILITHIUM */
#undef HAVE_ECC
#define HAVE_ECC
#undef HAVE_ECH
/* #undef HAVE_ECH */
#undef HAVE_ED25519
/* #undef HAVE_ED25519 */
#undef HAVE_ED448
/* #undef HAVE_ED448 */
#undef HAVE_ENCRYPT_THEN_MAC
#define HAVE_ENCRYPT_THEN_MAC
#undef HAVE_EX_DATA
#define HAVE_EX_DATA
#undef HAVE_EXTENDED_MASTER
#define HAVE_EXTENDED_MASTER
#undef HAVE_FFDHE_2048
#define HAVE_FFDHE_2048
#undef HAVE_HASHDRBG
#define HAVE_HASHDRBG
#undef HAVE_HKDF
#define HAVE_HKDF
#undef HAVE_HPKE
/* #undef HAVE_HPKE */
#undef HAVE_KEYING_MATERIAL
/* #undef HAVE_KEYING_MATERIAL */
#undef HAVE_LIBOQS
/* #undef HAVE_LIBOQS */
#undef HAVE_MAX_FRAGMENT
/* #undef HAVE_MAX_FRAGMENT */
#undef HAVE_OCSP
#define HAVE_OCSP
#undef HAVE_ONE_TIME_AUTH
#define HAVE_ONE_TIME_AUTH
#undef HAVE_PKCS7
/* #undef HAVE_PKCS7 */
#undef HAVE_POLY1305
#define HAVE_POLY1305
#undef HAVE_PTHREAD
#define HAVE_PTHREAD 1
#undef HAVE_REPRODUCIBLE_BUILD
/* #undef HAVE_REPRODUCIBLE_BUILD */
#undef HAVE_SESSION_TICKET
#define HAVE_SESSION_TICKET
#undef HAVE_SNI
#define HAVE_SNI
#undef HAVE_SUPPORTED_CURVES
#define HAVE_SUPPORTED_CURVES
#undef HAVE_THREAD_LS
#define HAVE_THREAD_LS
#undef HAVE_TLS_EXTENSIONS
#define HAVE_TLS_EXTENSIONS
#undef HAVE_TRUNCATED_HMAC
/* #undef HAVE_TRUNCATED_HMAC */
#undef HAVE_TRUSTED_CA
/* #undef HAVE_TRUSTED_CA */
#undef HAVE_X963_KDF
/* #undef HAVE_X963_KDF */
#undef NO_AES
/* #undef NO_AES */
#undef NO_AES_CBC
/* #undef NO_AES_CBC */
#undef NO_ASN
/* #undef NO_ASN */
#undef NO_ASN_CRYPT
/* #undef NO_ASN_CRYPT */
#undef NO_BIG_INT
/* #undef NO_BIG_INT */
#undef NO_CERTS
/* #undef NO_CERTS */
#undef NO_CHACHA_ASM
/* #undef NO_CHACHA_ASM */
#undef NO_CODING
/* #undef NO_CODING */
#undef NO_CURVED25519_128BIT
/* #undef NO_CURVED25519_128BIT */
#undef NO_CURVED448_128BIT
/* #undef NO_CURVED448_128BIT */
#undef NO_DES3
/* #undef NO_DES3 */
#undef NO_DH
/* #undef NO_DH */
#undef NO_DSA
#define NO_DSA
#undef NO_ERROR_QUEUE
/* #undef NO_ERROR_QUEUE */
#undef NO_ERROR_STRINGS
/* #undef NO_ERROR_STRINGS */
#undef NO_FILESYSTEM
/* #undef NO_FILESYSTEM */
#undef NO_INLINE
/* #undef NO_INLINE */
#undef NO_MD4
/* #undef NO_MD4 */
#undef NO_MD5
/* #undef NO_MD5 */
#undef NO_OLD_RNGNAME
/* #undef NO_OLD_RNGNAME */
#undef NO_OLD_SHA_NAMES
/* #undef NO_OLD_SHA_NAMES */
#undef NO_OLD_SSL_NAMES
/* #undef NO_OLD_SSL_NAMES */
#undef NO_OLD_TLS
#define NO_OLD_TLS
#undef NO_OLD_WC_NAMES
/* #undef NO_OLD_WC_NAMES */
#undef NO_PKCS12
/* #undef NO_PKCS12 */
#undef NO_PSK
#define NO_PSK
#undef NO_PWDBASED
/* #undef NO_PWDBASED */
#undef NO_RC4
#define NO_RC4
#undef NO_RSA
/* #undef NO_RSA */
#undef NO_SESSION_CACHE_REF
#define NO_SESSION_CACHE_REF
#undef NO_SHA
/* #undef NO_SHA */
#undef NO_WOLFSSL_MEMORY
/* #undef NO_WOLFSSL_MEMORY */
#undef OPENSSL_ALL
/* #undef OPENSSL_ALL */
#undef OPENSSL_EXTRA
#define OPENSSL_EXTRA
#undef OPENSSL_NO_SSL2
/* #undef OPENSSL_NO_SSL2 */
#undef OPENSSL_NO_SSL3
/* #undef OPENSSL_NO_SSL3 */
#undef SSL_TXT_TLSV1_2
/* #undef SSL_TXT_TLSV1_2 */
#undef TFM_ECC256
#define TFM_ECC256
#undef TFM_NO_ASM
/* #undef TFM_NO_ASM */
#undef TFM_TIMING_RESISTANT
#define TFM_TIMING_RESISTANT
#undef USE_FAST_MATH
/* #undef USE_FAST_MATH */
#undef WC_16BIT_CPU
/* #undef WC_16BIT_CPU */
#undef WC_ECC_NONBLOCK
/* #undef WC_ECC_NONBLOCK */
#undef WC_NO_ASYNC_THREADING
#define WC_NO_ASYNC_THREADING
#undef WC_NO_HARDEN
/* #undef WC_NO_HARDEN */
#undef WC_NO_HASHDRBG
/* #undef WC_NO_HASHDRBG */
#undef WC_NO_RNG
/* #undef WC_NO_RNG */
#undef WC_NO_RSA_OAEP
/* #undef WC_NO_RSA_OAEP */
#undef WC_RSA_BLINDING
#define WC_RSA_BLINDING
#undef WC_RSA_NO_PADDING
/* #undef WC_RSA_NO_PADDING */
#undef WC_RSA_PSS
#define WC_RSA_PSS
#undef WOLF_CRYPTO_CB
/* #undef WOLF_CRYPTO_CB */
#undef WOLFSSL_AARCH64_BUILD
/* #undef WOLFSSL_AARCH64_BUILD */
#undef WOLFSSL_AES_CFB
/* #undef WOLFSSL_AES_CFB */
#undef WOLFSSL_AES_COUNTER
#define WOLFSSL_AES_COUNTER
#undef WOLFSSL_AES_DIRECT
#define WOLFSSL_AES_DIRECT
#undef WOLFSSL_AES_OFB
/* #undef WOLFSSL_AES_OFB */
#undef WOLFSSL_AES_SIV
/* #undef WOLFSSL_AES_SIV */
#undef HAVE_AES_ECB
/* #undef HAVE_AES_ECB */
#undef WOLFSSL_AES_CTS
/* #undef WOLFSSL_AES_CTS */
#undef WOLFSSL_ALT_CERT_CHAINS
#define WOLFSSL_ALT_CERT_CHAINS
#undef WOLFSSL_APPLE_NATIVE_CERT_VALIDATION
/* #undef WOLFSSL_APPLE_NATIVE_CERT_VALIDATION */
#undef WOLFSSL_ASIO
/* #undef WOLFSSL_ASIO */
#undef WOLFSSL_BASE64_ENCODE
/* #undef WOLFSSL_BASE64_ENCODE */
#undef WOLFSSL_CAAM
/* #undef WOLFSSL_CAAM */
#undef WOLFSSL_CERT_EXT
/* #undef WOLFSSL_CERT_EXT */
#undef WOLFSSL_CERT_GEN
/* #undef WOLFSSL_CERT_GEN */
#undef WOLFSSL_CERT_GEN_CACHE
/* #undef WOLFSSL_CERT_GEN_CACHE */
#undef WOLFSSL_CERT_NAME_ALL
/* #undef WOLFSSL_CERT_NAME_ALL */
#undef WOLFSSL_CERT_REQ
/* #undef WOLFSSL_CERT_REQ */
#undef WOLFSSL_CMAC
/* #undef WOLFSSL_CMAC */
#undef WOLFSSL_DES_ECB
#define WOLFSSL_DES_ECB
#undef WOLFSSL_DH_CONST
/* #undef WOLFSSL_DH_CONST */
#undef WOLFSSL_DTLS
/* #undef WOLFSSL_DTLS */
#undef WOLFSSL_DTLS_CID
/* #undef WOLFSSL_DTLS_CID */
#undef WOLFSSL_DTLS13
/* #undef WOLFSSL_DTLS13 */
#undef WOLFSSL_DTLS_CH_FRAG
/* #undef WOLFSSL_DTLS_CH_FRAG */
#undef WOLFSSL_EITHER_SIDE
/* #undef WOLFSSL_EITHER_SIDE */
#undef WOLFSSL_ENCRYPTED_KEYS
#define WOLFSSL_ENCRYPTED_KEYS
#undef WOLFSSL_ERROR_CODE_OPENSSL
/* #undef WOLFSSL_ERROR_CODE_OPENSSL */
#undef WOLFSSL_IP_ALT_NAME
#define WOLFSSL_IP_ALT_NAME
#undef WOLFSSL_KEY_GEN
/* #undef WOLFSSL_KEY_GEN */
#undef WOLFSSL_NO_ASM
/* #undef WOLFSSL_NO_ASM */
#undef WOLFSSL_NO_SHAKE128
#define WOLFSSL_NO_SHAKE128
#undef WOLFSSL_NO_SHAKE256
#define WOLFSSL_NO_SHAKE256
#undef WOLFSSL_NO_TLS12
/* #undef WOLFSSL_NO_TLS12 */
#undef WOLFSSL_POST_HANDSHAKE_AUTH
/* #undef WOLFSSL_POST_HANDSHAKE_AUTH */
#undef WOLFSSL_PSS_LONG_SALT
#define WOLFSSL_PSS_LONG_SALT
#undef WOLFSSL_PUBLIC_MP
#define WOLFSSL_PUBLIC_MP
#undef WOLFSSL_QUIC
#define WOLFSSL_QUIC
#undef WOLFSSL_SEND_HRR_COOKIE
/* #undef WOLFSSL_SEND_HRR_COOKIE */
#undef WOLFSSL_SHA224
/* #undef WOLFSSL_SHA224 */
#undef WOLFSSL_SHA3
/* #undef WOLFSSL_SHA3 */
#undef WOLFSSL_SHA3_SMALL
/* #undef WOLFSSL_SHA3_SMALL */
#undef WOLFSSL_SHA384
#define WOLFSSL_SHA384
#undef WOLFSSL_SHA512
#define WOLFSSL_SHA512
#undef WOLFSSL_SHAKE128
/* #undef WOLFSSL_SHAKE128 */
#undef WOLFSSL_SHAKE256
/* #undef WOLFSSL_SHAKE256 */
#undef WOLFSSL_SRTP
/* #undef WOLFSSL_SRTP */
#undef WOLFSSL_SYS_CA_CERTS
#define WOLFSSL_SYS_CA_CERTS
#undef WOLFSSL_TICKET_HAVE_ID
/* #undef WOLFSSL_TICKET_HAVE_ID */
#undef WOLFSSL_TICKET_NONCE_MALLOC
/* #undef WOLFSSL_TICKET_NONCE_MALLOC */
#undef WOLFSSL_TLS13
#define WOLFSSL_TLS13
#undef WOLFSSL_USE_ALIGN
#define WOLFSSL_USE_ALIGN
#undef WOLFSSL_USER_SETTINGS
/* #undef WOLFSSL_USER_SETTINGS */
#undef WOLFSSL_USER_SETTINGS_ASM
/* #undef WOLFSSL_USER_SETTINGS_ASM */
#undef WOLFSSL_W64_WRAPPER
/* #undef WOLFSSL_W64_WRAPPER */
#undef WOLFSSL_WOLFSSH
#define WOLFSSL_WOLFSSH
#undef WOLFSSL_X86_64_BUILD
/* #undef WOLFSSL_X86_64_BUILD */
#undef NO_DES3_TLS_SUITES
#define NO_DES3_TLS_SUITES
#undef WOLFSSL_EXPERIMENTAL_SETTINGS
/* #undef WOLFSSL_EXPERIMENTAL_SETTINGS */
#undef WOLFSSL_HAVE_MLKEM
/* #undef WOLFSSL_HAVE_MLKEM */
#undef WOLFSSL_WC_MLKEM
/* #undef WOLFSSL_WC_MLKEM */
#undef WOLFSSL_WC_DILITHIUM
/* #undef WOLFSSL_WC_DILITHIUM */
#undef NO_WOLFSSL_STUB
/* #undef NO_WOLFSSL_STUB */
#undef HAVE_ECC_SECPR2
/* #undef HAVE_ECC_SECPR2 */
#undef HAVE_ECC_SECPR3
/* #undef HAVE_ECC_SECPR3 */
#undef HAVE_ECC_BRAINPOOL
/* #undef HAVE_ECC_BRAINPOOL */
#undef HAVE_ECC_KOBLITZ
/* #undef HAVE_ECC_KOBLITZ */
#undef HAVE_ECC_CDH
/* #undef HAVE_ECC_CDH */
#undef WOLFSSL_HAVE_LMS
/* #undef WOLFSSL_HAVE_LMS */
#undef WOLFSSL_WC_LMS
/* #undef WOLFSSL_WC_LMS */
#undef  WOLFSSL_LMS_SHA256_192
/* #undef WOLFSSL_LMS_SHA256_192 */
#undef  WOLFSSL_NO_LMS_SHA256_256
/* #undef WOLFSSL_NO_LMS_SHA256_256 */
#undef WOLFSSL_HAVE_XMSS
/* #undef WOLFSSL_HAVE_XMSS */
#undef WOLFSSL_WC_XMSS
/* #undef WOLFSSL_WC_XMSS */
#undef HAVE_SECRET_CALLBACK
/* #undef HAVE_SECRET_CALLBACK */
#undef WC_RSA_DIRECT
/* #undef WC_RSA_DIRECT */
#undef WOLFSSL_HAVE_SLHDSA
/* #undef WOLFSSL_HAVE_SLHDSA */
#undef WOLFSSL_WC_SLHDSA
/* #undef WOLFSSL_WC_SLHDSA */

#ifdef __cplusplus
}
#endif


#endif /* WOLFSSL_OPTIONS_H */

