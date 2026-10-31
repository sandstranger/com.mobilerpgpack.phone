#include "global.inc.hlsl"

// Shadow volumes render into a depth/stencil-only framebuffer (GL_NONE draw
// buffer on Android). No color output is required; leaving the fragment stage
// output-free avoids a redundant color export on tile-based GPUs.
void main()
{
}
