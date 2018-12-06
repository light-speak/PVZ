package cn.lintyone.androidgame26

import android.util.SparseArray
import cn.lintyone.androidgame26.plant.Plant
import cn.lintyone.androidgame26.plant.ShootPlant
import org.cocos2d.actions.CCScheduler

class CombatLine {
    private val plants = SparseArray<Plant>()
    private val zombies = ArrayList<Zombie>()
    private val shootPlants = ArrayList<ShootPlant>()

    init {
        CCScheduler.sharedScheduler().schedule("attackPlant", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("attackZombie", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("bulletHurtCompute", this, 0.2f, false)
    }

    /**
     * 添加植物
     * @param col 位置
     * @param plant 植物
     */
    fun addPlant(col: Int, plant: Plant) {
        plants.put(col, plant)
        if (plant is ShootPlant) {
            shootPlants.add(plant)
        }
    }

    /**
     * 添加僵尸
     * @param zombie 僵尸
     */
    fun addZombie(zombie: Zombie) {
        zombies.add(zombie)
    }

    /**
     * 当前位置是否有植物
     * @param col 位置
     * @return true or false
     */
    fun isContainPlant(col: Int) = plants[col] != null

    /**
     * 攻击植物
     */
    fun attackPlant(t: Float) {
        if (zombies.size != 0 && plants.size() != 0) {
            for (zombie in zombies) {
                val col = ((zombie.position.x - 280) / 105).toInt()
                if (isContainPlant(col)) {
                    when (zombie.state) {
                        Zombie.State.MOVE -> {
                            zombie.stopAllActions()
                            zombie.attack()
                        }
                        Zombie.State.ATTACK -> {
                            val plant = plants[col]
                            plant.hurtCompute(zombie.attack)
                            if (plant.hp == 0) {
                                plants.remove(col)
                                plant.removeSelf()
                                zombie.stopAllActions()
                                zombie.move()
                            }
                        }
                    }
                } else if (zombie.state == Zombie.State.ATTACK) {
                    zombie.stopAllActions()
                    zombie.move()
                }
            }
        }
    }

    /**
     * 攻击僵尸
     */
    fun attackZombie(t: Float) {
        if (shootPlants.isNotEmpty()) {
            for (shootPlant in shootPlants) {
                if (zombies.isEmpty()) {
                    shootPlant.stopAttackZombie()
                } else {
                    shootPlant.attackZombie()
                }
            }
        }
    }

    fun bulletHurtCompute(t: Float) {
        if (shootPlants.isNotEmpty() && zombies.isNotEmpty()) {
            for (shootPlant in shootPlants) {
                for (bullet in shootPlant.bullets) {
                    val iterator = zombies.iterator()
                    while (iterator.hasNext()) {
                        val zombie = iterator.next()
                        if (bullet.visible && bullet.position.x > zombie.position.x - 20 &&
                                bullet.position.x < zombie.position.x + 20) {
                            bullet.showBulletBlast(zombie)
                            bullet.visible = false
                            zombie.hurtCompute(bullet.attack)
                            if (zombie.hp == 0) {
                                zombie.removeSelf()
                                iterator.remove()
                            }
                        }
                    }
                }
            }
        }
    }
}