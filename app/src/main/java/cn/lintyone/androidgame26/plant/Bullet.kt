package cn.lintyone.androidgame26.plant

import cn.lintyone.androidgame26.zombie.ZombieNormal
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

abstract class Bullet(filePath: String, private val shootPlant: ShootPlant) : CCSprite(filePath) {

    private var speed = 200
    var attack = 20
    var isFire = false

    init {
        ready()
        move()
    }

    fun crossFire() {
        if (!isFire) {
            attack += 10
            texture = CCSprite.sprite("bullet/fire/00.png").texture
            val frames = ArrayList<CCSpriteFrame>()
            for (i in 0 until 2) {
                val frame = CCSprite.sprite(String.format(Locale.CHINA,
                        "bullet/fire/%02d.png", i)).displayedFrame()
                frames.add(frame)
            }
            val animation = CCAnimation.animationWithFrames(frames, 0.1f)
            val animate = CCAnimate.action(animation, true)
            val repeat = CCRepeatForever.action(animate)
            runAction(repeat)
        }
    }

    private fun ready() {
        this.setPosition(shootPlant.position.x + 30, shootPlant.position.y + 55)
        shootPlant.parent.addChild(this, 6)
        shootPlant.bullets.add(this)
    }

    private fun move() {
        val end = ccp(1400f, position.y)
        val t = CGPointUtil.distance(position, end) / speed
        val moveTo = CCMoveTo.action(t, end)
        val callFunc = CCCallFunc.action(this, "end")
        val sequence = CCSequence.actions(moveTo, callFunc)
        runAction(sequence)
    }

    fun end() {
        removeSelf()
        shootPlant.bullets.remove(this)
    }

    abstract fun showBulletBlast(zombie: ZombieNormal)

}