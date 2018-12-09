package cn.lintyone.androidgame26.plant.repeater

import cn.lintyone.androidgame26.plant.ShootPlant
import cn.lintyone.androidgame26.plant.peashooter.PeaBullet
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCSequence

class Repeater : ShootPlant("plant/Repeater/Frame%02d.png", 15) {

    var repeatInterval = 0.5f


    companion object {
        const val price = 200
    }


    override fun createBullet(t: Float) {
        val peaBullet = PeaBullet(this)
        val delayTime = CCDelayTime.action(repeatInterval)
        val callFunc = CCCallFunc.action(this, "createBulletTow")
        val sequence = CCSequence.actions(delayTime, callFunc)
        runAction(sequence)
    }

    fun createBulletTow() {
        val peaBullet = PeaBullet(this)
    }
}
