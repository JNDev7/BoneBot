package xyz.jeremynoesen.bonerbot

import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import xyz.jeremynoesen.bonerbot.modules.commands.Commands
import xyz.jeremynoesen.bonerbot.modules.Reactor
import xyz.jeremynoesen.bonerbot.modules.Responder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import xyz.jeremynoesen.bonerbot.modules.Welcomer
import kotlin.concurrent.thread

/**
 * Listener handlers for events
 *
 * @author Jeremy Noesen
 */
class Listener : ListenerAdapter() {

    /**
     * Respond and react to users when they say certain keywords or type commands
     *
     * @param e Message received event
     */
    override fun onMessageReceived(e: MessageReceivedEvent) {
        if (maxThreads <= 0 || numThreads < maxThreads) {
            numThreads++
            thread {
                try {
                    if ((!e.author.isBot || (listenToBots && e.author != BonerrBot.JDA!!.selfUser))
                            && !e.isFromType(ChannelType.PRIVATE)) {
                        if (!Commands.enabled || !Commands.perform(e.message)) {
                            if (Responder.enabled) Responder.respond(e.message)
                            if (Reactor.enabled) Reactor.react(e.message)
                        }
                    }
                } catch (ex: Exception) {
                    Messages.sendMessage(Messages.error, e.message)
                    ex.printStackTrace()
                }
                numThreads--
            }
        }
    }

    /**
     * Listen for message edits for fixing typos
     *
     * @param e Message update event
     */
    override fun onMessageUpdate(e: MessageUpdateEvent) {
        if (maxThreads <= 0 || numThreads < maxThreads) {
            numThreads++
            thread {
                try {
                    if ((!e.author.isBot || (listenToBots && e.author != BonerrBot.JDA!!.selfUser))
                            && !e.isFromType(ChannelType.PRIVATE)) {
                        if (!Commands.enabled || !Commands.perform(e.message)) {
                            if (Responder.enabled) Responder.respond(e.message)
                            if (Reactor.enabled) Reactor.react(e.message)
                        }
                    }
                } catch (ex: Exception) {
                    Messages.sendMessage(Messages.error, e.message)
                    ex.printStackTrace()
                }
                numThreads--
            }
        }
    }

    /**
     * Listen for member joins so they can be welcomed
     *
     * @param e Guild member join event
     */
    override fun onGuildMemberJoin(e: GuildMemberJoinEvent) {
        if (maxThreads <= 0 || numThreads < maxThreads) {
            numThreads++
            thread {
                if (Welcomer.enabled)
                    Welcomer.welcome(e.member, e.guild)
                numThreads--
            }
        }
    }

    companion object {
        /**
         * Number of threads currently running
         */
        var numThreads: Int = 0;

        /**
         * Limit to how many threads should run concurrently
         */
        var maxThreads: Int = 8;

        /**
         * Whether BonerrBot can listen to other bots for input
         */
        var listenToBots = false
    }
}
