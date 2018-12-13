package cn.lintyone.androidgame26.plant

import cn.lintyone.androidgame26.zombie.ZombieNormal
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import java.util.*

class Chomper : Plant("plant/Chomper/normal/Frame%02d.png", 13) {

    var canEat = false
    lateinit var zombie: ZombieNormal

    companion object {
        const val price = 150
    }

    override fun ready() {
        normal()
        setAnchorPoint(0.5f, 0f)
    }

    fun normal() {
        canEat = true
        stopAllActions()
        frames.clear()
        for (i in 0 until 13) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/Chomper/normal/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        runAction(repeatForever)
    }

    fun eat(zombie : ZombieNormal) {
        canEat = false
        this.zombie = zombie
        stopAllActions()
        frames.clear()
        for (i in 0 until 9) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/Chomper/eat/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)
        val ate = CCCallFunc.action(this, "ate")
        runAction(CCSequence.actions(animate,ate))
    }

    fun ate() {
        stopAllActions()
        zombie.removeSelf()
        frames.clear()
        for (i in 0 until 6) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, "plant/Chomper/ate/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.3f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        runAction(repeatForever)

        val delay = CCDelayTime.action(10f)
        val normal = CCCallFunc.action(this, "normal")
        runAction(CCSequence.actions(delay, normal))
    }
}
