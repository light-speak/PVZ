package cn.lintyone.androidgame26.plant.snowpea

import cn.lintyone.androidgame26.plant.ShootPlant

class SnowPea : ShootPlant("plant/SnowPea/Frame%02d.png", 15) {

    init {
        price = 175
    }

    override fun createBullet(t: Float) {
        val snowBullet = SnowBullet(this)
    }
}
