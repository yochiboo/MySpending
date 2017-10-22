package com.example.yochi.myspending.inputMethodService;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.example.yochi.myspending.R;

/**
   * Created by yochi on 2017/09/12.
   */
  public class NumPad extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;

    @Override
    public View onCreateInputView() {
      kv = (KeyboardView)getLayoutInflater().inflate(R.layout.num_pad, null);
      keyboard = new Keyboard(this, R.xml.num_pad);
      kv.setKeyboard(keyboard);
      kv.setOnKeyboardActionListener(this);
      //am = (AudioManager)getSystemService(AUDIO_SERVICE);
      return kv;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
      InputConnection ic = getCurrentInputConnection();

      switch(primaryCode) {
        case Keyboard.KEYCODE_DELETE:
          ic.deleteSurroundingText(1, 0);
          break;
        case Keyboard.KEYCODE_SHIFT:
          caps = !caps;
          keyboard.setShifted(caps);
          kv.invalidateAllKeys();
          break;
        case Keyboard.KEYCODE_DONE:
          ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
          break;
        default:
          char code = (char) primaryCode;
          if (caps && Character.isLetter(code)) {
            code = Character.toUpperCase(code);
          }
          ic.commitText(String.valueOf(code), 1);
          break;
      }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
  }
