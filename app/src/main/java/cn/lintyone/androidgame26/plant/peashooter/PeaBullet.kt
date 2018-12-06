package cn.lintyone.androidgame26.plant.peashooter

import cn.lintyone.androidgame26.Zombie
import cn.lintyone.androidgame26.plant.Bullet
import cn.lintyone.androidgame26.plant.ShootPlant
import org.cocos2d.actions.instant.CCHide
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCSprite

class PeaBullet(shootPlant: ShootPlant) : Bullet("bullet/bullet1.png", shootPlant) {
    override fun showBulletBlast(zombie: Zombie) {
        val sprite = CCSprite.sprite("bullet/bulletBlast1.png")
        sprite.position = ccp(zombie.position.x, zombie.position.y + 60)
        parent.addChild(sprite, 6)
        val delayTime = CCDelayTime.action(0.1f)
        val hide = CCHide.action()
        val sequence = CCSequence.actions(delayTime, hide)
        sprite.runAction(sequence)
    }
}