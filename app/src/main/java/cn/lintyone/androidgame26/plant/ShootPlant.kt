package cn.lintyone.androidgame26.plant

import org.cocos2d.actions.CCScheduler

/**
 * 可发射子弹的植物
 * @param format 植物资源路径
 * @param number 植物资源数量
 */
abstract class ShootPlant(format: String, number: Int) : Plant(format, number) {

    val bullets = ArrayList<Bullet>()
    var isAttack = false
    var attackInterval = 2.5f

    /**
     * 攻击僵尸
     */
    fun attackZombie() {
        if (!isAttack) {
            CCScheduler.sharedScheduler().schedule("createBullet", this, attackInterval, false)
            isAttack = true
        }
    }

    /**
     * 停止攻击
     */
    fun stopAttackZombie() {
        if (isAttack) {
            CCScheduler.sharedScheduler().unschedule("createBullet", this)
            isAttack = false
        }
    }

    /**
     * 创建子弹
     */
    abstract fun createBullet(t: Float)
}