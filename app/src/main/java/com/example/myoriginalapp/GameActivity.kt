package com.example.myoriginalapp

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_input_task.view.*
import java.util.*

class GameActivity : AppCompatActivity() {
    var flag:Boolean = false

//一つのオブジェクトに一つのクラス。継承処理したい。

// Canvas 関連　　CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

    /**
     * Viewのサブクラスを定義
     * 背景と、その上に乗っかった絵（＝コンストラクタ引数で渡ってくるビットマップ）。
     * 描画処理実体はonDraw内に書かれているcanvas.～ のところ。
     * (on)Invalidateで再描画される、つまりインスタンスのonDrawが呼ばれるたび、
     * canvas.～で仕込んである回転や移動処理が実行され、結果動いて見える。
     */
    open inner class MyView(context: Context?, bitmap: Bitmap) : View(context) {
        private val bitmap: Bitmap
        var currentX: Float
        var currentY: Float
        var dx: Float
        var dy: Float
        var bitmapHeight: Int
        var bitmapWidth: Int
        var rot = 0F
        var canvasWidth = 0
        var canvasHeight = 0
        private val mPainter = Paint()

        /**
         * onDrawは、Viewのインスタンスが作られた（レイアウトにaddViewされた？）ときと、
         * そのインスタンスで（on）invalidateが呼ばれたときに、実行されるそうだ。
         * View上に描きたい内容は、ここで渡されるCanvas canvasを使って描く感じか。
         * @param canvas
         */
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            //canvas.drawColor(Color.YELLOW) //背景色を設定。
            canvasWidth = canvas.width
            canvasHeight = canvas.height

            canvas.rotate(rot, currentX + bitmapWidth / 2, currentY + bitmapHeight / 2) //回転角度は第一引数、後ろ2つの引数で回転の中心座標を決め。
            //なお後ろ2つを0,0とかにするとわかるが、rotateは、Canvasごと（viewごと）回転させる。
            //ここでは、回転の軸が常に、乗っかっているビットマップ絵の中心になるように仕込んでるので、
            //結果的に見た目としては、絵だけが回転しているように見える。

            val rotationDegree = 20f //再描画ごとに回転する角度
            rot += rotationDegree

            //ビットマップの描画
            canvas.drawBitmap(bitmap, currentX, currentY, mPainter)
        }

        /**
         * 移動後の位置を計算
         * ディスプレイ領域から外に出ないように制御してる感じかな
         */
        fun move() {
            if (currentX + dx < 0) {
                dx = -dx
            }
            if (currentY + dy < 0) {
                dy = -dy
            }
            if (canvasWidth < currentX + dx + bitmapWidth) {
                dx = -dx
            }
            if (canvasHeight < currentY + dy + bitmapHeight) {
                dy = -dy
            }
            currentX += dx
            currentY += dy
        }


        /**
         * コンストラクタ
         */
        init {

            //WindowManagerクラスとDisplayクラスを使って画面のサイズを取得する
            val display = windowManager.defaultDisplay
            val point = Point(0, 0)
            display.getSize(point) //ここの処理で、引数(Point outSize)に渡した変数に対し、ディスプレイサイズのピクセルが代入される。
            val displayWidth = point.x //ディスプレイ幅を取得。
            val displayHeight = point.y //同、高さを取得。

            //引数に取ったビットマップ（今回は乗っかってる絵）とその幅＆高さを得る。
            this.bitmap = bitmap
            bitmapHeight = bitmap.height
            bitmapWidth = bitmap.width

            //開始位置を指定。画面中央は、ディスプレイ幅の1/2と高さの1/2で得ている。
            val x0 = (displayWidth-bitmapWidth/2).toFloat()
            val y0 = (400).toFloat()
//            val x0 = canvasWidth/2.toFloat()
//            val y0 = 100.toFloat()

            //x方向、y方向の、描画ごとの移動量。変えると、アニメーションの移動速度が変化する感じか。
            //ここでは起動のたび（MyViewインスタンス生成のたび？）Randomで速度が変わっている。
//            val r = Random()
//            dx = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
//            dy = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
            dx = 1.0.toFloat()
            dy = 10.0.toFloat()


//            //これはよくわからない。x0,y0でも動くぽいが。
            currentX = x0 - bitmapWidth / 2
            currentY = y0 - bitmapHeight / 2
//            currentX = x0
//            currentY = y0

            //アンチエイリアスの設定。View内の描画物に対して機能するのかな？よくわかんない。
            mPainter.isAntiAlias = true
        }
    }

    //BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB--Balloon関連--BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
private inner class blnView(blnContext: Context?, blnBitmap: Bitmap) : View(blnContext) {
    private val blnBitmap: Bitmap
    var blnCurrentX: Float
    var blnCurrentY: Float
    var blndx: Float = 0.0f
    var blndy: Float = 0.0f
    var blnbitmapHeight: Int
    var blnbitmapWidth: Int
    var rot = 0F
    var canvasWidth = 0
    var canvasHeight = 0
    private val mPainter = Paint()

    /**
     * onDrawは、Viewのインスタンスが作られた（レイアウトにaddViewされた？）ときと、
     * そのインスタンスで（on）invalidateが呼ばれたときに、実行されるそうだ。
     * View上に描きたい内容は、ここで渡されるCanvas canvasを使って描く感じか。
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasWidth = canvas.width
        canvasHeight = canvas.height

        //ビットマップの描画
        canvas.drawBitmap(blnBitmap, blnCurrentX, blnCurrentY, mPainter)
    }

    /**
     * 移動後の位置を計算
     * ディスプレイ領域から外に出ないように制御してる感じかな
     */
    fun move() {
//        if (blnCurrentX + blndx < 0) {
//            blndx = -blndx
//        }
        if (blnCurrentY + blndy < 400 - blndy*10) {//ふわふわの下限
            blndy = -blndy
        }
        if (400 + blndy*10 < blnCurrentY + blndy) {//ふわふわの上限
            blndy = -blndy
        }
//        if (canvasWidth < blnCurrentX + blndx + blnbitmapWidth) {
//            blndx = -blndx
//        }
//        if (canvasHeight < blnCurrentY + blndy + blnbitmapHeight) {
//            blndy = -blndy
//        }
        //baloonCurrentX += dx
        blnCurrentY += blndy
    }


    /**
     * コンストラクタ
     */
    init {

        //WindowManagerクラスとDisplayクラスを使って画面のサイズを取得する
        val display = windowManager.defaultDisplay
        val point = Point(0, 0)
        display.getSize(point) //ここの処理で、引数(Point outSize)に渡した変数に対し、ディスプレイサイズのピクセルが代入される。
        val displayWidth = point.x //ディスプレイ幅を取得。
        val displayHeight = point.y //同、高さを取得。

        //引数に取ったビットマップ（今回は乗っかってる絵）とその幅＆高さを得る。
        this.blnBitmap = blnBitmap
        blnbitmapHeight = blnBitmap.height
        blnbitmapWidth = blnBitmap.width

        //開始位置を指定。画面中央は、ディスプレイ幅の1/2と高さの1/2で得ている。
        val x0 = (blnbitmapWidth/2).toFloat()
        val y0 = (400).toFloat()
//            val x0 = canvasWidth/2.toFloat()
//            val y0 = 100.toFloat()

        //x方向、y方向の、描画ごとの移動量。変えると、アニメーションの移動速度が変化する感じか。
        //ここでは起動のたび（MyViewインスタンス生成のたび？）Randomで速度が変わっている。
        val r = Random()
//            dx = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
//        blndy = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
//        dx = 1.0.toFloat()
        blndy = 1.0.toFloat()


//            //これはよくわからない。x0,y0でも動くぽいが。
        blnCurrentX = x0 - blnbitmapWidth / 2
        blnCurrentY = y0 - blnbitmapHeight / 2
//            currentX = x0
//            currentY = y0

        //アンチエイリアスの設定。View内の描画物に対して機能するのかな？よくわかんない。
        mPainter.isAntiAlias = true
    }
}

//CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC--雲関連その１--CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
private inner class cld1View(cld1Context: Context?, cld1Bitmap: Bitmap) : View(cld1Context) {
    private val cld1Bitmap: Bitmap
    var cld1CurrentX: Float
    var cld1CurrentY: Float
    var cld1dx: Float = 0.0f
    var cld1dy: Float = 0.0f
    var cld1bitmapHeight: Int
    var cld1bitmapWidth: Int
    var rot = 0F
    var canvasWidth = 0
    var canvasHeight = 0
    private val mPainter = Paint()

    /**
     * onDrawは、Viewのインスタンスが作られた（レイアウトにaddViewされた？）ときと、
     * そのインスタンスで（on）invalidateが呼ばれたときに、実行されるそうだ。
     * View上に描きたい内容は、ここで渡されるCanvas canvasを使って描く感じか。
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasWidth = canvas.width
        canvasHeight = canvas.height

        //ビットマップの描画
        canvas.drawBitmap(cld1Bitmap, cld1CurrentX, cld1CurrentY, mPainter)
    }

    /**
     * 移動後の位置を計算
     * ディスプレイ領域から外に出ないように制御してる感じかな
     */
    fun move() {
//        if (cld1CurrentX + cld1dx < 0) {
//            cld1dx = -cld1dx
//        }
//        if (cld1CurrentY + cld1dy < 0) {
//            cld1dy = -cld1dy
//        }
        if (canvasWidth < cld1CurrentX + cld1dx - cld1bitmapWidth) {
            cld1CurrentX = (-cld1bitmapWidth).toFloat()
        }
//        if (canvasHeight < cld1CurrentY + cld1dy + cld1bitmapHeight) {
//            cld1dy = -cld1dy
//        }
        cld1CurrentX += cld1dx
//        cld1CurrentY += cld1dy
    }


    /**
     * コンストラクタ
     */
    init {

        //WindowManagerクラスとDisplayクラスを使って画面のサイズを取得する
        val display = windowManager.defaultDisplay
        val point = Point(0, 0)
        display.getSize(point) //ここの処理で、引数(Point outSize)に渡した変数に対し、ディスプレイサイズのピクセルが代入される。
        val displayWidth = point.x //ディスプレイ幅を取得。
        val displayHeight = point.y //同、高さを取得。

        //引数に取ったビットマップ（今回は乗っかってる絵）とその幅＆高さを得る。
        this.cld1Bitmap = cld1Bitmap
        cld1bitmapHeight = cld1Bitmap.height
        cld1bitmapWidth = cld1Bitmap.width

        //開始位置を指定。画面中央は、ディスプレイ幅の1/2と高さの1/2で得ている。
        val x0 = (cld1bitmapWidth).toFloat()
        val y0 = (200).toFloat()
//            val x0 = canvasWidth/2.toFloat()
//            val y0 = 100.toFloat()

        //x方向、y方向の、描画ごとの移動量。変えると、アニメーションの移動速度が変化する感じか。
        //ここでは起動のたび（MyViewインスタンス生成のたび？）Randomで速度が変わっている。
//        val r = Random()
//            dx = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
//            dy = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
        cld1dx = 1.0.toFloat()
//        cld1dy = 1.0.toFloat()


//            //これはよくわからない。x0,y0でも動くぽいが。
        cld1CurrentX = x0 - cld1bitmapWidth / 2
        cld1CurrentY = y0 - cld1bitmapHeight / 2
//            currentX = x0
//            currentY = y0

        //アンチエイリアスの設定。View内の描画物に対して機能するのかな？よくわかんない。
        mPainter.isAntiAlias = true
    }
}


//CCCCCCCCCCCCCCCCCCCCCC--雲関連その2--CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

 private inner class cld2View(cld1Context: Context?, cld1Bitmap: Bitmap) : View(cld1Context) {
        private val cld1Bitmap: Bitmap
        var cld1CurrentX: Float
        var cld1CurrentY: Float
        var cld1dx: Float = 0.0f
        var cld1dy: Float = 0.0f
        var cld1bitmapHeight: Int
        var cld1bitmapWidth: Int
        var rot = 0F
        var canvasWidth = 0
        var canvasHeight = 0
        private val mPainter = Paint()

        /**
         * onDrawは、Viewのインスタンスが作られた（レイアウトにaddViewされた？）ときと、
         * そのインスタンスで（on）invalidateが呼ばれたときに、実行されるそうだ。
         * View上に描きたい内容は、ここで渡されるCanvas canvasを使って描く感じか。
         * @param canvas
         */
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvasWidth = canvas.width
            canvasHeight = canvas.height

            //ビットマップの描画
            canvas.drawBitmap(cld1Bitmap, cld1CurrentX, cld1CurrentY, mPainter)
        }

        /**
         * 移動後の位置を計算
         * ディスプレイ領域から外に出ないように制御してる感じかな
         */
        fun move() {
//        if (cld1CurrentX + cld1dx < 0) {
//            cld1dx = -cld1dx
//        }
//        if (cld1CurrentY + cld1dy < 0) {
//            cld1dy = -cld1dy
//        }
            if (canvasWidth < cld1CurrentX + cld1dx - cld1bitmapWidth) {
                cld1CurrentX = (-cld1bitmapWidth).toFloat()
            }
//        if (canvasHeight < cld1CurrentY + cld1dy + cld1bitmapHeight) {
//            cld1dy = -cld1dy
//        }
            cld1CurrentX += cld1dx
//        cld1CurrentY += cld1dy
        }


        /**
         * コンストラクタ
         */
        init {

            //WindowManagerクラスとDisplayクラスを使って画面のサイズを取得する
            val display = windowManager.defaultDisplay
            val point = Point(0, 0)
            display.getSize(point) //ここの処理で、引数(Point outSize)に渡した変数に対し、ディスプレイサイズのピクセルが代入される。
            val displayWidth = point.x //ディスプレイ幅を取得。
            val displayHeight = point.y //同、高さを取得。

            //引数に取ったビットマップ（今回は乗っかってる絵）とその幅＆高さを得る。
            this.cld1Bitmap = cld1Bitmap
            cld1bitmapHeight = cld1Bitmap.height
            cld1bitmapWidth = cld1Bitmap.width

            //開始位置を指定。画面中央は、ディスプレイ幅の1/2と高さの1/2で得ている。
            val x0 = (300 + cld1bitmapWidth).toFloat()
            val y0 = (600).toFloat()
//            val x0 = canvasWidth/2.toFloat()
//            val y0 = 100.toFloat()

            //x方向、y方向の、描画ごとの移動量。変えると、アニメーションの移動速度が変化する感じか。
            //ここでは起動のたび（MyViewインスタンス生成のたび？）Randomで速度が変わっている。
//        val r = Random()
//            dx = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
//            dy = ((1.0 * r.nextFloat() - 1.0)*STEP).toFloat()
            cld1dx = 1.0.toFloat()
//        cld1dy = 1.0.toFloat()


//            //これはよくわからない。x0,y0でも動くぽいが。
            cld1CurrentX = x0 - cld1bitmapWidth / 2
            cld1CurrentY = y0 - cld1bitmapHeight / 2
//            currentX = x0
//            currentY = y0

            //アンチエイリアスの設定。View内の描画物に対して機能するのかな？よくわかんない。
            mPainter.isAntiAlias = true
        }
    }



    companion object {
        private const val STEP = 50 //これ増やすと、起動ごとの速度設定（ランダム）のふり幅が大きくなるようだ。
        const val SAIBYOUGA_KANKAKU_MS = 10 //viewの再描画の間隔（ms）。小さいほどアニメーションが滑らかに。
        const val SAIBYOUGA_COUNT = 5000 //再描画回数の指定。再描画用スレッド内の処理でインクリメントがこれに達すると終了。
    }


//---------------------------------------------------------------------------------------------------------------
    //ここからProgress Bar

    //  Timer　関連　　PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

    var cnt=100
    val hnd0=Handler()

    val rnb0: Runnable=object: Runnable{
        override fun run(){
            cnt--
            progressBar1.progress = cnt
            if(cnt>=0){//1000ミリ秒=1秒
                hnd0.postDelayed(this,100)
            }
            else if(cnt<0){
                Toast.makeText(applicationContext, "時間切れです", Toast.LENGTH_SHORT).show()
            }

        }

    }

//=================================以下main===================================================================================================


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

//  canvas 関連　CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC

        //ベースとなるレイアウトを得ておく。
        val relativeLayout = findViewById<RelativeLayout>(R.id.gameScreenView)

        //ビットマップ（＝回転する絵）を得ておく。
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.shuriken01) //drawableに突っ込んでおいた画像を使用。
        val balloonBitmap = BitmapFactory.decodeResource(resources, R.drawable.kikyu)
        val Cld1Bitmap = BitmapFactory.decodeResource(resources, R.drawable.kumo1)
        val Cld2Bitmap = BitmapFactory.decodeResource(resources, R.drawable.kumo2)

        //viewのサブクラスを得ておく。レイアウトに乗っかる、絵コンテンツの総体、とでも捉えたらよいのかな。
        //これがインスタンス？
        val myView = MyView(this, bitmap)
        val balloonView = blnView(this, balloonBitmap)
        val cld1View = cld1View(this, Cld1Bitmap)
        val cld2View = cld2View(this, Cld2Bitmap)

        //ベースのレイアウトにviewをaddする。乗っける、的な？
        relativeLayout.addView(cld1View)
        relativeLayout.addView(cld2View)
        relativeLayout.addView(myView)
        relativeLayout.addView(balloonView)
        //基本は、ViewのonDraw内（描画処理の実体）を1回呼んでおしまいなので、
        //1枚絵ならば、ここまでで完成。

        //アニメの理屈は、view丸ごとの再描画を、任意の間隔で繰り返す感じ。
        //んで今回は、再描画ごとにカウンタをインクリメントして、指定回数到達で停止、のような実装。
        //再描画ごとの移動や回転、つまり動きの表現は、viewのほうに仕込んであり、それが繰り返されることでアニメーションを表現。
        //Viewの描画処理（onDraw）を繰り返し呼ぶのはスレッド作っておこなう。UI(メイン）スレッドではやらない行為、なのだそうだ。
        val mainThread = Thread(Runnable {
//            for (i in 0 until SAIBYOUGA_COUNT) {
            while(flag){
                try {
                    //再描画までの時間稼ぎ
                    Thread.sleep(SAIBYOUGA_KANKAKU_MS.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                myView.postInvalidate() //←コレで結果的にViewのonDrawを呼ぶのだそう。
                balloonView.postInvalidate()
                cld1View.postInvalidate()
                cld2View.postInvalidate()
                //類似のinvalidateというメソッドもあるが、別スレッドからの場合はこちらのpost～を使うとのこと。
                myView.move() //再描画後にコレ。次の描画用に、新しい位置座標などを更新してる。
                balloonView.move()
                cld1View.move()
                cld2View.move()
            }
        })

 //=====================================================================================
 //　一時プロパティ
        //全体残り時間（仮にここで定める）
        var setMinutes:Int=61
        var setSeconds:Int=setMinutes*60




//   progress timer 関連　PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

        TimerProgressBar.max=setSeconds
        TimerProgressBar.progress=setSeconds

        progressBar1.setPadding(0, 0, 800, 0)
        progressBar1.max =cnt
        progressBar1.progress = cnt
        progressBar1.secondaryProgress = cnt


        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            hnd0.post(rnb0)
            var restSeconds=findViewById<TextView>(R.id.timerSecondsTextView)
            var restMinutes=findViewById<TextView>(R.id.timerMinutesTextView)
            var restHours=findViewById<TextView>(R.id.timerHoursTextView)
            setTimerProgress(setSeconds,restSeconds,restMinutes,restHours)
            flag = !flag
            if(flag){
                mainThread.start()
            }
        }

    }

    fun setTimerProgress(setSeconds:Int,rSeconds:TextView,rMinutes:TextView,rHours:TextView){
        var seconds = setSeconds
        var delayMillis:Long=1000
        val hnd=Handler()

        val rnb: Runnable=object: Runnable{
            override fun run(){
                seconds--
                TimerProgressBar.progress=seconds
                var hours=seconds/3600
                var minutes= (seconds%3600)/60
                var seconds = seconds%60

                checkDigits(hours,rHours)
                checkDigits(minutes,rMinutes)
                checkDigits(seconds,rSeconds)

                if(seconds>=0){//1000ミリ秒=1秒
                    hnd.postDelayed(this, delayMillis)
                }
                else if(seconds<0){
                    Toast.makeText(applicationContext, "時間切れです", Toast.LENGTH_SHORT).show()
                }
            }
        }
        hnd.post(rnb)
    }

    fun checkDigits(number:Int,text:TextView){
        if(number<10){
            text.text = "0"+ number.toString()
        }else{
            text.text = number.toString()
        }
    }





}