package cn.lintyone.androidgame26.plant

import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.instant.CCHide
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import java.util.*

class CherryBomb : Plant("plant/CherryBomb/Frame%02d.png", 19) {

    var row = 0
    var col = 0
    lateinit var callback: CherryBomb.CallBack

    init {
        price = 150
    }

    override fun ready() {
        this.setAnchorPoint(0.5f, 0f)
        for (i in 0 until 7) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA
                    , "plant/CherryBomb/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)

        frames.clear()
        for (i in 7 until 19) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA
                    , "plant/CherryBomb/Frame%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }
        val animation2 = CCAnimation.animationWithFrames(frames, 0.08f)
        val animate2 = CCAnimate.action(animation2, false)

        val hide = CCHide.action()
        val callback = CCCallFunc.action(this, "boom")
        val remove = CCCallFunc.action(this, "remove")
        val sequence = CCSequence.actions(animate, callback, animate2, hide, remove)
        this.runAction(sequence)
    }

    fun boom() {
        callback.boom(row, col)
    }

    fun remove() {
        stopAllActions()
        removeSelf()
    }


    interface CallBack {
        /**
         * 爆炸回调
         * @param row 行
         * @param col 列
         */
        fun boom(row: Int, col: Int)
    }
}