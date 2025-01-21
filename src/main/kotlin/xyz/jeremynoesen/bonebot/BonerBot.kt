package xyz.jeremynoesen.bonerbot

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import xyz.jeremynoesen.bonerbot.modules.Statuses
import java.awt.Toolkit
import javax.imageio.ImageIO


/**
 * Initializes bot, modules, and load all data
 *
 * @author Jeremy Noesen
 */
object BonerBot {

    /**
     * Instance of JDA for this bot
     */
    var JDA: JDA? = null

    /**
     * Create the bot and run it
     *
     * @param args Program arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("apple.awt.UIElement", "true") // Hide dock icon on macOS
        Toolkit.getDefaultToolkit()
        ImageIO.setUseCache(false)
        Config.loadData()
        JDA = JDABuilder.createLight(Config.botToken).addEventListeners(Listener())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT).build()
        if (Statuses.enabled) Statuses.setStatus()
    }
}
