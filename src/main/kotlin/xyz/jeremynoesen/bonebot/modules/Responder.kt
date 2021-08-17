package xyz.jeremynoesen.bonebot.modules

import xyz.jeremynoesen.bonebot.Logger
import net.dv8tion.jda.api.entities.Message
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * responder to words and phrases in a message
 *
 * @author Jeremy Noesen
 */
object Responder {

    /**
     * list of phrases loaded from the responses file
     */
    val responses = LinkedHashMap<String, String>()

    /**
     * cooldown for responder, in seconds
     */
    var cooldown = 180

    /**
     * whether this module is enabled or not
     */
    var enabled = true

    /**
     * speed at which the bot types each letter
     */
    var typingSpeed = 100L

    /**
     * last time the responder sent a message in milliseconds
     */
    private var prevTime = 0L

    /**
     * respond to a message if a trigger phrase is said
     *
     * @param message message to check and respond to
     */
    fun respond(message: Message) {
        try {
            val msg = message.contentRaw
            for (trigger in responses.keys) {
                if ((msg.contains(Regex(trigger)) || msg.lowercase().contains(trigger.lowercase()))
                        && (System.currentTimeMillis() - prevTime) >= cooldown * 1000) {
                    prevTime = System.currentTimeMillis()

                    if (typingSpeed > 0) message.channel.sendTyping().queue()

                    var toSend = responses[trigger]!!.replace("\$USER\$", message.author.asMention)
                        .replace("\\n", "\n")

                    var file: File? = null

                    if (toSend.contains("\$FILE\$")) {
                        val path = toSend.split("\$FILE\$")[1].trim()
                        toSend = toSend.replace("\$FILE\$", "").replace(path, "")
                            .replace("   ", " ").replace("  ", " ").trim()
                        try {
                            file = File(path)
                            if (file.isDirectory || file.isHidden) {
                                file = null
                            }
                        } catch (e: Exception) {
                        }
                    }

                    if (toSend.contains("\$REPLY\$")) {
                        toSend = toSend.replace("\$REPLY\$", "").replace("   ", " ")
                            .replace("  ", " ")
                        if (toSend.isNotEmpty()) message.channel.sendMessage(toSend).reference(message)
                            .queue()
                        if (file != null) message.channel.sendFile(file).reference(message).queue()
                    } else {
                        if (toSend.isNotEmpty()) message.channel.sendMessage(toSend).queue()
                        if (file != null) message.channel.sendFile(file).queue()
                    }
                }
            }
        } catch (e: Exception) {
            Logger.log(e, message.channel)
        }
    }
}