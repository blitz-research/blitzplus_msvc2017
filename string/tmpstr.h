
#ifndef TMPSTR_H
#define TMPSTR_H

//simple wrapper for temp const char * strings
struct BBTMPSTR{
	BBString *str;
public:
	BBTMPSTR( const char *p ):str( new BBString(p) ){
	}
	BBTMPSTR( const BBTMPSTR &t ):str(t.str){
		str->retain();
	}
	~BBTMPSTR(){
		str->release();
	}
	BBTMPSTR& operator=(const BBTMPSTR&t){
		t.str->retain();
		str->release();
		str=t.str;
	}
	operator BBString*()const{
		return str;
	}
};

#endif