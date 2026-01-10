//
// Created by sandstranger on 04.01.2026.
//
#include "SDL.h"
#include "SDL_mouse_c.h"
#include "SDL_vulkan.h"

static SDL_Window *window = nullptr;
static Uint32 windowId;
static SDL_Mouse *mouse;

void nativeGyroMouse(int dx, int dy) {
    if (!mouse){
        mouse = SDL_GetMouse();
        if (mouse){
            window = mouse ->focus;
            if (window) {
                windowId = SDL_GetWindowID(window);
            }
        }
    }

    if (!window) {
        window = SDL_GetGrabbedWindow();
        if (window) {
            windowId = SDL_GetWindowID(window);
        }
    }

    if (!window || !mouse) {
        return;
    }

    SDL_Event ev;
    ev.type = SDL_MOUSEMOTION;
    ev.motion.windowID = windowId;
    ev.motion.which = 0;
    ev.motion.state = 0;
    ev.motion.x = 0;
    ev.motion.y = 0;
    ev.motion.xrel = dx;
    ev.motion.yrel = dy;
    SDL_PushEvent(&ev);

    mouse->xdelta += dx;
    mouse->ydelta += dy;

    mouse->x += dx;
    mouse->y += dy;

    int w=0,h=0;
    Uint32 flags = SDL_GetWindowFlags(window);
    if (flags & SDL_WINDOW_VULKAN) {
        SDL_Vulkan_GetDrawableSize(window, &w, &h);
    } else {
        SDL_GL_GetDrawableSize(window, &w, &h);
    }
    if (mouse->x < 0) mouse->x = 0;
    if (mouse->y < 0) mouse->y = 0;
    if (mouse->x > w) mouse->x = w;
    if (mouse->y > h) mouse->y = h;
}