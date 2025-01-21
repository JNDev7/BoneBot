package xyz.jeremynoesen.bonerbot

import xyz.jeremynoesen.bonerbot.modules.*
import xyz.jeremynoesen.bonerbot.modules.commands.Commands
import xyz.jeremynoesen.bonerbot.modules.commands.Files
import xyz.jeremynoesen.bonerbot.modules.commands.Memes
import xyz.jeremynoesen.bonerbot.modules.commands.Quotes
import java.awt.Color
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.*

/**
 * Configuration file handler
 *
 * @author Jeremy Noesen
 */
object Config {

    /**
     * Bot token
     */
    var botToken = "TOKEN"

    /**
     * Color used for embeds
     */
    var embedColor = Color(0xFD0605)

    /**
     * Load config values and write default config if missing
     */
    fun loadData() {
        try {
            File("resources").mkdir()
            File("resources/memeimages").mkdir()
            File("resources/files").mkdir()
            val fileScanner = Scanner(File("resources/config.txt"))
            while (fileScanner.hasNextLine()) {
                val lineScanner = Scanner(fileScanner.nextLine())
                when (lineScanner.next()) {
                    "responder-enabled:" -> {
                        Responder.enabled = lineScanner.nextBoolean()
                    }

                    "responder-cooldown:" -> {
                        Responder.cooldown = lineScanner.nextInt()
                    }

                    "responder-delay:" -> {
                        Responder.delay = lineScanner.nextLong()
                    }

                    "responder-typing-speed:" -> {
                        Responder.typingSpeed = lineScanner.nextLong()
                    }

                    "reactor-enabled:" -> {
                        Reactor.enabled = lineScanner.nextBoolean()
                    }

                    "reactor-cooldown:" -> {
                        Reactor.cooldown = lineScanner.nextInt()
                    }

                    "reactor-delay:" -> {
                        Reactor.delay = lineScanner.nextLong()
                    }

                    "memes-enabled:" -> {
                        Memes.enabled = lineScanner.nextBoolean()
                    }

                    "memes-cooldown:" -> {
                        Memes.cooldown = lineScanner.nextInt()
                    }

                    "memes-size:" -> {
                        Memes.size = lineScanner.nextInt()
                    }

                    "memes-font-scale:" -> {
                        Memes.fontScale = lineScanner.nextFloat()
                    }

                    "statuses-enabled:" -> {
                        Statuses.enabled = lineScanner.nextBoolean()
                    }

                    "statuses-delay:" -> {
                        Statuses.delay = lineScanner.nextInt()
                    }

                    "commands-enabled:" -> {
                        Commands.enabled = lineScanner.nextBoolean()
                    }

                    "commands-cooldown:" -> {
                        Commands.cooldown = lineScanner.nextInt()
                    }

                    "commands-prefix:" -> {
                        Commands.prefix = lineScanner.next().lowercase()
                    }

                    "quotes-enabled:" -> {
                        Quotes.enabled = lineScanner.nextBoolean()
                    }

                    "quotes-cooldown:" -> {
                        Quotes.cooldown = lineScanner.nextInt()
                    }

                    "files-enabled:" -> {
                        Files.enabled = lineScanner.nextBoolean()
                    }

                    "files-cooldown:" -> {
                        Files.cooldown = lineScanner.nextInt()
                    }

                    "welcomer-enabled:" -> {
                        Welcomer.enabled = lineScanner.nextBoolean()
                    }

                    "listen-to-bots:" -> {
                        Listener.listenToBots = lineScanner.nextBoolean()
                    }

                    "max-threads:" -> {
                        Listener.maxThreads = lineScanner.nextInt()
                    }

                    "embed-color:" -> {
                        embedColor = Color.decode(lineScanner.next())
                    }

                    "bot-token:" -> {
                        botToken = lineScanner.next()
                    }
                }
                lineScanner.close()
            }
            fileScanner.close()
        } catch (_: FileNotFoundException) {
        }
        val file = File("resources/config.txt")
        file.delete()
        val pw = PrintWriter(file)
        pw.println("responder-enabled: ${Responder.enabled}")
        pw.println("responder-cooldown: ${Responder.cooldown}")
        pw.println("responder-delay: ${Responder.delay}")
        pw.println("responder-typing-speed: ${Responder.typingSpeed}")
        pw.println("reactor-enabled: ${Reactor.enabled}")
        pw.println("reactor-cooldown: ${Reactor.cooldown}")
        pw.println("reactor-delay: ${Reactor.delay}")
        pw.println("memes-enabled: ${Memes.enabled}")
        pw.println("memes-cooldown: ${Memes.cooldown}")
        pw.println("memes-size: ${Memes.size}")
        pw.println("memes-font-scale: ${Memes.fontScale}")
        pw.println("statuses-enabled: ${Statuses.enabled}")
        pw.println("statuses-delay: ${Statuses.delay}")
        pw.println("commands-enabled: ${Commands.enabled}")
        pw.println("commands-cooldown: ${Commands.cooldown}")
        pw.println("commands-prefix: ${Commands.prefix}")
        pw.println("quotes-enabled: ${Quotes.enabled}")
        pw.println("quotes-cooldown: ${Quotes.cooldown}")
        pw.println("files-enabled: ${Files.enabled}")
        pw.println("files-cooldown: ${Files.cooldown}")
        pw.println("welcomer-enabled: ${Welcomer.enabled}")
        pw.println("listen-to-bots: ${Listener.listenToBots}")
        pw.println("max-threads: ${Listener.maxThreads}")
        pw.println("embed-color: ${String.format("#%02x%02x%02x", embedColor.red, embedColor.green, embedColor.blue)}")
        pw.println("bot-token: $botToken")
        pw.close()
        loadData("resources/memetexts.txt", Memes.texts)
        loadData("resources/statuses.txt", Statuses.statuses)
        loadData("resources/commands.txt", Commands.commands)
        loadData("resources/responses.txt", Responder.responses)
        loadData("resources/reactions.txt", Reactor.reactions)
        loadData("resources/quotes.txt", Quotes.quotes)
        Messages.loadMessages()
    }

    /**
     * Load all data from file to array list
     *
     * @param filePath Path to file holding the data
     * @param list     List to load data into
     */
    private fun loadData(filePath: String, list: ArrayList<String>) {
        try {
            val fileScanner = Scanner(File(filePath))
            list.clear()
            while (fileScanner.hasNextLine()) {
                val line = fileScanner.nextLine()
                if (line.isNotBlank())
                    list.add(line)
            }
            fileScanner.close()
            list.trimToSize()
        } catch (e: FileNotFoundException) {
            val file = File(filePath)
            val pw = PrintWriter(file)
            pw.println()
            pw.close()
        }
    }

    /**
     * Load all data from file to a hashmap, where a string points to a string
     *
     * @param filePath Path to file holding the data
     * @param map     Hashmap to load data into
     */
    private fun loadData(filePath: String, map: LinkedHashMap<String, String>) {
        try {
            val fileScanner = Scanner(File(filePath))
            while (fileScanner.hasNextLine()) {
                val line = fileScanner.nextLine()
                if (line.isNotBlank()) {
                    val parts = line.split(": ", limit = 2)
                    map[parts[0]] = parts[1]
                }
            }
            fileScanner.close()
        } catch (e: FileNotFoundException) {
            val file = File(filePath)
            val pw = PrintWriter(file)
            pw.println()
            pw.close()
        }
    }

    /**
     * Load all data from file to a hashmap, where a string points to a pair of strings
     *
     * @param filePath Path to file holding the data
     * @param map     Hashmap to load data into
     */
    @JvmName("loadData1")
    private fun loadData(filePath: String, map: LinkedHashMap<String, Pair<String, String>>) {
        try {
            val fileScanner = Scanner(File(filePath))
            while (fileScanner.hasNextLine()) {
                val line = fileScanner.nextLine()
                if (line.isNotBlank()) {
                    val parts = line.split(": ", limit = 3)
                    map[parts[0]] = Pair(parts[1], parts[2])
                }
            }
            fileScanner.close()
        } catch (e: FileNotFoundException) {
            val file = File(filePath)
            val pw = PrintWriter(file)
            pw.println()
            pw.close()
        }
    }
}
