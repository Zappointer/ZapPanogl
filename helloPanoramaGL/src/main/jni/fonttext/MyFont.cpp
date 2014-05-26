
#include "MyFont.hpp"

MyFont::MyFont():mFont(NULL)
{
}

MyFont::~MyFont()
{
	if ( mFont != NULL ) delete mFont;
}

bool MyFont::Open(const char fontpath[])
{
	if ( mFont != NULL ) delete mFont;
	//Example of path: "/sdcard/tesseract/Vera.ttf"
	mFont = new FTExtrudeFont(fontpath);
	FT_Error error = mFont->Error();
	if (error != 0) {
		LOG_ERROR("Failed to load font at %s", fontpath);
		return false;
	}

	LOG_INFO("Succeed to load font at %s", fontpath);
	mFont->FaceSize(3);
	mFont->Depth(1.f);

	return true;
}