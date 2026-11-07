/*
===========================================================================

Doom 3 BFG Edition GPL Source Code
Copyright (C) 1993-2012 id Software LLC, a ZeniMax Media company. 

This file is part of the Doom 3 BFG Edition GPL Source Code ("Doom 3 BFG Edition Source Code").  

Doom 3 BFG Edition Source Code is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Doom 3 BFG Edition Source Code is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Doom 3 BFG Edition Source Code.  If not, see <http://www.gnu.org/licenses/>.

In addition, the Doom 3 BFG Edition Source Code is also subject to certain additional terms. You should have received a copy of these additional terms immediately following the terms and conditions of the GNU General Public License which accompanied the Doom 3 BFG Edition Source Code.  If not, please request a copy in writing from id Software at the address below.

If you have questions concerning this license or the applicable additional terms, you may contact in writing id Software LLC, c/o ZeniMax Media Inc., Suite 120, Rockville, Maryland 20850 USA.

===========================================================================
*/

#include "global.inc.hlsl"

struct VS_IN {
	float4 position : POSITION;
#if defined( USE_GPU_INSTANCING )
	float4 instanceMVPX : INSTANCE_MVP_X;
	float4 instanceMVPY : INSTANCE_MVP_Y;
	float4 instanceMVPZ : INSTANCE_MVP_Z;
	float4 instanceMVPW : INSTANCE_MVP_W;
	float4 instanceModelViewX : INSTANCE_MODELVIEW_X;
	float4 instanceModelViewY : INSTANCE_MODELVIEW_Y;
	float4 instanceModelViewZ : INSTANCE_MODELVIEW_Z;
	float4 instanceModelViewW : INSTANCE_MODELVIEW_W;
	float4 instanceLocalViewOrigin : INSTANCE_LOCAL_VIEW_ORIGIN;
#endif
};

struct VS_OUT {
	float4 position : POSITION;
};

void main( VS_IN vertex, out VS_OUT result ) {
#if defined( USE_GPU_INSTANCING )
	result.position.x = dot4( vertex.position, vertex.instanceMVPX );
	result.position.y = dot4( vertex.position, vertex.instanceMVPY );
	result.position.z = dot4( vertex.position, vertex.instanceMVPZ );
	result.position.w = dot4( vertex.position, vertex.instanceMVPW );
#else
	result.position.x = dot4( vertex.position, rpMVPmatrixX );
	result.position.y = dot4( vertex.position, rpMVPmatrixY );
	result.position.z = dot4( vertex.position, rpMVPmatrixZ );
	result.position.w = dot4( vertex.position, rpMVPmatrixW );
#endif
}