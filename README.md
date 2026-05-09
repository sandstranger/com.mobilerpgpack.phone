# Unofficial ports of Doom 1,2, Wolfenstein RPG, Doom64 EX Plus, PsyDoom, UZDoom, Perfect Dark, Arx Libertatis, FTEQW, Widelands, Vanilla-Conquer, Classic-RBDOOM-3-BFG for Android
This is an Android ports of Doom 1,2, Wolfenstein RPG, Doom64 EX Plus, PsyDoom, UZDoom, Perfect Dark, Arx Libertatis, FTEQW, Widelands, Vanilla-Conquer,
Classic-RBDOOM-3-BFG projects found here:

https://github.com/Erick194/DoomRPG-RE

https://github.com/Erick194/DoomIIRPG-RE

https://github.com/Erick194/WolfensteinRPG-RE

https://github.com/atsb/Doom64EX-Plus/

https://github.com/Styd051/DOOM64-EX-Plus-Enhanced

https://github.com/BodbDearg/PsyDoom

https://github.com/UZDoom/UZDoom

https://github.com/fgsfdsfgs/perfect_dark

https://github.com/arx/ArxLibertatis

https://github.com/fte-team/fteqw

https://github.com/widelands/widelands

https://github.com/TheAssemblyArmada/Vanilla-Conquer

https://github.com/MadDeCoDeR/Classic-RBDOOM-3-BFG

## STATUS UPDATE: READ-ONLY & LTS MODE

This repository is now in Long-Term Support (LTS) mode.

Public active development and feature implementation have ceased. The project is considered complete in its current state.

What this means:
* No New Features: No new functionality, optimizations, or new engine will be implemented publicly.
   Source Sync Only: Future releases will occur rarely and exclusively to synchronize with upstream source code updates of the original engines (e.g., security patches, critical bug fixes from upstream).
* No Community Support: I do not accept Issues, Pull Requests, or feature requests. I will not answer questions regarding compilation, installation, or 
sage.
* Self-Service: If you require specific changes, fork the repository and maintain your own version.

## System Requirements

This port for Android has the following system requirements:
### Minimum
* Operating system: Android 7.0 or later
* Graphics: videocard, which supports GLES 2.0 render
* Memory: 1GB system RAM

### Recommended
* Operating system: Android 12 or later (I tested it mostly on my phone with Android 12)

## Graphics

These source ports supported OpenGL ES 3.0 render with NG-GL4ES.

If your device is not supported OPenGL ES 3.0 render or you have graphics issues like black screen, then use binary

with OpenGL ES 2.0 render

## Angle

At latest release all these source ports except (Doom1RPG and Vanilla-Conquer) started to support Vulkan render with Angle help.
Angle support requries minimum Android 8 and vulkan 1.0 (1.1) support.
On mtk it will be better to emulate gles 2.0 render with angle for better fps

## Controllers Support
This Android ports support controllers

On-Screen Controls and physical controllers cannot be used together. Most legacy engines support only one gamepad.

On-screen controls already emulate an Xbox controller for player movement. Disable on-screen controls if you want real controller support.

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

For running Uzdoom follow gzdoom docs - https://zdoom.org/w/index.php?title=Main_Page

Uzdoom engine has rendering issues at all mobile gpu vendors, except adreno.

Do not expect working vulkan render and OpenGL emulation at gpu vendors except adreno.

At mobile vendors - like mali or powevr use GLES 2.0 or GLES 3.x render. 

This project contains code from GZDoom android port (https://github.com/emileb/gzdoom),

which is licensed under GPL v3. All modifications are clearly marked in commit history.

And using render changes (like OGL emulation, gles render and some bugfixes for Android platform)

credits for Android render changes and Android bugfixes to emileb - https://github.com/emileb

And contains code from glKarin GZDoom android port - https://github.com/glKarin/com.n0n3m4.diii4a

Gles 3.0 - Gles 3.2 native render support used from glKarin gzdoom port - https://github.com/glKarin/com.n0n3m4.diii4a

All credits for Gles 3.x native render support to glKarin - https://github.com/glKarin

This project is ALSO licensed under GPL v3.

## Perfect Dark

For running perfect dark follow these docs - https://github.com/fgsfdsfgs/perfect_dark?tab=readme-ov-file#perfect-dark-port

All Rom binaries neeed to have .z64 or .Z64 file extensions. Otherwise, the game will not start.

For running perfect dark - your device need to support OPEN GL ES 3.0 render. Otherwise, the game will not start again.

This perfect dark port also supported legacy ARMV7 devices without any issues.

## ArxLibertatis

This port is far superior in quality to the old Arx Libertatis port, which was the first I ever developed for Android

In particular, it is fully supports both GLES 3.2 and GLES 2.0 rendering, has more convenient controls, and it has full ArmV7 support.

It is also has a ton of other improvements.

For running this port follow these ArxLibertatis docs - https://wiki.arx-libertatis.org/Getting_the_game_data

This port uses code from the legacy Android openmw repository as a solution to not working changing brightness bug - https://github.com/xyzz/openmw-android

All credits for solution for not working changing brightness in Android Arx Libertatis to xyzz - https://github.com/xyzz

This port is using gamepads code implementation from ArxLibertatis nintendo switch port - https://github.com/fgsfdsfgs/ArxLibertatis

All credits for gamepads implementation in ArxLibertatis android port to fgsfdsfgs - https://github.com/fgsfdsfgs  

## FTEQW

For running FTEQW follow these docs - https://quakewiki.org/wiki/FTEQW_Wiki

FTEQW supports quake 1, quake 2, quake 3, hexen 2 and half life 1. However quake 2 and quake 3 support is nominal

This engine does not have quake 2 native support. Instead it - it is using this engine https://github.com/yquake2/yquake2

This engine loaded core yquake2 in runtime. You can play quake 2 single player campaign with this support. But Multiplayer will not work.

The same applies to Quake 3. Support for Quake 3 is purely nominal - therefore multiplayer does not work in it at all. You can only play with bots in the 

single player, and even then it is unstable.

There is no support for Half Life 1 at all. It just doesn't work at all. That's why I didn't even include it in the launcher

quake 1 net code also unstable with a lot of bugs.

You can play quake1 , quake 2 and hexen 2 single player I think without major bugs.

Rendering support: This engine supports opengl and vulkan on desktop. 

On mobiles - it is suppports gles 2.0, gles 3.0 and vulkan renders, because I fixed almost all vulkan renders bugs in this engine for mobiles,

so, it is working now even on mtk

However vulkan support in this engine architecturally inefficient. Rendering performance will be very low even on Adreno

Due to this - do not use it, use gles 2.0 or gles 3.0 render instead it even on adreno.

For audio - openal with oboe is using, instead of SDL2 with openSLES support.

This port of FTEQW contains code from glKarin's FTEQW Android port: https://github.com/glKarin/com.n0n3m4.diii4a

And also used some bugfixes from beloko FTEQW  Android port: https://github.com/emileb/fteqw

All credits for FTEQW Android bugfixes go to glKarin: https://github.com/glKarin and to emileb - https://github.com/emileb

I used these bugfixes for this legacy engine because fixing bugs in this awful legacy engine from scratch provides no practical benefit.

I do not have time to fix legacy engine bugs from zero if fixes already exist.

All credits and licenses are properly acknowledged in this port. Any other ideas or issues are not my responsibility.

If you want to fix bugs in this engine from scratch yourself, feel free to fork the FTEQW source code and adapt it for Android.

## Widelands

Widelands is a standalone RTS with its own assets. No third-party resources are required to run it on Android.

Supports GLES 2.0 and GLES 3.2 renders.

Works on ARMv7 devices.

Gamepads are not supported and will not be added.

Screen scaling and on-screen controls are implemented for mobile devices.

Multiplayer is dead in this RTS, only remote asset downloading and server connection were tested.

For more information, see the official wiki - https://www.widelands.org/wiki/Main%20Page/

## Vanilla-Conquer

This engine supports the classic Command & Conquer games:
- Command & Conquer: Tiberian Dawn  
- Command & Conquer: Red Alert  

To run Tiberian Dawn, you need the original game resources.

Recommended source: https://forums.cncnet.org/topic/8821-cc-1-tiberian-dawn-installation-on-windows-10/

On mobile devices, DOS mode is enabled by default to provide a larger and more comfortable game screen on phones. However, without additional resources, Tiberian Dawn will show only a black screen in DOS mode.

To fix this, download the required DOS version resources from here: https://www.moddb.com/games/cc-gold/downloads/command-and-conquer-dos

From the DOS version of the game, extract the file **LOCAL.MIX** and copy it into your CNCNet Tiberian Dawn resources folder.

To run Red Alert, use the official resources from CNCNet: https://cncnet.org/red-alert

Multiplayer in this RTS is effectively inactive. Only basic server connection functionality was tested.

ARMv7 devices should be supported.  

Gamepads are supported, because On-screen gamepad emulation is used, which works without any known issues

This port is using some code from Vanilla-Conquer nintendo switch port - https://github.com/capsterx-switch/Vanilla-Conquer

All credits for this code to capsterx-switch - https://github.com/capsterx-switch

## Classic-RBDOOM-3-BFG

This Doom 3 BFG Edition port supports only the OpenGL ES 3.2 renderer because of the heavy use of modern GLSL 3.x features on desktop.

The engine can run on ARMv7, but it is not officially supported.

If you experience low FPS on your phone, try reducing the screen resolution and lowering the graphics settings.

This fork of Classic-RBDOOM-3-BFG includes renderer code from glKarin’s Doom 3 BFG Edition Android port: https://github.com/glKarin/com.n0n3m4.diii4a

It also includes bug fixes from beloko’s dhewm3 Android port: https://github.com/emileb/d3es-multithread

All credit for the Android renderer changes in Doom 3 BFG Edition goes to glKarin: https://github.com/glKarin

Additional Android bug fixes are credited to emileb: https://github.com/emileb

## Keyboard and Mouse Support
This Android ports support keyboard and mouse

## Building

To build the APK file, clone this repository, open the project root directory in Android Studio and run the project.

Important: Bulding from source code supported only for fdroid buld variants, google variants are propritary due to crashlytics using.

To build, also need to install Python 3.x - https://www.python.org/downloads/, since it is used when building some dependencies.

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

Perfect Dark engine source code: [Licence](https://github.com/fgsfdsfgs/perfect_dark/blob/port/LICENSE) https://github.com/fgsfdsfgs/perfect_dark

ArxLibertatis engine source code: [Licence](https://github.com/arx/ArxLibertatis/blob/master/LICENSE)	https://github.com/arx/ArxLibertatis

FTEQW engine source code: [Licence](https://github.com/fte-team/fteqw/blob/master/LICENSE)	https://github.com/fte-team/fteqw

Widelands engine source code: [Licence](https://github.com/widelands/widelands/blob/master/COPYING) https://github.com/widelands/widelands

Vanilla-Conquer engine source code: [Licence](https://github.com/TheAssemblyArmada/Vanilla-Conquer/blob/vanilla/License.txt)  https://github.com/TheAssemblyArmada/Vanilla-Conquer

Classic-RBDOOM-3-BFG engine source code: [Licence](https://github.com/MadDeCoDeR/Classic-RBDOOM-3-BFG/blob/master/COPYING.txt) https://github.com/MadDeCoDeR/Classic-RBDOOM-3-BFG

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

Reorderable: [Licence](https://github.com/Calvin-LL/Reorderable/blob/main/LICENSE)	 https://github.com/Calvin-LL/Reorderable

kotlinx.serialization:	[Licence](https://github.com/Kotlin/kotlinx.serialization/blob/master/LICENSE.txt)	https://github.com/Kotlin/kotlinx.serialization

compose preferences(used in previous builds): [Licence](https://github.com/sproctor/ComposePreferences/blob/main/LICENSE)	https://github.com/sproctor/ComposePreferences

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

datastore (used in previous builds): [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt)	https://developer.android.com/jetpack/androidx/releases/datastore

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

multiplatform-settings (used in previous builds): [Licence](https://github.com/russhwolf/multiplatform-settings/blob/main/LICENSE.txt)  https://github.com/russhwolf/multiplatform-settings

slf4j: [Licence](https://github.com/qos-ch/slf4j/blob/master/LICENSE.txt)  https://github.com/qos-ch/slf4j

glad as UZDoom dependency: [Licence](https://github.com/Dav1dde/glad/blob/glad2/LICENSE)  https://github.com/Dav1dde/glad

androidx: [Licence](https://github.com/androidx/androidx/blob/androidx-main/LICENSE.txt) https://github.com/androidx

vorbis-android: [Licence](https://gitlab.xiph.org/xiph/vorbis/-/blob/main/COPYING) https://github.com/MoNTE48/libvorbis-android

opus: [Licence](https://github.com/xiph/opus/blob/main/COPYING)	 https://github.com/xiph/opus

MPG123-Android:	[Licence](https://github.com/rosuH/MPG123-Android/blob/master/LICENSE)	https://github.com/rosuH/MPG123-Android

flac: [Licence](https://github.com/xiph/flac/blob/master/COPYING.Xiph)	https://github.com/xiph/flac

libmp3lame-android: [Licence](https://github.com/xieyangxuejun/libmp3lame-android/blob/master/LICENSE)	 https://github.com/xieyangxuejun/libmp3lame-android

libsndfile: [Licence](https://github.com/libsndfile/libsndfile/blob/master/COPYING)  https://github.com/libsndfile/libsndfile

vpx-android: [Licence](https://github.com/cmeng-git/atalk-android/blob/master/LICENSE)	https://github.com/cmeng-git/atalk-android/tree/master/aTalk/jni/vpx

com.n0n3m4.diii4a: [Licence](https://github.com/glKarin/com.n0n3m4.diii4a/blob/master/LICENSE)	https://github.com/glKarin/com.n0n3m4.diii4a

custom-keyboard-android: [Licence](https://github.com/qinvent/custom-keyboard-android/blob/master/LICENSE)  https://github.com/qinvent/custom-keyboard-android

GLM: [Licence](https://github.com/g-truc/glm/blob/master/copying.txt)	https://github.com/g-truc/glm

code from nintendo switch ArxLibertatis port: [Licence](https://github.com/fgsfdsfgs/ArxLibertatis/blob/switch-new/LICENSE) https://github.com/fgsfdsfgs/ArxLibertatis

boost: [Licence](https://github.com/boostorg/boost/blob/master/LICENSE_1_0.txt)	https://github.com/boostorg/boost

bullet3: [Licence](https://github.com/bulletphysics/bullet3/blob/master/LICENSE.txt)	https://github.com/bulletphysics/bullet3

ffmpeg:	[Licence](https://github.com/FFmpeg/FFmpeg/blob/master/LICENSE.md)	 https://github.com/FFmpeg/FFmpeg

ffmpeg prebult binaries: [Licence](https://github.com/sk3llo/ffmpeg_kit_flutter/blob/master/LICENSE)	 https://github.com/sk3llo/ffmpeg_kit_flutter

libjpeg-turbo: [Licence](https://github.com/libjpeg-turbo/libjpeg-turbo/blob/main/LICENSE.md)	https://github.com/libjpeg-turbo/libjpeg-turbo

bzip2:  https://gitlab.com/bzip2/bzip2

ODE:  [Licence](https://github.com/thomasmarsh/ODE/blob/master/LICENSE-BSD.TXT) https://github.com/thomasmarsh/ODE

yquake2: [Licence](https://github.com/yquake2/yquake2/blob/master/LICENSE)  https://github.com/yquake2/yquake2

code from FTEQW beloko android port: https://github.com/emileb/fteqw

glbinding: [Licence](https://github.com/cginternals/glbinding/blob/master/LICENSE)  https://github.com/cginternals/glbinding

libtiff: [Licence](https://gitlab.com/libtiff/libtiff/-/blob/master/LICENSE.md) https://gitlab.com/libtiff/libtiff

asio: [Licence](https://github.com/chriskohlhoff/asio/blob/master/LICENSE_1_0.txt)  https://github.com/chriskohlhoff/asio/

SDL2 Image: [Licence](https://github.com/libsdl-org/SDL_image/blob/SDL2/LICENSE.txt) https://github.com/libsdl-org/SDL_image/tree/SDL2

Process Phoenix: [Licence](https://github.com/JakeWharton/ProcessPhoenix/blob/trunk/LICENSE.txt)  https://github.com/JakeWharton/ProcessPhoenix

Code from Vanilla-Conquer nintendo switch port: [Licence](https://github.com/capsterx-switch/Vanilla-Conquer/blob/vanilla/License.txt)  https://github.com/capsterx-switch/Vanilla-Conquer

Android Glob: https://github.com/freshHua/glob

Angle: [Licence](https://github.com/google/angle/blob/main/LICENSE) https://github.com/google/angle

android_openssl: [Licence](https://github.com/KDAB/android_openssl/blob/master/LICENSE) https://github.com/KDAB/android_openssl

sse2neon: [Licence](https://github.com/DLTcollab/sse2neon/blob/master/LICENSE) https://github.com/DLTcollab/sse2neon

d3es-multithread:  [Licence](https://github.com/emileb/d3es-multithread/blob/master/COPYING.txt) https://github.com/emileb/d3es-multithread

SPIRV-Tools - [Licence](https://github.com/KhronosGroup/SPIRV-Tools/blob/main/LICENSE) https://github.com/KhronosGroup/SPIRV-Tools

SPIRV-Headers - [Licence](https://github.com/KhronosGroup/SPIRV-Headers/blob/main/LICENSE) https://github.com/KhronosGroup/SPIRV-Headers

SPIRV-Cross - [Licence](https://github.com/KhronosGroup/SPIRV-Cross/blob/main/LICENSE) https://github.com/KhronosGroup/SPIRV-Cross

All UZDoom credits and Doom credits belong the original authors (id Software (Doom), Raven Software (Hexen/Heretic),

 Rogue Software (Strife), Digital Café (Chex), Nash Muhandes)

### Licenses

This project is a compilation of many open-source components. Each component is distributed under its own license. The source code and license terms for each component can be found in their respective repositories linked above.

Complete license texts for all third-party components are consolidated in the [THIRD-PARTY-LICENSES.TXT](https://github.com/sandstranger/com.mobilerpgpack.phone/blob/main/THIRD-PARTY-LICENSES.TXT)

The core project is distributed under the [GPL v3](LICENSE) license.

### Copyright Notices

Original copyright notices for all used components are preserved in their respective source files.
