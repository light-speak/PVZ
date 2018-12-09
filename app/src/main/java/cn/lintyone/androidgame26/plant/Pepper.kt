package cn.lintyone.androidgame26.plant

import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import java.util.*

class Pepper : Plant("plant/pepper/Frame%02d.png", 8) {

    lateinit var callback: Callback

    companion object {
        const val price = 125
    }

    override fun ready() {
        setAnchorPoint(0.5f, 0f)
        for (i in 0 until 8) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/pepper/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)
        val boom = CCCallFunc.action(this, "boom")
        runAction(CCSequence.actions(animate, boom))
    }


    fun boom() {
        stopAllActions()
        frames.clear()
        for (i in 0 until 8) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/pepper/fire/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        scale = 2.5f
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)
        val over = CCCallFunc.action(this, "over")
        runAction(CCSequence.actions(animate, over))
        callback.boom()
    }

    fun over() {
        removeSelf()
    }

    interface Callback {
        fun boom()
    }
}