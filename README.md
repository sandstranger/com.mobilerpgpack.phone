# Unofficial ports of Doom 1,2, Wolfenstein RPG, Doom64 EX Plus, PsyDoom, UZDoom for Android
This is an Android ports of Doom 1,2, Wolfenstein RPG, Doom64 EX Plus, PsyDoom, UZDoom projects found here:

https://github.com/Erick194/DoomRPG-RE

https://github.com/Erick194/DoomIIRPG-RE

https://github.com/Erick194/WolfensteinRPG-RE

https://github.com/atsb/Doom64EX-Plus/

https://github.com/Styd051/DOOM64-EX-Plus-Enhanced

https://github.com/BodbDearg/PsyDoom

https://github.com/UZDoom/UZDoom

# System Requirements

This port for Android has the following system requirements:
### Minimum
* Operating system: Android 7.0 or later
* Graphics: videocard, which supports GLES 2.0 render
* Memory: 1GB system RAM

### Recommended
* Operating system: Android 12 or later (I tested it mostly on my phone with Android 12)

# Graphics

These source ports supported OpenGL ES 3.2 render with NG-GL4ES, but have a lot of graphics issues.

Because NG-GL4ES is very buggy.

If your device is not supported OPenGL ES 3.2 render or you have graphics issues like black screen, then use binary

with OpenGL ES 2.0 render

## Controllers Support
This Android ports support controllers

## Doom RPG Default Controllers controls

| Action           | Button      	|
| ---------------  | ------------- 	|
| Move Forward     | D-pad up    	|
| Move Backward    | D-pad down  	|
| Turn Left        | D-pad left  	|
| Turn Right       | D-pad right 	|
| Attack/Talk/Use  | A           	|
| Next Weapon      | Right trigger  |
| Prev Weapon      | Left trigger   |
| Pass Turn        | X           	|
| Automap          | Select      	|
| Menu Open/Back   | Start       	|

## Doom 2 RPG Default Controllers controls

| Action           | Button         |
| ---------------  | -------------  |
| Move Forward     | D-pad up       |
| Move Backward    | D-pad down     |
| Turn Left        | D-pad left     |
| Turn Right       | D-pad right    |
| Attack/Talk/Use  | A              |
| Next Weapon      | Right trigger  |
| Prev Weapon      | Left trigger   |
| Pass Turn        | X              |
| Automap          | Select         |
| Menu Open/Back   | Start          |
| Menu Open/Back   | Start          |
| Items Info       | Left shoulder  |
| Drinks           | Right shoulder |
| Pda              | B				|
| Bot Dis          | Y				|

## Wolfenstein RPG Default Controllers controls

| Action           | Button         |
| ---------------  | -------------  |
| Move Forward     | D-pad up       |
| Move Backward    | D-pad down     |
| Turn Left        | D-pad left     |
| Turn Right       | D-pad right    |
| Attack/Talk/Use  | A              |
| Next Weapon      | Right trigger  |
| Prev Weapon      | Left trigger   |
| Pass Turn        | X              |
| Automap          | Select         |
| Menu Open/Back   | Start          |
| Menu Open/Back   | Start          |
| Items Info       | Left shoulder  |
| Syringes         | Right shoulder |
| Journal          | Y   			|

## Translation

This launcher supporting Doom RPG series machine translation to many languages by local AI directly on Android device via CTranslate2 API.

## Doom64 

For running Doom 64 EX Plus - get Doom 64 remaster from STEAM

https://store.steampowered.com/app/1148590/DOOM_64/

Mods can be used from this Doom64 discord server - https://discord.com/invite/doom-64-593915163896315905

## PsyDoom

For running PsyDoom - Get Doom PSX disc with cue as described here https://github.com/BodbDearg/PsyDoom?tab=readme-ov-file#running-the-game

PsyDoom requires vulkan 1.0 render suppport.

If your device does not support vulkan render, then use legacy opengl es render

## UZDoom

This project contains code from GZDoom android port (https://github.com/emileb/gzdoom),

which is licensed under GPL v3. All modifications are clearly marked in commit history.

And using render changes (like OGL emulation, gles render and some bugfixes for Android platform)

All credits for Android render changes and Android bugfixes to emileb - https://github.com/emileb

This project is ALSO licensed under GPL v3.

## Keyboard and Mouse Support
This Android ports support keyboard and mouse

## Building

To build the APK file, clone this repository, open the project root directory in Android Studio and run the project.

Important: Bulding from source code supported only for fdroid buld variants, google variants are propritary due to crashlytics using.

Only windows 11 25h2 x64 is supported as build host, others OS are not supported, because I am not using them.

## Download

To download a working APK file, either do it from F-Droid [here](https://f-droid.org/packages/com.mobilerpgpack.phone/), or in the [Releases Section](https://github.com/sandstranger/doom-wolf-rpg-android-port/releases/latest).

# How to run this engines
1. For running Wolfenstein rpg get wolfenstein rpg ipa file.

   Copy this ipa file to your device.

   Run this android port and specify path to this file in launcher

2. For running Doom1 rpg get doom1 rpg brew file

   get BarToZip exe from this PC release build https://github.com/Erick194/DoomRPG-RE/releases/tag/v.0.2.2 

   and follow this instructions https://www.doomworld.com/forum/topic/129997-doom-rpg-port-reverse-engineering-update-version-022-source-code-release/

   to get DoomRPG.zip file. Copy DoomRPG.zip to your device.

   Run this android port and specify path to this file in launcher.

4.  For running Doom2 rpg get Doom2 rpg ipa.

   Copy this ipa file to your device.
   
   Run this android port and specify path to this file in launcher

## Credits
This port using

DoomRPG engine source code: [Licence](https://github.com/Erick194/DoomRPG-RE/blob/main/LICENSE) 	https://github.com/Erick194/DoomRPG-RE

Doom2RPG engine source code: [Licence](https://github.com/Erick194/DoomIIRPG-RE/blob/main/LICENSE)	https://github.com/Erick194/DoomIIRPG-RE

WolfensteinRPG engine source code: [Licence](https://github.com/Erick194/WolfensteinRPG-RE/blob/main/LICENSE) 	https://github.com/Erick194/WolfensteinRPG-RE

Doom64EX-Plus engine source code: [Licence](https://github.com/atsb/Doom64EX-Plus/blob/main/COPYING) 	https://github.com/atsb/Doom64EX-Plus/

DOOM64-EX-Plus-Enhanced engine source code: [Licence](https://github.com/Styd051/DOOM64-EX-Plus-Enhanced/blob/main/COPYING) 	https://github.com/Styd051/DOOM64-EX-Plus-Enhanced

PsyDoom engine source code: [Licence](https://github.com/BodbDearg/PsyDoom/blob/master/LICENSE) 	https://github.com/BodbDearg/PsyDoom

UZDoom engine source code: [Licence](https://github.com/UZDoom/UZDoom/blob/trunk/LICENSE) 	https://github.com/UZDoom/UZDoom

Glslang: [Licence](https://github.com/KhronosGroup/glslang/blob/main/LICENSE.txt) 	https://github.com/KhronosGroup/glslang

VulkanMemoryAllocator: [Licence](https://github.com/GPUOpen-LibrariesAndSDKs/VulkanMemoryAllocator/blob/master/LICENSE.txt) 	https://github.com/GPUOpen-LibrariesAndSDKs/VulkanMemoryAllocator

volk: [Licence](https://github.com/zeux/volk/blob/master/LICENSE.md) 	https://github.com/zeux/volk

ZMusic: [Licence](https://github.com/ZDoom/ZMusic/blob/master/licenses/legal.txt) 	https://github.com/ZDoom/ZMusic

NG-GL4ES: [Licence](https://github.com/BZLZHH/NG-GL4ES/blob/main/LICENSE)	 https://github.com/BZLZHH/NG-GL4ES

NG-GL4ES android openmw fork: [Licence](https://github.com/Sisah2/NG-GL4ES/blob/public/LICENSE) 	https://github.com/Sisah2/NG-GL4ES

gl4es: [Licence](https://github.com/ptitSeb/gl4es/blob/master/LICENSE)  https://github.com/ptitSeb/gl4es

gl4es fork from minecraft devs: [Licence](https://github.com/Uniaball/gl4es/blob/master/LICENSE)  https://github.com/Uniaball/gl4es

fmod: [Licence](https://www.fmod.com/legal)		https://www.fmod.com/

libpng: [Licence](https://github.com/pnggroup/libpng/blob/libpng16/LICENSE)	 https://github.com/pnggroup/libpng

jna: [Licence](https://github.com/java-native-access/jna/blob/master/LICENSE) 	https://github.com/java-native-access/jna

file picker: [Licence](https://github.com/codekidX/storage-chooser/blob/master/LICENSE)	https://github.com/codekidX/storage-chooser

code from Android OMW: [Licence](https://gitlab.com/cavebros/openmw-android-docker/-/blob/main/LICENSE.txt) https://gitlab.com/cavebros/openmw-android-docker

code from xyzz legacy Android openmw (sdl changes): [Licence](https://github.com/xyzz/openmw-android/blob/master/LICENSE.txt)	https://github.com/xyzz/openmw-android

code from GZDOOM Android: [Licence](https://github.com/emileb/gzdoom/blob/master/LICENSE)	https://github.com/emileb/gzdoom

koin for DI: [Licence](https://github.com/InsertKoinIO/koin/blob/main/LICENSE)	https://github.com/InsertKoinIO/koin

oboe for audio: [Licence](https://github.com/google/oboe/blob/main/LICENSE) 	https://github.com/google/oboe 

openal: [Licence](https://github.com/kcat/openal-soft/blob/master/COPYING) [Licence](https://github.com/kcat/openal-soft/blob/master/LICENSE-pffft)		https://github.com/kcat/openal-soft

SDL2: [Licence](https://github.com/libsdl-org/SDL/blob/SDL2/LICENSE.txt)	https://github.com/libsdl-org/SDL/tree/SDL2

SDL2_mixer: [Licence](https://github.com/libsdl-org/SDL_mixer/blob/main/LICENSE.txt)	https://github.com/libsdl-org/SDL_mixer

SDL2_TTF: [Licence](https://github.com/libsdl-org/SDL_ttf/blob/SDL2/LICENSE.txt) 	https://github.com/libsdl-org/SDL_ttf/tree/SDL2

SDL3: [Licence](https://github.com/libsdl-org/SDL/blob/main/LICENSE.txt)	 https://github.com/libsdl-org/SDL/

fluidsynth: [Licence](https://github.com/FluidSynth/fluidsynth/blob/master/LICENSE)		https://github.com/FluidSynth/fluidsynth

fluidsynth android fork: [Licence](https://github.com/VolcanoMobile/fluidsynth-android/blob/master/LICENSE)		https://github.com/VolcanoMobile/fluidsynth-android

CTranslate2: [Licence](https://github.com/OpenNMT/CTranslate2/blob/master/LICENSE)		https://github.com/OpenNMT/CTranslate2

sentencepiece: [Licence](https://github.com/google/sentencepiece/blob/master/LICENSE)	https://github.com/google/sentencepiece

freetype: [Licence](https://gitlab.freedesktop.org/freetype/freetype/-/blob/master/LICENSE.TXT)		https://gitlab.freedesktop.org/freetype/freetype

java bing translate api: https://github.com/marmot-z/bing-translate-api-java

apache commons:[Licence](https://commons.apache.org/proper/commons-bsf/license.html)	 https://commons.apache.org/

draggable lazy column support: [Licence](https://github.com/ernestoyaquello/DragDropSwipeLazyColumn/blob/main/LICENSE)	https://github.com/ernestoyaquello/DragDropSwipeLazyColumn

compose preferences: [Licence](https://github.com/sproctor/ComposePreferences/blob/main/LICENSE)	https://github.com/sproctor/ComposePreferences

kotlin collections: [Licence](https://github.com/Kotlin/kotlinx.collections.immutable/blob/master/LICENSE.txt)		https://github.com/Kotlin/kotlinx.collections.immutable

Mlkit: [Licence](https://developers.google.com/ml-kit/terms)	https://developers.google.com/ml-kit

OpusMT EN -> RU: [Licence](https://github.com/Helsinki-NLP/OPUS-MT-train/blob/master/LICENSE)	https://github.com/Helsinki-NLP/OPUS-MT-train

M2M100: [Licence](https://github.com/facebookresearch/fairseq/blob/main/LICENSE) 	https://huggingface.co/facebook/m2m100_418M

small100: [Licence](https://github.com/alirezamshi-zz/small100/blob/main/LICENSE)	 https://github.com/alirezamshi-zz/small100

NLLB200: [Licence](https://github.com/facebookresearch/fairseq/blob/main/LICENSE)	https://huggingface.co/facebook/nllb-200-distilled-600M

google crashlytics for getting crashes: [Licence](https://firebase.google.com/terms/crashlytics)	 https://firebase.google.com/products/crashlytics

zip4j: [Licence](https://github.com/srikanth-lingala/zip4j/blob/master/LICENSE)		https://github.com/srikanth-lingala/zip4j

room ORM for sqlite: [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt)		https://developer.android.com/jetpack/androidx/releases/room

gson: [Licence](https://github.com/google/gson/blob/main/LICENSE)	https://github.com/google/gson

retrofit: [Licence](https://github.com/square/retrofit/blob/trunk/LICENSE.txt)	 https://github.com/square/retrofit

icu:  [Licence](https://github.com/unicode-org/icu/blob/main/LICENSE)	https://icu.unicode.org/

datastore: [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt)	https://developer.android.com/jetpack/androidx/releases/datastore

compose for all ui: [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt)	https://developer.android.com/compose

okhttp: [Licence](https://github.com/square/okhttp/blob/master/LICENSE.txt)	 https://github.com/square/okhttp

lombok: [Licence](https://github.com/projectlombok/lombok/blob/master/LICENSE)	 https://github.com/projectlombok/lombok

kotlin coroutines: [Licence](https://github.com/Kotlin/kotlinx.coroutines/blob/master/LICENSE.txt)	https://github.com/Kotlin/kotlinx.coroutines

legacy material dialogs: [Licence](https://github.com/afollestad/material-dialogs/blob/main/LICENSE.md)	 https://github.com/afollestad/material-dialogs/

for icons are using: [Licence](https://creativecommons.org/licenses/by/3.0/) 	https://game-icons.net/ 	

[Licence](https://github.com/google/material-design-icons/blob/master/LICENSE)  https://fonts.google.com/icons

LanaPixel ttf font:	[Licence](https://github.com/ericoporto/pixel-utf8-fonts/blob/main/lanapixel/LICENSE.txt) 	https://opengameart.org/content/lanapixel-localization-friendly-pixel-font

UnityFont EX ttf font:	[Licence](https://github.com/stgiga/UnifontEX/blob/main/LICENSE)	https://github.com/stgiga/UnifontEX

SDL_GameControllerDB: [Licence](https://github.com/mdqinc/SDL_GameControllerDB/blob/master/LICENSE)	 https://github.com/mdqinc/SDL_GameControllerDB

Kotlin: [Licence](https://github.com/JetBrains/kotlin/tree/master/license) [Licence](https://github.com/JetBrains/kotlin/blob/master/license/LICENSE.txt)	https://github.com/JetBrains/kotlin

Java: [Licence](https://github.com/openjdk/jdk/blob/master/LICENSE)	https://github.com/openjdk/jdk 	https://www.oracle.com/java/

C++: https://isocpp.org/

C: https://www.c-language.org/

Android NDK: https://github.com/android/ndk

Android SDK: https://github.com/AndroidSDKSources

Cmake: [Licence](https://gitlab.kitware.com/cmake/cmake/-/blob/master/Licenses/LGPLv3.txt)  https://gitlab.kitware.com/cmake/cmake

Clang: [Licence](https://github.com/llvm/llvm-project/blob/main/LICENSE.TXT)  https://github.com/llvm/llvm-project

Gradle: [Licence](https://github.com/gradle/gradle/blob/master/LICENSE)  https://github.com/gradle/gradle

Lemon: [Licence](https://en.wikipedia.org/wiki/Public_domain)  https://www.hwaci.com/sw/lemon/

Re2C: [Licence](https://re2c.org/#license)	https://re2c.org/	https://sourceforge.net/projects/re2c/

SQLite: [Licence](https://sqlite.org/copyright.html)	https://sqlite.org/	https://github.com/sqlite/sqlite

multiplatform-settings: [Licence](https://github.com/russhwolf/multiplatform-settings/blob/main/LICENSE.txt)  https://github.com/russhwolf/multiplatform-settings

slf4j: [Licence](https://github.com/qos-ch/slf4j/blob/master/LICENSE.txt)  https://github.com/qos-ch/slf4j

glad as UZDoom dependency: [Licence](https://github.com/Dav1dde/glad/blob/glad2/LICENSE)  https://github.com/Dav1dde/glad

androidx: [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt) https://github.com/androidx

All UZDoom credits and Doom credits belong the original authors (id Software (Doom), Raven Software (Hexen/Heretic),

 Rogue Software (Strife), Digital Café (Chex), Nash Muhandes)

### Licenses

This project is a compilation of many open-source components. Each component is distributed under its own license. The source code and license terms for each component can be found in their respective repositories linked above.

Complete license texts for all third-party components are consolidated in the [THIRD-PARTY-LICENSES.TXT](https://github.com/sandstranger/com.mobilerpgpack.phone/blob/main/THIRD-PARTY-LICENSES.TXT)

The core project is distributed under the [GPL v3](LICENSE) license.

### Copyright Notices

Original copyright notices for all used components are preserved in their respective source files.
