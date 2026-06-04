//
// Created by sandstranger on 04.01.2026.
//
#include <SDL3/SDL.h>
#include "SDL3/SDL_mouse_c.h"
#include "SDL3/SDL_mouse.h"

static SDL_Window *window = nullptr;
static Uint32 windowId;
static SDL_Mouse *mouse;

__attribute__((used)) __attribute__((visibility("default")))
void nativeGyroMouse(float dx, float dy) {
    if (!mouse) {
        mouse = (SDL_Mouse *) SDL_GetMouseVoid();
        if (mouse) {
            window = mouse->focus;
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
    ev.type = SDL_EVENT_MOUSE_MOTION;
    ev.motion.windowID = windowId;
    ev.motion.which = 0;
    ev.motion.state = 0;
    ev.motion.x = 0;
    ev.motion.y = 0;
    ev.motion.xrel = dx;
    ev.motion.yrel = dy;
    SDL_PushEvent(&ev);

    mouse->x_accu += dx;
    mouse->y_accu += dy;
    mouse->x += dx;
    mouse->y += dy;

    int w = 0, h = 0;
    SDL_GetWindowSize(window, &w, &h);
    if (mouse->x < 0) mouse->x = 0;
    if (mouse->y < 0) mouse->y = 0;
    if (mouse->x > w) mouse->x = w;
    if (mouse->y > h) mouse->y = h;
}