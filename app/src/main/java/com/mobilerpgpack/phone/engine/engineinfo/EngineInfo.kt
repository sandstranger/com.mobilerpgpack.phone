package com.mobilerpgpack.phone.engine.engineinfo

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Process
import android.system.Os
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobilerpgpack.phone.BuildConfig
import com.mobilerpgpack.phone.engine.EngineTypes
import com.mobilerpgpack.phone.main.KoinModulesProvider
import com.mobilerpgpack.phone.main.buildFullLibraryName
import com.mobilerpgpack.phone.main.gl4esFullLibraryName
import com.mobilerpgpack.phone.ui.screen.screencontrols.ButtonState
import com.mobilerpgpack.phone.utils.PreferencesStorage
import com.mobilerpgpack.phone.utils.callAs
import com.sun.jna.Function
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.io.File
import kotlin.getValue

abstract class EngineInfo(private val mainEngineLib: String,
                          private val allLibs : Array<String>,
                          private val buttonsToDraw : Collection<ButtonState>,
                          private val activeEngineType : EngineTypes,
                          private val pathToResourceFlow : Flow<String>
) : KoinComponent, IEngineInfo {

    protected val preferencesStorage : PreferencesStorage by inject()

    protected val scope = CoroutineScope(Dispatchers.Default)


    protected lateinit var resolution: Pair<Int, Int>
        private set

    protected var controlsOverlayUI : View? = null

    protected var virtualKeyboardView : View? = null

    protected var showVirtualKeyboardSavedState by mutableStateOf(false)

    protected lateinit var activity : Activity
        private set

    protected var needToShowControlsLastState : Boolean = false

    protected val pathToRootUserFolder : String = get (named(
        KoinModulesProvider.USER_ROOT_FOLDER_NAMED_KEY))

    private lateinit var needToShowScreenControlsNativeDelegate : Function

    private external fun pauseSound()

    private external fun resumeSound()

    protected open val engineInfoClazz : Class<*> get() = EngineInfo::class.java

    override val engineType: EngineTypes get() = activeEngineType

    override val pathToResource: Flow<String>  get() = pathToResourceFlow

    override val screenButtonsToDraw: Collection<ButtonState> get() = buttonsToDraw

    override val mainSharedObject: String get() = buildFullLibraryName(mainEngineLib)

    override val nativeLibraries: Array<String> get() = allLibs

    override suspend fun initialize(activity: Activity) {
        this.activity = activity
        initJna()
        initializeCommonEngineData()
        resolution = getRealScreenResolution()

        Os.setenv( "PATH_TO_RESOURCES",
            File (pathToResource.first()).absolutePath, true)
    }

    override fun onPause() {
        pauseSound()
    }

    override fun onResume() {
        resumeSound()
    }

    override fun onDestroy() {
        killEngine()
    }

   protected fun getRealScreenResolution(): Pair<Int, Int> {
        val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val realSize = Point()
        display.getRealSize(realSize)
        return realSize.x to realSize.y
    }

    protected suspend fun changeScreenControlsVisibility(){
        if (this@EngineInfo.controlsOverlayUI == null){
            return
        }

        while (true){
            val needToShowControls : Boolean = needToShowScreenControlsNativeDelegate
                .callAs(Boolean::class.java)

            if (needToShowControls != needToShowControlsLastState){
                this@EngineInfo.activity.runOnUiThread {
                    if (needToShowControls) {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.VISIBLE
                    } else {
                        this@EngineInfo.controlsOverlayUI!!.visibility = View.GONE
                    }
                    updateVirtualKeyboardVisibility(needToShowControls)
                }
            }
            needToShowControlsLastState = needToShowControls
            delay(200)
        }
    }

    protected fun updateVirtualKeyboardVisibility (showVirtualKeyboard: Boolean){
        virtualKeyboardView!!.visibility = if (showVirtualKeyboard && showVirtualKeyboardSavedState)
            View.VISIBLE else View.GONE
    }

    private fun initializeCommonEngineData (){
        val pathToSDL2ControllerDB = "${pathToRootUserFolder}${File.separator}gamecontrollerdb.txt"
        val pathToPsaFolder = getPathToPsaFolder()
        val psaFolder = File(pathToPsaFolder)

        if (!psaFolder.exists()){
            psaFolder.mkdirs()
        }

        Os.setenv("LIBGL_SIMPLE_SHADERCONV", "1", true)
        Os.setenv("LIBGL_DXTMIPMAP", "1", true)
        Os.setenv("LIBGL_ES","3",true)
        Os.setenv("LIBGL_GL","21", true)
        Os.setenv("LIBGL_DXT", "1", true)
        Os.setenv("LIBGL_NOTEXARRAY","0",true)
        Os.setenv("LIBGL_NOPSA", "0",true)
        Os.setenv("LIBGL_PSA_FOLDER",pathToPsaFolder,true)
        Os.setenv("SDL_VIDEO_GL_DRIVER", gl4esFullLibraryName, true)
        Os.setenv("PATH_TO_SDL2_CONTROLLER_DB", pathToSDL2ControllerDB,true)
    }

    private fun getPathToPsaFolder () =
        pathToRootUserFolder + File.separator + if (BuildConfig.LEGACY_GLES2) "gles2" else "gles3"

    private fun killEngine() = Process.killProcess(Process.myPid())

    private fun initJna(){
        needToShowScreenControlsNativeDelegate = Function.getFunction(mainEngineLib,
            "needToShowScreenControls")
        Native.register(engineInfoClazz, mainEngineLib)
    }
}