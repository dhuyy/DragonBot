package com.dhuy.dragonbot.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.win32.StdCallLibrary;

public class ApplicationWindow {
  public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    int GetWindowTextW(WinDef.HWND hWnd, char[] lpString, int nMaxCount);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer data);

    boolean ShowWindow(WinDef.HWND hWnd, int nCmdShow);

    boolean GetWindowInfo(WinDef.HWND hWnd, WINDOWINFO lpwndpl);

    boolean SetForegroundWindow(WinDef.HWND hWnd);
  }

  public void restore(String windowTitle) {
    User32 user32 = User32.INSTANCE;

    user32.EnumWindows(new WinUser.WNDENUMPROC() {
      @Override
      public boolean callback(WinDef.HWND hwnd, Pointer pointer) {
        WINDOWINFO info = new WINDOWINFO();
        char[] windowTitleCharacters = new char[512];

        user32.GetWindowInfo(hwnd, info);
        user32.GetWindowTextW(hwnd, windowTitleCharacters, 512);

        String currentWindowTitle = Native.toString(windowTitleCharacters);
        if (currentWindowTitle.equals(windowTitle)) {
          if ((info.dwStyle & WinUser.WS_ICONIC) == WinUser.WS_ICONIC) {
            user32.ShowWindow(hwnd, WinUser.SW_RESTORE);
            System.out.println("tava minimized");
          } else {
            user32.SetForegroundWindow(hwnd);
            System.out.println("nao tava minimized");
          }
        }

        return true;
      }
    }, null);
  }
}
