package xyz.jeremynoesen.bonerbot

import net.dv8tion.jda.api.entities.Message
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.lang.IllegalStateException
import java.util.*

/**
 * Messages and message file handler
 *
 * @author Jeremy Noesen
 */
object Messages {
    var helpTitle = "\$BOTDISPLAYNAME\$ Help"
    var helpAbout = "\$BOTDISPLAYNAME\$ aims to add more life to a server by responding and reacting to messages. It also adds" +
            " commands, which are listed below."
    var helpFormat = "• **`\$CMD\$`**: \$DESC\$"
    var helpDescription = "Show this help message."
    var memeDescription = "Generate a random or custom meme."
    var fileDescription = "Send a random or specific file."
    var quoteDescription = "Send a random quote."
    var helpCommand = "help"
    var memeCommand = "meme"
    var fileCommand = "file"
    var quoteCommand = "quote"
    var memeTitle = "\$AUTHORDISPLAYNAME\$ generated a meme:"
    var welcomeTitle = "\$AUTHORDISPLAYNAME\$ joined \$GUILD\$"
    var quoteTitle = "\$AUTHORDISPLAYNAME\$ summoned a quote:"
    var fileTitle = "\$AUTHORDISPLAYNAME\$ summoned a file:"
    var welcomeMessage = "Welcome \$AUTHORMENTION\$ to **\$GUILD\$**!"
    var error = "**An error occurred!** Please check the console!"
    var unknownCommand = "**Unknown command!**"
    var noFiles = "There are **no files** to send!"
    var unknownFile = "**Unknown file!**"
    var memeInputMissing = "Please provide the missing **text** and/or **image**!"
    var noQuotes = "There are no quotes to show!"
    var memeCooldown = "Another meme can be generated in **\$TIME\$** second(s)."
    var quoteCooldown = "Another quote can be sent in **\$TIME\$** second(s)."
    var fileCooldown = "Another file can be sent in **\$TIME\$** second(s)."
    var commandCooldown = "Commands can be used again in **\$TIME\$** second(s)."

    /**
     * Load all messages from the messages file
     */
    fun loadMessages() {
        try {
            val fileScanner = Scanner(File("resources/messages.txt"))
            while (fileScanner.hasNextLine()) {
                val lineScanner = Scanner(fileScanner.nextLine())
                when (lineScanner.next()) {
                    "help-title:" -> {
                        helpTitle = lineScanner.nextLine().trim()
                    }

                    "help-about:" -> {
                        helpAbout = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "help-format:" -> {
                        helpFormat = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "help-description:" -> {
                        helpDescription = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "meme-description:" -> {
                        memeDescription = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "file-description:" -> {
                        fileDescription = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "quote-description:" -> {
                        quoteDescription = lineScanner.nextLine().replace("\\n", "\n").trim()
                    }

                    "help-command:" -> {
                        helpCommand = lineScanner.nextLine().trim()
                    }

                    "meme-command:" -> {
                        memeCommand = lineScanner.nextLine().trim()
                    }

                    "file-command:" -> {
                        fileCommand = lineScanner.nextLine().trim()
                    }

                    "quote-command:" -> {
                        quoteCommand = lineScanner.nextLine().trim()
                    }

                    "meme-title:" -> {
                        memeTitle = lineScanner.nextLine().trim()
                    }

                    "welcome-title:" -> {
                        welcomeTitle = lineScanner.nextLine().trim()
                    }

                    "quote-title:" -> {
                        quoteTitle = lineScanner.nextLine().trim()
                    }

                    "file-title:" -> {
                        fileTitle = lineScanner.nextLine().trim()
                    }

                    "welcome-message:" -> {
                        welcomeMessage = lineScanner.nextLine().trim()
                    }

                    "error:" -> {
                        error = lineScanner.nextLine().trim()
                    }

                    "unknown-command:" -> {
                        unknownCommand = lineScanner.nextLine().trim()
                    }

                    "no-files:" -> {
                        noFiles = lineScanner.nextLine().trim()
                    }

                    "unknown-file:" -> {
                        unknownFile = lineScanner.nextLine().trim()
                    }

                    "meme-input-missing:" -> {
                        memeInputMissing = lineScanner.nextLine().trim()
                    }

                    "no-quotes:" -> {
                        noQuotes = lineScanner.nextLine().trim()
                    }

                    "meme-cooldown:" -> {
                        memeCooldown = lineScanner.nextLine().trim()
                    }

                    "file-cooldown:" -> {
                        fileCooldown = lineScanner.nextLine().trim()
                    }

                    "quote-cooldown:" -> {
                        quoteCooldown = lineScanner.nextLine().trim()
                    }

                    "command-cooldown:" -> {
                        commandCooldown = lineScanner.nextLine().trim()
                    }
                }
                lineScanner.close()
            }
            fileScanner.close()
        } catch (_: FileNotFoundException) {
        }
        val file = File("resources/messages.txt")
        file.delete()
        val pw = PrintWriter(file)
        pw.println("help-title: $helpTitle")
        pw.println("help-about: $helpAbout")
        pw.println("help-format: $helpFormat")
        pw.println("help-description: $helpDescription")
        pw.println("meme-description: $memeDescription")
        pw.println("file-description: $fileDescription")
        pw.println("quote-description: $quoteDescription")
        pw.println("help-command: $helpCommand")
        pw.println("meme-command: $memeCommand")
        pw.println("file-command: $fileCommand")
        pw.println("quote-command: $quoteCommand")
        pw.println("meme-title: $memeTitle")
        pw.println("welcome-title: $welcomeTitle")
        pw.println("quote-title: $quoteTitle")
        pw.println("file-title: $fileTitle")
        pw.println("welcome-message: $welcomeMessage")
        pw.println("error: $error")
        pw.println("unknown-command: $unknownCommand")
        pw.println("no-files: $noFiles")
        pw.println("unknown-file: $unknownFile")
        pw.println("meme-input-missing: $memeInputMissing")
        pw.println("no-quotes: $noQuotes")
        pw.println("meme-cooldown: $memeCooldown")
        pw.println("file-cooldown: $fileCooldown")
        pw.println("quote-cooldown: $quoteCooldown")
        pw.println("command-cooldown: $commandCooldown")
        pw.close()
    }

    /**
     * Send a message to the channel, pinging or replying to the user when specified and only sending when the message
     * is not empty
     *
     * @param message Message to send
     * @param cause Message causing this message to be sent
     */
    fun sendMessage(message: String, cause: Message) {
        var toSend = message
        if (toSend.contains("\$REPLY\$")) {
            toSend = toSend.replace("\$REPLY\$", "")
                    .replace("\$AUTHORMENTION\$", cause.member!!.asMention)
                    .replace("\$AUTHORDISPLAYNAME\$", cause.member!!.effectiveName)
                    .replace("\$AUTHORUSERNAME\$", cause.author.name)
                    .replace("\$BOTMENTION\$", cause.guild.getMember(BonerBot.JDA!!.selfUser)!!.asMention)
                    .replace("\$BOTDISPLAYNAME\$", cause.guild.getMember(BonerBot.JDA!!.selfUser)!!.effectiveName)
                    .replace("\$BOTUSERNAME\$", BonerBot.JDA!!.selfUser.name)
                    .replace("\$CHANNELMENTION\$", cause.channel.asMention)
                    .replace("\$CHANNELNAME\$", cause.channel.name)
                    .replace("\\n", "\n")
                    .replace("  ", " ")
                    .trim()
            try {
                toSend = toSend.replace("\$GUILD\$", cause.guild.name)
            } catch (_: IllegalStateException) {
            }
            if (toSend.isNotEmpty())
                cause.channel.sendMessage(toSend).setMessageReference(cause).queue()
        } else {
            toSend = toSend.replace("\$AUTHORMENTION\$", cause.member!!.asMention)
                    .replace("\$AUTHORDISPLAYNAME\$", cause.member!!.effectiveName)
                    .replace("\$AUTHORUSERNAME\$", cause.author.name)
                    .replace("\$BOTMENTION\$", cause.guild.getMember(BonerBot.JDA!!.selfUser)!!.asMention)
                    .replace("\$BOTDISPLAYNAME\$", cause.guild.getMember(BonerBot.JDA!!.selfUser)!!.effectiveName)
                    .replace("\$BOTUSERNAME\$", BonerBot.JDA!!.selfUser.name)
                    .replace("\$CHANNELMENTION\$", cause.channel.asMention)
                    .replace("\$CHANNELNAME\$", cause.channel.name)
                    .replace("\\n", "\n")
                    .replace("  ", " ")
                    .trim()
            try {
                toSend = toSend.replace("\$GUILD\$", cause.guild.name)
            } catch (_: IllegalStateException) {
            }
            if (toSend.isNotEmpty())
                cause.channel.sendMessage(toSend).queue()
        }
    }
}
