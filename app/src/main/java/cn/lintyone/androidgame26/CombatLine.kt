package cn.lintyone.androidgame26

import android.util.SparseArray
import cn.lintyone.androidgame26.plant.FireTree
import cn.lintyone.androidgame26.plant.Plant
import cn.lintyone.androidgame26.plant.ShootPlant
import cn.lintyone.androidgame26.zombie.ZombieNormal
import org.cocos2d.actions.CCScheduler

class CombatLine {
    private val plants = SparseArray<Plant>()
    private val zombies = ArrayList<ZombieNormal>()
    private val shootPlants = ArrayList<ShootPlant>()

    init {
        CCScheduler.sharedScheduler().schedule("attackPlant", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("attackZombie", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("bulletHurtCompute", this, 0.1f, false)
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

    fun removePlant(col: Int) {
        if (plants[col] is ShootPlant) {
            shootPlants.removeAt(col)
        }
        plants.remove(col)
    }

    /**
     * 添加僵尸
     * @param zombie 僵尸
     */
    fun addZombie(zombie: ZombieNormal) {
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
                        ZombieNormal.State.MOVE -> {
                            zombie.stopAllActions()
                            zombie.attack()
                        }
                        ZombieNormal.State.ATTACK -> {
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
                } else if (zombie.state == ZombieNormal.State.ATTACK) {
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

    /**
     * 子弹逻辑
     */
    fun bulletHurtCompute(t: Float) {
        if (shootPlants.isNotEmpty()) {
            for (shootPlant in shootPlants) {
                for (bullet in shootPlant.bullets) {
                    //火炬逻辑
                    for (i in 0 until 9) {
                        val temp = plants.get(i)
                        if (temp is FireTree) {
                            if (bullet.visible && bullet.position.x > temp.position.x - 30 &&
                                    bullet.position.x < temp.position.x + 30) {
                                bullet.crossFire()
                                bullet.isFire = true
                            }
                        }
                    }

                    if (zombies.isNotEmpty()) {
                        //僵尸逻辑
                        val iterator = zombies.iterator()
                        while (iterator.hasNext()) {
                            val zombie = iterator.next()
                            if (bullet.visible && bullet.position.x > zombie.position.x - 20 &&
                                    bullet.position.x < zombie.position.x + 20) {
                                bullet.showBulletBlast(zombie)
                                bullet.visible = false
                                zombie.hurtCompute(bullet.attack)
                                if (zombie.hp == 0) {
                                    zombie.die()
                                    iterator.remove()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun cherryBoom(col: Int) {
        if (zombies.isNotEmpty()) {
            val left = 280  + (if (col - 1 >= 0) col - 1 else col) * 105
            val right = 280 + 105 + (col + 1) * 105
            val iterator = zombies.iterator()
            while (iterator.hasNext()) {
                val zombie = iterator.next()
                if (zombie.position.x > left && zombie.position.x < right) {
                    zombie.boom()
                    iterator.remove()
                }
            }
        }
    }


}