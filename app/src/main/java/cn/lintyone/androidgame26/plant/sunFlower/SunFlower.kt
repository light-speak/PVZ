package cn.lintyone.androidgame26.plant.sunFlower

import cn.lintyone.androidgame26.CombatLayer
import cn.lintyone.androidgame26.plant.Plant
import org.cocos2d.actions.CCScheduler
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCJumpTo
import org.cocos2d.actions.interval.CCSequence

class SunFlower : Plant("plant/SunFlower/Frame%02d.png", 18) {
    private lateinit var sun: Sun
    var getSunInterval = 15f
    var removeSunInterval = 8f


    companion object {
        const val price = 50
    }


    init {
        CCScheduler.sharedScheduler().schedule("createSun", this, getSunInterval, false)
    }

    fun createSun(t: Float) {
        sun = Sun()
        sun.setPosition(position.x - 100, position.y + 40)
        this.parent.parent.addChild(sun)
        val jumpTo = CCJumpTo.action(0.5f,
                ccp(position.x - 100, position.y), 40f, 1)
        val callFunc1 = CCCallFunc.action(this, "addSun")
        val delayTime = CCDelayTime.action(removeSunInterval)
        val callFunc2 = CCCallFunc.action(this, "removeSun")
        val sequence = CCSequence.actions(jumpTo, callFunc1, delayTime, callFunc2)
        sun.runAction(sequence)
    }

    fun addSun() {
        (parent.parent as CombatLayer).addSun(sun)
    }

    fun removeSun() {
        (parent.parent as CombatLayer).removeSun(sun)
        if (!sun.isNowCollect) {
            sun.removeSelf()
        }
    }

    fun stopScheduler() {
        CCScheduler.sharedScheduler().unschedule("createSun", this)
    }
}
