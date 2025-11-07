//
// Created by sandstranger on 08.11.2025.
//
#include "SDL3/SDL_mouse.h"

__attribute__((used))
float getMouseX() {
    float ret = 0;
    SDL_GetMouseState(&ret, NULL);
    return ret;
}

__attribute__((used))
float getMouseY() {
    float ret = 0;
    SDL_GetMouseState(NULL, &ret);
    return ret;
}

__attribute__((used))
bool isMouseShown() {return SDL_CursorVisible();}