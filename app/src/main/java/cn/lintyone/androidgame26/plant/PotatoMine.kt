package cn.lintyone.androidgame26.plant

import cn.lintyone.androidgame26.Sound
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import java.util.*

class PotatoMine : Plant("plant/PotatoMine/noReady.gif", 0) {

    private lateinit var boom: CCSprite
    var canBoom = false



    companion object {
        const val price = 25
    }

    override fun ready() {
        this.setAnchorPoint(0.5f, 0f)
        val delay = CCDelayTime.action(10f)
        val callFunc = CCCallFunc.action(this, "up")
        runAction(CCSequence.actions(delay, callFunc))
    }

    fun up() {
        canBoom = true
        for (i in 0 until 8) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/PotatoMine/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        this.runAction(repeatForever)
    }

    fun boom() {
        canBoom = false
        stopAllActions()
        boom = CCSprite("plant/PotatoMine/boom.png")
        boom.position = position
        parent.addChild(boom)
        val delay = CCDelayTime.action(0.4f)
        val callFunc = CCCallFunc.action(this, "remove")
        runAction(CCSequence.actions(delay, callFunc))
        Sound.boom()
    }

    fun remove() {
        boom.removeSelf()
        removeSelf()
    }


}
