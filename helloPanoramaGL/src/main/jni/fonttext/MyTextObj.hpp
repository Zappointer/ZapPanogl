
#ifndef MY_TEXT_OBJ_HPP
#define MY_TEXT_OBJ_HPP

#include "MyFont.hpp"

class MyTextObj
{
public:
	MyTextObj();
	MyTextObj(const char* Text);
	~MyTextObj();

	int Draw(FTFont* font);

	void SetPos(float x, float y, float z);
	void SetColor(float r, float g, float b);
	void SetPause(bool pause){ mPause=pause; }
	void SetType(int type){ mType = type; }

	bool OpenFont(const char filepath[]);
private:

	char* mText;

	float mPosX;
	float mPosY;
	float mPosZ;
	float mColorR;
	float mColorG;
	float mColorB;
	int mType;

	float mDistCur;
	bool mPause;

	MyFont mMyFont;
};

#endif