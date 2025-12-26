//
// Created by sandstranger on 27.12.2025.
//

#include <SDL3/SDL.h>

static SDL_Joystick *virtualController = nullptr;
static SDL_JoystickID virtualControllerID = -1;

void createVirtualController(void) {
    if (virtualController != nullptr) {
        return;
    }

    SDL_VirtualJoystickDesc desc;
    SDL_INIT_INTERFACE(&desc);

    desc.type = SDL_JOYSTICK_TYPE_GAMEPAD;
    desc.vendor_id = 0x045E;
    desc.product_id = 0x0B12;

    desc.naxes = 6;
    desc.nbuttons = 16;
    desc.nhats = 0;

    desc.button_mask = 0x7FFF;
    desc.axis_mask = 0x3F;
    desc.name = "Xbox Series X Controller";

    virtualControllerID = SDL_AttachVirtualJoystick(&desc);
    virtualController = SDL_OpenJoystick(virtualControllerID);
    if (virtualController == nullptr) {
        SDL_Log("SDL_OpenJoystick failed: %s", SDL_GetError());
        SDL_DetachVirtualJoystick(virtualControllerID);
        virtualControllerID = -1;
        return;
    }
}

void setVirtualAxis(int axis, float axisValue) {
    if (virtualController != nullptr) {
        SDL_SetJoystickVirtualAxis(virtualController, axis, (Sint16) (axisValue * 32767.0f));
    }
}

void destroyVirtualController(void) {
    if (virtualController != nullptr) {
        SDL_CloseJoystick(virtualController);
        virtualController = nullptr;
        SDL_DetachVirtualJoystick(virtualControllerID);
        virtualControllerID = -1;
    }
}