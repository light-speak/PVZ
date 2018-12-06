package cn.lintyone.androidgame26.plant.peashooter

import cn.lintyone.androidgame26.plant.ShootPlant

class Peashooter : ShootPlant("plant/Peashooter/Frame%02d.png", 13) {

    init {
        price = 100
    }

    override fun createBullet(t: Float) {
        val peaBullet = PeaBullet(this)
    }
}
