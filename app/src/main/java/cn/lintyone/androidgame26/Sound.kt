package cn.lintyone.androidgame26

import android.content.Context
import org.cocos2d.nodes.CCDirector
import org.cocos2d.sound.SoundEngine

class Sound {


    companion object {

        /**
         * 缓存
         */
        fun build(context: Context) {
            SoundEngine.sharedEngine().preloadSound(context, R.raw.white)
            SoundEngine.sharedEngine().preloadSound(context, R.raw.choose)
            SoundEngine.sharedEngine().preloadSound(context, R.raw.begin)

            SoundEngine.sharedEngine().preloadEffect(context, R.raw.boom)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.chomp)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.coming)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.puff)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.shoot)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.car)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.attack)
            SoundEngine.sharedEngine().preloadEffect(context, R.raw.plant)
        }

        /**
         * 游戏开始
         */
        fun begin() {
            SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.begin, true)
        }

        /**
         * 卡片选择
         */
        fun choose() {
            SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.choose, true)
        }

        /**
         * 白天
         */
        fun white() {
            SoundEngine.sharedEngine().playSound(CCDirector.theApp, R.raw.white, true)
        }

        /**
         * 触摸
         */
        fun touch() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.puff)
        }

        /**
         * 啃植物
         */
        fun chomp() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.chomp)
        }

        /**
         * 僵尸来了
         */
        fun coming() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.coming)
        }

        /**
         * 爆炸
         */
        fun boom() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.boom)
        }

        /**
         * 子弹
         */
        fun shoot() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.shoot)
        }

        /**
         * 种植
         */
        fun plant() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.plant)
        }

        /**
         * 车开过
         */
        fun car() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.car)
        }

        /**
         * 僵尸被打到
         */
        fun attack() {
            SoundEngine.sharedEngine().playEffect(CCDirector.theApp, R.raw.attack)
        }


        fun onPause() {
            SoundEngine.sharedEngine().pauseSound()
        }

        fun onResume() {
            SoundEngine.sharedEngine().resumeSound()
        }

        fun onDestroy() {
            SoundEngine.sharedEngine().realesAllEffects()
            SoundEngine.sharedEngine().realesAllSounds()
            SoundEngine.purgeSharedEngine()
        }
    }
}