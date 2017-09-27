
#ifndef COLOR_I_H
#define COLOR_I_H

//#include "graphics.h"

inline BBColor::BBColor( unsigned argb ):argb(argb){
}

inline BBColor::BBColor( unsigned char r,unsigned char g,unsigned char b,unsigned char a ):argb( (a<<24)|(r<<16)|(g<<8)|b ){
}

inline BBColor::BBColor( int fmt,void *t_val ){
	unsigned val;
	switch(fmt){
	case BB_RGB565:
		val=*(unsigned short*)t_val;
		argb=0xff000000|((val&0xf800)<<8)|((val&0x7e0)<<5)|((val&0x1f)<<3);
		return;
	case BB_XRGB1555:
		val=*(unsigned short*)t_val;
		argb=0xff000000|((val&0x7c00)<<9)|((val&0x3e0)<<6)|((val&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000 | *(unsigned short*)t_val |	(*((unsigned char*)t_val+2)<<16);
		return;
	case BB_XRGB8888:
		argb=0xff000000 | *(unsigned short*)t_val;
		return;
	case BB_ARGB8888:
		argb=*(unsigned*)t_val;
		return;
	}
	argb=0xffff00ff;
}

inline BBColor::BBColor( int fmt,unsigned val ){
	switch( fmt ){
	case BB_RGB565:
		argb=0xff000000|((val&0xf800)<<8)|((val&0x7e0)<<5)|((val&0x1f)<<3);
		return;
	case BB_XRGB1555:
		argb=0xff000000|((val&0x7c00)<<9)|((val&0x3e0)<<6)|((val&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000|val;
		return;
	case BB_XRGB8888:
		argb=0xff000000|val;
		return;
	case BB_ARGB8888:
		argb=val;
		return;
	}
	argb=0xffff00ff;
}

inline unsigned BBColor::toPixel( int fmt )const{
	switch(fmt){
	case BB_RGB565:
		return ((argb&0xf80000)>>8)|((argb&0xfc00)>>5)|((argb&0xf8)>>3);
	case BB_XRGB1555:
		return ((argb&0xf80000)>>9)|((argb&0xf800)>>6)|((argb&0xf8)>>3);
	case BB_RGB888:
		return argb;
	case BB_XRGB8888:
		return argb;
	case BB_ARGB8888:
		return argb;
	}
	return 0xffff00ff;
}

inline void BBColor::toPixel( int fmt,void *val )const{
	switch(fmt){
	case BB_RGB565:
		*(unsigned short*)val=
		((argb&0xf80000)>>8)|((argb&0xfc00)>>5)|((argb&0xf8)>>3);
		return;
	case BB_XRGB1555:
		*(unsigned short*)val=
		((argb&0xf80000)>>9)|((argb&0xf800)>>6)|((argb&0xf8)>>3);
		return;
	case BB_RGB888:
		*(unsigned short*)val=argb;
		*((unsigned char *)val+2)=argb>>16;
		return;
	case BB_XRGB8888:
		*(unsigned*)val=argb;
		return;
	case BB_ARGB8888:
		*(unsigned*)val=argb;
		return;
	}
}

#endif
