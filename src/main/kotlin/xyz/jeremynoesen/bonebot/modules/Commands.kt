package xyz.jeremynoesen.bonebot.modules

import xyz.jeremynoesen.bonebot.Config
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * command handler with simple message responses
 *
 * @author Jeremy Noesen
 */
object Commands {

    /**
     * list of commands loaded from the command file
     */
    val commands = LinkedHashMap<String, Pair<String, String>>()

    /**
     * command prefix
     */
    var commandPrefix = "bb"

    /**
     * cooldown for commands, in seconds
     */
    var cooldown = 5

    /**
     * whether this module is enabled or not
     */
    var enabled = true

    /**
     * last time the command handler sent a message in milliseconds
     */
    private var prevTime = 0L

    /**
     * respond to a message if it is a command
     *
     * @param message message to check and respond to
     * @return true if command was performed
     */
    fun perform(message: Message): Boolean {
        try {
            val msg = message.contentRaw.lowercase()
            if (msg.startsWith(commandPrefix)) {
                message.channel.sendTyping().queue()
                if ((System.currentTimeMillis() - prevTime) >= cooldown * 1000) {
                    prevTime = System.currentTimeMillis()
                    when {
                        msg.startsWith(commandPrefix + "meme") && Memes.enabled -> {
                            Memes(message).generate()
                            return true
                        }
                        msg == commandPrefix + "quote" && Quotes.enabled -> {
                            Quotes.sendQuote(message)
                            return true
                        }
                        msg.startsWith(commandPrefix + "file") && Files.enabled -> {
                            Files.sendFile(message)
                            return true
                        }
                        msg == commandPrefix + "help" -> {
                            var commandList = "• **`$commandPrefix" + "help`**: Show this message.\n"
                            if (Memes.enabled) commandList += "• **`$commandPrefix" + "meme`**: Generate a meme.\n"
                            if (Files.enabled) commandList += "• **`$commandPrefix" + "file`**: Send a random file.\n"
                            if (Quotes.enabled) commandList += "• **`$commandPrefix" + "quote`**: Show a random quote.\n"
                            for (command in commands.keys)
                                commandList += "• **`$commandPrefix$command`**: ${commands[command]!!.first}\n"
                            val embedBuilder = EmbedBuilder()
                            val name = message.jda.selfUser.name
                            embedBuilder.setAuthor("$name Help", null, message.jda.selfUser.avatarUrl)
                            embedBuilder.setColor(Config.embedColor)
                            embedBuilder.setDescription("$commandList\n[**Source Code**](https://github.com/jeremynoesen/BoneBot)")
                            message.channel.sendMessage(embedBuilder.build()).queue()
                            return true
                        }
                        else -> {
                            for (command in commands.keys) {
                                if (msg == "$commandPrefix${command.lowercase()}") {

                                    var toSend =
                                        commands[command]!!.second.replace("\$USER\$", message.author.asMention)
                                            .replace("\\n", "\n")

                                    if (toSend.contains("\$CMD\$")) {
                                        val cmd = toSend.split("\$CMD\$")[1].trim()

                                        val procBuilder: ProcessBuilder?
                                        if (System.getProperty("os.name").contains("windows", true)) {
                                            procBuilder = ProcessBuilder("cmd.exe", "/c", cmd)
                                        } else {
                                            procBuilder = ProcessBuilder("/bin/sh", "-c", cmd)
                                        }
                                        val stream = procBuilder.start().inputStream

                                        toSend = toSend.replace("\$CMD\$", "")
                                            .replace(cmd, "").replace("   ", " ")
                                            .replace("  ", " ").trim()

                                        if (toSend.contains("\$CMDOUT\$")) {
                                            val stdInput = BufferedReader(InputStreamReader(stream))
                                            var output = ""
                                            for (line in stdInput.readLines()) output += "$line\n"
                                            output = output.removeSuffix("\n")
                                            toSend = toSend.replace("\$CMDOUT\$", output)
                                        }
                                    }

                                    if (toSend.contains("\$REACT\$")) {
                                        val emote = toSend.split("\$REACT\$")[1].trim()
                                        toSend = toSend.replace("\$REACT\$", "").replace(emote, "")
                                            .replace("   ", " ").replace("  ", " ").trim()
                                        message.addReaction(emote).queue()
                                    }

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
                                        toSend = toSend.replace("\$REPLY\$", "")
                                            .replace("   ", " ").replace("  ", " ")
                                        if (toSend.isNotEmpty()) {
                                            if (file != null) {
                                                message.channel.sendMessage(toSend).addFile(file).reference(message).queue()
                                            } else {
                                                message.channel.sendMessage(toSend).reference(message).queue()
                                            }
                                        }
                                    } else {
                                        if (toSend.isNotEmpty()) {
                                            if (file != null) {
                                                message.channel.sendMessage(toSend).addFile(file).queue()
                                            } else {
                                                message.channel.sendMessage(toSend).queue()
                                            }
                                        }
                                    }

                                    return true
                                }
                            }
                            message.channel.sendMessage("**Unknown command!**").queue()
                            return true
                        }
                    }
                } else {
                    val remaining = ((cooldown * 1000) - (System.currentTimeMillis() - prevTime)) / 1000
                    message.channel.sendMessage("Commands can be used again in **$remaining** seconds.")
                        .queue()
                    return true
                }
            }
        } catch (e: Exception) {
            message.channel.sendMessage("**An error occurred!** Please check the log file!").queue()
        }
        return false
    }
}