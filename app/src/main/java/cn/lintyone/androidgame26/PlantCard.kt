package cn.lintyone.androidgame26

import org.cocos2d.nodes.CCSprite
import java.util.*

class PlantCard(val id: Int) {
    val light = CCSprite.sprite(String.format(Locale.CHINA, "choose/card/p%02d.png", id))!!
    val dark = CCSprite.sprite(String.format(Locale.CHINA, "choose/card/p%02d.png", id))!!

    init {
        dark.opacity = 100
    }

}