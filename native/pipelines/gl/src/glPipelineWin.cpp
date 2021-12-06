#include "glPipelineWin.h"
#include <iostream>

static std::map<HWND, HGLRC> hglrc;
static std::map<HWND, HDC> dc;

LRESULT CALLBACK WndProc(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    switch (uMsg) {
    case WM_DESTROY:
    {
        wglDeleteContext(hglrc[hWnd]);
        ReleaseDC(hWnd, dc[hWnd]);
        DestroyWindow(hWnd);
    }
    }
    return DefWindowProc(hWnd, uMsg, wParam, lParam);
}

void nInit() {
    
}

HGLRC CreateHGLRC(HWND hWnd) {
    PIXELFORMATDESCRIPTOR pfd = {
      sizeof(PIXELFORMATDESCRIPTOR),
      1,                                // Version Number
      PFD_DRAW_TO_WINDOW |              // Format Must Support Window
      PFD_SUPPORT_OPENGL |              // Format Must Support OpenGL
      PFD_SUPPORT_COMPOSITION |         // Format Must Support Composition
      PFD_DOUBLEBUFFER,                 // Must Support Double Buffering
      PFD_TYPE_RGBA,                    // Request An RGBA Format
      32,                               // Select Our Color Depth
      0, 0, 0, 0, 0, 0,                 // Color Bits Ignored
      8,                                // An Alpha Buffer
      0,                                // Shift Bit Ignored
      0,                                // No Accumulation Buffer
      0, 0, 0, 0,                       // Accumulation Bits Ignored
      24,                               // 16Bit Z-Buffer (Depth Buffer)
      8,                                // Some Stencil Buffer
      0,                                // No Auxiliary Buffer
      PFD_MAIN_PLANE,                   // Main Drawing Layer
      0,                                // Reserved
      0, 0, 0                           // Layer Masks Ignored
    };

    HDC hdc = GetDC(hWnd);

    BOOL bResult = SetPixelFormat(hdc, ChoosePixelFormat(hdc, &pfd), &pfd);
    HGLRC m_hrc = wglCreateContext(hdc);

    ReleaseDC(hWnd, hdc);

    return m_hrc;
}

jlong nCreateWindow(jlong shareWith) {
    WNDCLASS wc;
    wc.style = CS_HREDRAW | CS_VREDRAW | CS_OWNDC;
    wc.lpfnWndProc = (WNDPROC)WndProc;
    wc.cbClsExtra = 0;
    wc.cbWndExtra = 0;
    wc.hInstance = GetModuleHandle(NULL);
    wc.hIcon = NULL;
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);
    wc.hbrBackground = (HBRUSH)CreateSolidBrush(0x00000000);
    wc.lpszMenuName = NULL;
    wc.lpszClassName = L"minui_gl";
    RegisterClass(&wc);

    // Create The Window
    HWND hwnd = CreateWindowEx(
        WS_EX_APPWINDOW,
        L"minui_gl",
        L"",
        WS_CLIPSIBLINGS | WS_CLIPCHILDREN | WS_SYSMENU | WS_MINIMIZEBOX | WS_CAPTION | WS_MAXIMIZEBOX | WS_THICKFRAME,
        0, 0,
        100, 100,
        NULL, NULL,
        GetModuleHandle(NULL),
        NULL);

    //HDC hDC = GetDC(hWnd);
    //SetPixelFormat(hDC, ChoosePixelFormat(hDC, &pfd), &pfd);

    HGLRC hRC = CreateHGLRC(hwnd);
    HDC hDC = GetDC(hwnd);
    hglrc[hwnd] = hRC;
    dc[hwnd] = hDC;

    
    DWM_BLURBEHIND bb = { 0 };
    HRGN hRgn = CreateRectRgn(0, 0, -1, -1);
    bb.dwFlags = DWM_BB_ENABLE | DWM_BB_BLURREGION;
    bb.hRgnBlur = hRgn;
    bb.fEnable = TRUE;
    DwmEnableBlurBehindWindow(hwnd, &bb);
    
   


    if (shareWith)
        wglShareLists(hglrc[(HWND)shareWith], hRC);

    wglMakeCurrent(hDC, hRC);
    if (!gladLoadWGL(hDC))
        std::cout << "Failed to load GLAD-WGL" << std::endl;
    

    
    

    wglMakeCurrent(nullptr, nullptr);

    return (jlong)hwnd;
}

void nSwapBuffers(jlong handle) {
    SwapBuffers(dc[(HWND)handle]);
}

void nPollEvents() {
    MSG msg;
    if (PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }
}

void nMakeCurrent(jlong handle) {
    wglMakeCurrent(dc[(HWND)handle], hglrc[(HWND)handle]);
}

void nSetVsync(jlong handle, jboolean value) {
    auto last_HDC = wglGetCurrentDC();
    auto last_HGLRC = wglGetCurrentContext();
    nMakeCurrent(handle);

    //PFNWGLSWAPINTERVALEXTPROC wglSwapIntervalEXT = (PFNWGLSWAPINTERVALEXTPROC)wglGetProcAddress("wglSwapIntervalEXT");
    wglSwapIntervalEXT(value);

    wglMakeCurrent(last_HDC, last_HGLRC);
}

jboolean nGetVsync(jlong handle) {
    auto last_HDC = wglGetCurrentDC();
    auto last_HGLRC = wglGetCurrentContext();
    nMakeCurrent(handle);

    //PFNWGLGETSWAPINTERVALEXTPROC wglGetSwapIntervalEXT = (PFNWGLGETSWAPINTERVALEXTPROC)wglGetProcAddress("wglGetSwapIntervalEXT");
    jboolean result = wglGetSwapIntervalEXT();

    wglMakeCurrent(last_HDC, last_HGLRC);
    return result;
}
