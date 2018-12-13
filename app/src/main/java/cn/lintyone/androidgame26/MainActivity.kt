package cn.lintyone.androidgame26

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import org.cocos2d.layers.CCScene
import org.cocos2d.nodes.CCDirector
import org.cocos2d.opengl.CCGLSurfaceView

class MainActivity : Activity() {
    private lateinit var director: CCDirector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val surfaceView = CCGLSurfaceView(this)
        setContentView(surfaceView)
        director = CCDirector.sharedDirector()
        director.attachInView(surfaceView)
        director.setDisplayFPS(true)
        director.setScreenSize(1280f, 768f)
        val scene = CCScene.node()
//        scene.addChild(LogoLayer())
        scene.addChild(CombatLayer())
        director.runWithScene(scene)
        Sound.build(this)
    }

    override fun onPause() {
        super.onPause()
        Sound.onPause()
        director.pause()
    }

    override fun onResume() {
        super.onResume()
        Sound.onResume()
        director.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Sound.onDestroy()
        director.end()
    }
}
