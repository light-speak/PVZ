package cn.lintyone.androidgame26

import android.view.MotionEvent
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.instant.CCHide
import org.cocos2d.actions.interval.CCAnimate
import org.cocos2d.actions.interval.CCDelayTime
import org.cocos2d.actions.interval.CCSequence
import org.cocos2d.layers.CCLayer
import org.cocos2d.layers.CCScene
import org.cocos2d.nodes.CCAnimation
import org.cocos2d.nodes.CCDirector
import org.cocos2d.nodes.CCSprite
import org.cocos2d.nodes.CCSpriteFrame
import org.cocos2d.transitions.CCJumpZoomTransition
import org.cocos2d.types.CGRect
import java.util.*

open class LogoLayer : CCLayer() {
    init {
        logo1()
    }

    private fun logo1() {
        val sprite = CCSprite.sprite("logo/logo1.png")
        sprite.setAnchorPoint(0f, 0f)
        addChild(sprite)
        val delayTime = CCDelayTime.action(2f)
        val hide = CCHide.action()
        val callFunc = CCCallFunc.action(this, "logo2")
        val sequence = CCSequence.actions(delayTime, hide, callFunc)
        sprite.runAction(sequence)
    }

    open fun logo2() {
        val sprite = CCSprite.sprite("logo/logo2.png")
        val winSize = CCDirector.sharedDirector().winSize
        sprite.setPosition(winSize.width / 2.toFloat(), winSize.height / 2.toFloat())
        addChild(sprite)
        val delayTime = CCDelayTime.action(2f)
        val hide = CCHide.action()
        val callFunc = CCCallFunc.action(this, "cg")
        val sequence = CCSequence.actions(delayTime, hide, callFunc)
        sprite.runAction(sequence)
    }

    open fun cg() {
        Sound.begin()
        val sprite = CCSprite.sprite("cg/cg00.png")
        sprite.setAnchorPoint(0f, 0f)
        addChild(sprite)
        val frames = ArrayList<CCSpriteFrame>()
        for (i in 0 until 19) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA,
                    "cg/cg%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }

        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)
        val callFunc = CCCallFunc.action(this, "setTouch")
        val sequence = CCSequence.actions(animate, callFunc)
        sprite.runAction(sequence)
    }

    open fun setTouch() {
        setIsTouchEnabled(true)
    }

    override fun ccTouchesBegan(event: MotionEvent?): Boolean {
        val point = convertTouchToNodeSpace(event)
        val rect = CGRect.make(390f,30f,490f,60f)
        if (CGRect.containsPoint(rect,point)) {
            val scene = CCScene.node()
            scene.addChild(MenuLayer())
            val jumpZoomTransition = CCJumpZoomTransition.transition(2f,scene)
            CCDirector.sharedDirector().runWithScene(jumpZoomTransition)
        }
        return super.ccTouchesBegan(event)
    }
}