package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

public class MusicManager {
    private final Music music;
    private float curVolume;
    private final int oneQatar;
    private boolean isIncreasing;
    private final float maxVolume,minVolume; //highest and lowest volume of the music
    private final boolean isMax;

    public MusicManager(Music music, float minVolume, float maxVolume,boolean isMax)
    {
        this.isMax = isMax;
        this.music = music;
        this.isIncreasing = true;
        this.maxVolume=maxVolume;
        this.minVolume=minVolume;
        oneQatar = 10;

        if(this.isMax)curVolume = this.maxVolume;
        else curVolume = this.minVolume;
        setVolume(curVolume);

    }


    public Music getMusic() {
        return music;
    }

    public void setVolume(float volume)
    {
        if(volume <= this.maxVolume && volume >= this.minVolume) {
            this.curVolume = volume;
            this.music.setVolume(this.curVolume);
        }
    }

    public float changeBy()
    {
        int totalChange = oneQatar * Gdx.graphics.getFramesPerSecond(); //game is 60 fps
        float change = Math.abs(maxVolume-minVolume);
        return change/totalChange;
    }

    public int timePassed()
    {
        return MathUtils.round(music.getPosition());
    }

    public int Qatar() {
        return timePassed() / oneQatar;
    }

    public boolean checkIncreasing() {
        if(isMax)
            isIncreasing = (Qatar() & 1) != 0;
        else
            isIncreasing = (Qatar() & 1) == 0;

        return isIncreasing;
    }

    public void update()
    {
        if(checkIncreasing())
            setVolume(getCurVolume() + changeBy());
        else
            setVolume(getCurVolume() - changeBy());
    }


    public float getCurVolume() {
        return curVolume;
    }



    public void cleanUp()
    {
        music.dispose();
    }
}
