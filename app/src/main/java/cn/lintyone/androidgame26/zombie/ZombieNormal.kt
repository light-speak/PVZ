package cn.lintyone.androidgame26.zombie

import cn.lintyone.androidgame26.CombatLayer
import cn.lintyone.androidgame26.Sound
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.base.CCSpeed
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.instant.CCHide
import org.cocos2d.actions.interval.*
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCSprite
import org.cocos2d.nodes.CCSpriteFrame
import org.cocos2d.types.CGPoint
import org.cocos2d.types.util.CGPointUtil
import java.util.*
import kotlin.collections.ArrayList

class ZombieNormal(
        private val combatLayer: CombatLayer,
        private val start: CGPoint,
        private val end: CGPoint
) : CCSprite("zombies/zombies_1/walk/Frame00.png") {

    enum class State {
        MOVE, ATTACK
    }
//20
    private val speed = 20
    val attack = 10
    var hp = 100
    var isSlow = false
    var state = State.MOVE
    lateinit var head: CCSprite

    init {
        setAnchorPoint(0.5f, 0f)
        position = start
        move()
    }

    fun move() {
        val t = CGPointUtil.distance(start, end) / speed
        val moveTo = CCMoveTo.action(t, end)
        val callFunc = CCCallFunc.action(combatLayer, "end")
        val sequence = CCSequence.actions(moveTo, callFunc)
        if (isSlow) {
            val speed = CCSpeed.action(sequence, 0.2f)
            runAction(speed)
        } else {
            runAction(sequence)
        }
        val frames = ArrayList<CCSpriteFrame>()
        for (i in 0 until 22) {
            val frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "zombies/zombies_1/walk/Frame%02d.png", i)).displayedFrame()
            frames.add(frame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.1f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        if (isSlow) {
            val speed = CCSpeed.action(repeatForever, 0.2f)
            runAction(speed)
        } else {
            runAction(repeatForever)
        }
        state = State.MOVE
    }

    fun attack() {
        val frames = ArrayList<CCSpriteFrame>()
        for (i in 0 until 21) {
            val frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "zombies/zombies_1/attack/Frame%02d.png", i)).displayedFrame()
            frames.add(frame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.1f)
        val animate = CCAnimate.action(animation, true)
        val repeatForever = CCRepeatForever.action(animate)
        if (isSlow) {
            val speed = CCSpeed.action(repeatForever, 0.2f)
            runAction(speed)
        } else {
            runAction(repeatForever)
        }
        state = State.ATTACK
    }

    fun slow() {
        isSlow = true
        stopAllActions()
        when (state) {
            State.MOVE -> {
                move()
            }
            State.ATTACK -> {
                attack()
            }
        }
        val tintTo1 = CCTintTo.action(0.1f, ccc3(150, 150, 255))
        val delayTime = CCDelayTime.action(3f)
        val tintTo2 = CCTintTo.action(0.1f, ccc3(255, 255, 255))
        val callFunc = CCCallFunc.action(this, "normal")
        val sequence = CCSequence.actions(tintTo1, delayTime, tintTo2, callFunc)
        runAction(sequence)
    }

    fun normal() {
        isSlow = false
        stopAllActions()
        when (state) {
            State.MOVE -> {
                move()
            }
            State.ATTACK -> {
                attack()
            }
        }
    }

    fun hurtCompute(hurt: Int) {
        hp -= hurt
        if (hp < 0) {
            hp = 0
        }
    }

    fun die() {
        stopAllActions()
        val sprite = CCSprite.sprite("zombies/zombies_1/die/head.png")
        sprite.position = position
        parent.parent.addChild(sprite)
        val jumpTo = CCJumpTo.action(1f,
                ccp(position.x - 30, position.y - 20), 40f, 1)
        val delay = CCDelayTime.action(0.7f)
        val hide = CCHide.action()
        head = sprite
        sprite.runAction(CCSequence.actions(jumpTo, delay, hide))

        val frames = ArrayList<CCSpriteFrame>()
        for (i in 0 until 9) {
            val frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "zombies/zombies_1/die/die%02d.png", i)).displayedFrame()
            frames.add(frame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation)
        val callback = CCCallFunc.action(this, "remove")
        val sequence = CCSequence.actions(animate, callback)
        runAction(sequence)
    }

    fun boom() {
        stopAllActions()
        val frames = ArrayList<CCSpriteFrame>()
        for (i in 1 until 21) {
            val frame = CCSprite.sprite(String.format(Locale.CHINA,
                    "zombies/zombies_1/boom/Frame%02d.png", i)).displayedFrame()
            frames.add(frame)
        }
        val animation = CCAnimation.animationWithFrames(frames, 0.1f)
        val animate = CCAnimate.action(animation)
        val callback = CCCallFunc.action(this, "remove")
        val sequence = CCSequence.actions(animate, callback)
        runAction(sequence)
    }

    fun remove() {
        removeSelf()
        head.removeSelf()
    }
}