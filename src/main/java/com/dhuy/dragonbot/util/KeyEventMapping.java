package com.dhuy.dragonbot.util;

import java.awt.event.KeyEvent;

public class KeyEventMapping {
  public KeyEventMapping() {}

  public int getKeyCodeFromChar(char c) {
    if (c == 'A' || c == 'a')
      return KeyEvent.VK_A;
    else if (c == 'B' || c == 'b')
      return KeyEvent.VK_B;
    else if (c == 'C' || c == 'c')
      return KeyEvent.VK_C;
    else if (c == 'D' || c == 'd')
      return KeyEvent.VK_D;
    else if (c == 'E' || c == 'e')
      return KeyEvent.VK_E;
    else if (c == 'F' || c == 'f')
      return KeyEvent.VK_F;
    else if (c == 'G' || c == 'g')
      return KeyEvent.VK_G;
    else if (c == 'H' || c == 'h')
      return KeyEvent.VK_H;
    else if (c == 'I' || c == 'i')
      return KeyEvent.VK_I;
    else if (c == 'J' || c == 'j')
      return KeyEvent.VK_J;
    else if (c == 'K' || c == 'k')
      return KeyEvent.VK_K;
    else if (c == 'L' || c == 'l')
      return KeyEvent.VK_L;
    else if (c == 'M' || c == 'm')
      return KeyEvent.VK_M;
    else if (c == 'N' || c == 'n')
      return KeyEvent.VK_N;
    else if (c == 'O' || c == 'o')
      return KeyEvent.VK_O;
    else if (c == 'P' || c == 'p')
      return KeyEvent.VK_P;
    else if (c == 'Q' || c == 'q')
      return KeyEvent.VK_Q;
    else if (c == 'R' || c == 'r')
      return KeyEvent.VK_R;
    else if (c == 'S' || c == 's')
      return KeyEvent.VK_S;
    else if (c == 'T' || c == 't')
      return KeyEvent.VK_T;
    else if (c == 'U' || c == 'u')
      return KeyEvent.VK_U;
    else if (c == 'V' || c == 'v')
      return KeyEvent.VK_V;
    else if (c == 'W' || c == 'w')
      return KeyEvent.VK_W;
    else if (c == 'Y' || c == 'y')
      return KeyEvent.VK_Y;
    else if (c == 'X' || c == 'x')
      return KeyEvent.VK_X;
    else if (c == 'Z' || c == 'z')
      return KeyEvent.VK_Z;
    else if (c == '0')
      return KeyEvent.VK_0;
    else if (c == '1')
      return KeyEvent.VK_1;
    else if (c == '2')
      return KeyEvent.VK_2;
    else if (c == '3')
      return KeyEvent.VK_3;
    else if (c == '4')
      return KeyEvent.VK_4;
    else if (c == '5')
      return KeyEvent.VK_5;
    else if (c == '6')
      return KeyEvent.VK_6;
    else if (c == '7')
      return KeyEvent.VK_7;
    else if (c == '8')
      return KeyEvent.VK_8;
    else if (c == '9')
      return KeyEvent.VK_9;
    else if (c == ' ')
      return KeyEvent.VK_SPACE;
    else if (c == '-')
      return KeyEvent.VK_MINUS;
    else if (c == 'ˇ')
      return KeyEvent.VK_DOWN;
    else if (c == '.')
      return KeyEvent.VK_PERIOD;
    else
      return 0;
  }

  public int getKeyCode(String stringKey) {
    if (stringKey.equals("F1"))
      return KeyEvent.VK_F1;
    else if (stringKey.equals("F2"))
      return KeyEvent.VK_F2;
    else if (stringKey.equals("F3"))
      return KeyEvent.VK_F3;
    else if (stringKey.equals("F4"))
      return KeyEvent.VK_F4;
    else if (stringKey.equals("F5"))
      return KeyEvent.VK_F5;
    else if (stringKey.equals("F6"))
      return KeyEvent.VK_F6;
    else if (stringKey.equals("F7"))
      return KeyEvent.VK_F7;
    else if (stringKey.equals("F8"))
      return KeyEvent.VK_F8;
    else if (stringKey.equals("F9"))
      return KeyEvent.VK_F9;
    else if (stringKey.equals("F10"))
      return KeyEvent.VK_F10;
    else if (stringKey.equals("F11"))
      return KeyEvent.VK_F11;
    else if (stringKey.equals("F12"))
      return KeyEvent.VK_F12;
    else if (stringKey.equals("ESC"))
      return KeyEvent.VK_ESCAPE;
    else if (stringKey.equals("ENTER"))
      return KeyEvent.VK_ENTER;
    else if (stringKey.equals("SPACE"))
      return KeyEvent.VK_SPACE;
    else if (stringKey.equals("UP"))
      return KeyEvent.VK_UP;
    else if (stringKey.equals("LEFT"))
      return KeyEvent.VK_LEFT;
    else if (stringKey.equals("RIGHT"))
      return KeyEvent.VK_RIGHT;
    else if (stringKey.equals("DOWN"))
      return KeyEvent.VK_DOWN;
    else
      return KeyEvent.VK_UNDEFINED;
  }
}
