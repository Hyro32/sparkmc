package one.hyro.scheduler

import one.hyro.Lib
import org.bukkit.Bukkit

object Schedule {
    /**
     * Run a task on the main thread.
     *
     * @param task The task to run.
     */
    inline fun runTask(crossinline task: () -> Unit) = Bukkit.getScheduler().runTask(Lib.plugin, Runnable { task() })

    /**
     * Run a task on the main thread after a delay.
     *
     * @param task The task to run.
     * @param delay The delay in ticks.
     */
    inline fun runTaskLater(crossinline task: () -> Unit, delay: Long) {
        Bukkit.getScheduler().runTaskLater(Lib.plugin, Runnable { task() }, delay)
    }

    /**
     * Run a task on the main thread repeatedly.
     *
     * @param task The task to run.
     * @param delay The delay in ticks.
     * @param period The period in ticks.
     */
    inline fun runTaskTimer(crossinline task: () -> Unit, delay: Long, period: Long) {
        Bukkit.getScheduler().runTaskTimer(Lib.plugin, Runnable { task() }, delay, period)
    }

    /**
     * Run a task on the main thread repeatedly. Every second.
     *
     * @param task The task to run.
     */
    inline fun runTaskTimer(crossinline task: () -> Unit) {
        Bukkit.getScheduler().runTaskTimer(Lib.plugin, Runnable { task() }, 0L, 20L)
    }
}