package one.hyro.registry

import one.hyro.minigame.Session
import java.util.*

object SessionsRegistry {
    val sessions: MutableList<Session> = mutableListOf()

    fun register(session: Session) = sessions.add(session)
    fun unregister(session: Session) = sessions.remove(session)
    fun isPlayerInAnySession(uuid: UUID) = sessions.any { it.isPlayerInSession(uuid) }
    fun clear() = sessions.clear()

    fun getSession(uuid: UUID): Session? {
        for (session in sessions) {
            if (!session.isPlayerInSession(uuid)) continue
            return session
        }
        return null
    }
}