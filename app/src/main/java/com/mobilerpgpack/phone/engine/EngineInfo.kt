package com.mobilerpgpack.phone.engine

import android.content.Context
import com.mobilerpgpack.phone.ui.screen.ButtonState
import kotlinx.coroutines.flow.Flow

class EngineInfo(val mainEngineLib: String,
                 val allLibs : Array<String>,
                 val buttonsToDraw : Collection<ButtonState>,
                 val pathToResourcesCallback: (Context) ->Flow<String?> )