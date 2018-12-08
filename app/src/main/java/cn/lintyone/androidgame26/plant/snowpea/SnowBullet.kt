package cn.lintyone.androidgame26.plant.snowpea

import cn.lintyone.androidgame26.zombie.ZombieNormal
import cn.lintyone.androidgame26.plant.ShootPlant
import cn.lintyone.androidgame26.plant.Bullet

class SnowBullet(shootPlant: ShootPlant) : Bullet("bullet/bullet2.png", shootPlant) {
    init {
        attack = 10
    }

    override fun showBulletBlast(zombie: ZombieNormal) {
        zombie.slow()
    }

}