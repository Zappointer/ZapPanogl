



#include <GLES/gl.h>

#include "MyTextObj.hpp"
//#include "logger.h"
#include <math.h>


#define LOG_TAG "jni_test"

const float DIST_MIN = -2.f;
const float DIST_MAX = -90.f;
const float DIST_DELTA = 0.2f;



MyTextObj::MyTextObj():mPosX(0.f),mPosY(0.f),mPosZ(-6.f),
		mColorR(0.f),mColorG(0.5f),mColorB(1.f),mType(1),mMyFont()
{
	char text[] = {"Invisibi"};
	if (text != NULL)
	{
		int size = strlen(text);
		mText = new char[size+1];
		strcpy( mText, text);
	}
	else mText = NULL;

	mDistCur = DIST_MAX;
	mPause = false;
}

MyTextObj::MyTextObj(const char* text):mPosX(0.f),mPosY(0.f),mPosZ(-6.f),
		mColorR(0.5f),mColorG(0.5f),mColorB(0.f),mType(1),mMyFont()
{
	if (text != NULL)
	{
		int size = strlen(text);
		mText = new char[size+1];
		strcpy( mText, text);
	}
	else mText = NULL;

	mDistCur = DIST_MAX;
	mPause = false;
}

bool MyTextObj::OpenFont(const char* filepath)
{
	return mMyFont.Open(filepath);
}

MyTextObj::~MyTextObj()
{
	if (mText != NULL)	delete mText;
}

void MyTextObj::SetPos(float x, float y, float z)
{
	mPosX = x;
	mPosY = y;
	mPosZ = z;
}

void MyTextObj::SetColor(float r, float g, float b)
{
	mColorR = r;
	mColorG = g;
	mColorB = b;
}

int MyTextObj::Draw(FTFont* font)
{
	static float _angle = 0;
	const float scale = 1.f;
	if(font == NULL || font->Error())
	{
		if (mMyFont.GetFont() == NULL)
		{
			LOG_ERROR("Test() font error");
			return -1;
		}
		font = mMyFont.GetFont();
	}
	glMatrixMode(GL_MODELVIEW);
	glPushMatrix();
	//glDisable(GL_CULL_FACE);
	glFrontFace(GL_CCW);
	//glLoadIdentity();

	if (mType == 1)
	{
		glTranslatef(mPosX, mPosY, mPosZ + mDistCur);
		glRotatef(60, -1, 0, 0);
	}
	else if (mType == 2)
	{
		glTranslatef(mPosX, mPosY, mPosZ);
		float degree = 30* sin(_angle);
		float degree2 = 10* _angle;
		glRotatef(degree, 1, 0, 0);
		glRotatef(degree2, 0, 1, 0);
		glScalef( scale, scale, scale);
	}
	else
	{
		glTranslatef(mPosX, mPosY, mPosZ);
		float degree = 10* sin(_angle);
		float degree2 = 30* cos(_angle);
		glRotatef(degree, 1, 0, 0);
		glRotatef(degree2, 0, 1, 0);
		glScalef( scale, scale, scale);
	}
	glColor4f(mColorR, mColorG, mColorB, 1.f);
	FTBBox ftbBox = font->BBox(mText);
	FTPoint upper = ftbBox.Upper();
	FTPoint lower = ftbBox.Lower();
	float moveX = 0 - (upper.Xf() - lower.Xf()) / 2;
	float moveY = 0 - (upper.Yf() - lower.Yf()) / 2;
	float moveZ = 0 - (upper.Zf() - lower.Zf()) / 2 ;
	//LOG_INFO("move %.1f, %.1f, %.1f", moveX, moveY, moveZ);
	FTPoint p(moveX, moveY, moveZ );
	font->Render(mText, -1, p);
	glPopMatrix();
	if (mPause == false)
	{
		if ( mDistCur < DIST_MIN )
		{
			mDistCur += DIST_DELTA;
		}
		else
		{
			mDistCur = DIST_MAX;
		}
		_angle += 0.02f;
	}

	glEnable(GL_CULL_FACE);
	return 0;
}
