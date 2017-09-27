
#ifndef COLOR_H
#define COLOR_H

enum{
	BB_RGB565=	1,
	BB_XRGB1555=2,
	BB_RGB888=	3,
	BB_XRGB8888=4,
	BB_ARGB4444=5,
	BB_ARGB1555=6,
	BB_ARGB8888=7
};

int bbBytesPerPixel( int fmt );

struct BBColor{
	union{
		unsigned argb;
		struct{
			unsigned char b,g,r,a;
		};
	};
	BBColor();
	BBColor( unsigned argb );
	BBColor( unsigned char r,unsigned char g,unsigned char b,unsigned char a=1 );

	BBColor( int fmt,void *pixel );
	BBColor( int fmt,unsigned pixel );

	unsigned	toPixel( int fmt )const;
	void		toPixel( int fmt,void *pix )const;

	void		fromPixel( int fmt,void *pix );
	void		fromPixel( int fmt,unsigned pix );

	BBColor		clamp( int fmt )const;

	static BBColor black();
	static BBColor white();
};

/***************************** Inlined for speed ********************************/

inline BBColor::BBColor(){
}
inline BBColor::BBColor( unsigned argb ):argb(argb){
}
inline BBColor::BBColor( unsigned char r,unsigned char g,unsigned char b,unsigned char a ):argb( (a<<24)|(r<<16)|(g<<8)|b ){
}
inline BBColor::BBColor( int fmt,void *pix ){
	unsigned val;
	switch(fmt){
	case BB_RGB565:
		val=*(unsigned short*)pix;
		argb=0xff000000 | ((val&0xf800)<<8) | ((val&0x7e0)<<5) | ((val&0x1f)<<3);
		return;
	case BB_XRGB1555:
		val=*(unsigned short*)pix;
		argb=0xff000000 | ((val&0x7c00)<<9) | ((val&0x3e0)<<6) | ((val&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000 | *(unsigned short*)pix | (*((unsigned char*)pix+2)<<16);
		return;
	case BB_XRGB8888:
		argb=0xff000000 | *(unsigned*)pix;
		return;
	case BB_ARGB8888:
		argb=*(unsigned*)pix;
		return;
	}
	argb=0xffff00ff;
}
inline BBColor::BBColor( int fmt,unsigned pix ){
	switch( fmt ){
	case BB_RGB565:
		argb=0xff000000 | ((pix&0xf800)<<8) | ((pix&0x7e0)<<5) | ((pix&0x1f)<<3);
		return;
	case BB_XRGB1555:
		argb=0xff000000 | ((pix&0x7c00)<<9) | ((pix&0x3e0)<<6) | ((pix&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000|pix;
		return;
	case BB_XRGB8888:
		argb=0xff000000|pix;
		return;
	case BB_ARGB8888:
		argb=pix;
		return;
	}
	argb=0xffff00ff;
}
inline unsigned BBColor::toPixel( int fmt )const{
	switch(fmt){
	case BB_RGB565:
		return ((argb>>8)&0xf800) | ((argb>>5)&0x7e0) | ((argb>>3)&0x1f);
	case BB_XRGB1555:
		return ((argb>>9)&0x7c00) | ((argb>>6)&0x3e0) | ((argb>>3)&0x1f);
	case BB_RGB888:
		return argb;
	case BB_XRGB8888:
		return argb;
	case BB_ARGB8888:
		return argb;
	}
	return 0xffff00ff;
}
inline void BBColor::toPixel( int fmt,void *pix )const{
	switch(fmt){
	case BB_RGB565:
		*(unsigned short*)pix=
		((argb>>8)&0xf800) | ((argb>>5)&0x7e0) | ((argb>>3)&0x1f);
		return;
	case BB_XRGB1555:
		*(unsigned short*)pix=
		((argb>>9)&0x7c00) | ((argb>>6)&0x3e0) | ((argb>>3)&0x1f);
		return;
	case BB_RGB888:
		*(unsigned short*)pix=argb;
		*((unsigned char *)pix+2)=argb>>16;
		return;
	case BB_XRGB8888:
		*(unsigned*)pix=argb;
		return;
	case BB_ARGB8888:
		*(unsigned*)pix=argb;
		return;
	}
}
inline void BBColor::fromPixel( int fmt,void *pix ){
	unsigned val;
	switch(fmt){
	case BB_RGB565:
		val=*(unsigned short*)pix;
		argb=0xff000000 | ((val&0xf800)<<8) | ((val&0x7e0)<<5) | ((val&0x1f)<<3);
		return;
	case BB_XRGB1555:
		val=*(unsigned short*)pix;
		argb=0xff000000 | ((val&0x7c00)<<9) | ((val&0x3e0)<<6) | ((val&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000 | *(unsigned short*)pix | (*((unsigned char*)pix+2)<<16);
		return;
	case BB_XRGB8888:
		argb=0xff000000 | *(unsigned*)pix;
		return;
	case BB_ARGB8888:
		argb=*(unsigned*)pix;
		return;
	}
	argb=0xffff00ff;
}
inline void BBColor::fromPixel( int fmt,unsigned pix ){
	switch( fmt ){
	case BB_RGB565:
		argb=0xff000000 | ((pix&0xf800)<<8) | ((pix&0x7e0)<<5) | ((pix&0x1f)<<3);
		return;
	case BB_XRGB1555:
		argb=0xff000000 | ((pix&0x7c00)<<9) | ((pix&0x3e0)<<6) | ((pix&0x1f)<<3);
		return;
	case BB_RGB888:
		argb=0xff000000 | pix;
		return;
	case BB_XRGB8888:
		argb=0xff000000 | pix;
		return;
	case BB_ARGB8888:
		argb=pix;
		return;
	}
	argb=0xffff00ff;
}
inline BBColor BBColor::clamp( int fmt )const{
	return BBColor( fmt,toPixel(fmt) );
}
inline BBColor BBColor::black(){
	return BBColor( 0xff000000 ); 
}
inline BBColor BBColor::white(){
	return BBColor( 0xffffffff ); 
}
inline int bbBytesPerPixel( int fmt ){
	switch( fmt ){
	case BB_RGB565:
	case BB_XRGB1555:
	case BB_ARGB4444:
	case BB_ARGB1555:
		return 2;
	case BB_RGB888:
		return 3;
	case BB_XRGB8888:
	case BB_ARGB8888:
		return 4;
	}
	return 0;
}

#endif
