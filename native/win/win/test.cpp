#include <windows.h>

#include <winrt/Windows.Data.Xml.Dom.h>
#include <winrt/Windows.UI.Notifications.h>
#include <iostream>
#include "DesktopNotificationManagerCompat.h"

using namespace winrt;
using namespace Windows::Data::Xml::Dom;
using namespace Windows::UI::Notifications;

int main(){
    std::cout << "Init..." << std::endl;
    DesktopNotificationManagerCompat::Register(L"Husker.Test", L"Display name", L"C:\\Users\\redfa\\Desktop\\a.png");
    std::cout << "Inited" << std::endl;
}