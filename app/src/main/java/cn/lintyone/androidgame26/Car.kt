package cn.lintyone.androidgame26

import org.cocos2d.actions.CCScheduler
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCMoveTo
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCSprite
import org.cocos2d.types.util.CGPointUtil

class Car : CCSprite("car.png") {

    var speed = 1000
    lateinit var callback: Callback
    var isGO = false

    fun go() {
        if (!isGO) {
            isGO = true
            val end = ccp(1500f, position.y)
            val t = CGPointUtil.distance(position, end) / speed
            val callFunc = CCCallFunc.action(this, "ya")
            val moveTo = CCMoveTo.action(t, end)
            val callFunc1 = CCCallFunc.action(this, "over")
            val sequence = CCSequence.actions(callFunc, moveTo, callFunc1)
            runAction(sequence)
        }
    }

    fun ya() {
        CCScheduler.sharedScheduler().schedule("attack", this, 0.05f, false)
    }

    fun attack(t: Float) {
        callback.attack()
    }

    fun over() {
        CCScheduler.sharedScheduler().unschedule("attack", this)
        stopAllActions()
        removeSelf()
        callback.hide()
    }

    interface Callback {
        fun attack()

        fun hide()
    }
}