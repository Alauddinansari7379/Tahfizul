package com.amtech.tahfizulquranonline.keyboard;


import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.amtech.tahfizulquranonline.R;

public class MyKeyboard extends InputMethodService
            implements KeyboardView.OnKeyboardActionListener{

    StringBuilder stringBuilder = new StringBuilder();
    DatabaseHelper db;
    private KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard numbers;
    private boolean caps = false;


    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        db.getInstance(getApplicationContext());
        numbers = new Keyboard(this, R.xml.numbers);
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(false);


        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/NotoNastaliqUrdu-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/NotoNastaliqUrdu-Regular.ttf");
        FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "fonts/NotoNastaliqUrdu-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/NotoNastaliqUrdu-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/NotoNastaliqUrdu-Regular.ttf");

        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    public void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {
        if(primaryCode == -101)
        {
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
        }

        if (primaryCode==500 || primaryCode==-101 || primaryCode==32 || primaryCode==-5 || primaryCode==-4 || primaryCode==505 || primaryCode==502){

        } else {
            kv.setPreviewEnabled(false);
        }
    }

    @Override
    public void onRelease(int primaryCode) {
        kv.setPreviewEnabled(false);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        InputConnection ic = getCurrentInputConnection();

        playClick(primaryCode);
        switch (primaryCode){
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
                if(Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }

                if(primaryCode==500 || primaryCode==-101 ||  primaryCode==502 || primaryCode==505)
                {
                        if(primaryCode == 502)
                        {
                            kv.setKeyboard(numbers);
                            kv.setPreviewEnabled(false);
                            kv.setOnKeyboardActionListener(this);
                        }
                        if(primaryCode == 505)
                        {
                            kv.setKeyboard(keyboard);
                            kv.setPreviewEnabled(false);
                            kv.setOnKeyboardActionListener(this);
                        }
                }
                else {
                    db.getInstance(getApplicationContext()).insertData_words(String.valueOf(code));
                    ic.commitText(String.valueOf(code), 1);

                }
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

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }
}
