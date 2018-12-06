package cn.lintyone.androidgame26.plant

import cn.lintyone.androidgame26.Zombie
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.CCMoveTo
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCSprite
import org.cocos2d.types.util.CGPointUtil

abstract class Bullet(filePath: String, private val shootPlant: ShootPlant) : CCSprite(filePath) {

    private val speed = 200
    var attack = 20

    init {
        ready()
        move()
    }

    private fun ready() {
        this.setPosition(shootPlant.position.x + 20, shootPlant.position.y + 50)
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

    abstract fun showBulletBlast(zombie: Zombie)

}