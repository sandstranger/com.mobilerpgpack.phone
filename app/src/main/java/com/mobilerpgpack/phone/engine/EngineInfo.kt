package com.mobilerpgpack.phone.engine

import android.content.Context
import com.mobilerpgpack.phone.ui.screen.ButtonState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class EngineInfo(val mainEngineLib: String,
                 val allLibs : Array<String>,
                 val buttonsToDraw : Collection<ButtonState>,
                 val pathToResourcesCallback: (Context) ->Flow<String?> = { EmptyFlow.defaultInstance })

private class EmptyFlow : Flow <String?>{
    override suspend fun collect(collector: FlowCollector<String?>) {}

    companion object {
        val defaultInstance = EmptyFlow()
    }
}