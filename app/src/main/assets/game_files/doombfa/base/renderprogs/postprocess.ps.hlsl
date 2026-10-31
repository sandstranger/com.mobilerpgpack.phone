/*
===========================================================================

Doom 3 BFG Edition GPL Source Code
Copyright (C) 1993-2012 id Software LLC, a ZeniMax Media company.
Copyright (C) 2015 Robert Beckebans

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

// *INDENT-OFF*
uniform sampler2D samp0		: register(s0);
uniform sampler2D samp1		: register(s1);
 
struct PS_IN
{
	float4 position : VPOS;
	half2 texcoord0 : TEXCOORD0_centroid;
};
 
struct PS_OUT
{
	float4 color : COLOR;
};
// *INDENT-ON*

#define USE_TECHNICOLOR						1		// [0 or 1]

#define Technicolor_Amount					0.5		// [0.00 to 1.00]
#define Technicolor_Power					4.0		// [0.00 to 8.00]
#define Technicolor_RedNegativeAmount		0.88	// [0.00 to 1.00]
#define Technicolor_GreenNegativeAmount		0.88	// [0.00 to 1.00]
#define Technicolor_BlueNegativeAmount		0.88	// [0.00 to 1.00]

#define USE_VIBRANCE						1
#define Vibrance							0.5	// [-1.00 to 1.00]
#define	Vibrance_RGB_Balance				float3( 1.0, 1.0, 1.0 )

#define USE_FILMGRAIN 						1

half3 overlay( half3 a, half3 b )
{
	// Branchless overlay blend. step() selects the low/high branch per channel.
	half3 lowResult = 2.0 * a * b;
	half3 highResult = 1.0 - 2.0 * ( 1.0 - a ) * ( 1.0 - b );
	half3 lowMask = 1.0 - step( half3( 0.5 ), b );
	return lerp( highResult, lowResult, lowMask );
}


void TechnicolorPass( inout half4 color )
{
	const half3 cyanFilter = half3( 0.0, 1.30, 1.0 );
	const half3 magentaFilter = half3( 1.0, 0.0, 1.05 );
	const half3 yellowFilter = half3( 1.6, 1.6, 0.05 );
	const half3 redOrangeFilter = half3( 1.05, 0.62, 0.0 );
	const half3 greenFilter = half3( 0.3, 1.0, 0.0 );
	
	half2 redNegativeMul   = color.rg * ( 1.0 / ( Technicolor_RedNegativeAmount * Technicolor_Power ) );
	half2 greenNegativeMul = color.rg * ( 1.0 / ( Technicolor_GreenNegativeAmount * Technicolor_Power ) );
	half2 blueNegativeMul  = color.rb * ( 1.0 / ( Technicolor_BlueNegativeAmount * Technicolor_Power ) );
	
	half redNegative   = dot( redOrangeFilter.rg, redNegativeMul );
	half greenNegative = dot( greenFilter.rg, greenNegativeMul );
	half blueNegative  = dot( magentaFilter.rb, blueNegativeMul );
	
	half3 redOutput   = half3( redNegative ) + cyanFilter;
	half3 greenOutput = half3( greenNegative ) + magentaFilter;
	half3 blueOutput  = half3( blueNegative ) + yellowFilter;
	
	half3 technicolorResult = redOutput * greenOutput * blueOutput;
	color.rgb = lerp( color.rgb, technicolorResult, half( Technicolor_Amount ) );
}


void VibrancePass( inout half4 color )
{
	const half3 vibrance = half3( Vibrance_RGB_Balance * Vibrance );
	
	half Y = dot( LUMINANCE_SRGB, color );
	
	half minColor = min( color.r, min( color.g, color.b ) );
	half maxColor = max( color.r, max( color.g, color.b ) );
	
	half colorSat = maxColor - minColor;
	
	color.rgb = lerp( half3( Y ), color.rgb, ( 1.0 + ( vibrance * ( 1.0 - ( sign( vibrance ) * colorSat ) ) ) ) );
}


void FilmgrainPass( inout half4 color )
{
	float4 jitterTC = ( fragment.position * rpScreenCorrectionFactor ) + rpJitterTexOffset;
	//float4 jitterTC = ( fragment.position * ( 1.0 / 128.0 ) ) + rpJitterTexOffset;
	//float2 jitterTC = fragment.position.xy * 2.0;
	//jitterTC.x *= rpWindowCoord.y / rpWindowCoord.x;
	
	half4 noiseColor = tex2D( samp1, fragment.position.xy + jitterTC.xy );
	//float Y = dot( LUMINANCE_VECTOR, noiseColor );
	//noiseColor.rgb = float3( Y, Y, Y );
	
	const half exposureFactor = 1.0;
	const half noiseIntensity = 1.7; //rpScreenCorrectionFactor.z;
	
	half t = lerp( 3.5 * noiseIntensity, 1.13 * noiseIntensity, exposureFactor );
	color.rgb = overlay( color.rgb, lerp( float3( 0.5 ), noiseColor.rgb, t ) );
	
	//color.rgb = noiseColor.rgb;
	//color.rgb = lerp( color.rgb, noiseColor.rgb, 0.3 );
}


void main( PS_IN fragment, out PS_OUT result )
{
	half2 tCoords = fragment.texcoord0;
	
	// base color with tone mapping and other post processing applied
	half4 color = tex2D( samp0, tCoords );
	
#if USE_TECHNICOLOR
	TechnicolorPass( color );
#endif
	
#if USE_VIBRANCE
	VibrancePass( color );
#endif
	
#if USE_FILMGRAIN
	FilmgrainPass( color );
#endif
	
	result.color = color;
}
