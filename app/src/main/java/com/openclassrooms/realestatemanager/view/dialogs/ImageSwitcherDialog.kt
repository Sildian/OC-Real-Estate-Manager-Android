package com.openclassrooms.realestatemanager.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.core.view.GestureDetectorCompat
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.dialog_image_switcher.*

/**************************************************************************************************
 * This dialog shows a set of images.
 * The user can swipe left or right to see the previous or next image
 * @Param context : the context
 * @param picturesPaths : the list of pictures paths (uri or url)
 * @param currentImagePosition : the current position in the pictures paths
 *************************************************************************************************/

class ImageSwitcherDialog(
        context: Context,
        val picturesPaths:List<String?>,
        val picturesDescriptions:List<String?>,
        var currentImagePosition:Int)

    : Dialog(context, R.style.Theme_MaterialComponents_NoActionBar),
        GestureDetector.OnGestureListener
{

    /**UI components**/

    private val imageSwitcher by lazy {dialog_image_switcher_switcher}
    private val pictureDescriptionText by lazy {dialog_image_switcher_description}

    /**Gesture detector**/

    private lateinit var gestureDetector:GestureDetectorCompat

    /**Life cycle**/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_switcher)
        initializeImageSwitcher()
        initializeDescription()
        initializeGestureDetector()
    }

    /**Initializations**/

    private fun initializeImageSwitcher(){

        /*Factory*/

        this.imageSwitcher.setFactory(object:ViewSwitcher.ViewFactory{
            override fun makeView(): View {
                return ImageView(context)
            }
        })

        /*Sets the images animations*/

        this.imageSwitcher.animation=AnimationUtils.loadAnimation(context, R.anim.anim_image_switcher_in)
        this.imageSwitcher.setInAnimation(context, R.anim.anim_image_switcher_in)
        this.imageSwitcher.setOutAnimation(context, R.anim.anim_image_switcher_out)

        /*Sets the current image*/

        Glide.with(context).load(this.picturesPaths[this.currentImagePosition])
                .into(this.imageSwitcher.currentView as ImageView)
    }

    private fun initializeDescription(){
        this.pictureDescriptionText.text=this.picturesDescriptions[this.currentImagePosition]
    }

    private fun initializeGestureDetector(){
        this.gestureDetector= GestureDetectorCompat(context, this)
    }

    /**Pictures management**/

    private fun decreaseCurrentImagePosition(){
        if(this.currentImagePosition>0){
            this.currentImagePosition--
            refreshImageSwitcher()
        }
    }

    private fun increaseCurrentImagePosition(){
        if(this.currentImagePosition<this.picturesPaths.size-1){
            this.currentImagePosition++
            refreshImageSwitcher()
        }
    }

    private fun refreshImageSwitcher(){
        Glide.with(context).load(this.picturesPaths[this.currentImagePosition])
                .into(this.imageSwitcher.nextView as ImageView)
        this.imageSwitcher.showNext()
        this.pictureDescriptionText.text=this.picturesDescriptions[this.currentImagePosition]
    }

    /**Gesture events management**/

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onShowPress(e: MotionEvent?) {
        //Nothing
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        //Nothing
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        //Nothing
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        /*Show the next or previous image (swipe left -> next image, swipe right -> previous image*/

        if(e1!=null&&e2!=null) {
            if (e2.getX() < e1.getX()) {
                increaseCurrentImagePosition()
            } else if (e2.getX() > e1.getX()) {
                decreaseCurrentImagePosition()
            }
        }
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        //Nothing
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        //Nothing
    }
}