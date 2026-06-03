# Unofficial Android Ports of Classic Game Engines

This repository contains a collection of unofficial Android ports for the following classic game engines and projects:

- **Doom RPG & Doom II RPG** ([RE](https://github.com/Erick194/DoomRPG-RE) | [II RE](https://github.com/Erick194/DoomIIRPG-RE))
- **Wolfenstein RPG** ([RE](https://github.com/Erick194/WolfensteinRPG-RE))
- **Doom64 EX Plus** ([atsb](https://github.com/atsb/Doom64EX-Plus/) | [Enhanced](https://github.com/Styd051/DOOM64-EX-Plus-Enhanced))
- **PsyDoom** ([BodbDearg](https://github.com/BodbDearg/PsyDoom))
- **UZDoom** ([UZDoom](https://github.com/UZDoom/UZDoom))
- **Perfect Dark** ([fgsfdsfgs](https://github.com/fgsfdsfgs/perfect_dark))
- **Arx Libertatis** ([arx](https://github.com/arx/ArxLibertatis))
- **FTEQW** ([fte-team](https://github.com/fte-team/fteqw))
- **Widelands** ([widelands](https://github.com/widelands/widelands))
- **Vanilla-Conquer** ([TheAssemblyArmada](https://github.com/TheAssemblyArmada/Vanilla-Conquer))
- **Classic-RBDOOM-3-BFG** ([MadDeCoDeR](https://github.com/MadDeCoDeR/Classic-RBDOOM-3-BFG))

---

## Status: Long-Term Support (LTS)

This repository is currently in **Long-Term Support (LTS)** mode. Active feature development is closed; the focus is strictly on stability and critical maintenance.

## Future Development

The following projects are scheduled for Android porting:
- **[Citadel](https://github.com/JosiahJack/Citadel)** (Next release)
- **[UnderworldGodot](https://github.com/hankmorgan/UnderworldGodot)** (Future roadmap)

*Note: There are no other open-source mobile ports planned after these projects.*

---

> **  Global Architecture Note: SDL3 Migration**
> All SDL2-based engines in this repository have been migrated to **SDL3** via [sdl2-compat](https://github.com/libsdl-org/sdl2-compat). Be aware that `sdl2-compat` on Android is **not officially supported** by the SDL team, but it is required for modernizing the input and window management layers across these legacy codebases.

##    System Requirements

### Minimum
- **OS:** Android 7.0 or later
- **GPU:** Hardware supporting OpenGL ES 2.0
- **RAM:** 1 GB system RAM

### Recommended
- **OS:** Android 12 or later

---

## Vulkan Support via ANGLE
Starting from the latest releases, all ports (except Doom1 RPG and Vanilla-Conquer) support **Vulkan** rendering through [ANGLE](https://github.com/google/angle).
- **Requirements:** Android 8.0+ and Vulkan 1.0/1.1 hardware support.

---

## Keyboard and Mouse Support
Full keyboard and mouse input is supported across the ports.

---

##  Controllers Support

Physical controllers and on-screen controls are mutually exclusive. Most legacy engines natively support only a single gamepad.
> **Note:** The on-screen controls emulate an Xbox controller layout. To use a physical controller, you must disable the on-screen overlay in the launcher settings.

### Default Gamepad Mappings

#### Doom RPG
| Action | Button |
| :--- | :--- |
| Move Forward / Backward | D-pad Up / Down |
| Turn Left / Right | D-pad Left / Right |
| Attack / Talk / Use | **A** |
| Next / Previous Weapon | Right / Left Trigger |
| Pass Turn | **X** |
| Automap | **Select** |
| Menu Open / Back | **Start** |

#### Doom II RPG
| Action | Button |
| :--- | :--- |
| Move Forward / Backward | D-pad Up / Down |
| Turn Left / Right | D-pad Left / Right |
| Attack / Talk / Use | **A** |
| Next / Previous Weapon | Right / Left Trigger |
| Pass Turn | **X** |
| Automap | **Select** |
| Menu Open / Back | **Start** |
| Items Info / Drinks | Left / Right Shoulder |
| PDA | **B** |
| Bot Dis | **Y** |

#### Wolfenstein RPG
| Action | Button |
| :--- | :--- |
| Move Forward / Backward | D-pad Up / Down |
| Turn Left / Right | D-pad Left / Right |
| Attack / Talk / Use | **A** |
| Next / Previous Weapon | Right / Left Trigger |
| Pass Turn | **X** |
| Automap | **Select** |
| Menu Open / Back | **Start** |
| Items Info / Syringes | Left / Right Shoulder |
| Journal | **Y** |

---

##  Translation

The launcher supports machine translation for the Doom RPG series into multiple languages. Translation is performed locally on the Android device via the [CTranslate2](https://github.com/OpenNMT/CTranslate2) API, requiring no internet connection.

---

##  Engine-Specific Instructions & Known Issues

### How to run Doom RPG Series
1. **Wolfenstein RPG:** Obtain the `.ipa` file, copy it to your device, and select its path in the launcher.
2. **Doom RPG:** Obtain the Brew file. Use the [BarToZip PC tool](https://github.com/Erick194/DoomRPG-RE/releases/tag/v.0.2.2) and follow the [Doomworld instructions](https://www.doomworld.com/forum/topic/129997-doom-rpg-port-reverse-engineering-update-version-022-source-code-release/) to generate `DoomRPG.zip`. Copy the ZIP to your device and select it in the launcher.
3. **Doom II RPG:** Obtain the `.ipa` file, copy it to your device, and select its path in the launcher.

### Doom 64 (Doom64 EX Plus)
- **Game Data:** Requires the Doom 64 Remaster from [Steam](https://store.steampowered.com/app/1148590/DOOM_64/).
- **Mods:** Community mods are available on the [Doom 64 Discord](https://discord.com/invite/doom-64-593915163896315905).

### PsyDoom
- **Game Data:** Requires a Doom PSX disc image (`.cue`/`.bin`). See the [official PsyDoom documentation](https://github.com/BodbDearg/PsyDoom?tab=readme-ov-file#running-the-game).
- **Rendering:** Requires Vulkan 1.0 support. Devices without Vulkan must use the legacy OpenGL ES renderer.
- **Mali/PowerVR Performance:** Low FPS on Mali and PowerVR GPUs is expected due to GPU stalls. This is an architectural limitation and will not be fixed.
- **Alternative:** For a playable PS1 Doom experience on Android, it is highly recommended to use [DOOM CE via UZDoom](https://www.moddb.com/mods/doom-ce/downloads/psx-doom-ce-205-full-version).

### UZDoom
- **Game Data:** Follow the [GZDoom documentation](https://zdoom.org/w/index.php?title=Main_Page).
- **Known Issues & Workarounds:**
  - **Vulkan Renderer:** The native Vulkan renderer remains unplayable and will not be fixed. Upstream developers are responsible for this code.
  - **OpenGL Emulation on Mali/PowerVR:** To avoid black screens caused by a bug in the UZDoom codebase (not the GPU drivers), OpenGL emulation now forces Vulkan rendering via ANGLE. **Requirements:** Vulkan support and Android 8.0+.
  - **OpenGL Emulation on Adreno:** Works natively with the stock GLES driver without the aforementioned bugs; forced ANGLE is not applied.
  - **Fallback:** If Vulkan/ANGLE is unavailable on Mali/PowerVR, use the legacy native OpenGL ES renderer (ported by beloko), which does not suffer from these black screen issues.
- **Credits:** Contains code from [emileb's GZDoom Android port](https://github.com/emileb/gzdoom) and [glKarin's GLES 3.x native render](https://github.com/glKarin/com.n0n3m4.diii4a). Licensed under GPL v3.

### Perfect Dark
- **Game Data:** Follow the [Perfect Dark port documentation](https://github.com/fgsfdsfgs/perfect_dark?tab=readme-ov-file#perfect-dark-port).
- **Requirements:** ROM binaries must have `.z64` or `.Z64` extensions. Requires OpenGL ES 3.0 support. Legacy ARMv7 devices are fully supported.

### Arx Libertatis
- **Game Data:** Follow the [Arx Libertatis wiki](https://wiki.arx-libertatis.org/Getting_the_game_data).
- **Features:** Fully supports GLES 3.0 and GLES 2.0 rendering, improved controls, and full ARMv7 support.
- **Vulkan on Mali:** Vulkan rendering via ANGLE is fully playable on Mali GPUs. Previous low FPS issues caused by GPU stalls have been resolved.
- **Credits:** Integrates brightness fixes from [xyzz's OpenMW Android port](https://github.com/xyzz/openmw-android) and gamepad implementations from the [Nintendo Switch port](https://github.com/fgsfdsfgs/ArxLibertatis).

### FTEQW
- **Game Data:** Follow the [FTEQW Wiki](https://quakewiki.org/wiki/FTEQW_Wiki).
- **Vulkan Renderer:** The stock Vulkan renderer is completely disabled due to severe architectural flaws and instability. Use ANGLE if Vulkan rendering is strictly required.
- **Engine Support:** 
  - **Quake 1, Hexen 2:** Fully playable in single-player. Multiplayer in Quake 1 is unstable.
  - **Quake 2:** Fully supported with all official addons via [yquake2](https://github.com/yquake2/yquake2) (all addons are pre-compiled for Android). Single-player is playable; multiplayer works only with local bots.
  -  **Quake 3:** Support is nominal. Single-player is partially playable; multiplayer is non-functional.
  - **Half-Life 1:** Not supported and excluded from the launcher.
- **Known Bugs:** Audio playback for certain sounds may fail in Quake2. This is an upstream FTEQW bug and will not be addressed.
- **Audio:** Uses SDL3 and AAUDIO.
- **Credits:** Integrates upstream Android bugfixes from [glKarin](https://github.com/glKarin/com.n0n3m4.diii4a) and [emileb](https://github.com/emileb/fteqw).

### Widelands
- **Game Data:** Standalone RTS; no third-party assets required.
- **Features:** Supports GLES 2.0/3.0, ARMv7, screen scaling, and on-screen controls.
- **Limitations:** Gamepads are not supported. Multiplayer is deprecated (only basic server connections were tested).
- **Docs:** [Official Wiki](https://www.widelands.org/wiki/Main%20Page/).

### Vanilla-Conquer
- **Supported Games:** Command & Conquer: Tiberian Dawn, Red Alert.
- **Game Data:** 
  - **Tiberian Dawn:** Requires original resources. DOS mode is enabled by default for better mobile scaling. If you see a black screen, extract `LOCAL.MIX` from the [DOS version](https://www.moddb.com/games/cc-gold/downloads/command-and-conquer-dos) and place it in your CNCNet resources folder.
  - **Red Alert:** Use official [CNCNet resources](https://cncnet.org/red-alert).
- **Features:** ARMv7 support. Gamepads are supported via on-screen emulation.
- **Known Issues:**
  - **Screen Reposition (#55):** Rendering behind the camera is standard on Android in 2026, but Vanilla-Conquer fails to handle screen reposition events correctly, causing UI elements to render off-screen.
  - **Hardware Back Button:** The Android hardware back button is non-functional.
  - **Lifecycle Crashes:** Calling `onPause`/`onResume` events can cause immediate render crashes.
- **Credits:** Integrates code from the [Nintendo Switch port](https://github.com/capsterx-switch/Vanilla-Conquer).

### Classic-RBDOOM-3-BFG
- **Rendering:** Strictly OpenGL ES 3.2 due to heavy reliance on modern GLSL 3.x features. GLES 3.1 and 3.0 API are supported, but expect issues, like lower FPS than with GLES 3.2 API
- **Texture Compression (DXT vs ETC2):** Hardware DXT decoding is implemented and functional, but it is highly recommended to disable it and use the ETC2 fallback. **Do not disable the ETC2 texture cache** (even though the toggle exists).
- **Adreno GPUs:** Enable Vulkan rendering via ANGLE for maximum frame rates.
- **Mali/PowerVR GPUs:** Doom 3 BFG is a heavy IMR (Immediate Mode Rendering) engine. Performance on tile-based GPUs is inherently limited. Use the custom Android UI options to disable heavy effects and lower the screen resolution. No further engine-level optimizations will be made for these architectures.
- **Memory Optimization (Texture Shrinking):** Enable texture shrinking (skipping MIP-map levels) in the launcher to save 200-300 MB of RAM. 
- **ARMv7 Limitations:** Running this engine on ARMv7 devices will likely result in OOM (Out of Memory) crashes due to the severe RAM limitations of 32-bit architectures. This is expected behavior.
- **Credits:** Integrates renderer code from [glKarin](https://github.com/glKarin/com.n0n3m4.diii4a) and bugfixes from [emileb's dhewm3 port](https://github.com/emileb/d3es-multithread).

---

## Building from Source

1. Clone the repository and open the root directory in Android Studio.
2. Install **Python 3.x** (required for building native dependencies).
3. *Highly Recommended:* Install [ccache](https://ccache.dev/) to drastically reduce compilation times for the 13 engines and native dependencies. (Windows users can use [Chocolatey](https://community.chocolatey.org/packages/ccache)).
4. Run the project.

> **Important:** Building from source is strictly supported only for **F-Droid** build variants. Google Play variants are proprietary due to Crashlytics integration.
> **Build Host:** Only **Windows 11 (25H2) x64** is officially supported as the build environment.

---

## Download

Pre-compiled APKs are available via:
- [F-Droid](https://f-droid.org/packages/com.mobilerpgpack.phone/)
- [GitHub Releases](https://github.com/sandstranger/doom-wolf-rpg-android-port/releases/latest)

---

## Credits & Third-Party Licenses

This project is a compilation of numerous open-source components. Each component is distributed under its own license. Complete license texts are consolidated in [THIRD-PARTY-LICENSES.TXT](https://github.com/sandstranger/com.mobilerpg.phone/blob/main/THIRD-PARTY-LICENSES.TXT).

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

etcpack - [Licence](https://github.com/wolfpld/etcpak/blob/master/LICENSE.txt) https://github.com/wolfpld/etcpak

*All UZDoom and Doom credits belong to the original authors (id Software, Raven Software, Rogue Software, Digital Café, Nash Muhandes).*

---

### Licenses

This project is a compilation of many open-source components. Each component is distributed under its own license. The source code and license terms for each component can be found in their respective repositories linked above.

Complete license texts for all third-party components are consolidated in the [THIRD-PARTY-LICENSES.TXT](https://github.com/sandstranger/com.mobilerpg.phone/blob/main/THIRD-PARTY-LICENSES.TXT).

The core project is distributed under the [GPL v3](LICENSE) license.

### Copyright Notices

Original copyright notices for all used components are preserved in their respective source files.
