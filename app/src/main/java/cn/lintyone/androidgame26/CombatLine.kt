package cn.lintyone.androidgame26

import android.util.SparseArray
import cn.lintyone.androidgame26.plant.*
import cn.lintyone.androidgame26.zombie.ZombieNormal
import org.cocos2d.actions.CCScheduler

class CombatLine(val car: Car) {
    private val plants = SparseArray<Plant>()
    private val zombies = ArrayList<ZombieNormal>()
    private val shootPlants = ArrayList<ShootPlant>()
    private val potatoPlants = ArrayList<PotatoMine>()
    private val chomperPlants = ArrayList<Chomper>()


    init {
        CCScheduler.sharedScheduler().schedule("attackPlant", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("attackZombie", this, 1f, false)
        CCScheduler.sharedScheduler().schedule("carCompute", this, 0.5f, false)
        CCScheduler.sharedScheduler().schedule("bulletHurtCompute", this, 0.1f, false)
        CCScheduler.sharedScheduler().schedule("potatoHurtCompute", this, 0.1f, false)

        car.callback = object : Car.Callback {
            override fun hide() {
                car.visible = false
            }

            override fun attack() {
                if (car.visible) {
                    if (zombies.isNotEmpty()) {
                        val iterator = zombies.iterator()
                        while (iterator.hasNext()) {
                            val zombie = iterator.next()
                            if (car.position.x > zombie.position.x - 50 &&
                                    car.position.x < zombie.position.x + 50) {
                                zombie.die()
                                iterator.remove()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 车的逻辑
     */
    fun carCompute(t: Float) {
        if (plants.get(0) == null) {
            val iterator = zombies.iterator()
            while (iterator.hasNext()) {
                val zombie = iterator.next()
                if (car.position.x > zombie.position.x - 80 && !car.isGO) {
                    car.go()
                    Sound.car()
                    break
                }
            }
        }
    }

    /**
     * 添加植物
     * @param col 位置
     * @param plant 植物
     */
    fun addPlant(col: Int, plant: Plant) {
        plants.put(col, plant)
        when (plant) {
            is ShootPlant -> {
                shootPlants.add(plant)
            }
            is PotatoMine -> {
                potatoPlants.add(plant)
            }
            is Pepper -> {
                plant.callback = object : Pepper.Callback {
                    override fun boom() {
                        val iterator = zombies.iterator()
                        while (iterator.hasNext()) {
                            val zombie = iterator.next()
                            zombie.boom()
                            iterator.remove()
                        }
                        plants.remove(col)
                    }
                }
            }
            is Chomper -> {
                chomperPlants.add(plant)
            }
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
            //子弹逻辑
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
                                Sound.attack()
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

    /**
     * 土豆计算
     */
    fun potatoHurtCompute(t: Float) {
        if (potatoPlants.isNotEmpty()) {
            //遍历所有格子
            for (i in 0 until 9) {
                //植物
                val temp = plants.get(i)
                when (temp) {
                    is PotatoMine -> {
                        val iterator = zombies.iterator()
                        while (iterator.hasNext()) {
                            val zombie = iterator.next()
                            if (temp.position.x > zombie.position.x - 60 &&
                                    temp.position.x < zombie.position.x + 60) {
                                if (temp.canBoom) {
                                    val iterator2 = zombies.iterator()
                                    while (iterator2.hasNext()) {
                                        val zombie2 = iterator2.next()
                                        if (temp.position.x > zombie2.position.x - 105 &&
                                                temp.position.x < zombie2.position.x + 105) {
                                            zombie2.boom()
                                            iterator2.remove()
                                        }
                                    }
                                    plants.remove(i)
                                    potatoPlants.remove(temp)
                                    temp.boom()
                                }
                                break
                            }
                        }
                    }
                }
            }
        }
        if (chomperPlants.isNotEmpty()) {
            if (zombies.isNotEmpty()) {
                for (chomper in chomperPlants) {
                    val iterator = zombies.iterator()
                    while (iterator.hasNext()) {
                        val zombie = iterator.next()
                        if (chomper.position.x > zombie.position.x - 150) {
                            if (chomper.canEat) {
                                iterator.remove()
                                chomper.eat(zombie)
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 樱桃爆炸
     * @param col 列
     */
    fun cherryBoom(col: Int) {
        if (zombies.isNotEmpty()) {
            val left = 280 + (if (col - 1 >= 0) col - 1 else col) * 105
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