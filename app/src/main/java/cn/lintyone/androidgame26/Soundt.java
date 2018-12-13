package cn.lintyone.androidgame26;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.sound.SoundEngine;

/**
 * @author linti
 */
public class Soundt {

    public static void build() {
        SoundEngine.sharedEngine().preloadSound(CCDirector.theApp, R.raw.white);
        SoundEngine.sharedEngine().preloadSound(CCDirector.theApp, R.raw.choose);
        SoundEngine.sharedEngine().preloadSound(CCDirector.theApp, R.raw.begin);

        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.boom);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.chomp);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.coming);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.puff);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.shoot);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.car);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.attack);
        SoundEngine.sharedEngine().preloadEffect(CCDirector.theApp, R.raw.plant);
    }

    private static void begin() {
        SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.begin, true);
    }
}
