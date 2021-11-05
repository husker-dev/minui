package com.husker.minui.core.notification

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

class WinNotification: Notification() {

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

    init{
        configureDefault(title, text)
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

    override fun configureImageNotification(src: String) {
        build {
            image(src, placement= Placement.Hero)
            text(title)
            text(text)
        }
    }

    override fun configureLargeImageNotification(src: String) {
        build {
            image(src)
            text(title)
            text(text)
        }
    }

    override fun configureSmallNotification(src: String) {
        build {
            image(src, placement= Placement.AppLogo)
            text(title)
            text(text)
        }
    }

    class WinNotificationBuilder(private val parent: WinNotification, var type: ToastTypes): VisualContainer(){
        private val actions = arrayListOf<Any>()

        fun action(text: String){
            actions.add(Action(text))
        }

        fun show(){
            parent.show()
        }

        override fun toString(): String {
            val scenario = if(type == ToastTypes.Default) "" else "scenario=\"${type.value}\""

            return  """
                        <toast $scenario launch="main">
                            <visual>
                                <binding template="ToastGeneric">
                                    ${childrenToString()}
                                </binding>
                            </visual>
                            <actions>
                                ${actions.joinToString(separator = "\n") { it.toString() }}
                            </actions>
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
            var src: String,
            var placement: Placement,
            var crop: ImageCrop,
            var removeMargin: Boolean,
            var align: ImageAlign,
            var alt: String,
            var query: Boolean
        ){
            override fun toString(): String {
                val placement = if(placement != Placement.Default) "placement=\"${placement.value}\"" else ""
                val crop = if(crop != ImageCrop.Default)                "hint-crop=\"${crop.value}\"" else ""
                val removeMargin = if(removeMargin)                     "hint-removeMargin=\"$removeMargin\"" else ""
                val query = if(query)                                   "addImageQuery=\"true\"" else ""
                val alt = if(alt.isNotEmpty())                          "alt=\"$alt\"" else ""
                val align = if(align != ImageAlign.Default)             "align=\"${align.value}\"" else ""
                return "<image $placement $crop $removeMargin $query $alt $align src=\"$src\"/>"
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

        class Action(var text: String){
            override fun toString(): String {
                return "<action content=\"$text\" arguments=\"a\"/>"
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
            src: String,
            placement: Placement = Placement.Default,
            crop: ImageCrop = ImageCrop.Default,
            removeMargin: Boolean = false,
            align: ImageAlign = ImageAlign.Default,
            alt: String = "",
            query: Boolean = false,
            supplier: WinNotificationBuilder.VisualImage.() -> Unit = {}
        ){
            val instance = WinNotificationBuilder.VisualImage(src, placement, crop, removeMargin, align, alt, query)
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