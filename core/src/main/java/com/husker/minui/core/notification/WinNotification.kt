package com.husker.minui.core.notification

import com.husker.minui.core.utils.ConcurrentArrayList
import com.husker.minui.graphics.Image
import com.husker.minui.natives.impl.win.Win

enum class ToastTypes(val value: String) {
    Default(""),
    Reminder("reminder"),
    Alarm("alarm"),
    IncomingCall("incomingCall")
}

enum class TextStyle(val value: String) {
    Default(""),
    Caption("caption"),
    CaptionSubtle("captionSubtle"),
    Body("body"),
    BodySubtle("bodySubtle"),
    Base("base"),
    BaseSubtle("baseSubtle"),
    Subtitle("subtitle"),
    SubtitleSubtle("subtitleSubtle"),
    Title("title"),
    TitleSubtle("titleSubtle"),
    TitleNumeral("titleNumeral"),
    Subheader("subheader"),
    SubheaderSubtle("subheaderSubtle"),
    SubheaderNumeral("subheaderNumeral"),
    Header("header"),
    HeaderSubtle("headerSubtle"),
    HeaderNumeral("headerNumeral")
}

enum class TextAlign(val value: String) {
    Default(""),
    Auto("auto"),
    Left("left"),
    Center("center"),
    Right("right")
}

enum class ImageCrop(val value: String) {
    Default(""),
    None("none"),
    Circle("circle")
}

enum class ImageAlign(val value: String) {
    Default(""),
    Stretch("stretch"),
    Left("left"),
    Center("center"),
    Right("right")
}

enum class Placement(val value: String) {
    Default(""),
    AppLogo("appLogoOverride"),
    Hero("hero")
}

enum class TextStacking(val value: String) {
    Default(""),
    Top("top"),
    Center("center"),
    Bottom("bottom")
}

enum class AudioType(val value: String) {
    Default("Default"),
    IM("IM"),
    Mail("Mail"),
    Reminder("Reminder"),
    SMS("SMS"),
    Alarm("Looping.Alarm"),
    Alarm2("Looping.Alarm2"),
    Alarm3("Looping.Alarm3"),
    Alarm4("Looping.Alarm4"),
    Alarm5("Looping.Alarm5"),
    Alarm6("Looping.Alarm6"),
    Alarm7("Looping.Alarm7"),
    Alarm8("Looping.Alarm8"),
    Alarm9("Looping.Alarm9"),
    Alarm10("Looping.Alarm10"),
    Call("Looping.Call"),
    Call2("Looping.Call2"),
    Call3("Looping.Call3"),
    Call4("Looping.Call4"),
    Call5("Looping.Call5"),
    Call6("Looping.Call6"),
    Call7("Looping.Call7"),
    Call8("Looping.Call8"),
    Call9("Looping.Call9"),
    Call10("Looping.Call10"),
}

class WinNotification: Notification() {

    companion object {
        private var count = 0
    }

    var content = ""

    private var _title = ""
    override var title: String
        get() = _title
        set(value) {
            _title = value
            configureDefault(title, text)
        }

    private var _text = ""
    override var text: String
        get() = _text
        set(value) {
            _text = value
            configureDefault(title, text)
        }

    private val id = count++

    init{
        configureDefault(title, text)
        Win.toastCallbackListeners.putIfAbsent(id, ConcurrentArrayList())
        Win.toastCallbackListeners[id]!!.add {
            if(it == -1)
                onClickListeners.iterate { l -> l.run() }
        }
    }

    fun build(type: ToastTypes = ToastTypes.Default, builder: WinNotificationBuilder.() -> Unit): WinNotificationBuilder{
        val instance = WinNotificationBuilder(this, type)
        builder.invoke(instance)
        content = instance.toString()
        return instance
    }

    private fun configureDefault(title: String, text: String){
        build {
            text(title)
            text(text)
        }
    }

    override fun configureImageNotification(image: Image) {
        build {
            image(image, placement= Placement.Hero)
            text(title)
            text(text)
        }
    }

    override fun configureLargeImageNotification(image: Image) {
        build {
            image(image)
            text(title)
            text(text)
        }
    }

    override fun configureSmallNotification(image: Image) {
        build {
            image(image, placement= Placement.AppLogo)
            text(title)
            text(text)
        }
    }

    class WinNotificationBuilder(private val notification: WinNotification, var type: ToastTypes): VisualContainer(){
        private val actions = arrayListOf<Any>()
        private var audio: ToastAudio = ToastAudio(silent = false, loop = false, type = AudioType.Default)

        fun action(
            text: String,
            image: Image? = null,
            onClick: () -> Unit = {}
        ){
            actions.add(ToastAction(text, image, onClick, notification))
        }

        fun audio(
            type: AudioType = AudioType.Default,
            silent: Boolean = false,
            loop: Boolean = false,
            supplier: ToastAudio.() -> Unit = {}
        ){
            audio = ToastAudio(silent, loop, type)
            supplier.invoke(audio)
        }

        fun show(){
            notification.show()
        }

        override fun toString(): String {
            val scenario = if(type == ToastTypes.Default) "" else "scenario=\"${type.value}\""
            return  """
                    <toast $scenario launch="${notification.id}_-1">
                        <visual>
                            <binding template="ToastGeneric">
                                ${childrenToString()}
                            </binding>
                        </visual>
                        <actions>
                            ${actions.joinToString(separator = "\n") { it.toString() }}
                        </actions>
                        $audio
                    </toast>
                    """
        }

        class VisualText(
            var text: String,
            var maxLines: Int,
            var minLines: Int,
            var style: TextStyle,
            var wrap: Boolean,
            var align: TextAlign
        ){
            override fun toString(): String {
                val maxLines = if(maxLines != -1)           "hint-maxLines=\"$maxLines\"" else ""
                val minLines = if(minLines != -1)           "hint-minLines=\"$minLines\"" else ""
                val style = if(style != TextStyle.Default)  "hint-style=\"${style.value}\"" else ""
                val wrap = if(wrap)                         "hint-wrap=\"$wrap\"" else ""
                val align = if(align != TextAlign.Default)  "hint-align=\"${align.value}\"" else ""
                return "<text $maxLines $minLines $style $wrap $align>${text}</text>"
            }
        }

        class VisualImage(
            var image: Image,
            var placement: Placement,
            var crop: ImageCrop,
            var removeMargin: Boolean,
            var align: ImageAlign,
            var alt: String,
            var query: Boolean
        ){
            override fun toString(): String {
                val placement = if(placement != Placement.Default)      "placement=\"${placement.value}\"" else ""
                val crop = if(crop != ImageCrop.Default)                "hint-crop=\"${crop.value}\"" else ""
                val removeMargin = if(removeMargin)                     "hint-removeMargin=\"$removeMargin\"" else ""
                val query = if(query)                                   "addImageQuery=\"true\"" else ""
                val alt = if(alt.isNotEmpty())                          "alt=\"$alt\"" else ""
                val align = if(align != ImageAlign.Default)             "align=\"${align.value}\"" else ""
                return "<image $placement $crop $removeMargin $query $alt $align src=\"${image.cacheFile().absolutePath}\"/>"
            }
        }

        class VisualGroup: VisualContainer(){
            override fun toString(): String {
                return "<group>${childrenToString()}</group>"
            }
        }

        class VisualSubGroup(
            var weight: Int,
            var textStacking: TextStacking
        ): VisualContainer(){
            override fun toString(): String {
                val weight = if(weight != -1)                               "hint-weight=\"$weight\"" else ""
                val textStacking = if(textStacking != TextStacking.Default) "hint-textStacking=\"${textStacking.value}\"" else ""
                return "<subgroup $weight $textStacking>${childrenToString()}</subgroup>"
            }
        }

        class ToastAudio(
            var silent: Boolean,
            var loop: Boolean,
            var type: AudioType
        ){
            override fun toString(): String {
                val silent = if(silent)                     "silent=\"$silent\"" else ""
                val loop = if(loop)                         "loop=\"$loop\"" else ""
                val type = if(type != AudioType.Default)    "src=\"ms-winsoundevent:Notification.${type.value}\"" else ""
                return "<audio $silent $loop $type/>"
            }
        }

        class ToastAction(
            var text: String,
            var image: Image?,
            var onClick: () -> Unit,
            private val notification: WinNotification,
        ){
            companion object{
                private var count = 0
            }
            private val index = count++

            init{
                Win.toastCallbackListeners[notification.id]!!.add {
                    if(it == index) onClick()
                }
            }

            override fun toString(): String {
                val image = if(image != null) "imageUri=\"file://${image!!.cacheFile().absolutePath}\"" else ""
                return "<action content=\"$text\" $image arguments=\"${notification.id}_$index\"/>"
            }
        }
    }

    open class VisualContainer{
        private val visuals = arrayListOf<Any>()

        fun text(
            text: String,
            maxLines: Int = -1,
            minLines: Int = -1,
            style: TextStyle = TextStyle.Default,
            wrap: Boolean = false,
            align: TextAlign = TextAlign.Default,
            supplier: WinNotificationBuilder.VisualText.() -> Unit = {}
        ){
            val instance = WinNotificationBuilder.VisualText(text, maxLines, minLines, style, wrap, align)
            supplier.invoke(instance)
            visuals.add(instance)
        }

        fun image(
            image: Image,
            placement: Placement = Placement.Default,
            crop: ImageCrop = ImageCrop.Default,
            removeMargin: Boolean = false,
            align: ImageAlign = ImageAlign.Default,
            alt: String = "",
            query: Boolean = false,
            supplier: WinNotificationBuilder.VisualImage.() -> Unit = {}
        ){
            val instance = WinNotificationBuilder.VisualImage(image, placement, crop, removeMargin, align, alt, query)
            supplier.invoke(instance)
            visuals.add(instance)
        }

        fun group(
            supplier: WinNotificationBuilder.VisualGroup.() -> Unit = {}
        ){
            val instance = WinNotificationBuilder.VisualGroup()
            supplier.invoke(instance)
            visuals.add(instance)
        }

        fun subGroup(
            weight: Int = -1,
            textStacking: TextStacking = TextStacking.Default,
            supplier: WinNotificationBuilder.VisualSubGroup.() -> Unit = {}
        ){
            val instance = WinNotificationBuilder.VisualSubGroup(weight, textStacking)
            supplier.invoke(instance)
            visuals.add(instance)
        }

        protected fun childrenToString(): String = visuals.joinToString(separator = "\n") { it.toString() }
    }
}