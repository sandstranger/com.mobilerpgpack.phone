//
// Vulkan frame pacing with Swappy (SwappyVk), set up with the launcher's frame pacing settings
// (SwappyJNILayer.initSwappy -> initSwappyGL). The Vulkan renderer calls these around the creation and
// destruction of its device and swapchain and instead of vkQueuePresentKHR. Each of them does nothing (and
// the boolean ones return false) when Swappy is not available: no settings from the launcher, a graphics
// debugger in the process, Swappy destroyed. One device and one swapchain at a time.
//

#pragma once

#include <stdbool.h>
#include <stdint.h>
#include <vulkan/vulkan.h>

#ifdef __cplusplus
extern "C" {
#endif

// Before vkCreateDevice: the device extensions Swappy needs (VK_GOOGLE_display_timing when the device has it).
// Writes up to maxNames extension names (static strings) to outNames and returns how many were written.
uint32_t SwappyVkController_GetDeviceExtensions(VkPhysicalDevice physicalDevice, uint32_t availableExtensionCount,
                                                const VkExtensionProperties* pAvailableExtensions,
                                                const char** outNames, uint32_t maxNames);

// After vkCreateDevice: the queue the swapchain images are presented on and its queue family.
void SwappyVkController_SetPresentQueue(VkDevice device, VkQueue queue, uint32_t queueFamilyIndex);

// After vkCreateSwapchainKHR: true when Swappy paces the presentation of this swapchain from now on
// (FIFO present mode only, Swappy paces vsync'd presentation).
bool SwappyVkController_InitSwapchain(VkPhysicalDevice physicalDevice, VkDevice device, VkSwapchainKHR swapchain,
                                      VkPresentModeKHR presentMode);

// Presents through Swappy when it paces the swapchain being presented: returns true, the result of the present
// in *pResult. False: the caller presents with vkQueuePresentKHR itself.
bool SwappyVkController_QueuePresent(VkQueue queue, const VkPresentInfoKHR* pPresentInfo, VkResult* pResult);

// Before vkDestroySwapchainKHR and vkDestroyDevice.
void SwappyVkController_DestroySwapchain(VkDevice device, VkSwapchainKHR swapchain);
void SwappyVkController_DestroyDevice(VkDevice device);

#ifdef __cplusplus
}
#endif
