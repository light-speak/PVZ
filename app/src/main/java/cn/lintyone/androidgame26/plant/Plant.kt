package cn.lintyone.androidgame26.plant

import cn.lintyone.androidgame26.Sound
import cn.lintyone.androidgame26.plant.peashooter.Peashooter
import cn.lintyone.androidgame26.plant.repeater.Repeater
import cn.lintyone.androidgame26.plant.snowpea.SnowPea
import cn.lintyone.androidgame26.plant.sunFlower.SunFlower
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import org.cocos2d.nodes.CCSpriteFrame
import java.util.*
import kotlin.collections.ArrayList

open class Plant(private val format: String, private val number: Int) : CCSprite(String.format(Locale.CHINA, format, 0)) {

    var frames = ArrayList<CCSpriteFrame>()
    var hp = 100


    companion object {

        fun getPlantByID(id: Int): Plant {
            return when (id) {
                0 -> Peashooter()
                1 -> SunFlower()
                2 -> CherryBomb()
                3 -> WallNut()
                4 -> PotatoMine()
                5 -> SnowPea()
                6 -> Chomper()
                7 -> Repeater()
                8 -> FireTree()
                9 -> Pepper()
                else -> {
                    Peashooter()
                }
            }
        }

        fun getPriceById(id: Int): Int {
            return when (id) {
                0 -> Peashooter.price
                1 -> SunFlower.price
                2 -> CherryBomb.price
                3 -> WallNut.price
                4 -> PotatoMine.price
                5 -> SnowPea.price
                6 -> Chomper.price
                7 -> Repeater.price
                8 -> FireTree.price
                9 -> Pepper.price
                else -> {
                    Peashooter.price
                }
            }
        }

        fun getPriceByType(plant: Plant): Int {
            return when (plant) {
                is Peashooter -> Peashooter.price
                is SunFlower -> SunFlower.price
                is CherryBomb -> CherryBomb.price
                is WallNut -> WallNut.price
                is PotatoMine -> PotatoMine.price
                is SnowPea -> SnowPea.price
                is Chomper -> Chomper.price
                is Repeater -> Repeater.price
                is FireTree -> FireTree.price
                is Pepper -> Pepper.price
                else -> {
                    Peashooter.price
                }
            }
        }
    }

    init {
        ready()
    }


    open fun ready() {
        this.setAnchorPoint(0.5f, 0f)
        for (i in 0 until number) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA, format, i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        this.runAction(repeatForever)
    }


    open fun hurtCompute(hurt: Int) {
        Sound.chomp()
        hp -= hurt
        if (hp < 0) {
            hp = 0
        }
    }

}

