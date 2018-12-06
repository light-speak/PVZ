package cn.lintyone.androidgame26.plant.sunFlower

import cn.lintyone.androidgame26.CombatLayer
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCMoveTo
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import org.cocos2d.nodes.CCSpriteFrame
import org.cocos2d.types.util.CGPointUtil
import java.util.*

class Sun : CCSprite("sun/Frame00.png") {
    var isNowCollect = false

    private val frames = ArrayList<CCSpriteFrame>()

    init {
        for (i in 0 until 22) {
            val frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "sun/Frame%02d.png", i)).displayedFrame()
            frames.add(frame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation)
        val repeatForever = CCRepeatForever.action(animate)
        runAction(repeatForever)
    }

    fun collect() {
        isNowCollect = true
        val end = ccp(50f, 720f)
        val t = CGPointUtil.distance(position, end) / 1000
        val moveTo = CCMoveTo.action(t, end)
        val callFunc = CCCallFunc.action(this, "addSunNumber")
        val sequence = CCSequence.actions(moveTo, callFunc)
        runAction(sequence)
    }

    fun addSunNumber() {
        (parent as CombatLayer).addSunNumber(50)
        removeSelf()
    }
}