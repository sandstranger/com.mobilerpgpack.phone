//
// Created by sandstranger on 04.01.2026.
//
#include <SDL3/SDL.h>
#include "SDL3/SDL_mouse_c.h"
#include "SDL3/SDL_mouse.h"

void nativeGyroMouse(float dx, float dy){
    SDL_Window* win = SDL_GetMouseFocus();
    if (!win) {
        return;
    }

    SDL_Event ev;
    ev.type = SDL_EVENT_MOUSE_MOTION;
    ev.motion.windowID = SDL_GetWindowID(win);
    ev.motion.which = 0;
    ev.motion.state = 0;
    ev.motion.x = 0;
    ev.motion.y = 0;
    ev.motion.xrel = dx;
    ev.motion.yrel = dy;
    SDL_PushEvent(&ev);

    SDL_Mouse *mouse = (SDL_Mouse *)SDL_GetMouseVoid();
    if (!mouse) {
        return;
    }
    mouse->x_accu += dx;
    mouse->y_accu += dy;
    mouse->x += dx;
    mouse->y += dy;

    int w=0,h=0;
    if(win){
        SDL_GetWindowSize(win,&w,&h);
        if(mouse->x < 0) mouse->x = 0;
        if(mouse->y < 0) mouse->y = 0;
        if(mouse->x > w) mouse->x = w;
        if(mouse->y > h) mouse->y = h;
    }
}