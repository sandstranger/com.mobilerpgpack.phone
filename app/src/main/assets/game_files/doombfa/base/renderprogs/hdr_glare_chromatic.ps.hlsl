/*
===========================================================================

Doom 3 BFG Edition GPL Source Code
Copyright (C) 1993-2012 id Software LLC, a ZeniMax Media company. 
Copyright (C) 2014-2015 Robert Beckebans

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

uniform sampler2D samp0		: register(s0);

struct PS_IN
{
	float4 position : VPOS;
	half2 texcoord0 : TEXCOORD0_centroid;
};

struct PS_OUT
{
	float4 color : COLOR;
};

float linterp( float t )
{
	return saturate( 1.0 - abs( 2.0 * t - 1.0 ) );
}

float remap( float t, float a, float b )
{
	return saturate( ( t - a ) / ( b - a ) );
}

float3 spectrumoffset( float t )
{
	float lo = step( t, 0.5 );
	float hi = 1.0 - lo;
	float w = linterp( remap( t, 1.0 / 6.0, 5.0 / 6.0 ) );
	float3 ret = float3( lo, 1.0, hi ) * float3( 1.0 - w, w, 1.0 - w );

	return ret; // pow( ret, float3( 1.0 / 2.2 ) );
}

void main( PS_IN fragment, out PS_OUT result )
{
	float2 st = fragment.texcoord0;
	
	// The mobile and desktop kernels sample their own taps below.
#if defined( TBR_MOBILE )
	// Pair adjacent Gaussian taps into one bilinear lookup. Weighted spectral
	// offsets preserve the broad chromatic profile with 9 reads instead of 17.
	const float mobileWeights[5] = float[5](
		0.13298076, 0.23227708, 0.13532693, 0.05115604, 0.01253929 );
	const float mobileOffsets[5] = float[5](
		0.0, 1.45842952, 3.40398478, 5.35180577, 7.30294060 );
	const float3 mobileSpectrum[5] = float3[](
		float3( 0.5, 0.5, 0.5 ),
		float3( 0.66247114, 0.25415705, 0.52921476 ),
		float3( 0.2, 0.24039848, 0.95960152 ),
		float3( 0.23518058, 0.75927769, 0.23518058 ),
		float3( 0.3, 0.5, 0.3 ) );

	const float scale = 13.0;
	const float weightScale = 1.9;
	float3 sumColor = float3( 0.0 );
	for( int i = 0; i < 5; ++i )
	{
		float2 offset = float2( mobileOffsets[i], 0.0 ) * rpWindowCoord.xy * scale;
		float weight = mobileWeights[i] * weightScale;
		float4 positiveColor = tex2D( samp0, st + offset );
		sumColor += positiveColor.rgb * ( mobileSpectrum[i] * weight );
		if( i > 0 )
		{
			float4 negativeColor = tex2D( samp0, st - offset );
			sumColor += negativeColor.rgb * ( mobileSpectrum[i] * weight );
		}
	}
#else
	const float gaussFact[9] = float[9](0.13298076, 0.12579441, 0.10648267, 0.08065691, 0.05467002, 0.03315905, 0.01799699, 0.00874063, 0.00379866);
	const float3 chromaticOffsets[9] = float3[](
		float3(0.5, 0.5, 0.5),
		float3(0.8, 0.3, 0.3),
		float3(0.5, 0.2, 0.8),
		float3(0.2, 0.2, 1.0),
		float3(0.2, 0.3, 0.9),
		float3(0.2, 0.9, 0.2),
		float3(0.3, 0.5, 0.3),
		float3(0.3, 0.5, 0.3),
		float3(0.3, 0.5, 0.3) );
	const int samples = 9;
	const float scale = 13.0;
	const float weightScale = 1.9;
	float3 sumColor = float3( 0.0 );
	for( int i = 0; i < samples; ++i )
	{
		float3 so = chromaticOffsets[i];
		float4 color = tex2D( samp0, st + float2( float( i ), 0.0 ) * rpWindowCoord.xy * scale );
		sumColor += color.rgb * ( so * gaussFact[i] * weightScale );
	}
	for( int i = 1; i < samples; ++i )
	{
		float3 so = chromaticOffsets[i];
		float4 color = tex2D( samp0, st + float2( -float( i ), 0.0 ) * rpWindowCoord.xy * scale );
		sumColor += color.rgb * ( so * gaussFact[i] * weightScale );
	}
#endif
	result.color = float4( sumColor, 1.0 );
	//result.color = float4( sumColor / float(samples), 1.0 );
	//result.color = float4( sumColor / sumSpectrum, 1.0 );
}
