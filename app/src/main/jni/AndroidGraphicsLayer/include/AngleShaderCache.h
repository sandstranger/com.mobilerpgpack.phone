#pragma once
#ifndef __cplusplus
#include <stdbool.h>
#endif

#ifdef __cplusplus
extern "C" {
#endif

bool angle_blobcache_install(const char* namespace_name);
void angle_blobcache_shutdown(void);

#ifdef __cplusplus
}
#endif