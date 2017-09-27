
#ifndef CONFIG_H
#define CONFIG_H

#define BASE_VER	147

#ifdef	PRO
#define	PRO_F		0x010000
#else
#define	PRO_F		0
#endif
#ifdef	BETA
#define	BETA_F		0x020000
#else
#define	BETA_F		0
#endif
#ifdef	MEMDEBUG
#define	MEMDEBUG_F	0x040000
#else
#define	MEMDEBUG_F	0
#endif
#ifdef	DEMO
#define	DEMO_F		0x080000
#else
#define	DEMO_F		0
#endif
#ifdef	PLUS
#define	PLUS_F		0x100000
#else
#define PLUS_F		0
#endif

#define VERSION		(BASE_VER|PRO_F|BETA_F|MEMDEBUG_F|DEMO_F|PLUS_F)

#endif
