
#ifndef MY_Font_HPP
#define MY_Font_HPP

#include <FTGL/ftgles.h>

class MyFont
{
public:
	MyFont();
	~MyFont();
	bool Open(const char filepath[]);
	FTFont* GetFont() { return mFont; };
private:
	FTFont* mFont;
};

#endif