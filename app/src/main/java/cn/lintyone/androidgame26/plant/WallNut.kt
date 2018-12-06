package cn.lintyone.androidgame26.plant

import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import org.cocos2d.nodes.CCSpriteFrame
import java.util.*

class WallNut : Plant("plant/WallNut/high/Frame%02d.png", 16) {
    init {
        price = 50
        hp = 600
    }

    override fun hurtCompute(hurt: Int) {
        super.hurtCompute(hurt)
        when (this.hp) {
            in 200..400 -> {
                stopAllActions()
                val frames = ArrayList<CCSpriteFrame>()
                for (i in 0 until 11) {
                    val frame = CCSprite.sprite(String.format(Locale.CHINA,
                            "plant/WallNut/middle/Frame%02d.png", i)).displayedFrame()
                    frames.add(frame)
                }
                val animation = CCAnimation.animationWithFrames(frames, 0.2f)
                val animate = CCAnimate.action(animation, true)
                val repeatForever = CCRepeatForever.action(animate)
                runAction(repeatForever)
            }
            in 1..199 -> {
                stopAllActions()
                val frames = ArrayList<CCSpriteFrame>()
                for (i in 0 until 15) {
                    val frame = CCSprite.sprite(String.format(Locale.CHINA,
                            "plant/WallNut/low/Frame%02d.png", i)).displayedFrame()
                    frames.add(frame)
                }
                val animation = CCAnimation.animationWithFrames(frames, 0.2f)
                val animate = CCAnimate.action(animation, true)
                val repeatForever = CCRepeatForever.action(animate)
                runAction(repeatForever)
            }
        }

    }
}