//
// Created by sandstranger on 27.12.2025.
//
#include "SDL.h"

static SDL_Joystick* virtualController = nullptr;
static int virtualControllerIndex = -1;

void createVirtualController (){
    if (virtualController != nullptr){
        return;
    }

    SDL_VirtualJoystickDesc desc;
    SDL_zero(desc);

    desc.version = SDL_VIRTUAL_JOYSTICK_DESC_VERSION;
    desc.type = SDL_JOYSTICK_TYPE_GAMECONTROLLER;
    desc.name = "Xbox Series X Controller";

    desc.naxes = 6;
    desc.nbuttons = 16;
    desc.nhats = 0;

    desc.button_mask = 0x7FFF;
    desc.axis_mask = 0x3F;

    desc.vendor_id = 0x045E;
    desc.product_id = 0x0B12;

    virtualControllerIndex = SDL_JoystickAttachVirtualEx(&desc);
    virtualController = SDL_JoystickOpen(virtualControllerIndex);
}

void setVirtualAxis(int axis, float axisValue){
    if (virtualController != nullptr){
        SDL_JoystickSetVirtualAxis(virtualController, axis, (Sint16)(32767. * axisValue));
    }
}

void destroyVirtualController() {
    if (virtualController != nullptr) {
        SDL_JoystickClose(virtualController);
        if (virtualControllerIndex >= 0) {
            SDL_JoystickDetachVirtual(virtualControllerIndex);
            virtualControllerIndex = -1;
        }
        virtualController = nullptr;
    }
}