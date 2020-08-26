package com.kundanapp.Safe_Around;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.paolorotolo.appintro.AppIntro;

public class Intro extends AppIntro {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_intro);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        editor.putBoolean("newuser", false);
        editor.commit();
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_intro));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.screentwo));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.screen3));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.screen4));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.screen5));
    }

    @Override
    public void onDonePressed(Fragment currentFragment)
    {
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment)
    {
        finish();
    }
}
