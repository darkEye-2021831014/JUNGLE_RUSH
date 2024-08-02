package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

public class MusicManager {
    private final Music music;
    private float curVolume;
    private final int oneQatar;
    private boolean isIncreasing;
    private int curVolumePercent;
    private final float defaultMaxVolume,defaultMinVolume;
    private float maxVolume,minVolume; //highest and lowest volume of the music
    private final boolean isMax;

    public MusicManager(Music music, float minVolume, float maxVolume,boolean isMax)
    {
        this.isMax = isMax;
        this.music = music;
        this.isIncreasing = true;
        this.defaultMaxVolume = maxVolume;
        this.defaultMinVolume = minVolume;
        this.maxVolume=this.defaultMaxVolume;
        this.minVolume=this.defaultMinVolume;
        this.curVolumePercent = 100;
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

    public void setCurVolume(float curVolume) {
        this.curVolume = curVolume;
    }

    public int getOneQatar() {
        return oneQatar;
    }

    public boolean isIncreasing() {
        return isIncreasing;
    }

    public void setIncreasing(boolean increasing) {
        isIncreasing = increasing;
    }

    public int getCurVolumePercent() {
        return curVolumePercent;
    }

    public void setCurVolumePercent(int newVolumePercent) {
        minVolume = (defaultMinVolume*newVolumePercent)/100f;
        maxVolume = (defaultMaxVolume*newVolumePercent)/100f;
        if(this.isMax)curVolume = this.maxVolume;
        else curVolume = this.minVolume;
        setVolume(curVolume);

        this.curVolumePercent = newVolumePercent;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    public float getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(float minVolume) {
        this.minVolume = minVolume;
    }

    public boolean isMax() {
        return isMax;
    }

    public void cleanUp()
    {
        music.dispose();
    }
}
