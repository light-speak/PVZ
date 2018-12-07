package cn.lintyone.androidgame26.plant.snowpea

import cn.lintyone.androidgame26.Zombie
import cn.lintyone.androidgame26.plant.ShootPlant
import cn.lintyone.androidgame26.plant.Bullet

class SnowBullet(shootPlant: ShootPlant) : Bullet("bullet/fireBullet.gif", shootPlant) {
    init {
        attack = 10
    }

    override fun showBulletBlast(zombie: Zombie) {
        zombie.slow()
    }

}