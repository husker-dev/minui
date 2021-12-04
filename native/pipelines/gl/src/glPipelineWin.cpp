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

jlong nCreateWindow(jlong shareWith) {
    WNDCLASS wc;
    wc.style = CS_HREDRAW | CS_VREDRAW | CS_OWNDC;
    wc.lpfnWndProc = (WNDPROC)WndProc;
    wc.cbClsExtra = 0;
    wc.cbWndExtra = 0;
    wc.hInstance = GetModuleHandle(NULL);
    wc.hIcon = LoadIcon(NULL, IDI_WINLOGO);
    wc.hCursor = LoadCursor(NULL, IDC_ARROW);
    wc.hbrBackground = NULL;
    wc.lpszMenuName = NULL;
    wc.lpszClassName = L"minui_gl";
    RegisterClass(&wc);

    // Create The Window
    HWND hWnd = CreateWindowEx(
        WS_EX_APPWINDOW | WS_EX_WINDOWEDGE,
        L"minui_gl",
        L"",
        WS_OVERLAPPEDWINDOW |                   // Defined Window Style
        WS_CLIPSIBLINGS |                       // Required Window Style
        WS_CLIPCHILDREN,                        // Required Window Style
        0, 0,
        100, 100,
        NULL,                                   // No Parent Window
        NULL,                                   // No Menu
        GetModuleHandle(NULL),                  // Instance
        NULL);

    static PIXELFORMATDESCRIPTOR pfd = {
        sizeof(PIXELFORMATDESCRIPTOR),              // Size Of This Pixel Format Descriptor
        1,                                          // Version Number
        PFD_DRAW_TO_WINDOW |                        // Format Must Support Window
        PFD_SUPPORT_OPENGL |                        // Format Must Support OpenGL
        PFD_DOUBLEBUFFER,                           // Must Support Double Buffering
        PFD_TYPE_RGBA,                              // Request An RGBA Format
        16,                                         // Select Our Color Depth
        0, 0, 0, 0, 0, 0,                           // Color Bits Ignored
        1,                                          // No Alpha Buffer
        0,                                          // Shift Bit Ignored
        0,                                          // No Accumulation Buffer
        0, 0, 0, 0,                                 // Accumulation Bits Ignored
        16,                                         // 16Bit Z-Buffer (Depth Buffer)  
        0,                                          // No Stencil Buffer
        0,                                          // No Auxiliary Buffer
        PFD_MAIN_PLANE,                             // Main Drawing Layer
        0,                                          // Reserved
        0, 0, 0                                     // Layer Masks Ignored
    };

    HDC hDC = GetDC(hWnd);
    SetPixelFormat(hDC, ChoosePixelFormat(hDC, &pfd), &pfd);

    HGLRC hRC = wglCreateContext(hDC);
    hglrc[hWnd] = hRC;
    dc[hWnd] = hDC;

    if (shareWith)
        wglShareLists(hglrc[(HWND)shareWith], hRC);

    return (jlong)hWnd;
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

    PFNWGLSWAPINTERVALEXTPROC wglSwapIntervalEXT = (PFNWGLSWAPINTERVALEXTPROC)wglGetProcAddress("wglSwapIntervalEXT");
    wglSwapIntervalEXT(value);

    wglMakeCurrent(last_HDC, last_HGLRC);
}

jboolean nGetVsync(jlong handle) {
    auto last_HDC = wglGetCurrentDC();
    auto last_HGLRC = wglGetCurrentContext();
    nMakeCurrent(handle);

    PFNWGLGETSWAPINTERVALEXTPROC wglGetSwapIntervalEXT = (PFNWGLGETSWAPINTERVALEXTPROC)wglGetProcAddress("wglGetSwapIntervalEXT");
    jboolean result = wglGetSwapIntervalEXT();

    wglMakeCurrent(last_HDC, last_HGLRC);
    return result;
}
