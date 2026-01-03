//
// Created by sandstranger on 04.01.2026.
//
#include "SDL.h"
#include "SDL_mouse_c.h"
#include "SDL_vulkan.h"

void nativeGyroMouse(int dx, int dy){
    SDL_Window* win = SDL_GetMouseFocus();
    if (!win) {
        return;
    }

    SDL_Event ev;
    ev.type = SDL_MOUSEMOTION;
    ev.motion.windowID = SDL_GetWindowID(win);
    ev.motion.which = 0;
    ev.motion.state = 0;
    ev.motion.x = 0;
    ev.motion.y = 0;
    ev.motion.xrel = dx;
    ev.motion.yrel = dy;
    SDL_PushEvent(&ev);

    SDL_Mouse *mouse = SDL_GetMouse();
    if (!mouse) {
        return;
    }
    mouse->xdelta += dx;
    mouse->ydelta += dy;

    mouse->x += dx;
    mouse->y += dy;

    int w=0,h=0;
    if(win){
        Uint32 flags = SDL_GetWindowFlags(win);
        if(flags & SDL_WINDOW_VULKAN) {
            SDL_Vulkan_GetDrawableSize(win,&w,&h);
        }
        else{
            SDL_GL_GetDrawableSize(win,&w,&h);
        }
        if(mouse->x < 0) mouse->x = 0;
        if(mouse->y < 0) mouse->y = 0;
        if(mouse->x > w) mouse->x = w;
        if(mouse->y > h) mouse->y = h;
    }
}