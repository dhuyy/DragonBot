package com.dhuy.dragonbot.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WINDOWINFO;
import com.sun.jna.win32.StdCallLibrary;

public class ApplicationWindow {
  private static final int MAX_TITLE_LENGTH = 1024;

  public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer data);

    boolean ShowWindow(HWND hWnd, int nCmdShow);

    boolean GetWindowInfo(HWND hWnd, WINDOWINFO lpwndpl);

    boolean SetForegroundWindow(HWND hWnd);

    boolean IsWindowVisible(HWND hWnd);

    HWND FindWindowA(String lpClassName, String lpWindowName);
  }

  public void restore(String characterName) {
    User32 user32 = User32.INSTANCE;

    user32.EnumWindows(new WinUser.WNDENUMPROC() {
      @Override
      public boolean callback(HWND hwnd, Pointer pointer) {
        WINDOWINFO info = new WINDOWINFO();
        char[] buffer = new char[MAX_TITLE_LENGTH];

        user32.GetWindowInfo(hwnd, info);
        user32.GetWindowTextW(hwnd, buffer, MAX_TITLE_LENGTH);

        String expectedWindowTitle = "Tibia - ".concat(characterName);
        String currentWindowTitle = Native.toString(buffer);

        if (currentWindowTitle.equals(expectedWindowTitle)) {
          if ((info.dwStyle & WinUser.WS_ICONIC) == WinUser.WS_ICONIC) {
            user32.ShowWindow(hwnd, WinUser.SW_RESTORE);
          } else {
            user32.SetForegroundWindow(hwnd);
          }
        }

        return true;
      }
    }, null);
  }
}
