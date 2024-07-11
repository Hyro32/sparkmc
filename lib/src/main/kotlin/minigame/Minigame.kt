package one.hyro.minigame

interface Minigame {
    fun waiting(session: Session)
    fun starting(session: Session)
    fun playing(session: Session)
    fun ending(session: Session)
}