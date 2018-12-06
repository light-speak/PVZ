package cn.lintyone.androidgame26

import org.cocos2d.layers.CCLayer
import org.cocos2d.layers.CCScene
import org.cocos2d.menus.CCMenu
import org.cocos2d.menus.CCMenuItemSprite
import org.cocos2d.nodes.CCDirector
import org.cocos2d.nodes.CCSprite
import org.cocos2d.transitions.CCFadeTRTransition

class MenuLayer : CCLayer() {
    init {
        val sprite = CCSprite.sprite("menu/main_menu_bg.png")
        sprite.setAnchorPoint(0f, 0f)
        addChild(sprite)

        val menu = CCMenu.menu()
        val startAdventureDefault = CCSprite.sprite("menu/start_adventure_default.png")
        val startAdventurePress = CCSprite.sprite("menu/start_adventure_press.png")
        val menuItemSprite = CCMenuItemSprite.item(startAdventureDefault, startAdventurePress, this, "start")
        menuItemSprite.setPosition(270f, 160f)
        menu.addChild(menuItemSprite)
        addChild(menu)

    }

    fun start(item: Any) {
        val scene = CCScene.node()
        scene.addChild(CombatLayer())
        val fadeTransition = CCFadeTRTransition.transition(2f, scene)
        CCDirector.sharedDirector().runWithScene(fadeTransition)
    }
}