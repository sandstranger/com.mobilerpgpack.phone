//
// Created by sandstranger on 21.06.2026.
//

#ifdef __cplusplus
extern "C" {
#endif
bool SwappySwapBuffers();
void DestroySwappy ();
// True when a graphics debugger/capture tool (RenderDoc, AGI, Arm Graphics Analyzer, Mali Graphics
// Debugger) is loaded into the process. Checked once, the first call must come after the GL context
// was created (the GLES layers are loaded with EGL). Swappy is bypassed then, and engines should keep
// to their plainest code paths.
bool AndroidGraphicsDebuggerAttached(void);
#ifdef __cplusplus
}
#endif