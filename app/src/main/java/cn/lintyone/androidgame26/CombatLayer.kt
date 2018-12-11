package cn.lintyone.androidgame26

import android.view.MotionEvent
import cn.lintyone.androidgame26.plant.CherryBomb
import cn.lintyone.androidgame26.plant.Plant
import cn.lintyone.androidgame26.plant.sunFlower.Sun
import cn.lintyone.androidgame26.plant.sunFlower.SunFlower
import cn.lintyone.androidgame26.zombie.ZombieNormal
import org.cocos2d.actions.CCScheduler
import org.cocos2d.actions.base.CCRepeatForever
import org.cocos2d.actions.instant.CCCallFunc
import org.cocos2d.actions.interval.*
import org.cocos2d.layers.CCLayer
import org.cocos2d.layers.CCScene
import org.cocos2d.layers.CCTMXTiledMap
import org.cocos2d.nodes.*
import org.cocos2d.transitions.CCFlipXTransition
import org.cocos2d.types.CGPoint
import org.cocos2d.types.CGRect
import org.cocos2d.types.CGSize
import org.cocos2d.types.ccColor3B
import java.util.*
import kotlin.collections.ArrayList

open class CombatLayer : CCLayer() {

    private lateinit var tmxTiledMap: CCTMXTiledMap
    private lateinit var show: ArrayList<CCSprite>
    private lateinit var winSize: CGSize
    private lateinit var startReady: CCSprite
    private lateinit var seedBank: CCSprite
    private lateinit var label: CCLabel
    private lateinit var seedChooser: CCSprite
    private lateinit var plantCards: ArrayList<PlantCard>
    private lateinit var selectPlantCards: ArrayList<PlantCard>
    private lateinit var seedChooserBtn: CCSprite
    private var seedChooserBtnIsClick = false
    private var isMove = false
    private var isStart = false
    private var selectCard: PlantCard? = null
    private var selectPlant: Plant? = null
    private val pointsTowers = ArrayList<ArrayList<CGPoint>>()
    private val combatLines = ArrayList<CombatLine>()
    private lateinit var pointsPath: ArrayList<CGPoint>
    private lateinit var random: Random
    var currentSunNumber = 500
    private val suns = ArrayList<Sun>()


    init {
        loadMap()
    }

    private fun loadMap() {
        Sound.choose()
        tmxTiledMap = CCTMXTiledMap.tiledMap("combat/map1.tmx")
        addChild(tmxTiledMap)
        val objectGroupShow = tmxTiledMap.objectGroupNamed("show")
        val objects = objectGroupShow.objects
        show = ArrayList()
        for (obj in objects) {
            val x = obj["x"]!!.toFloat()
            val y = obj["y"]!!.toFloat()
            val shake = CCSprite.sprite("zombies/zombies_1/shake/Frame00.png")
            shake.setPosition(x, y)
            tmxTiledMap.addChild(shake)
            show.add(shake)
            val frames = ArrayList<CCSpriteFrame>()
            for (i in 0 until 2) {
                val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA,
                        "zombies/zombies_1/shake/Frame%02d.png", i)).displayedFrame()
                frames.add(spriteFrame)
            }
            val animation = CCAnimation.animationWithFrames(frames, 0.2f)
            val animate = CCAnimate.action(animation, true)
            val repeatForever = CCRepeatForever.action(animate)
            shake.runAction(repeatForever)
        }
        winSize = CCDirector.sharedDirector().winSize
        val delayTime = CCDelayTime.action(2f)
        val moveBy = CCMoveBy.action(2f, ccp(winSize.width - tmxTiledMap.contentSize.width, 0f))
        val callFunc = CCCallFunc.action(this, "loadChoose")
        val sequence = CCSequence.actions(delayTime, moveBy, callFunc)
        tmxTiledMap.runAction(sequence)

        for (i in 0 until 5) {
            val pointsTower = ArrayList<CGPoint>()
            val objectGroupTower = tmxTiledMap.objectGroupNamed("tower$i")
            val objects2 = objectGroupTower.objects
            for (obj in objects2) {
                val x = obj["x"]!!.toFloat()
                val y = obj["y"]!!.toFloat()
                pointsTower.add(ccp(x, y))
            }
            pointsTowers.add(pointsTower)
        }


        for (i in 0 until 5) {
            val car = Car()
            val x = pointsTowers[i][1].x - 108 * 2 + i * 4
            val y = pointsTowers[i][1].y + 30
            car.setPosition(x, y)
            tmxTiledMap.addChild(car)
            combatLines.add(CombatLine(car))
        }
    }

    fun loadChoose() {
        seedBank = CCSprite.sprite("choose/SeedBank.png")
        seedBank.setAnchorPoint(0f, 1f)
        seedBank.setPosition(0f, winSize.height)
        addChild(seedBank)

        label = CCLabel.makeLabel(currentSunNumber.toString(), "", 20f)
        label.color = ccColor3B.ccBLACK
        label.setPosition(40f, 695f)
        addChild(label)

        seedChooser = CCSprite.sprite("choose/SeedChooser.png")
        seedChooser.setAnchorPoint(0f, 0f)
        addChild(seedChooser)

        val seedChooserBtnDisabled = CCSprite.sprite("choose/SeedChooser_Button_Disabled.png")
        seedChooserBtnDisabled.setPosition(seedChooser.contentSize.width / 2, 80f)
        seedChooser.addChild(seedChooserBtnDisabled)

        seedChooserBtn = CCSprite.sprite("choose/SeedChooser_Button.png")
        seedChooserBtn.setPosition(seedChooser.contentSize.width / 2, 80f)
        seedChooser.addChild(seedChooserBtn)
        seedChooserBtn.visible = false

        plantCards = ArrayList()
        selectPlantCards = ArrayList()
        for (i in 0 until 10) {
            val plantCard = PlantCard(i)
            plantCards.add(plantCard)
            plantCard.dark.setPosition(50 + 60 * (i % 9).toFloat(), 590f - (i / 9) * 80)
            seedChooser.addChild(plantCard.dark)
            plantCards.add(plantCard)
            plantCard.light.setPosition(50 + 60 * (i % 9).toFloat(), 590f - (i / 9) * 80)
            seedChooser.addChild(plantCard.light)
        }
        setIsTouchEnabled(true)
    }

    override fun ccTouchesBegan(event: MotionEvent?): Boolean {
        val point = convertTouchToNodeSpace(event)
        if (isStart) {
            if (CGRect.containsPoint(seedBank.boundingBox, point)) {
                Sound.touch()
                var currentCard: PlantCard? = null
                if (selectCard != null) {
                    selectCard!!.light.opacity = 255
                    currentCard = selectCard
                    selectCard = null
                }
                for (plantCard in selectPlantCards) {
                    if (CGRect.containsPoint(plantCard.light.boundingBox, point)) {
                        if (currentSunNumber < Plant.getPriceById(plantCard.id)) {
                            break
                        }
                        if (plantCard == currentCard) {
                            if (selectPlant is SunFlower) {
                                (selectPlant as SunFlower).stopScheduler()
                            }
                            currentCard.light.opacity = 255
                            selectCard = null
                            break
                        }
                        if (selectPlant is SunFlower) {
                            (selectPlant as SunFlower).stopScheduler()
                        }
                        selectCard = plantCard
                        selectCard!!.light.opacity = 100
                        selectPlant = Plant.getPlantByID(selectCard!!.id)
                    }
                }
            } else if (selectPlant != null && selectCard != null) {
                Sound.plant()
                val col = (point.x - 220).toInt() / 105
                val row = (point.y - 40).toInt() / 120
                if (col in 0..8 && row in 0..5) {
                    val combatLine = combatLines[row]
                    if (!combatLine.isContainPlant(col)) {
                        combatLine.addPlant(col, selectPlant!!)
                        selectPlant!!.position = pointsTowers[row][col]
                        tmxTiledMap.addChild(selectPlant)
                        addSunNumber(-Plant.getPriceByType(selectPlant!!))
                        if (selectPlant is CherryBomb) {
                            (selectPlant as CherryBomb).row = row
                            (selectPlant as CherryBomb).col = col
                            (selectPlant as CherryBomb).callback = object : CherryBomb.CallBack {
                                override fun boom(row: Int, col: Int) {
                                    combatLines[row].removePlant(col)
                                    combatLines[row].cherryBoom(col)
                                    if (row - 1 >= 0) {
                                        combatLines[row - 1].cherryBoom(col)
                                    }
                                    if (row + 1 <= 4) {
                                        combatLines[row + 1].cherryBoom(col)
                                    }
                                }
                            }
                        }
                        selectPlant = null
                        selectCard = null
                    }
                }

            } else {
                for (sun in suns) {
                    if (CGRect.containsPoint(sun.boundingBox, point)) {
                        sun.collect()
                        Sound.touch()
                    }
                }
            }

        } else {

            if (CGRect.containsPoint(seedChooser.boundingBox, point)) {
                if (selectPlantCards.size < 6) {
                    Sound.touch()
                    for (plantCard in plantCards) {
                        if (CGRect.containsPoint(plantCard.light.boundingBox, point)) {
                            if (!selectPlantCards.contains(plantCard)) {
                                selectPlantCards.add(plantCard)
                                val moveTo = CCMoveTo.action(0.1f,
                                        ccp(50 + 60 * selectPlantCards.size.toFloat(), 725f))
                                plantCard.light.runAction(moveTo)
                                if (selectPlantCards.size == 6) {
                                    seedChooserBtn.visible = true
                                }
                            }
                        }
                    }
                }
            }
            if (CGRect.containsPoint(seedBank.boundingBox, point)) {
                if (!seedChooserBtnIsClick) {
                    Sound.touch()
                    isMove = false
                    for (plantCard in selectPlantCards) {
                        if (CGRect.containsPoint(plantCard.light.boundingBox, point)) {
                            val moveTo = CCMoveTo.action(0.1f,
                                    plantCard.dark.position)
                            plantCard.light.runAction(moveTo)
                            selectPlantCards.remove(plantCard)
                            seedChooserBtn.visible = false
                            isMove = true
                            break
                        }
                    }
                }
            }
            if (isMove) {
                for (i in 0 until selectPlantCards.size) {
                    val plantCard = selectPlantCards[i]
                    val moveTo = CCMoveTo.action(0.1f, ccp(110 + 60 * i.toFloat(), 725f))
                    plantCard.light.runAction(moveTo)
                }
            }
            if (seedChooserBtn.visible) {
                Sound.touch()
                if (CGRect.containsPoint(seedChooserBtn.boundingBox, point)) {
                    seedChooserBtnIsClick = true
                    for (plantCard in selectPlantCards) {
                        addChild(plantCard.light)
                    }
                    seedChooser.removeSelf()
                    val moveTo = CCMoveTo.action(2f, ccp(-100f, 0f))
                    val callFunc = CCCallFunc.action(this, "startReady")
                    val sequence = CCSequence.actions(moveTo, callFunc)
                    tmxTiledMap.runAction(sequence)
                    Sound.coming()
                }
            }
        }
        return super.ccTouchesBegan(event)
    }

    fun startReady() {
        for (sprite in show) {
            sprite.removeSelf()
        }
        setIsTouchEnabled(false)
        startReady = CCSprite.sprite("startready/startReady_00.png")
        startReady.setPosition(winSize.width / 2, winSize.height / 2)
        addChild(startReady)
        val frames = ArrayList<CCSpriteFrame>()
        for (i in 0 until 3) {
            val spriteFrame = CCSprite.sprite(String.format(Locale.CHINA,
                    "startready/startReady_%02d.png", i)).displayedFrame()
            frames.add(spriteFrame)
        }

        val animation = CCAnimation.animationWithFrames(frames, 0.2f)
        val animate = CCAnimate.action(animation, false)
        val callFunc = CCCallFunc.action(this, "start")
        val sequence = CCSequence.actions(animate, callFunc)
        startReady.runAction(sequence)
    }

    open fun start() {
        startReady.removeSelf()
        setIsTouchEnabled(true)
        isStart = true


        pointsPath = ArrayList()
        val objectsGroupPath = tmxTiledMap.objectGroupNamed("path")
        for (obj in objectsGroupPath.objects) {
            val x = obj["x"]!!.toFloat()
            val y = obj["y"]!!.toFloat()
            pointsPath.add(CCNode.ccp(x, y))
        }
        random = Random()
        update()
        addZombie(1f)
        addZombie(1f)
        addZombie(1f)
        addZombie(1f)
        addZombie(1f)
        addZombie(1f)
        val delayTime1 = CCDelayTime.action(5f)
        Sound.white()
        val callFunc1 = CCCallFunc.action(this, "startAddZombie1")
        val delayTime2 = CCDelayTime.action(40f)
        val callFunc2 = CCCallFunc.action(this, "startAddZombie2")
        val delayTime3 = CCDelayTime.action(60f)
        val callFunc3 = CCCallFunc.action(this, "startAddZombie3")
        val sequence = CCSequence.actions(delayTime1, callFunc1, delayTime2, callFunc2, delayTime3, callFunc3)
        runAction(sequence)
    }

    fun startAddZombie1() {
        CCScheduler.sharedScheduler().schedule("addZombie", this, 20f, false)
    }

    fun startAddZombie2() {
        CCScheduler.sharedScheduler().schedule("addZombie", this, 10f, false)
    }

    fun startAddZombie3() {
        CCScheduler.sharedScheduler().schedule("addZombie", this, 5f, false)
    }

    open fun addZombie(t: Float) {
        val i = random.nextInt(5)
        val zombie = ZombieNormal(this, pointsPath[2 * i],
                pointsPath[2 * i + 1])
        tmxTiledMap.addChild(zombie, 5 - i)
        combatLines[i].addZombie(zombie)
    }

    open fun end() {
        setIsTouchEnabled(false)
        CCScheduler.sharedScheduler().unschedule("addZombie", this)
        for (node in tmxTiledMap.children) {
            node.stopAllActions()
        }
        val zombiesWon = CCSprite.sprite("zombieswon/ZombiesWon.png")
        zombiesWon.setPosition(winSize.width / 2, winSize.height / 2)
        addChild(zombiesWon)
        val delayTime = CCDelayTime.action(2f)
        val callFunc = CCCallFunc.action(this, "restart")
        val sequence = CCSequence.actions(delayTime, callFunc)
        zombiesWon.runAction(sequence)
    }

    fun restart() {
        val scene = CCScene.node()
        scene.addChild(MenuLayer())
        val flipXTransition = CCFlipXTransition
                .transition(2f, scene, 1)
        CCDirector.sharedDirector().replaceScene(flipXTransition)
    }

    fun addSunNumber(sunNumber: Int) {
        currentSunNumber += sunNumber
        label.setString(currentSunNumber.toString())
        update()
    }

    fun addSun(sun: Sun) {
        suns.add(sun)
    }

    fun removeSun(sun: Sun) {
        suns.remove(sun)
    }

    private fun update() {
        for (plantCard in selectPlantCards) {
            val price = Plant.getPriceById(plantCard.id)
            if (currentSunNumber >= price) {
                plantCard.light.opacity = 255
            } else {
                plantCard.light.opacity = 100
            }
        }
    }
}
